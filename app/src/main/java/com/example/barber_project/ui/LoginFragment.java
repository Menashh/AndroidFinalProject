package com.example.barber_project.ui;

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

import com.example.barber_project.R;
import com.example.barber_project.storage;
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

    public Boolean validateAdmin(String email, String pass){
        if ( ((email.equals("meni@gmail.com")) || (email.equals("sher@gmail.com"))||(email.equals("effi@gmail.com")))
                && pass.equals("123456") ){ return true; }
        else { return false; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText1 = view.findViewById(R.id.login_email);
        passwordEditText1 = view.findViewById(R.id.login_password);
        login = view.findViewById(R.id.login_button);
        register = view.findViewById(R.id.go_to_sign_up_button);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText1.getText().toString().trim();
                String password = passwordEditText1.getText().toString().trim();
                Boolean adminCheck = validateAdmin(email,password);


                if (validateInfo(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (adminCheck){
                                            Bundle bundle = new Bundle();
                                            bundle.putString("adminEmail",email);
                                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_adminFragment, bundle);
                                        }
                                        else{
                                            Bundle bundle = new Bundle();
                                            bundle.putString("userEmail",email);
                                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_userFragment2, bundle);
                                        }
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