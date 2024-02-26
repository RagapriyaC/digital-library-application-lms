package com.raga.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raga.library.entity.Book;
import com.raga.library.entity.BorrowingRecord;
import com.raga.library.entity.Patron;
import com.raga.library.repository.BookRepository;
import com.raga.library.repository.BorrowingRecordRepository;
import com.raga.library.repository.PatronRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class responsible for managing operations related to Borrowing and
 * Returns of a Book.
 */
@Service
public class BorrowingRecordService {

	@Autowired
	private PatronRepository patronRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowingRecordRepository borrowingRecordRepository;

	/**
	 * Allows a Patron to borrow a book
	 * 
	 * @param bookId   The ID of the book to be borrowed
	 * @param patronId The ID of the patron borrowing the book
	 * @return The borrowing record after the book is borrowed
	 * @throws EntityNotFoundException If the book or patron Id is not found
	 * @throws IllegalStateException If the patron already has an active borrowing record for the book
	 */
	@Transactional
	public BorrowingRecord borrowBook(Long bookId, Long patronId) {

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id : " + bookId));
		Patron patron = patronRepository.findById(patronId)
				.orElseThrow(() -> new EntityNotFoundException("Patron not found with id : " + patronId));

		// Check if there is any borrowing record and is returned or not for the given
		// book Id and and Patron Id
		List<BorrowingRecord> allBorrowingRecords = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId);
		boolean hasActiveBorrowingRecords = allBorrowingRecords.stream()
				.anyMatch(borrowingRecord -> borrowingRecord.getReturnDate() == null);

		if (hasActiveBorrowingRecords) { 
			// When the patron already has an active borrowing record
			
			throw new IllegalStateException("The patron already has an active borrowing record for the book!");
		}

		// Save a new borrowing record
		BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);
		return borrowingRecordRepository.save(borrowingRecord);
	}

	/**
	 * Allows a Patron to return a book
	 * 
	 * @param bookId   The ID of the book to be returned
	 * @param patronId The ID of the patron returning the book
	 * @return The borrowing record after the book is borrowed
	 * @throws EntityNotFoundException If No active borrowing record found for Book and Patron
	 * @throws IllegalStateException If More than one active borrowing record found for Book and Patron
	 */
	@Transactional
	public BorrowingRecord returnBook(Long bookId, Long patronId) {

		// Find all borrowing records for the given book Id and patron Id
		List<BorrowingRecord> allBorrowingRecords = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId);

		// If there are any active borrowing records and is not returned
		List<BorrowingRecord> activeBorrowingRecords = allBorrowingRecords.stream()
				.filter(borrowingRecord -> borrowingRecord.getReturnDate() == null).toList();

		if (activeBorrowingRecords.size() > 1) {
			// when more than one active borrowing record is found
			throw new IllegalStateException("More than one active borrowing record found for Book and Patron!");
		}

		if (!activeBorrowingRecords.isEmpty()) {
			// Update the return date and save
			BorrowingRecord borrowingRecord = activeBorrowingRecords.get(0);
			borrowingRecord.setReturnDate(LocalDate.now());
			return borrowingRecordRepository.save(borrowingRecord);
		} else {
			// when no active borrowing record is found
			throw new EntityNotFoundException("No active borrowing record found for Book and Patron!");
		}
	}
	
	/**
	 * Retrieves all books    
	 * 
	 * @return The list of all books   
	 */
	public List<BorrowingRecord> retrieveAllBorrowingRecord() {
		return borrowingRecordRepository.findAll();  
	}

}