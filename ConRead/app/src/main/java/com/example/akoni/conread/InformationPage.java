package com.example.akoni.conread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Akoni on 12/2/2017.
 */

public class InformationPage extends Fragment {
    private ListView electricInfo, waterInfo;
    private DatabaseReference db;
    private FirebaseUser user;
    private String[] device;
    private String[] waterData;
    private long Etotal, Wtotal, ref, aircon, phone, comp, tv, others;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information_page, container, false);

        electricInfo = rootView.findViewById(R.id.electricInformation);
        waterInfo = rootView.findViewById(R.id.waterInformation);
        db = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        device = new String[]{
                "Equipment Standard Info:",
                "Equipment    Wattage  Hrs/Day  Est. Use/Month",
                "Refrigerator   300         14          126.00  ",
                "Aircondition   1913        12          688.68  ",
                "Electric Fan   80          8           19.20",
                "Flourecent     54          4           6.24",
                "Computer       255         8           54.00",
                "Television     180         5           27.00",
                "Flat Iron      750         3           67.5 ",
                "Consumption:",
                "Total    ",
                "Last Computation"};

        waterData = new String[]{"Total          ", "Last Computation"};

        db.child("users").child(user.getUid()).child("Water").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String WaterDate = (String)dataSnapshot.child("date").getValue();
                Wtotal = (long)dataSnapshot.child("consumption").getValue();

                waterData[0] += "       =   " + Wtotal;
                waterData[1] += "     =   " + WaterDate;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
                adapter.addAll(waterData);
                waterInfo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.child("users").child(user.getUid()).child("Electricity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ElectricDate = (String)dataSnapshot.child("date").getValue();
                Etotal = (long)dataSnapshot.child("consumption").getValue();

                device[10] += "      =   " + Etotal;
                device[11] += "      =   " + ElectricDate;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                adapter.addAll(device);
                electricInfo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
