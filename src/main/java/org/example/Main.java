package org.example;

import org.example.util.ConnectionManager;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        try(var conn = ConnectionManager.open();
            Statement statement = conn.createStatement();) {

            System.out.print("Choise Login or Register: ");
            String choiseEnter = scanner.nextLine();

            GHOST: if (choiseEnter.equals("Login")) { //Login function

                PreparedStatement loginVerification = conn.prepareStatement("SELECT * FROM user WHERE login = ? and password = ?");
                loginVerification.setString(1, getElement("login"));
                loginVerification.setString(2, getElement("password"));
                ResultSet resultSet = loginVerification.executeQuery();

                while (resultSet.next()) {
                        Home.print();
                    break;
                }

            } else if (choiseEnter.equals("Register")) { //Register function

                String[] columnsName = {"first_name", "last_name", "birth", "about_me", "key_number", "petname", "login", "password", "email", "phone_number"};

                PreparedStatement newUsers = conn.prepareStatement(
                        "INSERT INTO user(first_name, last_name, birth, about_me, key_number, petname, login, `password`, email, phone_number, register_time)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                GHOST2: for (int i = 1, j = 0; i <= 10; i++, j++) {
                    String colElement = getElement(columnsName[j]);
                    if(columnsName[j].equals("login") || columnsName[j].equals("phone_number")) {
                        PreparedStatement tempVerification = conn.prepareStatement("SELECT * FROM user WHERE " + (columnsName[j].equals("login") ? "login" : "phone_number") + "=?");
                        tempVerification.setString(1, colElement);
                        ResultSet tempResulset = tempVerification.executeQuery();

                        while (tempResulset.next()) {
                            System.out.println("This " + (columnsName[j].equals("login") ? "login" : "phone_number") + " already exists(Такой " + (columnsName[j].equals("login") ? "логин" : "номер телефона") + " уже существует)");
                            break GHOST2;
                        }
                    }

                    newUsers.setString(i, colElement);

                }
                Date date = new Date();
                newUsers.setString(11, String.valueOf(new Timestamp(date.getTime())));
                newUsers.executeUpdate();
                Home.print();

            } else { //Incorrect request
                System.out.println("Such login already exists");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    static String getElement(String column) {
        Scanner scannerReg = new Scanner(System.in);
        System.out.print(column + ": ");
        String res = scannerReg.nextLine();
        return res;
    }

}
