package com.museum.management.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Integer> {

    List<Role> findAll();

}
