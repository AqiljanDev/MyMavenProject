package org.example.util;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(new java.sql.Timestamp(date.getTime()));
    }
}
