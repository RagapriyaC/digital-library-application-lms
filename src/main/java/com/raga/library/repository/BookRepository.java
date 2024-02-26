package com.raga.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raga.library.entity.Book;

/**
 * Repository interface responsible for performing CRUD operations on the Book
 * entity
 * 
 */
public interface BookRepository extends JpaRepository<Book, Long> {

}
