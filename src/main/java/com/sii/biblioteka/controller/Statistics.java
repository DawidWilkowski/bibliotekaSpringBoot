package com.sii.biblioteka.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.RentalRepository;
import com.sii.biblioteka.util.BookCategory;

@Controller
public class Statistics {

	@Autowired
	RentalRepository rentalRepository;

	@GetMapping(value = "/statistics")
	public String statistics(Model model) {
		model.addAttribute("lateReturns", getAllLateReturns());
		model.addAttribute("sumOfPenalties", getSumOfPenalties());
		return "statistics";
	}

	@GetMapping(value = "/lateReturns")
	public int getAllLateReturns() {
		List<Rental> allRentals = rentalRepository.findAll();
		int lateReturn = 0;
		for (Rental rental : allRentals) {
			int daysBetween = (int) ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
			System.out.println(daysBetween);
			if (daysBetween > 14) {
				lateReturn++;
			}

		}
		return lateReturn;
	}

	@GetMapping(value = "/sumOfPenalties")
	public int getSumOfPenalties() {
		List<Rental> allRentals = rentalRepository.findAll();
		float penalty = 0;
		float penaltyPercent = 0.01F;
		float sumOfPenalties = 0;

		for (Rental rental : allRentals) {

			if (rental.getEndDate() == null)
				continue;

			int daysBetween = (int) ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
			System.out.println(daysBetween);
			if (daysBetween > 14) {
				if (rental.getBook().getBookCategory() == BookCategory.BESTSELLER) {
					penaltyPercent = 0.02F;
				}
				penalty = rental.getBook().getPrice() * penaltyPercent * daysBetween;
				sumOfPenalties = sumOfPenalties + penalty;
				System.out.println("Your penalty is: " + Math.round(penalty));
			}
		}
		return Math.round(sumOfPenalties);
	}

	@GetMapping(value = "/noOfRentedBooks/{id}")
	public ResponseEntity<String> sumOfRentalsById(@PathVariable("id") Long id) {
		long noOfRentals = rentalRepository.findAllByClientId(id).size();
		return new ResponseEntity<String>("No. of rentals: " + noOfRentals, HttpStatus.OK);
	}

}
