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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Adapter3 extends FirebaseRecyclerAdapter<Aktivnost,Adapter3.myViewHolder> {

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
    public Adapter3(@NonNull FirebaseRecyclerOptions<Aktivnost> options) {
        super(options);
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onBindViewHolder(@NonNull Adapter3.myViewHolder holder, int position, @NonNull Aktivnost model) {
        holder.type.setText(model.getTypeA());
        holder.desc.setText(model.getDescA());
        holder.loc.setText(model.getLoc());
        holder.time.setText(model.getTime());
        holder.rep.setText(model.getRep());
        holder.urg.setText(model.getUrg());
        holder.own.setText(model.getOwnName());
        holder.date.setText(model.getDate());
        holder.rating.setText(model.getRatingEld());

        location = holder.loc.getText().toString();
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("location", location);
                view.getContext().startActivity(intent);
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
    public Adapter3.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity3,parent,false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(parent.getContext());
        context = parent.getContext();
        return new Adapter3.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView type,desc,loc,dis, time,rep,urg,own, date, rating;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.type);
            desc = (TextView) itemView.findViewById(R.id.desc);
            loc = (TextView) itemView.findViewById(R.id.loc);
            dis = (TextView) itemView.findViewById(R.id.dis);
            time = (TextView) itemView.findViewById(R.id.time);
            rep = (TextView) itemView.findViewById(R.id.rep);
            urg = (TextView) itemView.findViewById(R.id.urg);
            own = (TextView) itemView.findViewById(R.id.own);
            date = (TextView)itemView.findViewById(R.id.date);
            rating = (TextView) itemView.findViewById(R.id.rating);


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

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
