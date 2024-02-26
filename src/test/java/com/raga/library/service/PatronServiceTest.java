package com.raga.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raga.library.entity.Patron;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.repository.PatronRepository;

/**
 * Unit tests for the PatronService class. These tests cover the functionality
 * of PatronService
 * 
 */
@ExtendWith(MockitoExtension.class)
public class PatronServiceTest {

	@Mock
	private PatronRepository patronRepository;

	@InjectMocks
	private PatronService patronService;

	/**
	 * Test case for saving a Patron
	 */
	@Test
	public void testSavePatron() {
		// Given
		Patron patron = new Patron(1L, "John Doe", "123456789");
		given(patronRepository.save(any(Patron.class))).willReturn(patron);

		// When
		Patron savedPatron = patronService.savePatron(patron);

		// Then
		assertThat(savedPatron).isNotNull();
		assertThat(savedPatron.getName()).isEqualTo("John Doe");
	}

	/**
	 * Test case for retrieving a patron by ID
	 * 
	 * @throws ResourceNotFoundException if the patron id is not found
	 */
	@Test
	public void testRetrievePatronById() throws ResourceNotFoundException {
		// Given
		Optional<Patron> patron = Optional.of(new Patron(1L, "John Doe", "123456789"));
		given(patronRepository.findById(1L)).willReturn(patron);

		// When
		Patron retrievedPatron = patronService.retrievePatron(1L);

		// Then
		assertThat(retrievedPatron).isNotNull();
		assertThat(retrievedPatron.getName()).isEqualTo("John Doe");
	}

	/**
	 * Test case for retrieving all patrons
	 * 
	 * @throws ResourceNotFoundException if no patrons are found
	 */
	@Test
	public void testRetrieveAllPatrons() throws ResourceNotFoundException {
		Patron patron1 = new Patron(1L, "John Doe", "123456789");
		Patron patron2 = new Patron(2L, "Jane Smith", "987654321");
		List<Patron> listOfPatrons = Arrays.asList(patron1, patron2);
		given(patronRepository.findAll()).willReturn(listOfPatrons);

		// When
		List<Patron> listOfPatrons1 = patronService.retrieveAllPatrons();

		// Then
		assertThat(listOfPatrons1).isNotNull();
		assertThat(listOfPatrons1).hasSize(2);
		assertThat(listOfPatrons1.get(0).getName()).isEqualTo("John Doe");
		assertThat(listOfPatrons1.get(1).getName()).isEqualTo("Jane Smith");
	}

	/**
	 * Test case for deleting a patron
	 */
	@Test
	public void testDeletePatron() {
		patronService.deletePatron(1L);
	}

	/**
	 * Test case retrieving a patron Id which is not found
	 */
	@Test
	public void testRetrievePatronByIdNotFound() {
		// Given
		given(patronRepository.findById(1000L)).willReturn(Optional.empty());

		// When/Then
		assertThrows(ResourceNotFoundException.class, () -> patronService.retrievePatron(1000L));
	}

}
