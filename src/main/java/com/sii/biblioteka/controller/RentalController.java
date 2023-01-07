package com.sii.biblioteka.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sii.biblioteka.entity.Book;
import com.sii.biblioteka.entity.Client;
import com.sii.biblioteka.entity.Organization;
import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.ClientRepository;
import com.sii.biblioteka.repository.OrganizationRepository;
import com.sii.biblioteka.repository.RentalRepository;
import com.sii.biblioteka.util.BookCategory;
import com.sii.biblioteka.util.ClientType;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
public class RentalController {
	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

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

				if (daysBetween > 21 && rental.getEndDate() == null) {
					sendInfoAboutPenalty(penalty);
				}
			}

		}
		return new ResponseEntity<String>("Succesfully checked for penalty", HttpStatus.OK);

	}

	/**
	 * Send SMS about penalty via Twillio. 
	 * 
	 * @param penalty
	 */
	private ResponseEntity<String> sendInfoAboutPenalty(float penalty) {
		Twilio.init("TWILLIO_LOGIN", "TWILLIO_PASS");
		PhoneNumber sendTo = new PhoneNumber("SEND_TO_NUMBER");
		PhoneNumber sendFrom = new PhoneNumber("SEND_FROM_NUMBER");
		Message.creator(sendTo, sendFrom, "Hey its your library. Your penalty is: " + Math.round(penalty)
				+ " return your book as soon as possible. ").create();

		return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
	}

	/**
	 * Creates new rental for organization.
	 * 
	 * @param organizationId
	 * @param bookId
	 * 
	 */

	@PostMapping(value = "/newRentalOrganization/{organizationId}/{bookId}")
	public ResponseEntity<String> createRentalForOrganization(@PathVariable("organizationId") Long organizationId,
			@PathVariable("bookId") Long bookId) throws Exception {
		Organization organization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new Exception("no organization with this id"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("no book with this id"));
		String status = checkIfAvaliableForRentForOrganization(book, organization);
		if (status == "1") {
			Rental rental = new Rental();
			rental.setClient(null);
			rental.setOrganization(organization);
			rental.setClientType(ClientType.ORGANIZATION);
			rental.setBook(book);
			rental.setStartDate(LocalDate.now());
			rental.setEndDate(null);
			rentalRepository.save(rental);
			return new ResponseEntity<String>("Rental created successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(status, HttpStatus.OK);
		}
	}

	private String checkIfAvaliableForRentForOrganization(Book book, Organization organization) {
		if (organization.getLibrary() != book.getDepartment().getLibrary()) {
			return "book in another library";
		}

		if (rentalRepository.findFirstByBookIdAndEndDateIsNull(book.getId()) != null)
			return "book already rented";

		List<Rental> allRentedBooks = rentalRepository.findAllByOrganizationIdAndEndDateIsNull(organization.getId());
		System.out.println(allRentedBooks.size());

		if (allRentedBooks.size() >= 10) {
			return "Too many books already rented for this user";
		}

		return "1";
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
			rental.setOrganization(null);
			rental.setBook(book);
			rental.setClientType(ClientType.CLIENT);
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
		System.out.println(allRentedBooks.size());

		for (int i = 0; i < allRentedBooks.size(); i++) {
			if (allRentedBooks.get(i).getBook().getBookCategory() == BookCategory.BESTSELLER
					&& book.getBookCategory() == BookCategory.BESTSELLER) {
				return "One bestseller limit";
			}
		}
		if (client.getOrganization() == null) {
			if (allRentedBooks.size() >= 4) {
				return "Too many books already rented for this user";
			}
		} else {
			if (allRentedBooks.size() >= 6) {
				return "Too many books already rented for this user";
			}
		}
		return "1";
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
