package com.sii.biblioteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sii.biblioteka.entity.Client;
import com.sii.biblioteka.repository.BookRepository;
import com.sii.biblioteka.repository.ClientRepository;
import com.sii.biblioteka.repository.RentalRepository;

@Controller
public class UIController {
	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping(value = "/login")
	public String login(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		return "login";
	}

	@PostMapping(value = "/register")
	public String register(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		return "register";
	}

	@GetMapping(value = "/bookRental")
	public String boookRental(Model model) {
		model.addAttribute("clients", clientRepository.findAll());
		return "bookRental";
	}

	@PostMapping("/saveRental")
	public void saveRental() {

	}

	// add move to login
	@PostMapping("/save")
	public String saveUser(@ModelAttribute Client client) {
		Client clientInDatabase = clientRepository.findByUsername(client.getUsername());
		if (clientInDatabase == null) {
			clientRepository.save(client);
			return "redirect:/rentals";
		} else {
			// ADD CHECK IF USER EXIST
			return "redirect:/registerFailed";
		}
	}

}
