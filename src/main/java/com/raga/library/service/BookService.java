package com.raga.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raga.library.entity.Book;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.repository.BookRepository;

/**
 * Service class responsible for managing CRUD operations related to Books.
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Saves a Book
	 * 
	 * @param book to be saved
	 * @return The saved book.
	 */
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	/**
	 * Retrieves a book by ID
	 * 
	 * @param id - ID of the book to be retrieved
	 * @return The retrieved book.
	 * @throws ResourceNotFoundException
	 */
	public Book retrieveBook(Long id) throws ResourceNotFoundException {
		return bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));
	}

	/**
	 * Retrieves all books
	 * 
	 * @return The list of all books
	 */
	public List<Book> retrieveAllBooks() {
		return bookRepository.findAll();
	}

	/**
	 * Deletes a book by ID
	 * 
	 * @param id - ID of the book to be deleted
	 */
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
}
