package com.raga.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raga.library.entity.Patron;

/**
 * Repository interface responsible for performing CRUD operations on the Patron
 * entity
 * 
 */
public interface PatronRepository extends JpaRepository<Patron, Long> {

}
