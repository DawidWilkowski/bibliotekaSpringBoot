package com.sii.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sii.biblioteka.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
