package com.example.getApi.entity;
import jakarta.persistence.*;


public class Person {
    @Id
    private Long id;
    private String name;
    private int age;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] data;


    public Person() {
    }


    public Person(Long id, String Name, int age, byte[] data) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.data = data;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

