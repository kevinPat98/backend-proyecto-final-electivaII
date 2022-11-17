package edu.uptc.apirest.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "courses")

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(nullable = false, length = 45)
    private String name;
    @Column
    private double noteGeneral;
    @Column
    private Integer advanceGeneral;

    //Relación
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonIgnoreProperties({"course"})
    private List<Activity> activities;

  /*  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses")
   private List<Student> students;*/

   @ManyToOne
   @JoinColumn(name = "student_id")
   @JsonIgnoreProperties("courses")
   private Student student;



    public Course() {
        activities = new ArrayList<>();
       // students  = new ArrayList<>();
    }

    public Course(String name, int advanceGeneral ) {
        this.name = name;
        this.advanceGeneral = advanceGeneral;
        student.addCourse(this);
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

    public Double getNoteGeneral() {
        double noteG = Math.round(noteGeneral*100)/100d;
        return noteG;
    }

    public void setNoteGeneral(Double noteGeneral) {
        this.noteGeneral= noteGeneral;
    }

    public Integer getAdvanceGeneral() {
        return advanceGeneral;
    }

    public void setAdvanceGeneral(Integer advanceGeneral) {
        this.advanceGeneral= advanceGeneral;
    }

    public List<Activity> getActivities() {
        
        return activities;
    }

   /*  public List<Student> getStudents() {
        
        return students;
    }*/

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public Student getStudent(){
        return student;
    }

    public void setStudent (Student student){
        this.student = student;
    }

}
