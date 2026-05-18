package com.example.a8messenger;

public class User {
    private String id;  // в Firebase id представляет строку из набора символов
    private String name;
    private String lastName;
    private int age;
    private Boolean isOnline;


    public User(String id, String name, String lastName, int age, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.isOnline = isOnline;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getLastName() { return lastName; }

    public int getAge() { return age; }

    public Boolean getOnline() { return isOnline; }
}
