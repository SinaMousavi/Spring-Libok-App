package com.libok.springlibrarymallapp.book;

import com.libok.springlibrarymallapp.dto.BookDto;
import com.libok.springlibrarymallapp.dto.BookMapper;
import com.libok.springlibrarymallapp.model.book.Book;
import com.libok.springlibrarymallapp.repository.BookRepository;
import com.libok.springlibrarymallapp.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;



    @Test
    void findAllTest() {
        //given
        given(bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList())).willReturn(new ArrayList<>());

        //when
        List<BookDto> books = bookService.findAll();

        //then
        then(bookRepository).should().findAll();
        Assertions.assertThat(books).isNotNull();
    }

    @Test
    void findByIdTest() {
        //given
        Long id = 1L;
        Book bookEntity = new Book();
        bookEntity.setName("test");
        given(bookRepository.findById(id)).willReturn(Optional.of(bookEntity));

        //when
        BookDto book = bookService.findById(id);

        //then
        then(bookRepository).should().findById(id);
        assertThat(book).isNotNull();
        assertThat(book.getName()).isEqualTo("test");
    }

    @Test
    void findAllByNameOrAuthor() {
        //given
        String text = "test";
        given(bookRepository.findAllByNameOrAuthor(text)
                .stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList())).willReturn(new ArrayList<>());

        //when
        List<BookDto> books = bookService.findAllByNameOrAuthor(text);

        //then
        then(bookRepository).should().findAllByNameOrAuthor(text);
        Assertions.assertThat(books).isNotNull();
    }
}