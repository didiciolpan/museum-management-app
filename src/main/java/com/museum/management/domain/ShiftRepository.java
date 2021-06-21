package com.museum.management.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {

	List<Shift> findAll();
	
	@Query("SELECT s FROM Shift s, User u where s.user.id = u.id AND u.username = ?1")
	List<Shift> getMyShifts(String username);

}
