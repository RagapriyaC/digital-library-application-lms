package com.raga.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.raga.library.entity.Book;
import com.raga.library.exception.MethodArgumentNotValidException;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;

import jakarta.validation.Valid;

/**
 * Controller class responsible for handling HTTP requests for managing Books
 */
@Controller
@RequestMapping("/library")
public class BookViewController {

	@Autowired
	private BookService bookService;

	/**
	 * Displays the home page of the library application
	 * 
	 * @return ModelAndView object representing the home page view
	 */
	@GetMapping
	public ModelAndView viewHomePage() {
		ModelAndView modelAndView = new ModelAndView("home-page");
		return modelAndView;
	}

	/**
	 * Displays the dashboard of the library application
	 * 
	 * @return ModelAndView object representing the dashboard view
	 */
	@GetMapping("/dashboard")
	public ModelAndView viewDashboard() {
		ModelAndView modelAndView = new ModelAndView("dashboard");
		return modelAndView;
	}

	/**
	 * Retrieves a list of all books and displays them
	 * 
	 * @return ModelAndView object representing the view with the list of books
	 */
	@GetMapping("/books")
	public ModelAndView retrieveAllBooks() {
		List<Book> books = bookService.retrieveAllBooks();
		ModelAndView modelAndView = new ModelAndView("book-list");
		modelAndView.addObject("books", books);
		return modelAndView;
	}

	/**
	 * Retrieves a book by its ID and displays its details
	 * 
	 * @param id The ID of the book to retrieve.
	 * @return ModelAndView object representing the view with the book details
	 */
	@GetMapping("/books/{id}")
	public ModelAndView retrieveBookById(@PathVariable Long id) {
		Book book;
		ModelAndView modelAndView = new ModelAndView("book-details");

		try {
			book = bookService.retrieveBook(id);
			modelAndView.addObject("book", book);
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in retrieving Book : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Displays the form to add a new book
	 * 
	 * @return ModelAndView object representing the form to add a new book
	 */
	@GetMapping("/books/new")
	public ModelAndView showAddBookForm() {
		ModelAndView modelAndView = new ModelAndView("add-book");
		modelAndView.addObject("book", new Book());
		return modelAndView;
	}

	/**
	 * Adds a new book to the library
	 * 
	 * @param book          The book object to add
	 * @param bindingResult BindingResult object to handle validation errors
	 * @return ModelAndView object representing the redirection to the book list
	 *         view or the add book form if there are errors
	 * @throws MethodArgumentNotValidException If the book object fails validation
	 */
	@PostMapping("/books")
	public ModelAndView addNewBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult)
			throws MethodArgumentNotValidException {

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("add-book");
			modelAndView.addObject("book", book);
			return modelAndView;
		}

		bookService.saveBook(book);
		return new ModelAndView("redirect:/library/books");
	}

	/**
	 * Displays the form to edit a book
	 * 
	 * @param id The ID of the book to edit
	 * @return ModelAndView object representing the form to edit the book
	 */
	@GetMapping("/books/{id}/edit")
	public ModelAndView showEditBookForm(@PathVariable Long id) {
		Book book;
		ModelAndView modelAndView = new ModelAndView("edit-book");
		try {
			book = bookService.retrieveBook(id);
			modelAndView.addObject("book", book);
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in Editing Book : " + e.getMessage());
			modelAndView.setViewName("error");
		}

		return modelAndView;
	}

	/**
	 * Updates an existing book's information
	 * 
	 * @param id            The ID of the book to update
	 * @param updatedBook   The updated book object
	 * @param bindingResult BindingResult object to handle validation errors
	 * @return ModelAndView object representing the redirection to the book list
	 *         view or the edit book form if there are errors
	 * @throws MethodArgumentNotValidException If the updated book object fails
	 *                                         validation
	 */
	@PostMapping("/books/{id}/edit")
	public ModelAndView updateBookById(@PathVariable Long id, @Valid @ModelAttribute("book") Book updatedBook,
			BindingResult bindingResult) throws MethodArgumentNotValidException {

		ModelAndView modelAndView = new ModelAndView();  

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView1 = new ModelAndView("edit-book"); 
			modelAndView1.addObject("book", updatedBook);
			return modelAndView1; 
		}

		Book existingBook; 

		try {
			existingBook = bookService.retrieveBook(id);
			existingBook.setTitle(updatedBook.getTitle());
			existingBook.setAuthor(updatedBook.getAuthor());
			existingBook.setPublicationYear(updatedBook.getPublicationYear());
			existingBook.setIsbn(updatedBook.getIsbn());
			bookService.saveBook(existingBook);
			modelAndView.setViewName("redirect:/library/books");
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in updating Book : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Removes a book from the library
	 * 
	 * @param id The ID of the book to delete.
	 * @return ModelAndView object representing the redirection to the book list
	 *         view.
	 * @throws ResourceNotFoundException If the book to delete is not found.
	 */
	@PostMapping("/books/{id}/delete")
	public ModelAndView deleteBookById(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			bookService.retrieveBook(id);
			bookService.deleteBook(id);
			return new ModelAndView("redirect:/library/books");
		} catch (Exception e) {
			modelAndView.addObject("errorMessage", "Error deleting Book: Borrowing record found. Cannot delete a Book with active borrowing record !!!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

}
