package com.example.barber_project.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber_project.R;
import com.example.barber_project.Reservation;
import com.example.barber_project.adpters.CustomAdapterAdminMyReservations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMyReservationsRecycleViewFragment extends Fragment {

    private ArrayList<Reservation> dataSet;
    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapterAdminMyReservations adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        String Email = getArguments().getString("emailAdminMyReservations");
        String finalEmail =Email;
        String barberName;
        if (finalEmail.equals("sher@gmailcom")) {
            barberName="Sher Cohen";
        }
        else {
            barberName = "Meni Banin";
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.child("barberName").getValue().toString().equals(barberName)){
                        AdminMyReservationsRecycleViewFragment.this.dataSet.add(new Reservation(
                                childSnapshot.child("userName").getValue().toString(),
                                childSnapshot.child("dayName").getValue().toString(),
                                childSnapshot.child("date").getValue().toString(),
                                childSnapshot.child("time").getValue().toString()
                        ));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_my_reservations_recycle_view,
                container, false);

        recycleView = view.findViewById(R.id.adminMyReservationsRecycleView);
        layoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        dataSet = new ArrayList<Reservation>();

        assert getArguments() != null;
        String arg = getArguments().getString("emailAdminMyReservations");
        String finalArg = arg;
        String barberName;
        if (finalArg.equals("sher@gmailcom")){barberName = "Sher Cohen";}
        else{barberName = "Meni Banin";}

        adapter = new CustomAdapterAdminMyReservations(dataSet,barberName);
        recycleView.setAdapter(adapter);

        return view;
    }
}
