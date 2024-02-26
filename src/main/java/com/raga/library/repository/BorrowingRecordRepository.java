package com.raga.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raga.library.entity.BorrowingRecord;

/**
 * Repository interface responsible for performing CRUD operations on the BorrowingRecord
 * entity
 * 
 */
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long>{

	List<BorrowingRecord> findByBookIdAndPatronId(Long book_id, Long patron_Id);

	boolean existsByBookIdAndPatronId(Long bookId, Long patronId);
}
