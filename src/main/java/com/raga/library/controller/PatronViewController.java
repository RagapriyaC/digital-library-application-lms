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

import com.raga.library.entity.Patron;
import com.raga.library.exception.MethodArgumentNotValidException;
import com.raga.library.exception.ResourceNotFoundException;
import com.raga.library.service.PatronService;

import jakarta.validation.Valid;

/**
 * Controller class responsible for handling HTTP requests for managing Patrons
 * in the library
 */
@Controller
@RequestMapping("/library")
public class PatronViewController {

	@Autowired
	private PatronService patronService;

	/**
	 * Retrieve a list of all patrons and displays them
	 * 
	 * @return ModelAndView object representing the view with the list of patrons
	 */
	@GetMapping("/patrons")
	public ModelAndView retrieveAllPatrons() {
		List<Patron> patrons = patronService.retrieveAllPatrons();
		ModelAndView modelAndView = new ModelAndView("patron-list");
		modelAndView.addObject("patrons", patrons);
		return modelAndView;
	}

	/**
	 * Retrieves a patron by its ID and displays its details
	 * 
	 * @param id The ID of the patron to retrieve
	 * @return ModelAndView object representing the view with the patron details
	 */
	@GetMapping("/patrons/{id}")
	public ModelAndView retrievePatronById(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Patron patron = patronService.retrievePatron(id);
			modelAndView.addObject("patron", patron);
			modelAndView.setViewName("patron-details");
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in retrieving Patron : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Displays the form to add a new patron.
	 * 
	 * @return ModelAndView object representing the form to add a new patron
	 */
	@GetMapping("/patrons/new")
	public ModelAndView showAddPatronForm() {
		ModelAndView modelAndView = new ModelAndView("add-patron");
		modelAndView.addObject("patron", new Patron());
		return modelAndView;
	}

	/**
	 * Adds a new patron to the library
	 * 
	 * @param patron        The patron object to add
	 * @param bindingResult BindingResult object to handle validation errors
	 * @return ModelAndView object representing the redirection to the patron list
	 *         view or the add patron form if there are errors
	 * @throws MethodArgumentNotValidException If the patron object fails validation
	 */
	@PostMapping("/patrons")
	public ModelAndView createPatron(@Valid @ModelAttribute("patron") Patron patron, BindingResult bindingResult)
			throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView();

			modelAndView.setViewName("add-patron");
			modelAndView.addObject("patron", patron);
			return modelAndView;
		}
		patronService.savePatron(patron);
		return new ModelAndView("redirect:/library/patrons");
	}

	/**
	 * Displays the form to edit a patron
	 * 
	 * @param id The ID of the patron to edit
	 * @return ModelAndView object representing the form to edit the patron
	 */
	@GetMapping("/patrons/{id}/edit")
	public ModelAndView showEditPatronForm(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Patron patron = patronService.retrievePatron(id);
			modelAndView.addObject("patron", patron);
			modelAndView.setViewName("edit-patron");
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in Editing Patron : " + e.getMessage());
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Updates an existing patron's information
	 * 
	 * @param id            The ID of the patron to update
	 * @param updatedPatron The updated patron object
	 * @param bindingResult BindingResult object to handle validation errors
	 * @return ModelAndView object representing the redirection to the patron list
	 *         view or the edit patron form if there are errors
	 * @throws MethodArgumentNotValidException If the updated patron object fails
	 *                                         validation
	 */
	@PostMapping("/patrons/{id}")
	public ModelAndView updatePatronById(@PathVariable Long id, @Valid @ModelAttribute("patron") Patron updatedPatron,
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {

			ModelAndView modelAndView1 = new ModelAndView();

			modelAndView1.setViewName("edit-patron");
			modelAndView1.addObject("patron", updatedPatron);
			return modelAndView1;
		}
		try {
			Patron existingPatron = patronService.retrievePatron(id);
			existingPatron.setName(updatedPatron.getName());
			existingPatron.setContactNumber(updatedPatron.getContactNumber());
			patronService.savePatron(existingPatron);
			modelAndView.setViewName("redirect:/library/patrons");
		} catch (ResourceNotFoundException e) {
			modelAndView.addObject("errorMessage", "Error in Updating Patron : " + e.getMessage());

			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	/**
	 * Removes a patron from the system
	 * 
	 * @param id The ID of the patron to delete
	 * @return ModelAndView object representing the redirection to the patron list
	 *         view
	 */
	@PostMapping("/patrons/{id}/delete")
	public ModelAndView deletePatronById(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			patronService.retrievePatron(id);
			patronService.deletePatron(id);
		return new ModelAndView("redirect:/library/patrons");
		}
		catch (Exception e){
			modelAndView.addObject("errorMessage", "Error deleting Patron: Borrowing record found. Cannot delete a Patron with active borrowing record !!!");
			modelAndView.setViewName("error");
		}
		return modelAndView; 
	} 
}
