package edu.uptc.apirest.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "students")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 10)
    private String phone;
    @Column(nullable = false, length = 45)
    private String email;

  /* JoinTable(
        name = "student_has_courses",
        joinColumns = @JoinColumn(name = "id_student", nullable = false),
        inverseJoinColumns = @JoinColumn(name ="id_course", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Course> courses;

    public void addCourse(Course course){
        if(this.courses == null){
            this.courses = new ArrayList<>();
        }
        
        this.courses.add(course);
    }*/ 

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    @JsonIgnoreProperties({"student"})
    private List<Course> courses;


    public Student() {
        courses  = new ArrayList<>();
    }

    public Student(String name, String lastName, String phone, String email ) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName= lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone= phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public List<Course> getCourses() {
        
        return courses;
    }

    public void  addCourse(Course course){
        courses.add(course);
    }

}
