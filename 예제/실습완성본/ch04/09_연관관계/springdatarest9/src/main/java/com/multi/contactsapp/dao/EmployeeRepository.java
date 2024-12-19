package com.multi.contactsapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.multi.contactsapp.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	List<Employee> findByEmpNameStartingWith(String empName);

	@Query("select e from Employee e left join fetch e.department d where e.empName like :name%")
	List<Employee> queryEmpByFetchJoin(String name);

	@Query("select e.id, e.empName, e.phone, d.id, d.deptName, d.location from Employee e join e.department d "
			+ "where e.empName like :name%")
	List<Object[]> queryEmpsByJPQL(String name);

}
