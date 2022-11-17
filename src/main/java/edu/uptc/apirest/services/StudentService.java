package edu.uptc.apirest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.apirest.entities.Student;
import edu.uptc.apirest.repositories.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository repository;




    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getStudents(){

        return repository.findAll();
    }

    public Student save( Student student){

        return repository.save(student);
    }

    public Student findById(int id){

        Optional<Student> optAct = repository.findById(id);

        return optAct.isPresent() ? optAct.get() : null;
    }

   /*  public Act update(Act act){

        if(act.getId()!=null){
           Act e = actRepository.findById(act.getId()).get();
            if(e != null){
                if(act.getDescription()!=null){
                    e.setDescription(act.getDescription());
                }
                if(act.getMeet()!=null){
                    e.setMeet(act.getMeet());
                }
                actRepository.save(e);
                return e;
            }else{
                return act;
            }
        }else{
            return act;
        }
    }*/

    public Student update(Student student){
        // if( findById( student.getId()) !=  null ){
        //     return repository.save( student );
        // }
        // return null;
        if(student.getId()!=null){
            Student e = repository.findById(student.getId()).get();
             if(e != null){
                 if(student.getName()!=null){
                     e.setName(student.getName());
                 }
                 if(student.getLastName()!=null){
                     e.setLastName(student.getLastName());
                 }
                 if(student.getPhone()!=null){
                     e.setPhone(student.getPhone());
                 }
                 if(student.getEmail()!=null){
                    e.setEmail(student.getEmail());
                }
                 repository.save(e);
                 return e;
             }else{
                 return student;
             }
         }else{
             return student;
         }
    } 

    public Student delete(int id) {
        Student std = findById( id );
        if( std != null){
            repository.delete( std );
        }

        return std;
    }

}
