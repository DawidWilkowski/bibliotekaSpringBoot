package com.sii.biblioteka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sii.biblioteka.entity.Book;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.DepartmentRepository;
import com.sii.biblioteka.util.BookCategory;

@Controller
public class BookController {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	@GetMapping(value = "/booksListing")
	public String booksListing(Model model) {
		model.addAttribute("listBooks", bookRepository.findAll());
		return "books";
	}

	@GetMapping("/showNewBookForm")
	public String showNewBookForm(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		model.addAttribute("departments", departmentRepository.findAll());
		return "new_book";
	}

	@PostMapping("/saveBook")
	public String saveEmployee(@ModelAttribute("book") Book book) {
		bookRepository.save(book);
		return "redirect:/booksListing";
	}

	@GetMapping(value = "/books")
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping(value = "/books/{id}")
	public Optional<Book> getBookById(@PathVariable Long id) {
		return bookRepository.findById(id);
	}

	@GetMapping(value = "/books", params = "title")
	public List<Book> getBookByTitle(@RequestParam("title") String title) {
		return bookRepository.findByTitle(title);
	}

	@GetMapping(value = "/books", params = "author")
	public List<Book> getBookByAuthor(@RequestParam("author") String author) {
		return bookRepository.findByAuthor(author);
	}

	@GetMapping(value = "/books", params = "category")
	public List<Book> getBookByCategory(@RequestParam("category") BookCategory bookCategory) {
		return bookRepository.findByBookCategory(bookCategory);
	}

	@GetMapping(value = "/books", params = "description")
	public List<Book> getBookByDescription(@RequestParam("description") String description) {
		return bookRepository.findByDescriptionContaining(description);
	}

	@GetMapping(value = "/books", params = "department")
	public List<Book> getBookByDepartment(@RequestParam("department") String department) {
		return bookRepository.getBooksByDepartment(department);
	}
}
