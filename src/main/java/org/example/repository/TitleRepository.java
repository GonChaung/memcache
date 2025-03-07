package org.example.repository;

import org.example.model.Title;
import org.example.model.composite.TitleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, TitleId> {
    List<Title> findByEmpNoAndToDateAfter(Integer empNo, LocalDate toDate);
}
