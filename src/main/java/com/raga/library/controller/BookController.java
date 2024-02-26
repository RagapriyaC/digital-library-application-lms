package com.raga.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raga.library.entity.Book;
import com.raga.library.exception.MethodArgumentNotValidException;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;

import jakarta.validation.Valid;

/**
 * This class is responsible for handling RESTful endpoints for managing Books
 * in the library in the library
 */
@RestController
@RequestMapping("/library/api")
public class BookController {

	@Autowired
	private BookService bookService;

	/**
	 * Retrieves a list of all books
	 *
	 * @return List of all books
	 */
	@GetMapping("/books")
	public List<Book> retrieveAllBooks() {
		return bookService.retrieveAllBooks();
	}

	/**
	 * Retrieves details of a specific book by ID
	 *
	 * @param id ID of the book to retrieve
	 * @return ResponseEntity containing the book details
	 * @throws ResourceNotFoundException If the book ID is not found
	 */
	@GetMapping("/books/{id}")
	public ResponseEntity<Book> retrieveBookById(@PathVariable Long id) throws ResourceNotFoundException {
		Book book = bookService.retrieveBook(id);
		return ResponseEntity.ok().body(book);
	}

	/**
	 * Adds a new book to the library
	 *
	 * @param book          The new book to be added
	 * @param bindingResult to handle validation errors
	 * @return ResponseEntity containing the added book
	 * @throws MethodArgumentNotValidException If validation errors occur
	 */
	@PostMapping("/books")
	public ResponseEntity<String> addNewBook(@Valid @RequestBody Book book, BindingResult bindingResult)
			throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(bindingResult);
		}
		bookService.saveBook(book);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Book added successfully with title : " + book.getTitle());
	}

	/**
	 * Updates an existing book's information
	 *
	 * @param id            The ID of the book to be updated
	 * @param book          The updated book information
	 * @param bindingResult BindingResult to handle validation errors
	 * @return ResponseEntity containing the updated book
	 * @throws MethodArgumentNotValidException If validation errors occur
	 * @throws ResourceNotFoundException       If book id is not found
	 */
	@PutMapping("/books/{id}")
	public ResponseEntity<String> updateBookById(@PathVariable Long id, @Valid @RequestBody Book updatedBook,
			BindingResult bindingResult) throws MethodArgumentNotValidException, ResourceNotFoundException {
		if (bindingResult.hasErrors())
			if (bindingResult.hasErrors()) {
				throw new MethodArgumentNotValidException(bindingResult);
			}

		Book existingBook = bookService.retrieveBook(id);
		existingBook.setTitle(updatedBook.getTitle());
		existingBook.setAuthor(updatedBook.getAuthor());
		existingBook.setPublicationYear(updatedBook.getPublicationYear());
		existingBook.setIsbn(updatedBook.getIsbn());

		bookService.saveBook(existingBook);
		return ResponseEntity.status(HttpStatus.OK).body("Book has been Updated successfully");
	}

	/**
	 * Removes a book from the library
	 *
	 * @param id The ID of the book to be deleted
	 * @return Success message indicating the book deletion
	 * @throws ResourceNotFoundException If the book with the given ID is not found
	 */
	@DeleteMapping("/books/{id}")
	public String deleteBookById(@PathVariable Long id){
		try {
			bookService.retrieveBook(id);
			bookService.deleteBook(id);
			return "Book Deleted Successfully";
		} catch (Exception e) {
			return "Error deleting Book: Borrowing record found. Cannot delete a Book with active borrowing record !!!";
		}
	}

}
