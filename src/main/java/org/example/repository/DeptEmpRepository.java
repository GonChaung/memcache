package org.example.repository;

import org.example.model.DeptEmp;
import org.example.model.composite.DeptEmpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {
    Optional<DeptEmp> findByEmpNo(Integer empNo);
    List<DeptEmp> findByEmpNoAndToDateAfter(Integer empNo, LocalDate toDate);
}
