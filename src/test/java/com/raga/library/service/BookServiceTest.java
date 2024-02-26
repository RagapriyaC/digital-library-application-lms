package com.raga.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raga.library.entity.Book;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.repository.BookRepository;

/**
 * Unit tests for the BookService class. These tests cover the functionality of
 * BookService
 * 
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	/**
	 * Test case for saving a Book
	 */
	@Test
	public void testSaveBook() {
		// Given
		Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		given(bookRepository.save(any(Book.class))).willReturn(book);

		// When
		Book savedBook = bookService.saveBook(book);

		// Then
		assertThat(savedBook).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("The Great Gatsby");
	}

	/**
	 * Test case for retrieving a Book by ID
	 * 
	 * @throws ResourceNotFoundException if the book is not found
	 */
	@Test
	public void testRetrieveBookById() throws ResourceNotFoundException {
		// Given
		Optional<Book> book = Optional
				.of(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636"));
		given(bookRepository.findById(1L)).willReturn(book);

		// When
		Book retrievedBook = bookService.retrieveBook(1L);

		// Then
		assertThat(retrievedBook).isNotNull();
		assertThat(retrievedBook.getTitle()).isEqualTo("The Great Gatsby");
	}

	/**
	 * Test case for retrieving all books
	 * 
	 * @throws ResourceNotFoundException if no books are found
	 */
	@Test
	public void testRetrieveAllBooks() throws ResourceNotFoundException {
		Book book1 = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Book book2 = new Book(2L, "The Catcher in the Rye", "J.D. Salinger", 1900, "1234567890123");
		List<Book> listOfBooks = Arrays.asList(book1, book2);
		given(bookRepository.findAll()).willReturn(listOfBooks);

		// When
		List<Book> listOfBooks1 = bookService.retrieveAllBooks();

		// Then
		assertThat(listOfBooks1).isNotNull();
		assertThat(listOfBooks1).hasSize(2);
		assertThat(listOfBooks1.get(0).getTitle()).isEqualTo("The Great Gatsby");
		assertThat(listOfBooks1.get(1).getAuthor()).isEqualTo("J.D. Salinger");
	}

	/**
	 * Test case for deleting a book
	 */
	@Test
	public void testDeleteBook() {
		bookService.deleteBook(1L);
	}

	/**
	 * Test case retrieving a patron Id which is not found
	 */
	@Test
	public void testRetrieveBookByIdNotFound() {
		// Given
		given(bookRepository.findById(1000L)).willReturn(Optional.empty());

		// When/Then
		assertThrows(ResourceNotFoundException.class, () -> bookService.retrieveBook(1000L));
	}

}
