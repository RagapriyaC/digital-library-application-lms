package com.raga.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raga.library.entity.Patron;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.repository.PatronRepository;

/**
 * Service class responsible for for managing CRUD operations related to Patrons.
 */
@Service
public class PatronService {
 
	@Autowired 
	private PatronRepository patronRepository;

	/**
	 * Saves a Patron
	 * 
	 * @param patron to be saved
	 * @return The saved patron.
	 */
	public Patron savePatron(Patron patron) {
		return patronRepository.save(patron);
	}

	/**
	 * Retrieves a patron by ID
	 * 
	 * @param id - ID of the patron to be retrieved
	 * @return The retrieved patron.
	 * @throws ResourceNotFoundException
	 */
	public Patron retrievePatron(Long id) throws ResourceNotFoundException {
		return patronRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patron not found for this id :: " + id));
	}

	/**
	 * Retrieves all patrons
	 * 
	 * @return The list of all patrons
	 */
	public List<Patron> retrieveAllPatrons() {
		return patronRepository.findAll();
	}

	/**
	 * Deletes a patron by ID
	 * 
	 * @param id - ID of the patron to be deleted
	 */
	public void deletePatron(Long id) {
		patronRepository.deleteById(id);
	}

}
