package com.libok.springlibrarymallapp.book;

import com.libok.springlibrarymallapp.controller.BookController;
import com.libok.springlibrarymallapp.dto.BookDto;
import com.libok.springlibrarymallapp.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    BookService bookService;

    @Mock
    Model model;

    @InjectMocks
    BookController controller;

    List<BookDto> books = new ArrayList<>();
    BookDto bookDto = new BookDto();

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        books.add(new BookDto());

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void bookTest() {
        //given
        given(bookService.findById(1L)).willReturn(bookDto);

        //when
        String view = controller.book(1L,model);

        //then
        then(bookService).should().findById(1L);
        then(model).should().addAttribute(anyString(), any());
        assertThat("bookView").isEqualToIgnoringCase(view);
    }

    @Test
    void bookControllerTest() throws Exception {
        mockMvc.perform((get("/book")).param("bookId","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookView"));
    }

    @Test
    void findAllTest() {
        //given
        given(bookService.findAll()).willReturn(books);

        //when
        String view = controller.findAll(model,null);

        //then
        then(bookService).should().findAll();
        then(model).should().addAttribute(anyString(), any());
        assertThat("index").isEqualToIgnoringCase(view);
    }

    @Test
    void findAllControllerTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("index"));
    }
    @Test
    void findAllControllerWithParamTest() throws Exception {
        mockMvc.perform(get("/").param("text","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("index"));
    }
}