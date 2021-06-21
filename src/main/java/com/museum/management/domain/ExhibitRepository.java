package com.museum.management.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitRepository extends JpaRepository<Exhibit, Integer> {

	List<Exhibit> findAll();
}
