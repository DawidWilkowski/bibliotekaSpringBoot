package com.sii.biblioteka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sii.biblioteka.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

	List<Rental> findAllByClientIdAndEndDateIsNull(Long id);

	List<Rental> findAll();

	List<Rental> findAllByClientId(Long id);

	Rental findFirstByBookIdAndEndDateIsNull(Long id);

	Rental findBookById(Long id);
}
