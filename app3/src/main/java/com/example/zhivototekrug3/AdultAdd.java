package com.example.zhivototekrug3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdultAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText editTypeActivity, editDescActivity, editTime, editRepetitive, editUrgency, editLocation, editDate;
    private Button addButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;
    List<Address> listGeoCoder;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_add);

        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton = findViewById(R.id.addActivity);
        addButton.setOnClickListener(this);

        editDate = findViewById(R.id.date);
        editTypeActivity = findViewById(R.id.typeActivity);
        editDescActivity = findViewById(R.id.descActivity);
        editTime = findViewById(R.id.time);
        editRepetitive = findViewById(R.id.repetitive);
        editUrgency = findViewById(R.id.urgency);
        editLocation = findViewById(R.id.location);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdultAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        editDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

//        if(ContextCompat.checkSelfPermission(AdultAdd.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(AdultAdd.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    100);
//        }

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            // There are no request codes
                            Intent data = result.getData();
                            latitude = data.getDoubleExtra("lat", 0);
                            longitude = data.getDoubleExtra("lon", 0);
                            try {
                                listGeoCoder = new Geocoder(getApplicationContext()).getFromLocation(latitude,longitude,1);
                                editLocation.setText(listGeoCoder.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (result.getResultCode() == 0) {
                            Toast.makeText(AdultAdd.this, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), SettingLocation.class);
                //startActivityForResult(intent,1);
                someActivityResultLauncher.launch(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                latitude = data.getDoubleExtra("lat", 0);
                longitude = data.getDoubleExtra("lon", 0);
                try {
                    listGeoCoder = new Geocoder(this).getFromLocation(latitude,longitude,1);
                    editLocation.setText(listGeoCoder.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == 0) {
                Toast.makeText(AdultAdd.this, "Error !", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addActivity){
            addActivity();
            startActivity(new Intent(this, Adult.class));
        }

    }

    private void addActivity() {
        String type = editTypeActivity.getText().toString().trim();
        String desc = editDescActivity.getText().toString().trim();
        String time = editTime.getText().toString().trim();
        String rep = editRepetitive.getText().toString().trim();
        String urg = editUrgency.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String loc = editLocation.getText().toString().trim();

        if (type.isEmpty()) {
            editTypeActivity.setError("Type of the Activity is required !");
            editTypeActivity.requestFocus();
            return;
        }

        if (desc.isEmpty()) {
            editDescActivity.setError("Description of the Activity is required !");
            editDescActivity.requestFocus();
            return;
        }

        if (time.isEmpty()) {
            editTime.setError("Time for the Activity is required !");
            editTime.requestFocus();
            return;
        }

        if (rep.isEmpty()) {
            editRepetitive.setError("Repetition information for the Activity is required !");
            editRepetitive.requestFocus();
            return;
        }

        if(!rep.equals("repetitive")&&!rep.equals("one-time"))
        {
            editUrgency.setError("Valid Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if (urg.isEmpty()) {
            editUrgency.setError("Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if(!urg.equals("yes")&&!urg.equals("no"))
        {
            editUrgency.setError("Valid Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if(date.isEmpty())
        {
            editDate.setError("Date for the Activity is required !");
            editDate.requestFocus();
            return;
        }

        if (loc.isEmpty()) {
            editLocation.setError("Location for the Activity is required !");
            editLocation.requestFocus();
            return;
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        DatabaseReference activitiesRef = FirebaseDatabase.getInstance().getReference("Activities");
        mDatabase.child("Users").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User currentUser = task.getResult().getValue(User.class);
                Log.d("tag1", currentUser.fullName);

                if(currentUser == null){
                    return;
                }

                Aktivnost activity = new Aktivnost(type,desc,time,rep,urg,loc, userID, date);
                activity.setOwnName(currentUser.getFullName());
                activity.setRatingEld(currentUser.getRating());
                activity.setOwnTel(currentUser.getNumber());
                activity.setOwnMail(currentUser.getEmail());


                activitiesRef.child(activity.activityID).setValue(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AdultAdd.this, "Activity has been added successfully!", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(AdultAdd.this, "Error! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", e.toString());
                            Toast.makeText(AdultAdd.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            }
        });

    }



}