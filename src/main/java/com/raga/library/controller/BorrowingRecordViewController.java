package com.raga.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.raga.library.entity.Book;
import com.raga.library.entity.BorrowingRecord;
import com.raga.library.entity.Patron;
import com.raga.library.service.BookService;
import com.raga.library.service.BorrowingRecordService;
import com.raga.library.service.PatronService;

/**
 * Controller class responsible for handling HTTP requests related to borrowing
 * records in the library application
 */
@Controller
@RequestMapping("/library")
public class BorrowingRecordViewController {

	@Autowired
	private BorrowingRecordService borrowingRecordService;

	@Autowired
	private BookService bookService;

	@Autowired
	private PatronService patronService;

	/**
	 * Retrieves all borrowing records and displays them
	 * 
	 * @return ModelAndView object representing the view with the list of borrowing
	 *         records
	 */
	@GetMapping("/borrowingRecords")
	public ModelAndView retrieveAllBooks() {
		List<BorrowingRecord> borrowingRecord = borrowingRecordService.retrieveAllBorrowingRecord();
		ModelAndView modelAndView = new ModelAndView("borrowingRecord-list");
		modelAndView.addObject("borrowingRecord", borrowingRecord);
		return modelAndView;
	}

	/**
	 * Displays the form for borrowing a book
	 * 
	 * @return ModelAndView object representing the form for borrowing a book
	 */
	@GetMapping("/borrowingBook")
	public ModelAndView borrowBookForm() {
		ModelAndView modelAndView = new ModelAndView("borrow-book");
		return modelAndView;
	}

	/**
	 * Displays the form for returning a book
	 * 
	 * @return ModelAndView object representing the form for returning a book
	 */
	@GetMapping("/returningBook")
	public ModelAndView returnBookRecord() {
		List<BorrowingRecord> borrowingRecord = borrowingRecordService.retrieveAllBorrowingRecord();
		ModelAndView modelAndView = new ModelAndView("return-book");
		modelAndView.addObject("borrowingRecord", borrowingRecord);
		return modelAndView;
	}

	/**
	 * Method that allows a patron to borrow a book
	 * 
	 * @param bookId   The ID of the book to be borrowed
	 * @param patronId The ID of the patron borrowing the book
	 * @return ModelAndView object representing the success message or error message
	 */
	@GetMapping("/borrow/{bookId}/patron/{patronId}")
	public ModelAndView borrowABook(@PathVariable Long bookId, @PathVariable Long patronId) {

		ModelAndView modelAndView = new ModelAndView("borrow-success");

		try {
			BorrowingRecord borrowingRecord = borrowingRecordService.borrowBook(bookId, patronId);
			Book book = bookService.retrieveBook(bookId);
			Patron patron = patronService.retrievePatron(patronId);
			modelAndView.addObject("message", "Book Borrowed Successfully on " + borrowingRecord.getBorrowDate());
			modelAndView.addObject("book", book);
			modelAndView.addObject("patron", patron);
		} catch (Exception e) {
			modelAndView.addObject("errorMessage", "Error in borrowing the book : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Record the return of a borrowed book by a patron
	 * 
	 * @param bookId   The ID of the book to be returned
	 * @param patronId The ID of the patron returning the book
	 * @return ModelAndView object representing the success message or error message
	 */
	@GetMapping("/return/{bookId}/patron/{patronId}")
	public ModelAndView returnABook(@PathVariable Long bookId, @PathVariable Long patronId) {

		ModelAndView modelAndView = new ModelAndView("return-success");

		try {
			BorrowingRecord borrowingRecord = borrowingRecordService.returnBook(bookId, patronId);
			Book book = bookService.retrieveBook(bookId);
			Patron patron = patronService.retrievePatron(patronId);
			modelAndView.addObject("message", "Book Returned Successfully on " + borrowingRecord.getReturnDate());
			modelAndView.addObject("book", book);
			modelAndView.addObject("patron", patron);
		} catch (Exception e) {
			modelAndView.addObject("errorMessage", "Error in returning the book : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}
}
