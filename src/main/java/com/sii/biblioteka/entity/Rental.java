package com.sii.biblioteka.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sii.biblioteka.util.ClientType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "rental")
public class Rental {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = true)
	@JsonIgnore
	private Client client;

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = true)
	@JsonIgnore
	private Organization organization;

	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	@JsonIgnore
	private Book book;
	@Temporal(TemporalType.DATE)
	private LocalDate startDate;
	@Temporal(TemporalType.DATE)
	private LocalDate endDate;

	private ClientType clientType;

	public Rental() {
	};

	public Rental(Client client, Organization organization, ClientType clientType, Book book, LocalDate startDate,
			LocalDate endDate) {
		this.client = client;
		this.organization = organization;
		this.clientType = clientType;
		this.book = book;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;

	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
