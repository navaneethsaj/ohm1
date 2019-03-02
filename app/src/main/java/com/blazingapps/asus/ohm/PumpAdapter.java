package com.blazingapps.asus.ohm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class PumpAdapter extends RecyclerView.Adapter<PumpAdapter.MyViewHolder> {

//    private List<Pump_List> pump_lists;
    private Context context;
    JSONArray pump_lists;

    public PumpAdapter(JSONArray pump_lists){
        this.pump_lists=pump_lists;
        //this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        TextView slowwait;
        TextView fastwait;
        TextView slowrate;
        TextView fastrate;
        RatingBar ratingBar;
        TextView distance;
        public MyViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.pump_name);
            slowrate = view.findViewById(R.id.slowrate);
            fastrate = view.findViewById(R.id.fastrate);
            slowwait = view.findViewById(R.id.slowwait);
            fastwait = view.findViewById(R.id.fastwait);
            distance = view.findViewById(R.id.distance);
            ratingBar = view.findViewById(R.id.ratings);
        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pump_card, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        try {

            holder.name.setText(pump_lists.getJSONObject(position).getString("name"));
            holder.slowrate.setText(pump_lists.getJSONObject(position).getString("slow_rate"));
            holder.fastrate.setText(pump_lists.getJSONObject(position).getString("fast_rate"));
            holder.slowwait.setText(pump_lists.getJSONObject(position).getString("slow_wait"));
            holder.fastwait.setText(pump_lists.getJSONObject(position).getString("fast_wait"));
            holder.distance.setText(pump_lists.getJSONObject(position).getString("distance"));
            holder.ratingBar.setRating(5);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return pump_lists.length();
    }


}
