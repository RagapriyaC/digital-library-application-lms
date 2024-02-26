package com.raga.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raga.library.entity.Patron;
import com.raga.library.exception.MethodArgumentNotValidException;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.PatronService;

import jakarta.validation.Valid;

/**
 * This class is responsible for handling RESTful endpoints for managing Patrons
 * in the library
 */
@RestController
@RequestMapping("/library/api")
public class PatronController {

	@Autowired
	private PatronService patronService;

	/**
	 * Retrieve a list of all patrons
	 *
	 * @return List of all patrons
	 */
	@GetMapping("/patrons")
	public List<Patron> retrieveAllPatrons() {
		return patronService.retrieveAllPatrons();
	}

	/**
	 * Retrieve details of a specific patron by ID
	 *
	 * @param id ID of the patron to retrieve
	 * @return ResponseEntity containing the details of the patron
	 * @throws ResourceNotFoundException if the patron Id is not found
	 */
	@GetMapping("/patrons/{id}")
	public ResponseEntity<Patron> retrievePatronById(@PathVariable Long id) throws ResourceNotFoundException {
		Patron patron = patronService.retrievePatron(id);
		return ResponseEntity.ok().body(patron);
	}

	/**
	 * Adds a new patron to the library
	 *
	 * @param book          The new Patron to be added
	 * @param bindingResult to handle validation errors
	 * @return ResponseEntity containing the added Patron
	 * @throws MethodArgumentNotValidException If validation errors occur
	 */
	@PostMapping("/patrons")
	public ResponseEntity<String> createPatron(@Valid @RequestBody Patron patron, BindingResult bindingResult)
			throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(bindingResult);
		}
		patronService.savePatron(patron);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Patron added successfully with Name : " + patron.getName());
	}

	/**
	 * Updates an existing patron's information
	 *
	 * @param id            The ID of the Patron to be updated
	 * @param book          The updated Patron information
	 * @param bindingResult BindingResult to handle validation errors
	 * @return ResponseEntity containing the updated Patron
	 * @throws MethodArgumentNotValidException If validation errors occur
	 * @throws ResourceNotFoundException       If Patron Id is not found
	 */
	@PutMapping("/patrons/{id}")
	public ResponseEntity<String> updatePatronById(@PathVariable Long id, @Valid @RequestBody Patron updatedPatron,
			BindingResult bindingResult) throws MethodArgumentNotValidException, ResourceNotFoundException {
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(bindingResult);
		}
		// Check if the book with the given ID exists
		Patron existingPatron = patronService.retrievePatron(id);

		existingPatron.setName(updatedPatron.getName());
		existingPatron.setContactNumber(updatedPatron.getContactNumber());

		patronService.savePatron(existingPatron);
		return ResponseEntity.status(HttpStatus.OK).body("Patron has been Updated successfully");
	}

	/**
	 * Removes a patron from the system
	 *
	 * @param id The ID of the patron to be removed
	 * @return Success message indicating the patron deletion
	 * @throws ResourceNotFoundException If the patron ID is not found
	 */
	@DeleteMapping("/patrons/{id}")
	public String deletePatronById(@PathVariable Long id){
		try {
			patronService.retrievePatron(id);
			patronService.deletePatron(id);
			return "Patron Deleted Successfully";
		} catch (Exception e) {
			return "Error deleting Patron: Borrowing record found. Cannot delete a Patron with active borrowing record !!!";
		}
	}
}
