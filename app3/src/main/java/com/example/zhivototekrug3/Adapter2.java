package com.example.zhivototekrug3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Adapter2 extends FirebaseRecyclerAdapter<Aktivnost,Adapter2.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Adapter2.OnItemClickListener listener;
    private FusedLocationProviderClient fusedLocationClient;
    List<Address> geocoder;
    String location;
    private Context context;
    public Adapter2(@NonNull FirebaseRecyclerOptions<Aktivnost> options) {
        super(options);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Aktivnost model) {
        holder.type.setText(model.getTypeA());
        holder.desc.setText(model.getDescA());
        holder.loc.setText(model.getLoc());
        holder.time.setText(model.getTime());
        holder.act.setText(model.getState());
        holder.rep.setText(model.getRep());
        holder.urg.setText(model.getUrg());
        holder.vol.setText(model.getVolName());
        holder.tel.setText(model.getVolTel());
        holder.mail.setText(model.getVolmail());
        holder.rating.setText(model.getRatingVol());
        holder.date.setText(model.getDate());
        holder.reviews.setText(model.getReportVol());

        if(holder.reviews.getText().equals("/"))
        {
            holder.reviews.setVisibility(View.GONE);
            holder.reviewws.setVisibility(View.GONE);
        }

        if(!holder.reviews.getText().equals("/"))
        {
            holder.reviews.setVisibility(View.VISIBLE);
            holder.reviewws.setVisibility(View.VISIBLE);
        }

        if(holder.mail.getText().equals("/"))
        {
            holder.mail.setVisibility(View.GONE);
            holder.email.setVisibility(View.GONE);
        }

        if(holder.tel.getText().equals("/"))
        {
            holder.tel.setVisibility(View.GONE);
            holder.phonenumber.setVisibility(View.GONE);
        }

        if(holder.vol.getText().equals("/")){
            holder.vol.setVisibility(View.GONE);
            holder.volunteer.setVisibility(View.GONE);
            holder.ratingg.setVisibility(View.GONE);
            holder.rating.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
        }

        if(!holder.mail.getText().equals("/"))
        {
            holder.mail.setVisibility(View.VISIBLE);
            holder.email.setVisibility(View.VISIBLE);
        }

        if(!holder.tel.getText().equals("/"))
        {
            holder.tel.setVisibility(View.VISIBLE);
            holder.phonenumber.setVisibility(View.VISIBLE);
        }

        if(!holder.vol.getText().equals("/")){
            holder.vol.setVisibility(View.VISIBLE);
            holder.volunteer.setVisibility(View.VISIBLE);
            holder.ratingg.setVisibility(View.VISIBLE);
            holder.rating.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.VISIBLE);
            holder.accept.setVisibility(View.VISIBLE);
        }

        if(holder.act.getText().equals("finished"))
        {
            holder.review.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
        }

        if(!holder.act.getText().equals("finished"))
        {
            holder.review.setVisibility(View.GONE);
        }

        if(holder.act.getText().equals("scheduled"))
        {
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
        }

        if(!holder.act.getText().equals("scheduled") && !holder.act.getText().equals("finished") && !holder.act.getText().equals("active"))
        {
            holder.accept.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.VISIBLE);
        }

        location = holder.loc.getText().toString();
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("location",location);
                view.getContext().startActivity(intent);
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("vol", "/");
                map1.put("volName", "/");
                map1.put("volTel", "/");
                map1.put("volmail", "/");
                map1.put("state", "active");
                FirebaseDatabase.getInstance().getReference().child("Activities")
                        .child(getRef(position).getKey()).updateChildren(map1);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("state", "scheduled");
                FirebaseDatabase.getInstance().getReference().child("Activities")
                        .child(getRef(position).getKey()).updateChildren(map1);
            }
        });

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.type.getContext()).setContentHolder(new ViewHolder(R.layout.dialog2))
                        .setExpanded(true, 1100).create();
                View tmpView = dialogPlus.getHolderView();
                EditText reviews = tmpView.findViewById(R.id.review);
                EditText ranks = tmpView.findViewById(R.id.rankV);
                Button submits = tmpView.findViewById(R.id.submit);

                dialogPlus.show();

                submits.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String reportEld = reviews.getText().toString().trim();
                        String rating = ranks.getText().toString().trim();
                        if(reportEld.isEmpty()){
                            reviews.setError("Review is requered");
                            reviews.requestFocus();
                            return;
                        }
                        if(rating.isEmpty())
                        {
                            ranks.setError("Rating is requered");
                            ranks.requestFocus();
                            return;
                        }
                        Map<String,Object> map1 = new HashMap<>();
                        Map<String,Object> map2 = new HashMap<>();
                        map1.put("reportEld", reportEld);
                        map1.put("ratingVol", rating);
                        map2.put("rating", rating);
                        FirebaseDatabase.getInstance().getReference().child("Activities")
                                .child(getRef(position).getKey()).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(model.getTmpVol()).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        try {
            geocoder = new Geocoder(context).getFromLocationName(location,1);
            double latitude = geocoder.get(0).getLatitude();
            double longtitude = geocoder.get(0).getLongitude();
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {

                    Location startPoint = new Location("locationA");
                    startPoint.setLongitude(location.getLongitude());
                    startPoint.setLatitude(location.getLatitude());

                    Location endPoint = new Location("locationB");
                    endPoint.setLongitude(longtitude);
                    endPoint.setLatitude(latitude);

                    double distance = startPoint.distanceTo(endPoint) / 1000;
                    BigDecimal bg = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
                    holder.dis.setText(bg + "km");

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity2,parent,false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(parent.getContext());
        context = parent.getContext();
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView type,desc,loc,dis,time,rep,urg,act,vol,tel, mail, volunteer, phonenumber, email, ratingg, rating, date;
        TextView reviewws, reviews;
        Button decline, accept, review;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.type);
            desc = (TextView) itemView.findViewById(R.id.desc);
            loc = (TextView) itemView.findViewById(R.id.loc);
            time = (TextView) itemView.findViewById(R.id.time);
            rep = (TextView) itemView.findViewById(R.id.rep);
            urg = (TextView) itemView.findViewById(R.id.urg);
            act = (TextView) itemView.findViewById(R.id.act);
            vol = (TextView) itemView.findViewById(R.id.vol);
            tel = (TextView) itemView.findViewById(R.id.phone);
            mail = (TextView) itemView.findViewById(R.id.mail);
            volunteer = (TextView) itemView.findViewById(R.id.volunteer);
            phonenumber = (TextView) itemView.findViewById(R.id.phonenumber);
            email = (TextView) itemView.findViewById(R.id.email);
            ratingg = (TextView) itemView.findViewById(R.id.ratingg);
            rating = (TextView) itemView.findViewById(R.id.rating);
            date = (TextView) itemView.findViewById(R.id.date);
            reviews = (TextView) itemView.findViewById(R.id.review);
            reviewws = (TextView)itemView.findViewById(R.id.revieww);
            dis = (TextView) itemView.findViewById(R.id.dis);

            decline = (Button) itemView.findViewById(R.id.declineVolunteer);
            review = (Button) itemView.findViewById(R.id.AddReview);
            accept = (Button) itemView.findViewById(R.id.acceptVolunteer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DataSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(Adapter2.OnItemClickListener listener)
    {
        this.listener = listener;
    }

}