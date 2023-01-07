package com.sii.biblioteka.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.sii.biblioteka.controller.RentalController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RentalController.class)
public class RentalControllerTest {
	@Autowired
	private MockMvc mvc;

}
