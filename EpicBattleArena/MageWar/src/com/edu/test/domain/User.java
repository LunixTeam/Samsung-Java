package com.edu.test.domain;

import com.edu.test.service.Printable;

public class User implements Printable {
    public String username;
    public String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void printInfo() {
        System.out.println();
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
    }
}
