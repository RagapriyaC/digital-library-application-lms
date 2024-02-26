package com.raga.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.raga.library.entity.Book;
import com.raga.library.entity.BorrowingRecord;
import com.raga.library.entity.Patron;
import com.raga.library.repository.BookRepository;
import com.raga.library.repository.BorrowingRecordRepository;
import com.raga.library.repository.PatronRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the BorrowingRecord class. These tests cover the functionality
 * of BorrowingRecord
 * 
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BorrowingRecordServiceTest {

	@Mock
	private BorrowingRecordRepository borrowingRecordRepository;

	@Mock
	private PatronRepository patronRepository;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BorrowingRecordService borrowingRecordService;

	/**
	 * Test case for successfully borrowing a book
	 */
	@Test
	public void testBorrowBook() {
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");

		// When
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
		when(borrowingRecordRepository.findByBookIdAndPatronId(eq(bookId), eq(patronId)))
				.thenReturn(Collections.emptyList());
		when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		BorrowingRecord result = borrowingRecordService.borrowBook(bookId, patronId);

		// Then
		assertNotNull(result);
		assertEquals(bookId, result.getBook().getId());
		assertEquals(patronId, result.getPatron().getId());
		assertNull(result.getReturnDate());
	}

	/**
	 * Test case for successfully returning a book.
	 */
	@Test
	public void testReturnBook() {
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");
		BorrowingRecord activeBorrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);

		// When
		when(borrowingRecordRepository.findByBookIdAndPatronId(eq(bookId), eq(patronId)))
				.thenReturn(Arrays.asList(activeBorrowingRecord));
		when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		BorrowingRecord result = borrowingRecordService.returnBook(bookId, patronId);

		// Then
		assertNotNull(result);
		assertEquals(bookId, result.getBook().getId());
		assertEquals(patronId, result.getPatron().getId());
		assertNotNull(result.getReturnDate());
	}

	/**
	 * Test case for handling the case when no active borrowing record is found
	 */
	@Test
	public void testReturnBookNoActiveRecord() {
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		// When
		when(borrowingRecordRepository.findByBookIdAndPatronId(eq(bookId), eq(patronId)))
				.thenReturn(Collections.emptyList());
		// Then
		assertThrows(EntityNotFoundException.class, () -> borrowingRecordService.returnBook(bookId, patronId));
	}

	/**
	 * Test case for handling the case where an exception occurs during borrowing a
	 * book
	 */
	@Test
	public void testBorrowBookException() {
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");
		List<BorrowingRecord> activeBorrowingRecord = new ArrayList<>();
		activeBorrowingRecord.add(new BorrowingRecord(book, patron, null, null));

		// When
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
		when(borrowingRecordRepository.findByBookIdAndPatronId(eq(bookId), eq(patronId)))
				.thenReturn(activeBorrowingRecord);
		when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Then
		assertThrows(IllegalStateException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));
	}

	/**
	 * Test case for handling the case where an exception occurs during returning a
	 * book
	 */
	@Test
	public void testReturnBookException() {
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");
		List<BorrowingRecord> activeBorrowingRecord = new ArrayList<>();
		activeBorrowingRecord.add(new BorrowingRecord(book, patron, LocalDate.now(), null));
		activeBorrowingRecord.add(new BorrowingRecord(book, patron, LocalDate.now(), null));

		// When
		when(borrowingRecordRepository.findByBookIdAndPatronId(eq(bookId), eq(patronId)))
				.thenReturn(activeBorrowingRecord);

		// Then
		assertThrows(IllegalStateException.class, () -> borrowingRecordService.returnBook(bookId, patronId));
	}

	/**
	 * Test case for handling the case when the book ID is not found
	 */
	@Test
	public void testBorrowBookIdNotFound() {

		// Given
		Long bookId = 100L;
		Long patronId = 2L;
		Patron patron = new Patron(patronId, "John Doe", "123456789");

		// When
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
		when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

		// Then
		assertThrows(EntityNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));
	}

	/**
	 * Test case for handling the case when the patron ID is not found
	 */
	@Test
	public void testBorrowBookPatronIdNotFound() {

		// Given
		Long bookId = 100L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");

		// When
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

		// Then
		assertThrows(EntityNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));
	}

	/**
	 * Test case to retrieve all Borrowing records
	 */
	@Test
	public void testRetrieveAllBorrowingRecord() {
		
		// Given
		Long bookId = 1L;
		Long patronId = 2L;
		Book book = new Book(bookId, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780141182636");
		Patron patron = new Patron(patronId, "John Doe", "123456789");
		BorrowingRecord activeBorrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);
		List<BorrowingRecord> mockBorrowingRecords = new ArrayList<>();
		mockBorrowingRecords.add(activeBorrowingRecord);
		
		// When
		when(borrowingRecordRepository.findAll()).thenReturn(mockBorrowingRecords);
		List<BorrowingRecord> actualBorrowingRecords = borrowingRecordService.retrieveAllBorrowingRecord();
		
		// Then
		assertEquals(mockBorrowingRecords, actualBorrowingRecords);
	}
}
