package com.multi.contactsapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.dao.EmployeeRepository;
import com.multi.contactsapp.domain.Department;
import com.multi.contactsapp.domain.Employee;

@RestController
@RequestMapping(value = "emps")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("find1/{name}")
	public List<Employee> getDepartment1(@PathVariable("name") String name) {
		return employeeRepository.findByEmpNameStartingWith(name);
	}

	@GetMapping("find2/{name}")
	public List<Employee> getDepartment2(@PathVariable("name") String name) {
		List<Employee> employeeList = employeeRepository.findByEmpNameStartingWith(name);
		if (employeeList.size() > 0) {
			for (Employee e : employeeList) {
				e.getDepartment().setEmployees(null);
			}
		}
		return employeeList;
	}

	@GetMapping("find3/{name}")
	public List<Employee> getDepartment3(@PathVariable("name") String name) {
		List<Employee> list = employeeRepository.queryEmpByFetchJoin(name);
		if (list.size() > 0) {
			for (Employee e : list) {
				e.getDepartment().setEmployees(null);
			}
		}
		return list;
	}

	@GetMapping("find4/{name}")
	public List<Employee> getDepartment4(@PathVariable("name") String name) {
		List<Object[]> list = employeeRepository.queryEmpsByJPQL(name);
		List<Employee> empList = new ArrayList<Employee>();
		if (list.size() > 0) {
			for (Object[] objs : list) {
				Employee e = new Employee((String) objs[0], (String) objs[1], (String) objs[2]);
				e.setDepartment(new Department((String) objs[3], (String) objs[4], (String) objs[5]));
				e.getDepartment().setEmployees(null);
				empList.add(e);
			}
		}
		return empList;
	}

}
