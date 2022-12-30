package com.sii.biblioteka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sii.biblioteka.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

	List<Rental> findAllByClientIdAndEndDateIsNull(Long id);

	List<Rental> findAllByClientId(Long id);

	@Query(value = "SELECT DATEDIFF(endDate,startDate) FROM rental ", nativeQuery = true)
	List<Integer> dateDiffrence();

	Rental findFirstByBookIdAndEndDateIsNull(Long id);
}
