package com.sii.biblioteka.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sii.biblioteka.entity.Book;
import com.sii.biblioteka.entity.Client;
import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.ClientRepository;
import com.sii.biblioteka.repository.RentalRepository;
import com.sii.biblioteka.util.BookCategory;

@RestController
public class RentalController {
	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Checks if there is any book not returned on time.
	 */
	@GetMapping(value = "/checkForPenalty")
	public ResponseEntity<String> checkForPenalty() {
		List<Rental> allRentals = rentalRepository.findAll();
		for (Rental rental : allRentals) {
			LocalDate endDate = rental.getEndDate();
			if (rental.getEndDate() == null)
				endDate = LocalDate.now();
			long daysBetween = ChronoUnit.DAYS.between(rental.getStartDate(), endDate);
			if (daysBetween > 14) {
				float penaltyPercent = 0.01F;
				float penalty = rental.getBook().getPrice() * penaltyPercent * daysBetween;
				if (rental.getBook().getBookCategory() == BookCategory.BESTSELLER) {
					penaltyPercent = 0.02F;
					penalty = rental.getBook().getPrice() * penaltyPercent * daysBetween;
				}

			}

		}
		return new ResponseEntity<String>("Succesfully checked for penalty", HttpStatus.OK);

	}

	/**
	 * Creates new rental for client.
	 * 
	 * @param clientId
	 * @param bookId
	 */
	@PostMapping(value = "/newRental/{clientId}/{bookId}")
	public ResponseEntity<String> createRentalForClient(@PathVariable("clientId") Long clientId,
			@PathVariable("bookId") Long bookId) throws Exception {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new Exception("no client with this id"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("no book with this id"));
		String status = checkIfAvaliableForRentForClient(book, client);
		if (status == "1") {
			Rental rental = new Rental();
			rental.setClient(client);
			rental.setBook(book);
			rental.setStartDate(LocalDate.now());
			rental.setEndDate(null);
			rentalRepository.save(rental);
			return new ResponseEntity<String>("Rental created successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(status, HttpStatus.OK);
		}
	}

	/**
	 * Checks if book is available for rent: (book is not rented already, max 1
	 * bestseller limit, max 4 books rented per person )
	 * 
	 * @param book
	 * @param client
	 * @return
	 */
	private String checkIfAvaliableForRentForClient(Book book, Client client) {
		if (client.getLibrary() != book.getDepartment().getLibrary()) {
			return "book in another library";
		}

		if (rentalRepository.findFirstByBookIdAndEndDateIsNull(book.getId()) != null)
			return "book already rented";

		List<Rental> allRentedBooks = rentalRepository.findAllByClientIdAndEndDateIsNull(client.getId());

		for (int i = 0; i < allRentedBooks.size(); i++) {
			if (allRentedBooks.get(i).getBook().getBookCategory() == BookCategory.BESTSELLER
					&& book.getBookCategory() == BookCategory.BESTSELLER) {
				return "One bestseller limit";
			}
		}
		if (allRentedBooks.size() >= 4) {
			return "Too many books already rented for this user";
		}

		return "1";

	}

	@GetMapping(value = "/UI")
	public String viewHomePage(Model model) {
		model.addAttribute("listRentals", rentalRepository.findAll());
		return "index";
	}

	/**
	 * End rental with given id.
	 * 
	 * @param id - rental id
	 * @throws Exception
	 */
	@PutMapping(value = "/endRental/{id}")
	public void endRental(@PathVariable("id") Long id) throws Exception {
		Rental rentalToEnd = rentalRepository.findById(id).orElseThrow(() -> new Exception("no rental with this id"));
		if (rentalToEnd.getEndDate() == null) {
			rentalToEnd.setEndDate(LocalDate.now());
			rentalRepository.save(rentalToEnd);
		} else {
			throw new Exception("Book already returned");

		}
	}

}
