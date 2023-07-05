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
import com.example.barber_project.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends Fragment {

    Button go_to_login_btn;
    Button sign_up_button;
    EditText emailEditText;
    EditText passwordEditText1;
    EditText passwordEditText2;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth=FirebaseAuth.getInstance();
        go_to_login_btn = view.findViewById(R.id.go_to_login_btn);
        sign_up_button =view.findViewById(R.id.signup_button);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText1 = view.findViewById(R.id.signup_password);
        passwordEditText2 = view.findViewById(R.id.signup_password2);


        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password1 = passwordEditText1.getText().toString().trim();
                String password2 = passwordEditText2.getText().toString().trim();

                if (validateInfo(email, password1, password2)) {
                    mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        insertUserRealTime(new User(email, task.getResult().getUser().getUid()));

                                        Bundle bundle = new Bundle();
                                        bundle.putStringArray("credentials", new String[]{email, password1});

                                        Toast.makeText(getContext(),"SignUp succeed",Toast.LENGTH_LONG).show();
                                        Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_loginFragment,bundle);
                                    } else {
                                        Toast.makeText(getContext(),"The email address is not valid / Short password",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            private boolean validateInfo(String email, String password1, String password2) {
                if (email == null || password1 == null || password2 == null || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(getActivity(),"Please enter all the fields.",Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (!password1.equals(password2)) {
                    Toast.makeText(getActivity(),"Passwords do not match, Please try again.",Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }


            public void insertUserRealTime(User user) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users").child(user.getUid());
                myRef.setValue(user);
            }
        });


        go_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        });

        return view;
    }
}