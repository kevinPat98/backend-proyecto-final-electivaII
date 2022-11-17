package edu.uptc.apirest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.apirest.entities.Course;
import edu.uptc.apirest.repositories.CourseRepository;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository repository;




    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> getCourses(){

        return repository.findAll();
    }

    public Course save( Course course){

        return repository.save(course);
    }

    public Course findById(int id){

        Optional<Course> optAct = repository.findById(id);

        return optAct.isPresent() ? optAct.get() : null;
    }

    public Course update(Course course){

        if(course.getId()!=null){
           Course e = repository.findById(course.getId()).get();
            if(e != null){
                if(course.getName()!=null){
                    e.setName(course.getName());
                }
                if(course.getNoteGeneral()!=null){
                    e.setNoteGeneral(course.getNoteGeneral());
                }
                if(course.getAdvanceGeneral()!=null){
                    e.setAdvanceGeneral(course.getAdvanceGeneral());
                }
                repository.save(e);
                return e;
            }else{
                return course;
            }
        }else{
            return course;
        }
    }

   /*  public Course update(Course course){
        if( findById( course.getId()) !=  null ){
            return repository.save( course );
        }
        return null;
    } */

    public Course delete(int id) {
        Course course = findById( id );
        if( course != null){
            repository.delete( course );
        }

        return course;
    }

}
