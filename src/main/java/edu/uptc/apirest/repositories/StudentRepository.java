package edu.uptc.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.apirest.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    
}
