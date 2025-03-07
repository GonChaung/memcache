package org.example.repository;

import org.example.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = """
        SELECT e.emp_no, e.first_name, e.last_name, d.dept_name, MAX(s.salary) AS max_salary
        FROM employees e
        JOIN dept_emp de ON e.emp_no = de.emp_no
        JOIN departments d ON de.dept_no = d.dept_no
        JOIN (SELECT emp_no, salary FROM salaries WHERE to_date = '9999-01-01') s
        ON e.emp_no = s.emp_no
        WHERE s.salary > (SELECT AVG(salary) FROM salaries)
        GROUP BY e.emp_no, e.first_name, e.last_name, d.dept_name
        ORDER BY max_salary DESC LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTop10HighestPaidEmployeesWithDepartment();

    @Query(value = "SELECT MAX(emp_no) FROM employees", nativeQuery = true)
    Long findMaxEmpNoWithLock();

    @Query(value = """
        SELECT t.title 
        FROM titles t
        WHERE t.emp_no = :empNo 
        AND t.to_date = '9999-01-01'
        ORDER BY t.from_date DESC 
        LIMIT 1
    """, nativeQuery = true)
    String findTitleByEmpNo(@Param("empNo") Integer empNo);

    Optional<Employee> findByEmpNo(Integer empNo);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.deptEmpList WHERE e.empNo = :id")
    Optional<Employee> findByIdWithDept(@Param("id") Integer id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.salaryList WHERE e.empNo = :id")
    Optional<Employee> findByIdWithSalary(@Param("id") Integer id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.titleList WHERE e.empNo = :id")
    Optional<Employee> findByIdWithTitle(@Param("id") Integer id);

}
