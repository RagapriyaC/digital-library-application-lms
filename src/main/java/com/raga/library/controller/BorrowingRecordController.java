package com.raga.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raga.library.entity.BorrowingRecord;
import com.raga.library.service.BorrowingRecordService;

/**
 * This class is responsible for handling RESTful endpoints for managing
 * Borrowing and returning records in the library
 */
@RestController
@RequestMapping("/library/api")
public class BorrowingRecordController {

	@Autowired
	private BorrowingRecordService borrowingRecordService;

	/**
	 * Retrieves all borrowing records
	 * 
	 * @return list of all borrowing
	 *         records
	 */
	@GetMapping("/borrowingRecords")
	public List<BorrowingRecord> retrieveAllBooks() {
		return borrowingRecordService.retrieveAllBorrowingRecord();
	}
	
	/**
	 * Method that allows a patron to borrow a book
	 *
	 * @param bookId The ID of the book to be borrowed
	 * @param patronId The ID of the patron borrowing the book
	 * @return ResponseEntity containing the result of the borrow record
	 */
	@PostMapping("/borrow/{bookId}/patron/{patronId}")
	public ResponseEntity<?> borrowABook(@PathVariable Long bookId, @PathVariable Long patronId) {
		try {
			BorrowingRecord borrowingRecord = borrowingRecordService.borrowBook(bookId, patronId);
			
			return ResponseEntity.status(HttpStatus.CREATED).body("Book Borrowed Successfully on " + borrowingRecord.getBorrowDate());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error in borrowing the book: " + e.getMessage());
		}
	}
}
