package com.sii.biblioteka.entity;

import com.sii.biblioteka.util.BookCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
// private dzial.id;
	private BookCategory bookCategory = BookCategory.NORMALNA;
	private String author;
	private String opis;
// wypozyczalnia.id
	@ManyToOne
	@JoinColumn(name = "departament_id", nullable = false)
	private Department department;

	public Book() {
	}

	public Book(String title, BookCategory bookCategory, String author, String opis, Department department) {

		this.title = title;
		this.bookCategory = bookCategory;
		this.author = author;
		this.opis = opis;
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BookCategory getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

}
