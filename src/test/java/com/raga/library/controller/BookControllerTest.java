package com.raga.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raga.library.entity.Book;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;
import com.raga.library.service.BorrowingRecordService;
import com.raga.library.service.PatronService;

/**
 * Unit tests for the BookController class. These tests cover the functionality
 * of BookController
 * 
 */
@WebMvcTest
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@MockBean
	private PatronService patronService;

	@MockBean
	private BorrowingRecordService borrowingRecordService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test case for retrieving all Books.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testRetrieveAllBooks() throws Exception {

		// given
		Book book1 = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Book book2 = new Book(2L, "The Catcher in the Rye", "J.D. Salinger", 1900, "1234567890123");
		List<Book> listOfBooks = Arrays.asList(book1, book2);
		given(bookService.retrieveAllBooks()).willReturn(listOfBooks);

		// when
		ResultActions resultActions = mockMvc.perform(get("/library/api/books"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].title").value("The Great Gatsby"))
				.andExpect(jsonPath("$[0].author").value("F. Scott Fitzgerald"))
				.andExpect(jsonPath("$[1].title").value("The Catcher in the Rye"))
				.andExpect(jsonPath("$[1].author").value("J.D. Salinger"));
	}

	/**
	 * Test case for retrieving a Book by ID.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testRetrieveBookById() throws Exception {

		// Given
		Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		given(bookService.retrieveBook(1L)).willReturn(book);

		// When
		ResultActions resultActions = mockMvc
				.perform(get("/library/api/books/1").contentType(MediaType.APPLICATION_JSON));

		// Then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("The Great Gatsby"))
				.andExpect(jsonPath("$.author").value("F. Scott Fitzgerald"));

	}

	/**
	 * Test case for Creating a new Book.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testCreateBook() throws Exception {

		// given
		Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		given(bookService.saveBook(ArgumentMatchers.any(Book.class))).willReturn(book);

		// when
		ResultActions resultActions = mockMvc.perform(post("/library/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book)));

		// then
		resultActions.andDo(print()).andExpect(status().isCreated());
	}

	/**
	 * Test case for updating a Book by ID.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testUpdateBookById() throws Exception {

		// Given
		Long bookId = 1L;

		Book existingBook = new Book(bookId, "The Catcher in the Rye", "J.D. Salinger", 1900, "1234567890123");
		Book updatedBook = new Book(null, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");

		when(bookService.retrieveBook(bookId)).thenReturn(existingBook);
		when(bookService.saveBook(any(Book.class))).thenReturn(existingBook);

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/books/1")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedBook)));

		// Then
		resultActions.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * Test case for deleting a Book by ID.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testDeleteBookById() throws Exception {

		// Given
		given(bookService.retrieveBook(1L))
				.willReturn(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636"));

		// When
		ResultActions resultActions = mockMvc.perform(delete("/library/api/books/1"));

		// Then
		resultActions.andExpect(status().isOk()).andExpect(content().string("Book Deleted Successfully"));
	}

	/**
	 * Test case for creating a Book using Invalid Input Data.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testCreateBookInvalidInput() throws Exception {
		// Given
		Book book = new Book(1L, "", "", null, "");
		given(bookService.saveBook(ArgumentMatchers.any(Book.class))).willReturn(book);

		// When
		ResultActions resultActions = mockMvc.perform(post("/library/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book)));

		// Then
		resultActions.andExpect(status().isBadRequest());
	}

	/**
	 * Test case for deleting a Book by ID which is not found.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testDeleteBookByIdNotFound() throws Exception {
		// Given
		doThrow(new ResourceNotFoundException("Book not found")).when(bookService).retrieveBook(1L);

		// When
		ResultActions resultActions = mockMvc.perform(delete("/library/api/books/1"));

		// Then
		resultActions.andExpect(status().isOk());
	}

	/**
	 * Test case for updating a Book using Invalid Input Data.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testUpdateBookInvalidInput() throws Exception {
		// Given
		Book book = new Book(1L, "T", "F", 900, "9780141182636");
		given(bookService.saveBook(ArgumentMatchers.any(Book.class))).willReturn(book);

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/books/1")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book)));

		// Then
		resultActions.andExpect(status().isBadRequest());
	}

	/**
	 * Test case for retrieving a Book by ID which is not found.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testRetrieveBookByIdNotFound() throws Exception {
		// Given
		given(bookService.retrieveBook(1L)).willThrow(new ResourceNotFoundException("Book not found"));

		// When
		ResultActions resultActions = mockMvc
				.perform(get("/library/api/books/1").contentType(MediaType.APPLICATION_JSON));

		// Then
		resultActions.andExpect(status().isNotFound());
	}
		 
}
