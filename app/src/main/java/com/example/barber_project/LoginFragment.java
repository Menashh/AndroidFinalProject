package com.example.barber_project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    FirebaseAuth mAuth;
    EditText emailEditText1;
    EditText passwordEditText1;
    Button login;
    Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText1 = view.findViewById(R.id.login_email);
        passwordEditText1 = view.findViewById(R.id.login_password);
        login = view.findViewById(R.id.login_button);
        register = view.findViewById(R.id.go_to_sign_up_button);

        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null && getArguments().getStringArray("credentials") != null) {
            emailEditText1.setText(getArguments().getStringArray("credentials")[0]);
            passwordEditText1.setText(getArguments().getStringArray("credentials")[1]);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText1.getText().toString().trim();
                String password = passwordEditText1.getText().toString().trim();


                if (validateInfo(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(getContext(),"Login succeed",Toast.LENGTH_LONG).show();
                                        String currentUID = task.getResult().getUser().getUid();
                                        storage.setCurrentUID(currentUID);
                                        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_typesFragment);
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Email and password do not match.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

            public boolean validateInfo(String email, String password) {
                boolean isValid = false;

                if ((email == null && password == null) || (email.isEmpty() && password.isEmpty()))
                    Toast.makeText(getActivity(),"Please enter your email, and password.",Toast.LENGTH_SHORT).show();
                else if (email == null || email.isEmpty())
                    Toast.makeText(getActivity(),"Please enter your email address",Toast.LENGTH_SHORT).show();
                else if (password == null || password.isEmpty())
                    Toast.makeText(getActivity(),"Please enter your password",Toast.LENGTH_SHORT).show();
                else
                    isValid = true;

                return isValid;
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        return view;
    }
}