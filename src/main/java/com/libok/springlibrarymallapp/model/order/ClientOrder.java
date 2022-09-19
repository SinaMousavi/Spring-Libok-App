package com.libok.springlibrarymallapp.model.order;

import com.libok.springlibrarymallapp.model.book.Book;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ClientOrder {

    private Order order;

    public ClientOrder() {
        clear();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void add(Book book) {
        order.getBooks().add(book);
    }

    public void clear() {
        order = new Order();
        order.setStatus(OrderStatus.NEW);
    }
}
