package com.app.groundfloor24.clinicsmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.groundfloor24.clinicsmap.model.Clinics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FilterClinicsAdapter extends RecyclerView.Adapter<FilterClinicsAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    List<Clinics> stationList2;
    List<Clinics> stationList2Full;
    Context context;
    double lat,lng;

    // data is passed into the constructor
    FilterClinicsAdapter(Context context, List<Clinics> data, double lat, double lng) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        stationList2 = data;
        stationList2Full = new ArrayList<>(data);
        this.lat = lat;
        this.lng = lng;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.station_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = stationList2.get(position).getName();

       // double distance = distance(Double.parseDouble(stationList2.get(position).getLat()),Double.parseDouble(stationList2.get(position).getLng()),lat,lng );
        //holder.tvPrice.setText(String.format("%.2f", distance)+" miles away");
        //holder.tvPrice2.setText(stationList2.get(position).getDistance()+" miles away");

        // add images related to stations names

        final int min = 1;
        final int max = 3;
        final int random = new Random().nextInt((max - min) + 1) + min;

        if(random==1){
            holder.img.setImageResource(R.drawable.glen);

        }
        if(random==2){
            holder.img.setImageResource(R.drawable.bri);

        }
        if(random==3){
            holder.img.setImageResource(R.drawable.qeen);

        }
        //adding name
        holder.tvName.setText(name);
        //holder.tvPrice2.setVisibility(View.GONE);
       // holder.tvPrice2.setText("1.78 £");

//        holder.tvName.setText(animal);
//        holder.tvName.setText(animal);


        //on clicking layout
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("price",name);
                intent.putExtra("lat",stationList2.get(position).getLat());
                intent.putExtra("lng",stationList2.get(position).getLng());

                intent.putExtra("random",random);
                context.startActivity(intent);

            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return stationList2.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Clinics> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(stationList2Full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Clinics item : stationList2Full) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stationList2.clear();
            stationList2.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



        // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName,tvPrice;
        ImageView img;
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvDes);
            img = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



}