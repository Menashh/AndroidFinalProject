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

public class UserFragment extends Fragment {
    private TextView textViewWelcome;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        textViewWelcome = view.findViewById(R.id.textViewUserWelcome);

        assert getArguments() != null;
        String arg = getArguments().getString("userEmail");
        arg = arg.replace(".","");
        String userEmail = arg;

        Button makeReservationButton = view.findViewById(R.id.buttonMakeReservation);
        makeReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    String userEmail = getArguments().getString("userEmail");
                    navigateToBarbersFragment(userEmail);
                }
            }
        });

        Button myReservationsButton = view.findViewById(R.id.buttonUserMyReservations);
        myReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    String userEmail = getArguments().getString("userEmail");
                    navigateToMyReserevationsFragment(userEmail);
                }
            }
        });

        return view;
    }

    private void fetchUserName(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(userEmail);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue(String.class);
                if (userName != null) {
                    textViewWelcome.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void navigateToBarbersFragment(String userEmail) {
        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        Navigation.findNavController(requireView()).navigate(R.id.action_userFragment_to_barbersFragment, bundle);
    }

    private void navigateToMyReserevationsFragment(String userEmail) {
        Bundle bundle = new Bundle();
        bundle.putString("userEmailMyReservations", userEmail);
        Navigation.findNavController(requireView()).navigate(R.id.action_userFragment_to_userMyReservationsRecycleViewFragment2, bundle);
    }
}
