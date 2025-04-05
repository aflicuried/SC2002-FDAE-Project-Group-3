package Controller;

import Entity.User;

import java.util.Scanner;

public abstract class BaseController {
    protected Scanner sc = new Scanner(System.in);
    protected User currentUser;

    public BaseController(User user) {//reference always User
        this.currentUser = user;
    }

    public abstract void start();
}
