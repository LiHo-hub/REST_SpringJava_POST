package com.example.demo;

//WICHTIG: hibernate.cfg.xml <property name="hibernate.hbm2ddl.auto">XYZ</property> ändern!
// In Zusammenarbeit mit Christopher Alb.
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;


@SpringBootApplication
@RestController
public class DemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

// Gibt einen Gruß zurück
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "Lina") String name) {
        return String.format("Hello %s!", name);
    }

    // Wenn der Text "check mich" im Body (plain text) gesendet wird, bekommt man eine 200 zurück, sonst 400!
    @PostMapping("/check")
    public ResponseEntity<String> checker(@RequestBody String wordchecker) {
        // Hier checkt er gegen mit equals() --> nicht == verwenden!
        if (wordchecker.equals("check mich")) {
            // HttpStatus.OK ist 200 also alles i.o.
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        // HttpStatus.BadRequest ist 400 also fehlerhafte Eingabe
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    //Präsentationsschicht Java -> JSON
    @GetMapping("/student")
    public String viewStudents() {
        Student student1 = new Student("Lina");
        student1.setAge(20);
        student1.setId(3L);

        String studentObjectMappedToJSONString = null;
        ObjectMapper om = new ObjectMapper();
        try {
            studentObjectMappedToJSONString = om.writeValueAsString(student1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return studentObjectMappedToJSONString;
    }


    // Fügt einen neuen Studierenden in die DB ein (A_I für ID)
    @PostMapping("/sqlstudent")
    public String createPerson(@RequestParam(value = "name") String name) {
        Student student = new Student(name);
        student.setAge(26);

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        session.beginTransaction();
        session.persist(student);
        session.flush();
        session.close();
        return "Studierende(r) wurde erfolgreich in der Datenbank persistiert!";
    }

    //Präsentationsschicht DB -> JSON: Gibt den Studierenden mit der ID=1 zurück
    @GetMapping("/sqlstudent")
    public String viewSQLStudents() {
        Student student = new Student();

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        session.beginTransaction();
        Student studi = session.load(Student.class, 1L);
        session.flush();

        String studentObjectMappedToJSONString = null;
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            studentObjectMappedToJSONString = om.writeValueAsString(studi);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return studentObjectMappedToJSONString;


    }


}

