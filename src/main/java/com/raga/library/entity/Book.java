package com.raga.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * This class represents an entity for books in the library. It includes
 * attributes such as ID, title, author, publication year and ISBN
 */
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Book Title is required")
	@Size(min = 2, message = "Title should have at least 2 characters")
	private String title;

	@NotEmpty(message = "Author Name is required")
	@Size(min = 3, message = "Author name should have at least 3 characters")
	private String author;

	@NotNull(message = "Publication Year is required")
	@Min(value = 1000, message = "Publication year must be greater than or equal to 1000")
	@Max(value = 2024, message = "Publication year must be less than or equal to 2024")
	private Integer publicationYear;

	@NotEmpty(message = "ISBN is required")
	@Pattern(regexp = "^[0-9]{13}$", message = "ISBN must be a 13-digit number")
	private String isbn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Book() {
		super();
	}

	public Book(Long id,
			@NotEmpty(message = "Book Title is required") @Size(min = 2, message = "Title should have at least 2 characters") String title,
			@NotEmpty(message = "Author Name is required") @Size(min = 3, message = "Author name should have at least 3 characters") String author,
			@NotNull(message = "Publication Year is required") @Min(value = 1000, message = "Publication year must be greater than or equal to 1000") @Max(value = 2024, message = "Publication year must be less than or equal to 2024") Integer publicationYear,
			@NotEmpty(message = "ISBN is required") @Pattern(regexp = "^[0-9]{13}$", message = "ISBN must be a 13-digit number") String isbn) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.isbn = isbn;    
	}
}
