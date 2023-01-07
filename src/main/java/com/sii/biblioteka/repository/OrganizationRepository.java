package com.sii.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sii.biblioteka.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
