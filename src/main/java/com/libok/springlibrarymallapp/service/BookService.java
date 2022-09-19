package com.libok.springlibrarymallapp.service;

import com.libok.springlibrarymallapp.dto.BookDto;
import com.libok.springlibrarymallapp.dto.BookMapper;
import com.libok.springlibrarymallapp.exception.BookNotFoundException;
import com.libok.springlibrarymallapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto findById(Long id) {
        Optional<BookDto> bookDto = bookRepository.findById(id).map(BookMapper::toDto);
        return bookDto.orElseThrow(BookNotFoundException::new);
    }

    public List<BookDto> findAllByNameOrAuthor(String text) {
       return bookRepository.findAllByNameOrAuthor(text)
               .stream()
               .map(BookMapper::toDto)
               .collect(Collectors.toList());

    }
}
