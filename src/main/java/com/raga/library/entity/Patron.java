package com.raga.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * This class represents an entity for patrons. It includes attributes such as
 * ID, name and contactNumber
 */
@Entity
public class Patron {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Patron name is required")
	@Size(min = 3, message = "Patron name should have at least 3 characters")
	private String name;

	@NotEmpty(message = "Patron Contact Number is required")
	@Size(min = 9, message = "Contact Number should have at least 9 characters")
	private String contactNumber;

	public Long getId() { 
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Patron() {
		super();
	}

	public Patron(Long id,
			@NotEmpty(message = "Patron name is required") @Size(min = 3, message = "Patron name should have at least 3 characters") String name,
			@NotEmpty(message = "Patron Contact Number is required") @Size(min = 9, message = "Contact Number should have at least 9 characters") String contactNumber) {
		super();
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
	}

}
