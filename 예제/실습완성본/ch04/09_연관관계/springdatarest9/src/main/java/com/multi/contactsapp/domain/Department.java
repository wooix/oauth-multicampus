package com.multi.contactsapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@JsonInclude(Include.NON_NULL)
public class Department {
	@Id
	@Column(name = "DEPT_ID")
	private String id;
	private String deptName;
	private String location;

	@OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
	private List<Employee> employees = new ArrayList<Employee>();

	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Department(String id, String deptName, String location) {
		super();
		this.id = id;
		this.deptName = deptName;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}
