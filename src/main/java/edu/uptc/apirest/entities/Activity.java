package edu.uptc.apirest.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "activity")

public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(nullable = false, length = 45)
    private String name;
    @Column(nullable = false)
    private LocalDateTime expirationDate;
    @Column
    private double note;
    @Column(nullable = false)
    private int valueNote;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties({"activities"})
    private Course course;


    public Activity() {
    }

    public Activity(String name, LocalDateTime expirationDate, double note, int valueNote, Course course ) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.note = note;
        this.valueNote = valueNote;
        course.addActivity(this);
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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate= expirationDate;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public Double getNote() {
        return note;
    }

    public void setValueNote(Integer valueNote) {
        this.valueNote = valueNote;
    }

    public Integer getValueNote() {
        return valueNote;
    }

    public Course getCourse(){
        return course;
    }

    public void setCourse(Course course){
        this.course=course;
    }
    
    /*Suma cada nota */
    int suma =0;
    public double sumValuesNotes(double valueNote){
        suma += valueNote;
        return suma;
    }

    /*Calcular Valor de cada actividad respecto a su nota */
    public double valueNoteActivity(double note, int valueNote){
        return note*valueNote/100;
    }
}
