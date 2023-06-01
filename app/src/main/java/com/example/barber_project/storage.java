package com.example.barber_project;

public class storage {

    private static String uid;

    public static void setCurrentUID(String uid) { storage.uid = uid; }
    public static String getCurrentUID() { return storage.uid; }
}
