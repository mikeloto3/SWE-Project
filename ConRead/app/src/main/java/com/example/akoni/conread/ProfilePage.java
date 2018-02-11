package com.example.akoni.conread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Akoni on 12/2/2017.
 */

public class ProfilePage extends Fragment {
    private EditText name, address, contact, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_page, container, false);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Button button = rootView.findViewById(R.id.editButton);;

        name = rootView.findViewById(R.id.profileName);
        address = rootView.findViewById(R.id.profileAddress);
        contact = rootView.findViewById(R.id.profileContact);
        username = rootView.findViewById(R.id.profileUsername);

        reference.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fir = (String) dataSnapshot.child("first_name").getValue();
                String add = (String) dataSnapshot.child("address").getValue();
                String con = (String) dataSnapshot.child("contact").getValue();
                String use = (String) dataSnapshot.child("username").getValue();
                String las = (String) dataSnapshot.child("last_name").getValue();

                String full_name = fir + " " + las;

                name.setText(full_name);
                address.setText(add);
                contact.setText(con);
                username.setText(use);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(ProfilePage.this, "Database Error.", Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfile.class));
            }
        });
        return rootView;
    }
}