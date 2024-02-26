package com.raga.library.controller;

import com.raga.library.entity.Patron;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PatronViewController class. These tests cover the
 * functionality of PatronViewController
 * 
 */
public class PatronViewControllerTest {

	@Mock
	private PatronService patronService;

	@InjectMocks
	private PatronViewController patronViewController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Test case for retrieving all patrons
	 */
	@Test
	public void testRetrieveAllPatrons() {
		List<Patron> mockPatrons = new ArrayList<>();
		when(patronService.retrieveAllPatrons()).thenReturn(mockPatrons);

		ModelAndView modelAndView = patronViewController.retrieveAllPatrons();

		assertEquals("patron-list", modelAndView.getViewName());
		assertEquals(mockPatrons, modelAndView.getModel().get("patrons"));
	}

	/**
	 * Test case for the retrieval of a patron by ID
	 *
	 * @throws ResourceNotFoundException if the patron is not found
	 */
	@Test
	public void testRetrievePatronById() throws ResourceNotFoundException {
		long id = 1L;
		Patron mockPatron = new Patron();

		when(patronService.retrievePatron(id)).thenReturn(mockPatron);

		ModelAndView modelAndView = patronViewController.retrievePatronById(id);

		assertEquals("patron-details", modelAndView.getViewName());
		assertEquals(mockPatron, modelAndView.getModel().get("patron"));
	}

	/**
	 * Test case for the retrieval of a patron by ID when a ResourceNotFoundException is
	 * thrown
	 *
	 * @throws ResourceNotFoundException if the patron is not found
	 */
	@Test
	public void testRetrievePatronById_ResourceNotFoundException() throws ResourceNotFoundException {
		long id = 1L;
		when(patronService.retrievePatron(id)).thenThrow(ResourceNotFoundException.class);

		ModelAndView modelAndView = patronViewController.retrievePatronById(id);

		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test case for showing the form to add a patron
	 */
	@Test
	public void testShowAddPatronForm() {
		ModelAndView modelAndView = patronViewController.showAddPatronForm();

		assertEquals("add-patron", modelAndView.getViewName());
	}

	/**
	 * Test case for creating a patron
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testCreatePatron() throws Exception {
		Patron patron = new Patron();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);

		ModelAndView modelAndView = patronViewController.createPatron(patron, bindingResult);

		assertEquals("redirect:/library/patrons", modelAndView.getViewName());
	}

	/**
	 * Test case for creating a patron with binding errors
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testCreatePatron_WithBindingErrors() throws Exception {
		Patron patron = new Patron();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);

		ModelAndView modelAndView = patronViewController.createPatron(patron, bindingResult);

		assertEquals("add-patron", modelAndView.getViewName());
		assertEquals(patron, modelAndView.getModel().get("patron"));
	}

	/**
	 * Test case for showing the form to edit a patron
	 *
	 * @throws ResourceNotFoundException if the patron is not found
	 */
	@Test
	public void testShowEditPatronForm() throws ResourceNotFoundException {
		long id = 1L;
		Patron mockPatron = new Patron();
		when(patronService.retrievePatron(id)).thenReturn(mockPatron);

		ModelAndView modelAndView = patronViewController.showEditPatronForm(id);

		assertEquals("edit-patron", modelAndView.getViewName());
		assertEquals(mockPatron, modelAndView.getModel().get("patron"));
	}

	/**
	 * Test case for showing the form to edit a patron when a ResourceNotFoundException is
	 * thrown
	 *
	 * @throws ResourceNotFoundException if the patron is not found
	 */
	@Test
	public void testShowEditPatronForm1() throws ResourceNotFoundException {
		long id = 1L;
		when(patronService.retrievePatron(id)).thenThrow(ResourceNotFoundException.class);

		ModelAndView modelAndView = patronViewController.showEditPatronForm(id);

		assertEquals("error", modelAndView.getViewName());
	}

	/**
	 * Test case for updating a patron by ID
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdatePatronById() throws Exception {
		long id = 1L;
		Patron existingPatron = new Patron();
		Patron updatedPatron = new Patron();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(patronService.retrievePatron(id)).thenReturn(existingPatron);

		ModelAndView modelAndView = patronViewController.updatePatronById(id, updatedPatron, bindingResult);

		assertEquals("redirect:/library/patrons", modelAndView.getViewName());

	}

	/**
	 * Test case for updating a patron by ID with binding errors
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdatePatronById1() throws Exception {
		long id = 1L;
		Patron existingPatron = new Patron();
		Patron updatedPatron = new Patron();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		when(patronService.retrievePatron(id)).thenReturn(existingPatron);

		ModelAndView modelAndView = patronViewController.updatePatronById(id, updatedPatron, bindingResult);

		assertEquals("edit-patron", modelAndView.getViewName());
		assertEquals(updatedPatron, modelAndView.getModel().get("patron"));
	}

	/**
	 * Test case for updating a patron by ID when the patron is not found
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdatePatronById2() throws Exception {
		long id = 1L;
		Patron updatedPatron = new Patron();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(patronService.retrievePatron(id)).thenThrow(ResourceNotFoundException.class);

		ModelAndView modelAndView = patronViewController.updatePatronById(id, updatedPatron, bindingResult);

		assertEquals("error", modelAndView.getViewName());

	}

	/**
	 * Test case for deleting a patron by ID
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeletePatronById() throws Exception {
		long id = 1L;
		ModelAndView modelAndView = patronViewController.deletePatronById(id);

		assertEquals("redirect:/library/patrons", modelAndView.getViewName());
	}

	/**
	 * Test case for deleting a patron by ID when the patron is not found
	 *
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeletePatronById2() throws Exception {
		long id = 1L;
		when(patronService.retrievePatron(id)).thenThrow(ResourceNotFoundException.class);
		ModelAndView modelAndView = patronViewController.deletePatronById(id);
		assertEquals("error", modelAndView.getViewName());
	}
}
