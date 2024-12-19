package com.multi.contactsapp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.dao.DepartmentRepository;
import com.multi.contactsapp.domain.Department;

@RestController
@RequestMapping(value = "depts")
public class DepartmentController {
	@Autowired
	private DepartmentRepository departmentRepository;

//	@GetMapping("{deptid}")
//	public Optional<Department> getDepartment(@PathVariable("deptid") String deptid) {
//		return departmentRepository.findById(deptid);
//	}

	@GetMapping("{deptid}")
	public Optional<Department> getDepartment(@PathVariable("deptid") String deptid) {
		Optional<Department> optDept = departmentRepository.findById(deptid);
		if (optDept.isPresent()) {
			Department dept = optDept.get();
			for (int i = 0; i < dept.getEmployees().size(); i++) {
				dept.getEmployees().get(i).setDepartment(null);
			}
		}
		return optDept;
	}

}
