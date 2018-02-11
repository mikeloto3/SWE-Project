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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    private EditText name, username, address, contact, oldPass, newPass, repass;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private Button save, cancel;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        save = findViewById(R.id.saveButton);
        cancel = findViewById(R.id.cancelButton);
        progress = new ProgressDialog(this);

        name = findViewById(R.id.editName);
        username = findViewById(R.id.editUsername);
        address = findViewById(R.id.editAddress);
        contact = findViewById(R.id.editContact);
        oldPass = findViewById(R.id.editOldPass);
        newPass = findViewById(R.id.editNewPass);
        repass = findViewById(R.id.editRePass);

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        ref.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String first = (String)dataSnapshot.child("first_name").getValue();
                String last = (String)dataSnapshot.child("last_name").getValue();
                String nam = first + " " + last;

                name.setText(nam);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void editProfile(){
        String user = username.getText().toString().trim();
        String add = address.getText().toString().trim();
        String cont = contact.getText().toString().trim();
        String pass = newPass.getText().toString().trim();
        String renew = repass.getText().toString().trim();
        String old = oldPass.getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(add)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(cont)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(renew)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(old)){
            Toast.makeText(EditProfile.this, "Please fill up the boxes.", Toast.LENGTH_LONG).show();
            return;
        }

        if(pass.equals(renew)){
            changePassword(pass);

            ref.child("users").child(auth.getCurrentUser().getUid()).child("username").setValue(user);
            ref.child("users").child(auth.getCurrentUser().getUid()).child("address").setValue(add);
            ref.child("users").child(auth.getCurrentUser().getUid()).child("contact").setValue(cont);

            startActivity(new Intent(EditProfile.this, TabMenu.class));
        }
        else {
            Toast.makeText(EditProfile.this, "Password doesn't match.", Toast.LENGTH_LONG).show();
        }
    }

    private void changePassword(String newpassword){
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            progress.setMessage("Changing Profile...");
            progress.show();
            user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Changes had been saved.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(EditProfile.this, "Password not change.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(EditProfile.this, "User is invalid.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        Intent i = new Intent(EditProfile.this, TabMenu.class);
        startActivity(i);
        super.finish();
    }
}
