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

import edu.uptc.apirest.entities.Course;
import edu.uptc.apirest.services.CourseService;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:4200/")
public class CourseController {
    
    @Autowired
    CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable <Course> get(){
        return service.getCourses();
    }

    @GetMapping("/{id}")
    public Course findById(@PathVariable int id){
        return service.findById(id);
    }

    @PostMapping
    public Course save(@RequestBody Course course){

        return service.save(course);
    }

    @PutMapping("/{id}")
    public Course update(@RequestBody Course course, @PathVariable int id){
        return service.update(course);
    }

    @DeleteMapping("/{id}")
    public Course delete(@PathVariable("id") int id){
        return service.delete(id);
    }
}
