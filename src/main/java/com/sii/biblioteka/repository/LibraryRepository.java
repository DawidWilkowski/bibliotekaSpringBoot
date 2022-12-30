package com.sii.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sii.biblioteka.entity.Library;

public interface LibraryRepository extends JpaRepository<Library, Long> {

}
