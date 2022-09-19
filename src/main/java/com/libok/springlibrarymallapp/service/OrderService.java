package com.libok.springlibrarymallapp.service;

import com.libok.springlibrarymallapp.model.book.Book;
import com.libok.springlibrarymallapp.repository.BookRepository;
import com.libok.springlibrarymallapp.model.order.ClientOrder;
import com.libok.springlibrarymallapp.model.order.Order;
import com.libok.springlibrarymallapp.model.order.OrderDetails;
import com.libok.springlibrarymallapp.repository.OrderDetailsRepository;
import com.libok.springlibrarymallapp.repository.OrderRepository;
import com.libok.springlibrarymallapp.model.user.User;
import com.libok.springlibrarymallapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private ClientOrder clientOrder;
    private BookRepository bookRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private UserService userService;
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public OrderService(ClientOrder clientOrder, BookRepository bookRepository, OrderRepository orderRepository, UserRepository userRepository, UserService userService, OrderDetailsRepository orderDetailsRepository) {
        this.clientOrder = clientOrder;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public Optional<Book> addBookToOrder(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        book.ifPresent(clientOrder::add);

        return book;
    }

    public void deleteBookFromOrder(Long bookIndex) {
        int index = bookIndex.intValue();
        Order order = clientOrder.getOrder();
        order.getBooks().remove(index);
    }

    public void proceedOrder(OrderDetails orderDetails, Authentication authentication) {

        String email = null;
        if (authentication != null)
            email = authentication.getName();

        Optional<User> user = userRepository.findByEmailOpt(email);
        Optional<User> userGithub = userRepository.findByEmailOpt(email + "@github.com");

        orderDetailsRepository.save(orderDetails);

        Order order = clientOrder.getOrder();
        order.setOrderDetails(orderDetails);

        user.ifPresent(order::setUser);
        userGithub.ifPresent(order::setUser);

        if (!user.isPresent() && !userGithub.isPresent())
            order.setUser(OAuth2UserToUser(authentication, email));

        updateStock(order.getBooks());

        orderRepository.save(order);
        clientOrder.clear();
    }

    public double sumOrderCost(){
        Order order = clientOrder.getOrder();
        return order
                .getBooks().stream()
                .mapToDouble(Book::getPrice)
                .sum();
    }

    private void updateStock(List<Book> books) {
        books.forEach(book -> {
            int stock = book.getStock();
            book.setStock(stock - 1);
            bookRepository.save(book);
        });
    }

    private User OAuth2UserToUser(Authentication authentication, String email) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = ((OAuth2AuthenticationToken) authentication);
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        Map<String, Object> map = oAuth2User.getAttributes();
        String name = (String) map.get("login");

        User newUser = new User();
        newUser.setEmail(email + "@github.com");
        newUser.setFirstname(name);
        newUser.setLastname(name);
        //TODO generate random password
        newUser.setPassword("default");
        newUser.setEnabled(true);

        userService.addWithDefaultRole(newUser);

        return newUser;
    }



}
