package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "datensaetze")
public class Student {
    private String name;
    private int age;
    private Long id;

    public Student(String name) {
        this.name = name;
    }

    // Konstruktor
    public Student() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
}

