package edu.uptc.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.apirest.entities.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{
    
}
