package com.example.akoni.conread;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private Button logIn;
    private TextView forgot;
    public EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        logIn = findViewById(R.id.button);
        forgot = findViewById(R.id.forgotText);
        email = findViewById(R.id.SignIn_Email);
        password = findViewById(R.id.SignIn_Password);
        mAuth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        logIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent i = new Intent(SignIn.this, TabMenu.class);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(SignIn.this, "Email/Password is incorrect.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
        );
    }
}
