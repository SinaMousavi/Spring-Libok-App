package com.libok.springlibrarymallapp.repository;

import com.libok.springlibrarymallapp.model.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select a from Book a where lower(a.name) like lower(concat('%', :search, '%')) " +
            "or lower(a.author) like lower(concat('%', :search, '%'))")
    List<Book> findAllByNameOrAuthor(@Param("search") String search);

}
