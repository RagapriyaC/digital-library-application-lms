package com.raga.library.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.raga.library.entity.Book;
import com.raga.library.entity.BorrowingRecord;
import com.raga.library.entity.Patron;
import com.raga.library.service.BookService;
import com.raga.library.service.BorrowingRecordService;
import com.raga.library.service.PatronService;

/**
 * Unit tests for the BorrowingRecordController class. These tests cover the
 * functionality of BorrowingRecordController
 * 
 */
@WebMvcTest
public class BorrowingRecordControllerTest {

	@InjectMocks
	private BorrowingRecordController borrowingRecordController;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@MockBean
	private PatronService patronService;

	@MockBean
	private BorrowingRecordService borrowingRecordService;

	/**
	 * Test case for borrowing a book.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testBorrowABook() throws Exception {
		// Given
		Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(2L, "John Doe", "123456789");
		BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);

		given(borrowingRecordService.borrowBook(1L, 2L)).willReturn(borrowingRecord);

		// When
		ResultActions resultActions = mockMvc.perform(post("/library/api/borrow/1/patron/2"));

		// Then
		resultActions.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
	}

	/**
	 * Test case for handling errors during borrowing a book.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testBorrowABookError() throws Exception {
		given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willThrow(new RuntimeException("Some error"));

		// When
		ResultActions resultActions = mockMvc.perform(post("/library/api/borrow/1/patron/2"));

		// Then
		resultActions.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
				.andExpect(content().string("Error in borrowing the book: Some error"));
	}

	/**
	 * Test case for returning a book.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testReturnABook() throws Exception {
		// Given
		Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(2L, "John Doe", "123456789");
		BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);

		given(borrowingRecordService.returnBook(1L, 2L)).willReturn(borrowingRecord);

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/return/1/patron/2"));

		// Then
		resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
	}

	/**
	 * Test case for handling errors during returning a book.
	 * 
	 * @throws Exception if an error occurs during the test.
	 */
	@Test
	public void testReturnABookError() throws Exception {
		// Given
		given(borrowingRecordService.returnBook(anyLong(), anyLong())).willThrow(new RuntimeException("Some error"));

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/return/1/patron/2"));

		// Then
		resultActions.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
				.andExpect(content().string("Error in returning the book: Some error"));
	}
	
	/**
	 * Test case to retrieve all Borrowing records
	 * @throws Exception 
	 */
	@Test
	public void testRetrieveAllBorrowingRecord() throws Exception {
		
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");
		BorrowingRecord activeBorrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);
		List<BorrowingRecord> mockBorrowingRecords = new ArrayList<>();
		mockBorrowingRecords.add(activeBorrowingRecord);
				
		given(borrowingRecordService.retrieveAllBorrowingRecord()).willReturn(mockBorrowingRecords);

		ResultActions resultActions = mockMvc.perform(get("/library/api/borrowingRecords"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].book.title").value("The Great Gatsby"))
				.andExpect(jsonPath("$[0].book.author").value("F. Scott Fitzgerald"));
				
	}
}
