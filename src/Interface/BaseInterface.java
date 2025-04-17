package Interface;

import Entity.User;

import java.util.Scanner;

public abstract class BaseInterface {
    Scanner sc = new Scanner(System.in);
    final User currentUser;
    int choice;

    public BaseInterface(User currentUser) {//reference always User
        this.currentUser = currentUser;
    }

    public abstract void start();
}
