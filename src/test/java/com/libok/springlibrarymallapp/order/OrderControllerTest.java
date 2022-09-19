package com.libok.springlibrarymallapp.order;

import com.libok.springlibrarymallapp.controller.OrderController;
import com.libok.springlibrarymallapp.model.book.Book;
import com.libok.springlibrarymallapp.model.order.ClientOrder;
import com.libok.springlibrarymallapp.model.order.OrderDetails;
import com.libok.springlibrarymallapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @Mock
    ClientOrder clientOrder;

    @Mock
    Model model;

    @Mock
    Authentication authentication;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OrderController controller;


    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void addBookToOrderTest() {
        //given
        Long bookId=1L;
        given(orderService.addBookToOrder(bookId)).willReturn(Optional.of(new Book()));

        //when
        String view = controller.addBookToOrder(bookId,model);

        //then
        then(orderService).should().addBookToOrder(bookId);
        then(model).should().addAttribute(anyString(), any());
        assertThat("message").isEqualToIgnoringCase(view);
    }

    @Test
    void addBookToOrderNotFoundTest() {
        //given
        Long bookId=1L;
        given(orderService.addBookToOrder(bookId)).willReturn(Optional.empty());

        //when
        String view = controller.addBookToOrder(bookId,model);

        //then
        then(orderService).should().addBookToOrder(bookId);
        then(model).should().addAttribute(anyString(), any());
        assertThat("message").isEqualToIgnoringCase(view);
    }

    @Test
    void addBookToOrderControllerTest() throws Exception {
        mockMvc.perform((get("/order-add")).param("bookId","1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("message"));
    }

    @Test
    void deleteBookFromOrderTest() {
        //given
        Long bookId=1L;

        //when
        String view = controller.deleteBookFromOrder(bookId,model);

        //then
        then(orderService).should().deleteBookFromOrder(bookId);
        then(model).should(times(3)).addAttribute(anyString(), any());
        assertThat("orderView").isEqualToIgnoringCase(view);
    }

    @Test
    void deleteBookFromOrderControllerTest() throws Exception {
        mockMvc.perform((get("/order-delete")).param("bookIndex","1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("order"))
                .andExpect(model().attributeExists("sum"))
                .andExpect(model().attributeExists("orderDetails"))
                .andExpect(view().name("orderView"));
    }

    @Test
    void getCurrentOrderTest() {
        //when
        String view = controller.getCurrentOrder(model);

        //then
        then(model).should(times(3)).addAttribute(anyString(), any());
        assertThat("orderView").isEqualToIgnoringCase(view);
    }

    @Test
    void getCurrentOrderControllerTest() throws Exception {
        mockMvc.perform((get("/order")))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("order"))
                .andExpect(model().attributeExists("sum"))
                .andExpect(model().attributeExists("orderDetails"))
                .andExpect(view().name("orderView"));
    }

    @Test
    void proceedOrderTest() {
        //given
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setAddress("test");
        orderDetails.setTelephone("777124214");

        //when
        String view = controller.proceedOrder(model,orderDetails,bindingResult,authentication);

        //then
        then(orderService).should().proceedOrder(orderDetails,authentication);
        then(model).should().addAttribute(anyString(), any());
        assertThat("message").isEqualToIgnoringCase(view);
    }

    @Test
    void proceedOrderValidTest() throws Exception {
        mockMvc.perform((post("/order-finalize"))
                    .param("address","Not empty")
                    .param("telephone","876868122"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("message"));
    }

    @Test
    void proceedOrderNotValidTest() throws Exception {
        mockMvc.perform((post("/order-finalize"))
                .param("address","Not empty")
                .param("telephone","2224"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("order"))
                .andExpect(model().attributeExists("sum"))
                .andExpect(view().name("orderView"));
    }
}