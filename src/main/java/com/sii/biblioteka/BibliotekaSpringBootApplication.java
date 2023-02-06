package com.sii.biblioteka;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sii.biblioteka.entity.Book;
import com.sii.biblioteka.entity.Client;
import com.sii.biblioteka.entity.Department;
import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.ClientRepository;
import com.sii.biblioteka.repository.DepartmentRepository;
import com.sii.biblioteka.repository.RentalRepository;
import com.sii.biblioteka.util.BookCategory;

@SpringBootApplication
public class BibliotekaSpringBootApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BibliotekaSpringBootApplication.class, args);

	}

	@Autowired
	BookRepository bookRepository;
	@Autowired
	DepartmentRepository departamentRepository;
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	RentalRepository rentalRepository;

	/**
	 * Initialize H2 database with sample data
	 */
	public void run(String... args) throws Exception {

		Department fantasyBydgoszcz = new Department("Fantasy");
		Department historyczneZakopane = new Department("Historyczne");
		Department naukaWarszawa = new Department("Nauka");

		departamentRepository.saveAll(Arrays.asList(fantasyBydgoszcz, historyczneZakopane, naukaWarszawa));

		Book infekcja = new Book("Infekcja", BookCategory.NORMALNA, "Andrzej Wardziak", "Apokalipsa zombie w Warszawie",
				29.99F, fantasyBydgoszcz);
		Book zabicDrozda = new Book("Zabic drozda", BookCategory.OLD, "Harper Lee",
				"Historia rasizmu w Stanach Zjednoczonych", 39.99F, historyczneZakopane);
		Book wiedzmin = new Book("Wiedzmin", BookCategory.BESTSELLER, "Andrzej Sapkowski", "Książka fantasy", 49.99F,
				fantasyBydgoszcz);
		bookRepository.saveAll(Arrays.asList(infekcja, zabicDrozda, wiedzmin));

		Client user1 = new Client("ClientName1", "ClientSurname1", "user1", "pass1");
		Client user2 = new Client("ClientName2", "ClientSurname2", "user2", "pass2");
		Client user3 = new Client("ClientName3", "ClientSurname3", "user3", "pass3");
		clientRepository.saveAll(Arrays.asList(user1, user2, user3));

		Rental rental1 = new Rental(user1, infekcja, LocalDate.of(2020, 5, 1), LocalDate.of(2021, 5, 1));
		Rental rental2 = new Rental(user2, zabicDrozda, LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 3));
		Rental rental3 = new Rental(user3, wiedzmin, LocalDate.of(2021, 5, 1), LocalDate.of(2021, 6, 2));
		rentalRepository.saveAll(Arrays.asList(rental1, rental2, rental3));

	}

}
