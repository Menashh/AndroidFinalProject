package com.example.barber_project.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.barber_project.R;
import com.example.barber_project.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class OrderDetailsFragment extends Fragment {

    public void optionalReservationsForSpecificDay(String barberEmail, String dayName,
                                                   String userEmail, String date, int dayOfMonth,
                                                   int month, int year){

        String path = "barbers/" + barberEmail + "/days/" + dayName;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String []options;
                    List<String> reservations = new ArrayList<>();
                    String startHour, finishHour;
                    String startPath = "/" + dayName +"StartHour";
                    String finishPath ="/" + dayName +"FinishHour";
                    startHour = task.getResult().child(startPath).getValue().toString();
                    finishHour = task.getResult().child(finishPath).getValue().toString();

                    String []tempWorkingHours = Data.workingHours;
                    String []tempReservations = Data.reservations;
                    int startHourIndex = 0, finishHourIndex = 0;

                    for (int i = 0; i<tempWorkingHours.length; i++){
                        if (startHour.equals(tempWorkingHours[i])){
                            startHourIndex = i;
                        }
                        if (finishHour.equals(tempWorkingHours[i])){
                            finishHourIndex = i;
                        }
                    }

                    if (finishHourIndex <=10){
                        for(int i = startHourIndex; i< finishHourIndex; i++){
                            reservations.add(tempReservations[i]);
                        }
                    }
                    else {
                        for(int i = startHourIndex; i< finishHourIndex - 1; i++){
                            reservations.add(tempReservations[i]);
                        }
                    }

                    options = reservations.toArray(new String[0]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose an option");

                    int finalMonth = month;
                    String[] finalOptions = options;
                    builder.setItems(finalOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedOption = finalOptions[which];

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                            builder2.setTitle("Reservation at "+ dayName+ " " + dayOfMonth + "/" + (finalMonth) + "/" + year);
                            builder2.setMessage("Are you sure you want to make it an reservation?");
                            builder2.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveAppointment(userEmail, barberEmail, dayName, date,selectedOption,
                                            dayOfMonth, finalMonth, year);
                                }
                            });
                            builder2.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder2.show();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    public void saveReservationInner(String userEmailID, String barberEmailID, String dayName, String date,
                                     String time, int dayOfMonth, int month, int year, String appointmentID,
                                     String userName, String barberName){
        Reservation appointment = new Reservation(appointmentID, userEmailID, barberEmailID, userName, barberName,
                dayName, date, time, dayOfMonth, month, year);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations").child(appointment.reservationID);
        myRef.setValue(appointment);
        Toast.makeText(getActivity(),"Your reservation has been saved", Toast.LENGTH_LONG).show();
    }

    public void saveAppointment(String userEmailID, String barberEmailID, String dayName,
                                String date, String time, int dayOfMonth, int month, int year){
        final Boolean[] flag = {false};
        String reservationID;
        final String[] userName = new String[2];
        String barberName;

        if (barberEmailID.equals("meni@gmailcom")){
            barberName = "Meni Banin";
        }
        else if (barberEmailID.equals("sher@gmailcom")){
            barberName = "Sher Cohen";
        }
        else { barberName = "Effi";}
        reservationID = dayOfMonth + "-" + month + "-" + year + " -- " + time + " -- " + barberName;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    if (childSnapshot.child("reservationID").getValue().toString().equals(reservationID)){
                        flag[0]= true;
                        break;
                    }
                }
                if (flag[0]){
                    Toast.makeText(getActivity(),
                            "This reservation is unavailable. Please choose another one.",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String path;
                    path = "users/" + userEmailID + "/name";
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    DatabaseReference myRef2 = database2.getReference(path);
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userName[0] = snapshot.getValue().toString();
                            saveReservationInner(userEmailID, barberEmailID, dayName, date, time,
                                    dayOfMonth, month, year, reservationID, userName[0], barberName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderdetails, container, false);

        assert getArguments() != null;
        String arg = getArguments().getString("userEmail");
        String userEmail = arg;
        arg = getArguments().getString("barberEmail");
        String barberEmail = arg;

        CalendarView calendarView = view.findViewById(R.id.calendarViewMakeReservation);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
                        Calendar.LONG, Locale.getDefault());
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;

                if ((dayName.equals("Saturday"))||(dayName.equals("Monday"))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You can't make a reservation on Monday or Saturday");
                    builder.show();
                }

                else{
                    final Boolean[] flag = {false};
                    String path;
                    path = "barbers/" + barberEmail +"/daysOff";
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(path);
                    int finalMonth = month;
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                if(childSnapshot.child("date").getValue().toString().equals(date)){
                                    flag[0] = true;
                                }
                            }
                            if (flag[0]){
                                Toast.makeText(getActivity(),
                                        "This day is unavailable. Please choose another one.",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                optionalReservationsForSpecificDay(barberEmail, dayName, userEmail, date,
                                        dayOfMonth, finalMonth, year);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        return view;
    }
}