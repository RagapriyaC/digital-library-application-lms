package com.raga.library.controller;

import com.raga.library.entity.Book;
import com.raga.library.entity.BorrowingRecord;
import com.raga.library.entity.Patron;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;
import com.raga.library.service.BorrowingRecordService;
import com.raga.library.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BorrowingRecordViewController class. These tests cover the
 * functionality of BorrowingRecordViewController
 * 
 */
public class BorrowingRecordViewControllerTest {

	@Mock
	private BorrowingRecordService borrowingRecordService;

	@Mock
	private BookService bookService;

	@Mock
	private PatronService patronService;

	@InjectMocks
	private BorrowingRecordViewController borrowingRecordViewController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the retrieval of all borrowing records
	 */
	@Test
	public void testRetrieveAllBooks() {
		List<BorrowingRecord> mockBorrowingRecords = new ArrayList<>();
		when(borrowingRecordService.retrieveAllBorrowingRecord()).thenReturn(mockBorrowingRecords);

		ModelAndView modelAndView = borrowingRecordViewController.retrieveAllBooks();

		assertEquals("borrowingRecord-list", modelAndView.getViewName());
		assertEquals(mockBorrowingRecords, modelAndView.getModel().get("borrowingRecord"));
	}

	/**
	 * Test for borrowing a book successfully
	 *
	 * @throws ResourceNotFoundException if the book or patron is not found
	 */
	@Test
	public void testBorrowABook() throws ResourceNotFoundException {
		long bookId = 1L;
		long patronId = 1L;
		BorrowingRecord mockBorrowingRecord = new BorrowingRecord();
		Book mockBook = new Book();
		Patron mockPatron = new Patron();

		when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(mockBorrowingRecord);
		when(bookService.retrieveBook(bookId)).thenReturn(mockBook);
		when(patronService.retrievePatron(patronId)).thenReturn(mockPatron);

		ModelAndView modelAndView = borrowingRecordViewController.borrowABook(bookId, patronId);

		assertEquals("borrow-success", modelAndView.getViewName());
		assertEquals("Book Borrowed Successfully on " + mockBorrowingRecord.getBorrowDate(),
				modelAndView.getModel().get("message"));
	}

	/**
	 * Test for borrowing a book when a ResourceNotFoundException is thrown for the
	 * patron
	 *
	 * @throws ResourceNotFoundException if the book or patron is not found
	 */
	@Test
	public void testBorrowABook1() throws ResourceNotFoundException {
		long bookId = 1L;
		long patronId = 1L;
		BorrowingRecord mockBorrowingRecord = new BorrowingRecord();
		Book mockBook = new Book();
		when(borrowingRecordService.returnBook(bookId, patronId)).thenReturn(mockBorrowingRecord);
		when(bookService.retrieveBook(bookId)).thenReturn(mockBook);
		when(patronService.retrievePatron(patronId)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = borrowingRecordViewController.borrowABook(bookId, patronId);
		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test for returning a book successfully
	 *
	 * @throws ResourceNotFoundException if the book or patron is not found
	 */
	@Test
	public void testReturnABook() throws ResourceNotFoundException {
		long bookId = 1L;
		long patronId = 1L;
		BorrowingRecord mockBorrowingRecord = new BorrowingRecord();
		Book mockBook = new Book();
		Patron mockPatron = new Patron();

		when(borrowingRecordService.returnBook(bookId, patronId)).thenReturn(mockBorrowingRecord);
		when(bookService.retrieveBook(bookId)).thenReturn(mockBook);
		when(patronService.retrievePatron(patronId)).thenReturn(mockPatron);

		ModelAndView modelAndView = borrowingRecordViewController.returnABook(bookId, patronId);

		assertEquals("return-success", modelAndView.getViewName());
		assertEquals("Book Returned Successfully on " + mockBorrowingRecord.getReturnDate(),
				modelAndView.getModel().get("message"));
	}

	/**
	 * Test for returning a book when a ResourceNotFoundException is thrown for the
	 * patron
	 *
	 * @throws ResourceNotFoundException if the book or patron is not found
	 */
	@Test
	public void testReturnABook1() throws ResourceNotFoundException {
		long bookId = 1L;
		long patronId = 1L;
		BorrowingRecord mockBorrowingRecord = new BorrowingRecord();
		Book mockBook = new Book();
		when(borrowingRecordService.returnBook(bookId, patronId)).thenReturn(mockBorrowingRecord);
		when(bookService.retrieveBook(bookId)).thenReturn(mockBook);
		when(patronService.retrievePatron(patronId)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = borrowingRecordViewController.returnABook(bookId, patronId);
		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test for showing the form to borrow a book
	 */
	@Test
	public void testBorrowBookForm() {
		ModelAndView modelAndView = borrowingRecordViewController.borrowBookForm();
		assertEquals("borrow-book", modelAndView.getViewName());
	}

	/**
	 * Test for showing the borrowing records for returning a book
	 */
	@Test
	public void testReturnBookRecord() {
		List<BorrowingRecord> mockBorrowingRecords = new ArrayList<>();
		when(borrowingRecordService.retrieveAllBorrowingRecord()).thenReturn(mockBorrowingRecords);

		ModelAndView modelAndView = borrowingRecordViewController.returnBookRecord();

		assertEquals("return-book", modelAndView.getViewName());
		assertEquals(mockBorrowingRecords, modelAndView.getModel().get("borrowingRecord"));
	}
}
