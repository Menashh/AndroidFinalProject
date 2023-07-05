package com.example.barber_project;

public class Reservation {
    public String reservationID;
    public String userEmailID;
    public String barberEmailID;
    public String userName;
    public String barberName;
    public String dayName;
    public String date;
    public String time;
    public int dayOfMonth;
    public int month;
    public int year;

    public Reservation(String reservationID, String userEmailID, String barberEmailID,
                       String userName, String barberName, String dayName,
                       String date, String time, int dayOfMonth, int month, int year) {
        this.reservationID = reservationID;
        this.userEmailID = userEmailID;
        this.barberEmailID = barberEmailID;
        this.userName = userName;
        this.barberName = barberName;
        this.dayName = dayName;
        this.date = date;
        this.time = time;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    public Reservation(String userName, String dayName, String date, String time) {
        this.userName = userName;
        this.dayName = dayName;
        this.date = date;
        this.time = time;
    }

}
