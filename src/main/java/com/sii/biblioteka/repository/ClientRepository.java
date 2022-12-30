package com.sii.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sii.biblioteka.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
