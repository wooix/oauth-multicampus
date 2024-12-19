package com.multi.contactsapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multi.contactsapp.domain.Department;

public interface DepartmentRepository  extends JpaRepository<Department, String> {

}

