package com.libok.springlibrarymallapp.order;

import com.libok.springlibrarymallapp.model.book.Book;
import com.libok.springlibrarymallapp.repository.BookRepository;
import com.libok.springlibrarymallapp.model.order.ClientOrder;
import com.libok.springlibrarymallapp.model.order.Order;
import com.libok.springlibrarymallapp.model.order.OrderDetails;
import com.libok.springlibrarymallapp.repository.OrderDetailsRepository;
import com.libok.springlibrarymallapp.repository.OrderRepository;
import com.libok.springlibrarymallapp.service.OrderService;
import com.libok.springlibrarymallapp.model.user.User;
import com.libok.springlibrarymallapp.repository.UserRepository;
import com.libok.springlibrarymallapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;



@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    ClientOrder clientOrder;

    @Mock
    BookRepository bookRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;


    @Mock
    UserService userService;

    @Mock
    OrderDetailsRepository orderDetailsRepository;

    @InjectMocks
    OrderService orderService;


    @Test
    void addBookToOrderTest() {
        //given
        Long id = 1L;
        Book bookEntity = new Book();
        bookEntity.setName("test");
        given(bookRepository.findById(id)).willReturn(Optional.of(bookEntity));

        //when
        Optional<Book> book = orderService.addBookToOrder(id);

        //then
        then(bookRepository).should().findById(id);
        then(clientOrder).should().add(book.get());
        assertThat(book).isNotEmpty();
        assertThat(book.get().getName()).isEqualTo("test");
    }

    @Test
    void deleteBookFromOrder() {
        //given
        Long id = 1L;
        Order order = new Order();
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setPrice(8.00);
        Book book2 = new Book();
        book2.setPrice(1.24);
        books.add(book1);
        books.add(book2);
        order.setBooks(books);

        given(clientOrder.getOrder()).willReturn(order);
        //when
        orderService.deleteBookFromOrder(id);

        //then
        then(clientOrder).should().getOrder();
    }


    @Test
    void proceedOrderTest() {
        //given
        Authentication authentication = auth();
        OrderDetails orderDetails = new OrderDetails();
        Order order = new Order();
        given(userRepository.findByEmailOpt(authentication.getName())).willReturn(Optional.of(new User()));
        given(clientOrder.getOrder()).willReturn(order);

        //when
        orderService.proceedOrder(orderDetails,authentication);

        //then
        then(userRepository).should().findByEmailOpt(authentication.getName());
        then(userRepository).should().findByEmailOpt(authentication.getName()+"@github.com");
        then(orderDetailsRepository).should().save(orderDetails);
        then(clientOrder).should().getOrder();
        then(orderRepository).should().save(order);
        then(clientOrder).should().clear();
    }

    @Test
    void sumOrderCostTest() {
        //given
        Order order = new Order();
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setPrice(8.00);
        Book book2 = new Book();
        book2.setPrice(1.24);
        books.add(book1);
        books.add(book2);

        order.setBooks(books);

        given(clientOrder.getOrder()).willReturn(order);

        //when
        double sum = orderService.sumOrderCost();

        //then
        then(clientOrder).should()
                .getOrder();
        assertThat(sum).isEqualTo(9.24);

    }


    Authentication auth(){
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "test";
            }
        };
    }
}