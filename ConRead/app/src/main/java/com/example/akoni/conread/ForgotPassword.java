package com.example.akoni.conread;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText backupEmail;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private Button sendB, cancelB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backupEmail = findViewById(R.id.recoveryEmail);
        sendB = findViewById(R.id.send);
        cancelB = findViewById(R.id.cancel);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = backupEmail.getText().toString().trim();
                progressDialog.setMessage("Sending Email. Please Wait.");

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter email for recovery.", Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.show();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Reset password was sent to your email.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ForgotPassword.this, SignIn.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Sending Failed.", Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        startActivity(new Intent(ForgotPassword.this, SignIn.class));
        super.finish();
    }
}
