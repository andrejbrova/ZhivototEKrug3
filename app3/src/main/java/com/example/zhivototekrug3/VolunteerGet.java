package com.example.zhivototekrug3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerGet extends AppCompatActivity implements Adapter3.OnItemClickListener{

    RecyclerView recyclerView;
    Adapter3 adapter3;

    String email,number, name, volID, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_get);


        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user != null)
                        {
                            email = user.getEmail();
                            number = user.getNumber();
                            name = user.getFullName();
                            volID = FirebaseAuth.getInstance().getUid();
                            rating = user.getRating();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        recyclerView = (RecyclerView) findViewById(R.id.activitiesVolunteerGet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Aktivnost> options =
                new FirebaseRecyclerOptions.Builder<Aktivnost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Activities").orderByChild("volName").equalTo("/"), Aktivnost.class)
                        .build();

        adapter3 = new Adapter3(options);
        recyclerView.setAdapter(adapter3);

        adapter3.setOnItemClickListener(new Adapter3.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                Aktivnost activity = documentSnapshot.getValue(Aktivnost.class);
                activity.setVolName(name);
                activity.setVolmail(email);
                activity.setVolTel(number);
                activity.setVol(volID);
                activity.setTmpVol(volID);
                activity.setRatingVol(rating);
                activity.setState("waiting");
                FirebaseDatabase.getInstance().getReference().child("Activities").child(documentSnapshot.getKey()).setValue(activity);
            }
        });
    }

    @Override
    public void onItemClick(DataSnapshot documentSnapshot, int position) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter3.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter3.stopListening();
    }
}