package com.example.sgipc;

public class Member {
    private String name;
    private String designation;

    public Member(String name, String designation) {
        this.name = name;
        this.designation = designation;
    }

    public String getName() { return name; }
    public String getDesignation() { return designation; }
}