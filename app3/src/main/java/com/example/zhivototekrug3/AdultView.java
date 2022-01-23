package com.example.zhivototekrug3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class AdultView extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_view);

        recyclerView = (RecyclerView) findViewById(R.id.activitiesAdult);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Aktivnost> options =
                new FirebaseRecyclerOptions.Builder<Aktivnost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Activities").orderByChild("own").equalTo(FirebaseAuth.getInstance().getUid()), Aktivnost.class)
                        .build();


        adapter2 = new Adapter2(options);
        recyclerView.setAdapter(adapter2);

        adapter2.setOnItemClickListener(new Adapter2.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                FirebaseDatabase.getInstance().getReference().child("Activities").child(documentSnapshot.getKey()).removeValue();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter2.stopListening();
    }


}