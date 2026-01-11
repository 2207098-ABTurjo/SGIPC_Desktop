package com.example.sgipc;

public class User {
    public String name;
    public String roll;
    public String email;
    public String codeforcesHandle;
    public String memberType;

    public User() {}

    public User(String name, String roll, String email, String codeforcesHandle, String memberType) {
        this.name = name;
        this.roll = roll;
        this.email = email;
        this.codeforcesHandle = codeforcesHandle;
        this.memberType = memberType;
    }
}