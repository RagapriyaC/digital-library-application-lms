package com.raga.library.controller;

import com.raga.library.entity.Book;
import com.raga.library.exception.MethodArgumentNotValidException;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BookViewController class. These tests cover the
 * functionality of BookViewController
 * 
 */
public class BookViewControllerTest {

	@Mock
	private BookService bookService;

	@InjectMocks
	private BookViewController bookViewController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Test to view HomePage
	 */

	@Test
	public void testViewHomePage() {
		ModelAndView modelAndView = bookViewController.viewHomePage();
		assertEquals("home-page", modelAndView.getViewName());
	}

	/**
	 * Test to view dashboard
	 */
	@Test
	public void testViewDashboard() {
		ModelAndView modelAndView = bookViewController.viewDashboard();
		assertEquals("dashboard", modelAndView.getViewName());
	}

	/**
	 * Test for retrieval of all books
	 */
	@Test
	public void testRetrieveAllBooks() {
		List<Book> mockBooks = new ArrayList<>();
		when(bookService.retrieveAllBooks()).thenReturn(mockBooks);

		ModelAndView modelAndView = bookViewController.retrieveAllBooks();

		assertEquals("book-list", modelAndView.getViewName());
		assertEquals(mockBooks, modelAndView.getModel().get("books"));
	}

	/**
	 * Test for retrieving a book by ID successfully
	 *
	 * @throws ResourceNotFoundException if the book is not found
	 */
	@Test
	public void testRetrieveBookById() throws ResourceNotFoundException {
		long id = 1L;
		Book mockBook = new Book();

		when(bookService.retrieveBook(id)).thenReturn(mockBook);

		ModelAndView modelAndView = bookViewController.retrieveBookById(id);

		assertEquals("book-details", modelAndView.getViewName());
		assertEquals(mockBook, modelAndView.getModel().get("book"));
	}

	/**
	 * Test for retrieving a book by ID when a ResourceNotFoundException is thrown
	 *
	 * @throws ResourceNotFoundException if the book is not found
	 */
	@Test
	public void testRetrieveBookById1() throws ResourceNotFoundException {
		long id = 1L;
		when(bookService.retrieveBook(id)).thenThrow(ResourceNotFoundException.class);

		ModelAndView modelAndView = bookViewController.retrieveBookById(id);

		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test for showing the add book form
	 */
	@Test
	public void testShowAddBookForm() {
		ModelAndView modelAndView = bookViewController.showAddBookForm();

		assertEquals("add-book", modelAndView.getViewName());
	}

	/**
	 * Test for adding a new book with a valid book object
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testAddNewBook_WithValidBook() throws Exception {
		Book mockBook = new Book();
		BindingResult bindingResult = mock(BindingResult.class);

		ModelAndView modelAndView = bookViewController.addNewBook(mockBook, bindingResult);

		assertEquals("redirect:/library/books", modelAndView.getViewName());
	}

	/**
	 * Test for adding a new book with an invalid book object
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testAddNewBook_WithInvalidBook() throws Exception {
		Book mockBook = new Book();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		ModelAndView modelAndView = bookViewController.addNewBook(mockBook, bindingResult);

		assertEquals("add-book", modelAndView.getViewName());
		assertEquals(mockBook, modelAndView.getModel().get("book"));
	}

	/**
	 * Test for showing the edit book form
	 *
	 * @throws ResourceNotFoundException if the book is not found
	 */
	@Test
	public void testShowEditBookForm() throws ResourceNotFoundException {
		long id = 1L;
		Book mockBook = new Book();
		when(bookService.retrieveBook(id)).thenReturn(mockBook);

		ModelAndView modelAndView = bookViewController.showEditBookForm(id);

		assertEquals("edit-book", modelAndView.getViewName());
		assertEquals(mockBook, modelAndView.getModel().get("book"));
	}

	/**
	 * Test for updating a book by ID with a valid book object
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdateBookByIdWithValidBook() throws Exception {
		long id = 1L;
		Book mockBook = new Book();
		Book updatedBook = new Book();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(bookService.retrieveBook(id)).thenReturn(mockBook);

		ModelAndView modelAndView = bookViewController.updateBookById(id, updatedBook, bindingResult);

		assertEquals("redirect:/library/books", modelAndView.getViewName());
	}

	/**
	 * Test for updating a book by ID with an invalid book object
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdateBookByIdWithInvalidBook() throws Exception {
		long id = 1L;
		Book mockBook = new Book();
		Book updatedBook = new Book();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		when(bookService.retrieveBook(id)).thenReturn(mockBook);

		ModelAndView modelAndView = bookViewController.updateBookById(id, updatedBook, bindingResult);

		assertEquals("edit-book", modelAndView.getViewName());
	}

	/**
	 * Test for deleting a book by ID
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeleteBookById() throws Exception {
		long id = 1L;
		ModelAndView modelAndView = bookViewController.deleteBookById(id);
		assertEquals("redirect:/library/books", modelAndView.getViewName());
	}

	/**
	 * Test for showing the edit book form when a ResourceNotFoundException is
	 * thrown
	 *
	 * @throws ResourceNotFoundException if the book is not found
	 */
	@Test
	public void testShowEditBookForm1() throws ResourceNotFoundException {
		long id = 1L;
		when(bookService.retrieveBook(id)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = bookViewController.showEditBookForm(id);
		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test for updating a book by ID when a ResourceNotFoundException is thrown
	 *
	 * @throws ResourceNotFoundException       if the book is not found
	 * @throws MethodArgumentNotValidException if the method argument is not valid
	 */
	@Test
	public void testUpdateBookById() throws ResourceNotFoundException, MethodArgumentNotValidException {
		long id = 1L;
		Book updatedBook = new Book();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(bookService.retrieveBook(id)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = bookViewController.updateBookById(id, updatedBook, bindingResult);
		assertEquals("error", modelAndView.getViewName());
	}
	
	/**
	 * Test case for deleting a book by ID when the book is not found
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeleteBookById2() throws Exception {
		long id = 1L;
		when(bookService.retrieveBook(id)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = bookViewController.deleteBookById(id);
		assertEquals("error", modelAndView.getViewName());
	}

}
