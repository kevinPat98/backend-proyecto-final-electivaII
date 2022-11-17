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

import edu.uptc.apirest.entities.Activity;
import edu.uptc.apirest.services.ActivityService;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "https://front-proyecto-final-electiva-ii.vercel.app/")
public class ActivityController {
    
    @Autowired
    ActivityService service;

    public ActivityController(ActivityService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable <Activity> get(){
        return service.getActivities();
    }

    @GetMapping("/{id}")
    public Activity findById(@PathVariable int id){
        return service.findById(id);
    }

    @PostMapping
    public Activity save(@RequestBody Activity activity){

        return service.save(activity);
    }

    @PutMapping("/{id}")
    public Activity update(@RequestBody Activity activity, @PathVariable int id){
        return service.update(activity);
    }

    @DeleteMapping("/{id}")
    public Activity delete(@PathVariable("id") int id){
        return service.delete(id);
        
    }
}
