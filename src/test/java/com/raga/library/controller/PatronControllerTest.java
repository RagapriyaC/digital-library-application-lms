package com.raga.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raga.library.entity.Patron;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.BookService;
import com.raga.library.service.BorrowingRecordService;
import com.raga.library.service.PatronService;

/**
 * Unit tests for the PatronController class. These tests cover the
 * functionality of PatronController
 * 
 */
@WebMvcTest
public class PatronControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@MockBean
	private PatronService patronService;

	@MockBean
	private BorrowingRecordService borrowingRecordServiceMock;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test case for retrieving all patrons
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testRetrieveAllPatrons() throws Exception {

		// given
		Patron patron1 = new Patron(1L, "John Doe", "123456789");
		Patron patron2 = new Patron(2L, "Jane Smith", "987654321");
		List<Patron> listOfPatrons = Arrays.asList(patron1, patron2);
		given(patronService.retrieveAllPatrons()).willReturn(listOfPatrons);

		// when
		ResultActions resultActions = mockMvc.perform(get("/library/api/patrons"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("John Doe"))
				.andExpect(jsonPath("$[0].contactNumber").value("123456789"))
				.andExpect(jsonPath("$[1].name").value("Jane Smith"))
				.andExpect(jsonPath("$[1].contactNumber").value("987654321"));
	}

	/**
	 * Test case for retrieving a patron by ID
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testRetrievePatronById() throws Exception {

		// Given
		Patron patron = new Patron(1L, "John Doe", "123456789");
		given(patronService.retrievePatron(1L)).willReturn(patron);

		// When
		ResultActions resultActions = mockMvc
				.perform(get("/library/api/patrons/1").contentType(MediaType.APPLICATION_JSON));

		// Then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.contactNumber").value("123456789"));
	}

	/**
	 * Test case for creating a new Patron
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testCreatePatron() throws Exception {

		// given
		Patron patron = new Patron(1L, "John Doe", "123456789");
		given(patronService.savePatron(ArgumentMatchers.any(Patron.class))).willReturn(patron);

		// when
		ResultActions resultActions = mockMvc.perform(post("/library/api/patrons")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patron)));

		// then
		resultActions.andDo(print()).andExpect(status().isCreated());
	}

	/**
	 * Test case for updating a patron by ID
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdatePatronById() throws Exception {
		// Given
		Long patronId = 1L;
		Patron existingPatron = new Patron(2L, "Jane Smith", "987654321");
		Patron updatedPatron = new Patron(1L, "John Doe", "123456789");

		when(patronService.retrievePatron(patronId)).thenReturn(existingPatron);
		when(patronService.savePatron(any(Patron.class))).thenReturn(existingPatron);

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/patrons/1")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedPatron)));

		// Then
		resultActions.andDo(print()).andExpect(status().isOk());

	}

	/**
	 * Test case for updating a patron by ID
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeletePatronById() throws Exception {

		// Given
		given(patronService.retrievePatron(1L)).willReturn(new Patron(1L, "John", "123456789"));

		// When
		ResultActions resultActions = mockMvc.perform(delete("/library/api/patrons/1"));

		// Then
		resultActions.andExpect(status().isOk()).andExpect(content().string("Patron Deleted Successfully"));
	}

	/**
	 * Test case for creating a patron using Invalid Input Data
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testCreatePatronInvalidInput() throws Exception {
		// Given
		Patron patron = new Patron(1L, "", "");
		given(patronService.savePatron(ArgumentMatchers.any(Patron.class))).willReturn(patron);

		// When
		ResultActions resultActions = mockMvc.perform(post("/library/api/patrons")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patron)));

		// Then
		resultActions.andExpect(status().isBadRequest());
	}

	/**
	 * Test case for updating a patron using Invalid Input Data
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testUpdatePatronInvalidInput() throws Exception {
		// Given
		Patron patron = new Patron(1L, "", "");
		given(patronService.savePatron(ArgumentMatchers.any(Patron.class))).willReturn(patron);

		// When
		ResultActions resultActions = mockMvc.perform(put("/library/api/patrons/1")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patron)));

		// Then
		resultActions.andExpect(status().isBadRequest());
	}
	
	/**
	 * Test case for deleting a patron by ID which is not found
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testDeletePatronByIdNotFound() throws Exception {
		// Given
		doThrow(new ResourceNotFoundException("Patron not found")).when(patronService).retrievePatron(1L);

		// When
		ResultActions resultActions = mockMvc.perform(delete("/library/api/patrons/1"));

		// Then
		resultActions.andExpect(status().isOk());
	}

	/**
	 * Test case for retrieving a patron by ID which is not found
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	public void testRetrievePatronByIdNotFound() throws Exception {
		// Given
		given(patronService.retrievePatron(1L)).willThrow(new ResourceNotFoundException("Patron not found"));

		// When
		ResultActions resultActions = mockMvc
				.perform(get("/library/api/patrons/1").contentType(MediaType.APPLICATION_JSON));

		// Then
		resultActions.andExpect(status().isNotFound());
	}

}
