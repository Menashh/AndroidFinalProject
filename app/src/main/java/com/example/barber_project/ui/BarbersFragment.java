package com.example.barber_project.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.barber_project.R;


public class BarbersFragment extends Fragment {
    public BarbersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barbers2, container, false);
        Button buttonSher = view.findViewById(R.id.sherBarber);
        Button buttonMeni = view.findViewById(R.id.meniBarber);

        assert getArguments() != null;
        String userEmail = getArguments().getString("userEmail");

        setBarberButtonClickListener(buttonSher, userEmail, "sher@gmailcom");
        setBarberButtonClickListener(buttonMeni, userEmail, "meni@gmailcom");

        return view;
    }

    private void setBarberButtonClickListener(Button button, String userEmail, String barberEmail) {
        button.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("userEmail", userEmail);
            bundle.putString("barberEmail", barberEmail);
            Navigation.findNavController(view).navigate(R.id.action_barbersFragment_to_orderDetailsFragment, bundle);
        });
    }

}
