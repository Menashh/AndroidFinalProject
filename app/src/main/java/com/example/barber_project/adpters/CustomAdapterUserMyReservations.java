package com.example.barber_project.adpters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber_project.R;
import com.example.barber_project.Reservation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapterUserMyReservations extends RecyclerView.Adapter<CustomAdapterUserMyReservations.MyViewHolder> {
    private ArrayList<Reservation> dataSet;
    private String path;

    public CustomAdapterUserMyReservations(ArrayList<Reservation> dataSet, String path) {
        this.dataSet = dataSet;
        this.path = path;
    }

    public void removeReservation(String path) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservations").child(path);
        ref.removeValue();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewDate;
        TextView textViewDay;
        TextView textViewBarberName;
        TextView textViewTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewUserMyReservations);
            textViewDay = itemView.findViewById(R.id.textViewUserCardsLayoutMyReservationsDay);
            textViewDate = itemView.findViewById(R.id.textViewUserCardsLayoutMyReservationsDate);
            textViewTime = itemView.findViewById(R.id.textViewUserCardsLayoutMyReservationsTime);
            textViewBarberName = itemView.findViewById(R.id.textViewUserCardsLayoutMyReservationsBarberName);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_my_reservations_cards_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        CardView cardView = viewHolder.cardView;
        TextView textViewDate = viewHolder.textViewDate;
        TextView textViewDay = viewHolder.textViewDay;
        TextView textViewBarberName = viewHolder.textViewBarberName;
        TextView textViewTime = viewHolder.textViewTime;

        Reservation reservation = dataSet.get(position);

        textViewDate.setText(reservation.date);
        textViewDay.setText(reservation.dayName);
        textViewBarberName.setText(reservation.userName);
        textViewTime.setText(reservation.time);

        String ReservationID = textViewDate.getText().toString() + " -- " +
                textViewTime.getText().toString() + " -- " +
                textViewBarberName.getText().toString();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Removing the reservation");
                builder.setMessage("Are you sure you want to remove it?");
                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeReservation(ReservationID);
                        Toast.makeText(view.getContext(), "Your reservation has been removed.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

