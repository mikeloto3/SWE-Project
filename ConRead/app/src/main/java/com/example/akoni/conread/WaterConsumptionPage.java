package com.example.akoni.conread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Akoni on 12/2/2017.
 */

public class WaterConsumptionPage extends Fragment {
    Spinner month, date, year;
    private ImageView imageView;
    private Button capture, upload;
    static final int CAM_REQUEST = 1;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private Uri photoURI;
    private ArrayAdapter<CharSequence> adapter_month, adapter_date, adapter_year;
    private ProgressDialog progressDialog;
    private String mCurrentPhotoPath;
    private int CAMERA_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.water_consumption_page, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(getContext());

        month = rootView.findViewById(R.id.wtr_month);
        date = rootView.findViewById(R.id.wtr_date);
        year = rootView.findViewById(R.id.wtr_year);

        adapter_month = ArrayAdapter.createFromResource(getContext(), R.array.month, android.R.layout.simple_spinner_item);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapter_month);

        adapter_year = ArrayAdapter.createFromResource(getContext(), R.array.year, android.R.layout.simple_spinner_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter_year);

        adapter_date = ArrayAdapter.createFromResource(getContext(), R.array.daythirtyone, android.R.layout.simple_spinner_item);
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(adapter_date);

        imageView = rootView.findViewById(R.id.water_image);
        capture = rootView.findViewById(R.id.water_upload);
        upload = rootView.findViewById(R.id.water_consumption);

        upload.setEnabled(false);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cam_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cam_intent.resolveActivity(getActivity().getPackageManager()) != null){
                    File photoFile = null;

                    try{
                        photoFile = createImageFile();
                    }
                    catch (IOException ex){
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if(photoFile != null){
                        photoURI = Uri.fromFile(photoFile);
                        cam_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        cam_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(cam_intent, CAMERA_REQUEST);
                    }


                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Uploading Photo.");
                progressDialog.show();
                StorageReference filepath = mStorageRef.child("Photos").child(user.getUid()).child("Water").child(photoURI.getLastPathSegment());
                filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        Toast.makeText(getContext(), "Upload Finished.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), ElectricConsumption.class);
                        startActivity(i);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Upload has stopped.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAM_REQUEST && resultCode == RESULT_OK){
            upload.setEnabled(true);

            addGalleryPic();
            setPic();
        }
    }

    private File createImageFile() throws IOException{
        //Image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void addGalleryPic(){
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScan.setData(contentUri);
        getContext().sendBroadcast(mediaScan);
    }

    private void setPic(){
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        //Get Dimensions of Bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

}