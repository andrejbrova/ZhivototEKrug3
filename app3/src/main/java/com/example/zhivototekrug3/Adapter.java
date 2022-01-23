package com.example.zhivototekrug3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

public class Adapter extends FirebaseRecyclerAdapter<Aktivnost, Adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;
    private FusedLocationProviderClient fusedLocationClient;
    List<Address> geocoder;
    String location;
    private Context context;

    public Adapter(@NonNull FirebaseRecyclerOptions<Aktivnost> options) {
        super(options);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Aktivnost model) {
        holder.type.setText(model.getTypeA());
        holder.desc.setText(model.getDescA());
        holder.loc.setText(model.getLoc());
        holder.time.setText(model.getTime());
        holder.rep.setText(model.getRep());
        holder.urg.setText(model.getUrg());
        holder.own.setText(model.getOwnName());
        holder.rev.setText(model.getReportEld());
        holder.date.setText(model.getDate());
        holder.act.setText(model.getState());
        holder.pho.setText(model.getOwnTel());
        holder.mail.setText(model.getOwnMail());
        holder.rat.setText(model.getRatingEld());

        if (holder.rev.getText().equals("/")) {
            holder.rev.setVisibility(View.GONE);
            holder.revv.setVisibility(View.GONE);
        }

        if (!holder.rev.getText().equals("/")) {
            holder.rev.setVisibility(View.VISIBLE);
            holder.revv.setVisibility(View.VISIBLE);
        }

        if (holder.act.getText().equals("waiting")) {
            holder.addReview.setVisibility(View.GONE);
            holder.finishActivity.setVisibility(View.GONE);
            holder.mail.setVisibility(View.GONE);
            holder.pho.setVisibility(View.GONE);
            holder.email.setVisibility(View.GONE);
            holder.phonenumber.setVisibility(View.GONE);
        }

        if (holder.act.getText().equals("scheduled")) {
            holder.finishActivity.setVisibility(View.VISIBLE);
            holder.addReview.setVisibility(View.GONE);
            holder.mail.setVisibility(View.VISIBLE);
            holder.pho.setVisibility(View.VISIBLE);
            holder.email.setVisibility(View.VISIBLE);
            holder.phonenumber.setVisibility(View.VISIBLE);
        }

        if (holder.act.getText().equals("finished")) {
            holder.finishActivity.setVisibility(View.GONE);
            holder.addReview.setVisibility(View.VISIBLE);
        }

        location = holder.loc.getText().toString();
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("location", location);
                view.getContext().startActivity(intent);
            }
        });

        holder.finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("state", "finished");
                FirebaseDatabase.getInstance().getReference().child("Activities")
                        .child(getRef(position).getKey()).updateChildren(map1);
            }
        });


        holder.addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.type.getContext()).setContentHolder(new ViewHolder(R.layout.dialog))
                        .setExpanded(true, 1100).create();

                View tmpView = dialogPlus.getHolderView();
                EditText reviews = tmpView.findViewById(R.id.review);
                EditText ranks = tmpView.findViewById(R.id.rankA);
                Button submits = tmpView.findViewById(R.id.submit);

                dialogPlus.show();

                submits.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String reportVol = reviews.getText().toString().trim();
                        String rating = ranks.getText().toString().trim();
                        if (reportVol.isEmpty()) {
                            reviews.setError("Review is requered");
                            reviews.requestFocus();
                            return;
                        }
                        if (rating.isEmpty()) {
                            ranks.setError("Rating is requered");
                            ranks.requestFocus();
                            return;
                        }
                        Map<String, Object> map1 = new HashMap<>();
                        Map<String, Object> map2 = new HashMap<>();
                        map1.put("reportVol", reportVol);
                        map1.put("ratingEld", rating);
                        map2.put("rating", rating);
                        FirebaseDatabase.getInstance().getReference().child("Activities")
                                .child(getRef(position).getKey()).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(model.getOwn()).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            geocoder = new Geocoder(context).getFromLocationName(location, 1);
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
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }







    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_,parent,false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(parent.getContext());
        context = parent.getContext();
        return new Adapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView type,desc,loc,dis,time,rep,urg,own, rev, revv, date, act, pho, mail, rat;
        TextView phonenumber, email;
        Button addReview, finishActivity;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.type);
            desc = (TextView) itemView.findViewById(R.id.desc);
            loc = (TextView) itemView.findViewById(R.id.loc);
            time = (TextView) itemView.findViewById(R.id.time);
            rep = (TextView) itemView.findViewById(R.id.rep);
            urg = (TextView) itemView.findViewById(R.id.urg);
            own = (TextView) itemView.findViewById(R.id.own);
            rev = (TextView) itemView.findViewById(R.id.rev);
            revv = (TextView) itemView.findViewById(R.id.revv);
            date = (TextView) itemView.findViewById(R.id.date);
            act = (TextView) itemView.findViewById(R.id.act);
            mail = (TextView) itemView.findViewById(R.id.mail);
            pho = (TextView)itemView.findViewById(R.id.phone);
            rat = (TextView) itemView.findViewById(R.id.rating);
            phonenumber = (TextView) itemView.findViewById(R.id.phonenumber);
            email = (TextView) itemView.findViewById(R.id.email);
            dis = (TextView) itemView.findViewById(R.id.dis);

            finishActivity = (Button) itemView.findViewById(R.id.finishActivity);
            addReview = (Button) itemView.findViewById(R.id.addReview);

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

    public void setOnItemClickListener(Adapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
