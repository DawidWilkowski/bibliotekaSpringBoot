package com.sii.biblioteka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sii.biblioteka.entity.Client;
import com.sii.biblioteka.repository.ClientRepository;

@RestController
public class ClientController {
	@Autowired
	ClientRepository clientRepository;

	@GetMapping(value = "/clients")
	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	@GetMapping(value = "/clients/{id}")
	public Optional<Client> getClientById(@PathVariable Long id) {
		return clientRepository.findById(id);
	}
}
