package com.libok.springlibrarymallapp.controller;

import com.libok.springlibrarymallapp.dto.BookDto;
import com.libok.springlibrarymallapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book")
    public String book(@RequestParam Long bookId, Model model) {
        BookDto book= bookService.findById(bookId);
        model.addAttribute("book", book);
        return "bookView";
    }

    @GetMapping("/")
    public String findAll(Model model,@RequestParam(value = "text",required = false) String text) {
        List<BookDto> books;
        if (text != null)
            books = bookService.findAllByNameOrAuthor(text);
        else
            books = bookService.findAll();

        model.addAttribute("books", books);
        return "index";
    }
}