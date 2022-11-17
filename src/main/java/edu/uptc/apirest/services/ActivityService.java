package edu.uptc.apirest.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.apirest.entities.Activity;
import edu.uptc.apirest.entities.Course;
import edu.uptc.apirest.repositories.ActivityRepository;

@Service
public class ActivityService {
    
    @Autowired
    private ActivityRepository repository;

    private  CourseService courseService;

    public ActivityService(ActivityRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public List<Activity> getActivities(){
        return repository.findAll();
    }
    //se va a validar que los valores de las actividades no superen el 100
    public Activity save( Activity activity){
        Integer id = activity.getCourse().getId();

        if(validarValorNota (activity,  id) == true){
            return repository.save(activity);
        }else{
            return null;
        }

    }

    public Activity findById(int id){

        Optional<Activity> optAct = repository.findById(id);

        return optAct.isPresent() ? optAct.get() : null;
    }

    public Activity update(Activity act){
        Boolean status = false;
        Integer beforeAdvance = obtenerValueNoteBefore(act.getId());
        Course course= obtenerCourseBefore(act.getId());
                act.setCourse(course);
        if(act.getId()!=null){
           Activity e = repository.findById(act.getId()).get();
            if(e != null){
                if(act.getName()!=null){
                    e.setName(act.getName());
                }
                if(act.getExpirationDate()!=null){
                    e.setExpirationDate(act.getExpirationDate());
                }
                if(act.getNote()!=0){
                    e.setNote(act.getNote());
                }
                if(act.getValueNote()!=null){
                   //validar que el nuevo ponderado de la nota, no supere el 100% [Se envia el beforeValue para poder restarlo]
                    if(validarUpdateValorNota(act,  act.getCourse().getId(), (double) beforeAdvance) == true){
                        if(updateAdvance(act, (int)beforeAdvance)== true ){
                            updateNoteGeneral(act);
                            e.setValueNote(act.getValueNote());
                            status = true;
                        }  
                    }
                    
                }
                if(act.getCourse()!=null){
                    e.setCourse(act.getCourse());
                
                }
                if(status==true){
                    repository.save(e);
                    return e;
                }else{
                    return null; //retorna null si no cumple con las condiciones
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    //Asignar Avance, validar que porcentaje tiene cada actividade
    public void assignAdvance(Activity activity){
        int sum=0;
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                    sum += getActivities().get(i).getValueNote();
                }
            }
            System.out.println("note: "+activity.getNote());
            if(activity.getNote() == 0){
                activity.getCourse().setAdvanceGeneral(sum);
                courseService.update(activity.getCourse());
            }else{
                sum+=activity.getValueNote();
                activity.getCourse().setAdvanceGeneral(sum);
                courseService.update(activity.getCourse());
            }
            
    }

    //Actualizar Avance del curso
    public Boolean updateAdvance(Activity activity, Integer beforeAdvance){
        int sum=0;
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                    sum += getActivities().get(i).getValueNote();
                }
            }
            sum-=beforeAdvance;
            if(sum<=100){
                if(activity.getNote() == 0){
                    activity.getCourse().setAdvanceGeneral(sum);
                    courseService.update(activity.getCourse());
                    return true;
                }else{
                    sum+=activity.getValueNote();
                    activity.getCourse().setAdvanceGeneral(sum);
                    courseService.update(activity.getCourse());
                    return true;
                }

            }
        
           return false;
            
    }

   //Calcular nota general de acuerdo a las actividades
    public void assignNoteGeneral(Activity activity){
        double sum=0;
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                     sum += activity.valueNoteActivity(getActivities().get(i).getNote(), getActivities().get(i).getValueNote());
                }
            }
            sum+=activity.valueNoteActivity(activity.getNote(), activity.getValueNote());
            activity.getCourse().setNoteGeneral(sum);
            courseService.update(activity.getCourse()); 
    }

    //Actualizar Nota general
    public void updateNoteGeneral(Activity activity){
        double sum=0;
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                     sum += activity.valueNoteActivity(getActivities().get(i).getNote(), getActivities().get(i).getValueNote());
                }
            }
           
           if(activity.getNote() == 0){
            activity.getCourse().setNoteGeneral(sum);
            courseService.update(activity.getCourse());
           }else{
            activity.getCourse().setNoteGeneral(sum);
            courseService.update(activity.getCourse()); 
           }
           
    }
    
    //Validar que la nota ingresada sea menor om igual a 100 y validar que la suma de los valores de cada actividad no supere el 100
    public Boolean validarValorNota(Activity activity, Integer id){
        double sum=0;

        if(activity.getValueNote() <= 100){
           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                     sum = activity.sumValuesNotes(getActivities().get(i).getValueNote());
                }
            }
            sum+=activity.getValueNote();
            System.out.println("SUMA"+sum);
            if(sum<=100){
                assignNoteGeneral(activity);
                assignAdvance(activity);
                return true;
            }

        }
        return false;
    }
    //Restar, sumar y validar que la nota ingresada sea menor om igual a 100 y validar que la suma de los valores de cada actividad no supere el 100
    public Boolean validarUpdateValorNota(Activity activity, Integer idCourse, Double note){
        double sum=0;

        if(activity.getValueNote() <= 100){
           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == idCourse){
                     sum = activity.sumValuesNotes(getActivities().get(i).getValueNote());
                }
            }
            sum-=note;
            sum+=activity.getValueNote();

            if(sum<=100){
                assignNoteGeneral(activity);
                assignAdvance(activity);
                courseService.update(activity.getCourse());
                return true;
            }

        }
        return false;
    }



    public Course obtenerCourseBefore (Integer idActivity){
        for (int i = 0; i < getActivities().size(); i++) {
            if(getActivities().get(i).getId() == idActivity){

                  return getActivities().get(i).getCourse();
             }
         }
         return null;
    }

    public Integer obtenerValueNoteBefore (Integer idActivity){
        for (int i = 0; i < getActivities().size(); i++) {
            if(getActivities().get(i).getId() == idActivity){
                  return getActivities().get(i).getValueNote();
             }
         }
         return null;
    }

    public void deleteAdvance(Activity activity){
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                    if(getActivities().get(i).getId() == activity.getId()){
                        activity.getCourse().setAdvanceGeneral(activity.getCourse().getAdvanceGeneral() - activity.getValueNote());
                        activity.setValueNote(0);
                        courseService.update(activity.getCourse());
                    }   
                }
            }
    }
    //Actualizar nota general cuando se ha eliminado una actividad
    public void updateNoteGeneralofDelete(Activity activity){
        double sum=0;
        Integer id = activity.getCourse().getId();

           for (int i = 0; i < getActivities().size(); i++) {
               if(getActivities().get(i).getCourse().getId() == id){
                     sum += activity.valueNoteActivity(getActivities().get(i).getNote(), getActivities().get(i).getValueNote());
                }
            }
            sum-=activity.valueNoteActivity(activity.getNote(), activity.getValueNote());
            activity.getCourse().setNoteGeneral(sum);
            courseService.update(activity.getCourse()); 
    }


    public String alertActivity (Activity activity){
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime act =  activity.getExpirationDate();

        int hoursN  = now.getHour(), minutesN = now.getMinute();
        int hours  = act.getHour(), minutes = act.getMinute();

        int totalDays = act.getDayOfMonth()-now.getDayOfMonth();
        int totalHours =  hours-hoursN, totalMinu = minutes-minutesN;

        String text;
         //mes igual
        if(now.getYear()==act.getYear() && now.getMonthValue()==act.getMonthValue()){
            //Dos casos [negativo o positivos]
            if(totalDays==0){
                if(totalMinu >=0 ){
                     text ="Te quedan  "+totalHours+" horas y "+totalMinu+" minutos para presentar tu actividad '"+activity.getName()+"' del curso '"
                     +activity.getCourse().getName();
                     return text;
                }else{
                 totalHours-=1;
                 totalMinu=60+totalMinu;
                 text ="Te quedan  "+totalHours+" horas y "+totalMinu+" minutos para presentar tu actividad '"+activity.getName()+"' del curso '"
                     +activity.getCourse().getName();
                     return text;
                }
            }else if(totalDays>0 && totalDays<=3){
                text ="Te quedan sólo "+totalDays+" días para presentar tu actividad '"+activity.getName()+"' del curso '"
                +activity.getCourse().getName();
                return text;
            }
            
        }

        return "NA"; //no cumple ninguna
    }

    

  /*  public Activity update(Activity activity){
        if( findById( activity.getId()) !=  null ){
            return repository.save( activity );
        }
        return null;
    } */

    public Activity delete(int id) {
        Activity activity = findById( id );
        if( activity != null){
            deleteAdvance(activity);
            updateNoteGeneralofDelete(activity);
            repository.delete( activity );
        }

        return activity;
    }

}


    // Calcular diferencia de dias
  /* public void calculateDifferenceDays(Activity activity){
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime act =  activity.getExpirationDate();

        int hoursN  = now.getHour(), minutesN = now.getMinute(), secondsN = now.getSecond(), yearN = now.getYear(), monthN = now.getMonthValue(), daysN = now.getDayOfMonth();
        int hours  = act.getHour(), minutes = act.getMinute(), seconds = act.getSecond(), year = act.getYear(), month = act.getMonthValue(), day = act.getDayOfMonth();
        //cuando son varios meses y los dias son positivos o negativos
        int totalDays;
        int totalMonths = month - monthN;
        if(year-yearN ==0  && month > monthN &&  day-daysN>0){ //diciemnbre 20 
            if( totalMonths==1){
                if(now.getMonthValue()==04 || now.getMonthValue()==06 || now.getMonthValue()==04 || now.getMonthValue()==9 || now.getMonthValue()==11   ){
                    totalDays= 30 - now.getDayOfMonth() + act.getDayOfMonth();
                    System.out.println("1.Total de días "+totalDays);
                }else if(act.getMonthValue()==02 ){
                    totalDays= 28 - now.getDayOfMonth() + act.getDayOfMonth();
                    System.out.println("2.Total de días "+totalDays);
                }else{
                    totalDays= 31 - now.getDayOfMonth() + act.getDayOfMonth();
                    System.out.println("3.Total de días "+totalDays);
                }
            }else{
                for(int i= now.getMonthValue(); i<act.getMonthValue(); i++){
                    System.out.println("meses" + i);
                }

            }
            
            
            int  totalDays = day-daysN;
            System.out.println("Te quedan "+totalDays+" dias para presentar tu actividad '"+activity.getName()+"' del curso '"
            +activity.getCourse().getName());   
        }
        if(year-yearN ==0  && month > monthN &&  day-daysN<=0){
            int totalMonth = month- monthN;
            if(act.getMonthValue()==04 || act.getMonthValue()==06 || act.getMonthValue()==04 || act.getMonthValue()==9 || act.getMonthValue()==11   ){
                
            }else if(act.getMonthValue()==02 ){

            }else{

            }
            if(totalMonth==1){
                
            }
            int  totalDays = day-daysN;
            System.out.println("Te quedan "+totalDays+" dias para presentar tu actividad '"+activity.getName()+"' del curso '"
            +activity.getCourse().getName());   
        }
        if(year-yearN <=0 ||  month-monthN<=0 || day-daysN<=0){
            System.out.println("Tu actividad ya venció");
        }
         //quedan dias 
         if(year-yearN ==0 && month-monthN==0 && day-daysN!=0){
            int  totalDays = day-daysN;
            System.out.println("Te quedan menos de "+totalDays+" dias para presentar tu actividad '"+activity.getName()+"' del curso '"
            +activity.getCourse().getName());  
        }
        //quedan sólo horas
        if(year-yearN ==0 && month-monthN==0 && day-daysN==0){
           int  totalHours = hours-hoursN, totalMinu = minutes-minutesN;
           if(totalMinu >=0 ){
                System.out.println("1Te quedan  "+totalHours+" horas y "+totalMinu+" minutos para presentar tu actividad '"+activity.getName()+"' del curso '"
                +activity.getCourse().getName());  
           }else{
            totalHours-=1;
            totalMinu=60+totalMinu;
            System.out.println("2Te quedan  "+totalHours+" horas y "+totalMinu+" minutos para presentar tu actividad '"+activity.getName()+"' del curso '"
                +activity.getCourse().getName()); 
           }
            
        }
    } */ 