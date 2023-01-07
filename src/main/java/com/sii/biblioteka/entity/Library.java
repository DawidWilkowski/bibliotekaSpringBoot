package com.sii.biblioteka.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "library")
public class Library {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String streetName;
	private String city;
	@OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Department> departments;

	@OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Client> clients;

	@OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Organization> organization;

	public Library() {

	}

	public Library(String streetName, String city) {
		this.streetName = streetName;
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

	public Set<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(Set<Organization> organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return streetName + " " + city;
	}

}
