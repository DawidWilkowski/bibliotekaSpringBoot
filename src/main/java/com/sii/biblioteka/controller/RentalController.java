package com.sii.biblioteka.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping(value = "/rentals")
	public List<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}

	/**
	 * Creates new rental if possible.
	 * 
	 * @param clientId
	 * @param bookId
	 * @throws Exception
	 */
	@PostMapping(value = "/newRental/{clientId}/{bookId}")
	public void createRental(@PathVariable("clientId") Long clientId, @PathVariable("bookId") Long bookId)
			throws Exception {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new Exception("no client with this id"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("no book with this id"));
		checkIfAvaliableForRent(clientId, bookId, book);
		Rental rental = new Rental();
		rental.setClient(client);
		rental.setBook(book);
		rental.setStartDate(LocalDate.now());
		rental.setEndDate(null);
		rentalRepository.save(rental);

	}

	/**
	 * Checks if book is avalible for rent: (book is not rented already, max 1
	 * bestseller limit, max 4 books rented per person )
	 * 
	 * @param id     - client id
	 * @param bookId
	 * @param book
	 * @throws Exception
	 */
	private void checkIfAvaliableForRent(Long clientId, Long bookId, Book book) throws Exception {

		if (rentalRepository.findFirstByBookIdAndEndDateIsNull(bookId) != null)
			throw new Exception("book already rented");

		List<Rental> allRentedBooks = rentalRepository.findAllByClientIdAndEndDateIsNull(clientId);
		System.out.println(allRentedBooks.size());

		for (int i = 0; i < allRentedBooks.size(); i++) {
			if (allRentedBooks.get(i).getBook().getBookCategory() == BookCategory.BESTSELLER
					&& book.getBookCategory() == BookCategory.BESTSELLER) {
				throw new Exception("One bestseller limit");
			}
		}

		if (allRentedBooks.size() >= 4) {
			throw new Exception("Too many books already rented for this user");
		}

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

//	@GetMapping(value = "/clientIdWithNoReturn/{id}")
//	public int rentalByIdWithNoReturn(@PathVariable("id") Long id) {
//		List<Rental> allRentedBooks = rentalRepository.findAllByClientIdAndEndDateIsNull(id);
//		return allRentedBooks.size();
//	}
//
//	@GetMapping(value = "/clientId/{id}")
//	public List<Rental> rentalById(@PathVariable("id") Long id) {
//		return rentalRepository.findAllByClientId(id);
//	}

}
