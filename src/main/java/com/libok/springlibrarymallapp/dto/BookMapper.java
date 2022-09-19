package com.libok.springlibrarymallapp.dto;

import com.libok.springlibrarymallapp.model.book.Book;

public class BookMapper {

    public static BookDto toDto(Book book) {
        BookDto dto =new BookDto();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setBookCategory(book.getBookCategory());
        dto.setPrice(book.getPrice());
        dto.setPages(book.getPages());
        dto.setDescription(book.getDescription());
        dto.setStock(book.getStock());
        dto.setImgUrl(book.getImgUrl());
        return dto;
    }

    static Book toEntity(BookDto book) {
        Book entity =new Book();
        entity.setId(book.getId());
        entity.setIsbn(book.getIsbn());
        entity.setName(book.getName());
        entity.setAuthor(book.getAuthor());
        entity.setBookCategory(book.getBookCategory());
        entity.setPrice(book.getPrice());
        entity.setPages(book.getPages());
        entity.setDescription(book.getDescription());
        entity.setStock(book.getStock());
        entity.setImgUrl(book.getImgUrl());
        return entity;
    }
}