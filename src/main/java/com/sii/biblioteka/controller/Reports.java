package com.sii.biblioteka.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.RentalRepository;
import com.sii.biblioteka.util.BookCategory;

@RestController
@RequestMapping("reports")
public class Reports {

	@Autowired
	RentalRepository rentalRepository;

	@GetMapping(value = "/lateReturns")
	public ResponseEntity<String> getAllLateReturns() {
		List<Rental> allRentals = rentalRepository.findAll();
		long lateReturn = 0L;
		for (Rental rental : allRentals) {
			long daysBetween = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
			if (daysBetween > 14) {
				lateReturn++;
			}

		}
		return new ResponseEntity<String>("No. of late returns: " + lateReturn, HttpStatus.OK);
	}

	@GetMapping(value = "/sumOfPenalties")
	public ResponseEntity<String> getSumOfPenalties() {
		List<Rental> allRentals = rentalRepository.findAll();
		float sumOfPenalties = 0;

		for (Rental rental : allRentals) {
			LocalDate endDate = rental.getEndDate();

			if (rental.getEndDate() == null)
				endDate = LocalDate.now();

			long daysBetween = ChronoUnit.DAYS.between(rental.getStartDate(), endDate);
			System.out.println(daysBetween);
			if (daysBetween > 14) {
				float penaltyPercent = 0.01F;
				float penalty = rental.getBook().getPrice() * penaltyPercent * daysBetween;
				if (rental.getBook().getBookCategory() == BookCategory.BESTSELLER) {
					penaltyPercent = 0.02F;
					penalty = rental.getBook().getPrice() * penaltyPercent * daysBetween;
				}
				sumOfPenalties = sumOfPenalties + penalty;
				System.out.println("Your penalty is: " + Math.round(penalty));

			}

		}
		return new ResponseEntity<String>("Sum of penalties: " + Math.round(sumOfPenalties), HttpStatus.OK);

	}

	@GetMapping(value = "/noOfRentedBooks/{id}")
	public ResponseEntity<String> sumOfRentalsById(@PathVariable("id") Long id) {
		long noOfRentals = rentalRepository.findAllByClientId(id).size();
		return new ResponseEntity<String>("No. of rentals: " + noOfRentals, HttpStatus.OK);
	}

	@GetMapping(value = "/noOfRentedBooksByLibrary/{id}")
	public ResponseEntity<String> noOfRentedBooksByLibrary(@PathVariable("id") Long id) {
		long noOfRentalsbyClient = rentalRepository.findAllByClientId(id).size();
		long noOfRentalsbyOrganization = rentalRepository.findAllByOrganizationId(id).size();
		return new ResponseEntity<String>("No. of rentals: " + (noOfRentalsbyClient + noOfRentalsbyOrganization),
				HttpStatus.OK);
	}

}
