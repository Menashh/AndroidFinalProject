package com.example.barber_project.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.barber_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        TextView textViewEnter = view.findViewById(R.id.textViewAdminEnter);
        Button buttonChangeWorkingHours = view.findViewById(R.id.buttonAdminChangeWorkingHours);
        Button buttonChanges = view.findViewById(R.id.buttonAdminAddDayOff);
        Button buttonDaysOff = view.findViewById(R.id.buttonAdminMyDaysOff);
        Button buttonWorkingHours = view.findViewById(R.id.buttonAdminMyWorkingHours);
        Button buttonMyReservations = view.findViewById(R.id.buttonAdminMyReservations);


        assert getArguments() != null;
        String Email = getArguments().getString("adminEmail");
        Email = Email.replace(".","");
        String finalEmail = Email;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("barbers").child(finalEmail);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewEnter.setText(String.valueOf(dataSnapshot.child("name").getValue()) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonChangeWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("emailAdminWorkHoursString",finalEmail);
                Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminChangeWorkHoursFragment, bundle);
            }
        });

        buttonChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("emailAdminAddDayOff",finalEmail);
                Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminAddDayOff, bundle);
            }
        });

        buttonDaysOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("emailAdminDaysOff",finalEmail);
                Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminDaysOffRecycleViewFragment, bundle);
            }
        });

        buttonWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("emailAdminMyWorkHoursString",finalEmail);
                Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminWorkHoursFragment, bundle);
            }
        });

        buttonMyReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("emailAdminMyReservations",finalEmail);
                Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminMyReservationsRecycleViewFragment, bundle);
            }
        });

        return view;
    }
}