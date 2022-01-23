package com.example.zhivototekrug3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Volunteer extends AppCompatActivity implements View.OnClickListener{

    private Button getNewActivity, viewActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        getNewActivity = (Button) findViewById(R.id.getActivity);
        getNewActivity.setOnClickListener(this);

        viewActivities = (Button) findViewById(R.id.viewGottenActivity);
        viewActivities.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getActivity:
                startActivity(new Intent(this, VolunteerGet.class));
                break;
            case R.id.viewGottenActivity:
                startActivity(new Intent(this,VolunteerView.class));
                break;
        }

    }


    public void logout(android.view.View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}