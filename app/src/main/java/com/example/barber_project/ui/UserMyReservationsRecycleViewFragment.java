package com.example.barber_project.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber_project.R;
import com.example.barber_project.Reservation;
import com.example.barber_project.adpters.CustomAdapterUserMyReservations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMyReservationsRecycleViewFragment extends Fragment {

    private ArrayList<Reservation> dataSet;
    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapterUserMyReservations adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        String arg = getArguments().getString("userEmailMyReservations");

        String finalArg = arg;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.child("userEmailID").getValue().toString().equals(finalArg)) {
                        UserMyReservationsRecycleViewFragment.this.dataSet.add(new Reservation(
                                childSnapshot.child("barberName").getValue().toString(),
                                childSnapshot.child("dayName").getValue().toString(),
                                childSnapshot.child("date").getValue().toString(),
                                childSnapshot.child("time").getValue().toString()
                        ));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_my_reservations_recycle_view, container, false);

        recycleView = view.findViewById(R.id.userMyReservationsRecycleView);

        layoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(layoutManager);

        recycleView.setItemAnimator(new DefaultItemAnimator());

        dataSet = new ArrayList<Reservation>();

        assert getArguments() != null;
        String arg = getArguments().getString("userEmailMyReservations");
        String finalArg = arg;

        adapter = new CustomAdapterUserMyReservations(dataSet, finalArg);
        recycleView.setAdapter(adapter);

        return view;
    }
}