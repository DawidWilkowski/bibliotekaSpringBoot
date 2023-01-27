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
import com.sii.biblioteka.entity.Library;
import com.sii.biblioteka.entity.Rental;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.ClientRepository;
import com.sii.biblioteka.repository.DepartmentRepository;
import com.sii.biblioteka.repository.LibraryRepository;
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
	LibraryRepository libraryRepository;
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
		Library libraryBydgoszcz = new Library("Mostowa 5", "Bydgoszcz");
		Library libraryWarszawa = new Library("Kochanowkiego 12", "Warszawa");
		Library libraryZakopane = new Library("Lipowa 52", "Zakopane");
		libraryRepository.saveAll(Arrays.asList(libraryBydgoszcz, libraryWarszawa, libraryZakopane));

		Department fantasyBydgoszcz = new Department("Fantasy", libraryBydgoszcz);
		Department historyczneZakopane = new Department("Historyczne", libraryZakopane);
		Department naukaWarszawa = new Department("Nauka", libraryWarszawa);

		departamentRepository.saveAll(Arrays.asList(fantasyBydgoszcz, historyczneZakopane, naukaWarszawa));

		Book infekcja = new Book("Infekcja", BookCategory.NORMALNA, "Andrzej Wardziak", "Apokalipsa zombie w Warszawie",
				29.99F, fantasyBydgoszcz);
		Book zabicDrozda = new Book("Zabic drozda", BookCategory.OLD, "Harper Lee",
				"Historia rasizmu w Stanach Zjednoczonych", 39.99F, historyczneZakopane);
		Book wiedzmin = new Book("Wiedzmin", BookCategory.BESTSELLER, "Andrzej Sapkowski", "Książka fantasy", 49.99F,
				fantasyBydgoszcz);
		bookRepository.saveAll(Arrays.asList(infekcja, zabicDrozda, wiedzmin));

		Client tomaszPolak = new Client("Tomasz", "Polak", 511223645, libraryBydgoszcz);
		Client karolinaPierwsza = new Client("Karolina", "Pierwsza", 566445125, libraryZakopane);
		Client dominikNiejaki = new Client("Dominik", "Niejaki", 466551352, libraryWarszawa);
		clientRepository.saveAll(Arrays.asList(tomaszPolak, karolinaPierwsza, dominikNiejaki));

		Rental rental1 = new Rental(tomaszPolak, infekcja, LocalDate.of(2020, 5, 1), LocalDate.of(2021, 5, 1));
		Rental rental2 = new Rental(tomaszPolak, zabicDrozda, LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 3));
		Rental rental3 = new Rental(dominikNiejaki, wiedzmin, LocalDate.of(2021, 5, 1), LocalDate.of(2021, 6, 2));
		rentalRepository.saveAll(Arrays.asList(rental1, rental2, rental3));

	}

}
