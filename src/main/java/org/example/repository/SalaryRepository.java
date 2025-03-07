package org.example.repository;

import org.example.model.Salary;
import org.example.model.composite.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
    Optional<Salary> findByEmpNoAndToDateIsNull(Integer empNo);
    List<Salary> findByEmpNoAndToDateAfter(Integer empNo, LocalDate toDate);
}
