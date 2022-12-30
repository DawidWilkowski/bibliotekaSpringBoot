package com.sii.biblioteka.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sii.biblioteka.repository.RentalRepository;

public class Raports {
	@Autowired
	private RentalRepository rentalRepository;

	public void returnedAfterFourteenDays() {
		List<Integer> listOfIntegers = rentalRepository.dateDiffrence();
		for (Integer integer : listOfIntegers) {
			System.out.println(integer);
		}

	}
}
