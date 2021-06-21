package com.museum.management.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {

	List<Incident> findAll();
	
	@Query("SELECT s FROM Incident s, User u where s.user.id = u.id AND u.username = ?1")
	List<Incident> getMyIncidents(String username);
}
