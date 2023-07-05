package com.example.barber_project.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.barber_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminWorkHoursFragment extends Fragment {


    public void workHours(String day, String path, TextView textView){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);

        final String[] value = new String[1];
        String start = day + "StartHour";
        String finish = day + "FinishHour";

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value[0] = dataSnapshot.child(day).child(start).getValue() + " - " +
                        dataSnapshot.child(day).child(finish).getValue();
                textView.setText(value[0]);
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
        View view = inflater.inflate(R.layout.fragment_admin_work_hours, container, false);

        assert getArguments() != null;
        String arg = getArguments().getString("emailAdminMyWorkHoursString");
        String finalArg = arg;

        TextView textSundayHours = view.findViewById(R.id.textViewSundayHours);
        TextView textTuesdayHours = view.findViewById(R.id.textViewTuesdayHours);
        TextView textWednesdayHours = view.findViewById(R.id.textViewWednesdayHours);
        TextView textThursdayHours = view.findViewById(R.id.textViewThursdayHours);
        TextView textFridayHours = view.findViewById(R.id.textViewFridayHours);

        String path = "barbers/" + finalArg + "/days";

        workHours("Sunday", path, textSundayHours);
        workHours("Tuesday", path, textTuesdayHours);
        workHours("Wednesday", path, textWednesdayHours);
        workHours("Thursday", path, textThursdayHours);
        workHours("Friday", path, textFridayHours);

        return view;
    }
}