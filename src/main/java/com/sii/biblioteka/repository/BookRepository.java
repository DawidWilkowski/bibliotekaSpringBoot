package com.sii.biblioteka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sii.biblioteka.entity.Book;
import com.sii.biblioteka.util.BookCategory;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

	List<Book> findByBookCategory(BookCategory bookCategory);

	List<Book> findByOpisContaining(String opis);

	@Query(value = "SELECT book.id, book.author, book.book_category,book.opis,book.title,book.departament_id,department.name AS department_name FROM book, department WHERE book.departament_id  = department.id AND department.name = ?", nativeQuery = true)
	List<Book> getBooksByDepartment(String department);
}
