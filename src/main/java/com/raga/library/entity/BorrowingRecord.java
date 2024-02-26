package com.raga.library.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * This class represents an entity for borrowing records in the library. It
 * includes attributes such as Book, Patron, borrowDate and returnDate
 */
@Entity
public class BorrowingRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@JoinColumn(name = "patron_id")
	private Patron patron;

	private LocalDate borrowDate;
	
	private LocalDate returnDate;

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Book getBook() {
		return book;
	}

	public Patron getPatron() {
		return patron;
	}
	
	public BorrowingRecord() {
		super();
	}

	public BorrowingRecord( Book book, Patron patron, LocalDate borrowDate, LocalDate returnDate) {
		super();
		this.book = book;
		this.patron = patron;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
	}

}
