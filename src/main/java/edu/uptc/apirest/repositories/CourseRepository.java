package edu.uptc.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.apirest.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer>{
    
}
