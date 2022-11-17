package edu.uptc.apirest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.apirest.entities.Student;
import edu.uptc.apirest.services.StudentService;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:4200/")
public class StudentController {
    
    @Autowired
    StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable <Student> get(){
        return service.getStudents();
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable int id){
        return service.findById(id);
    }

    @PostMapping
    public Student save(@RequestBody Student std){

        return service.save(std);
    }

    @PutMapping("/{id}")
    public Student update(@RequestBody Student std, @PathVariable int id){
        return service.update(std);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable("id") int id){
        return service.delete(id);
    }
}
