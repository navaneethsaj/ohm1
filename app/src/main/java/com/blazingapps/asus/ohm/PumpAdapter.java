package com.blazingapps.asus.ohm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PumpAdapter extends RecyclerView.Adapter<PumpAdapter.MyViewHolder> {

    private List<Pump_List> pump_lists;
    private Context context;


    public PumpAdapter(List<Pump_List> pump_lists){
        this.pump_lists=pump_lists;
        //this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public MyViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.pump_name);

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
        Pump_List pump_list = pump_lists.get(position);
        holder.name.setText(pump_list.getName());
    }

    @Override
    public int getItemCount() {
        return pump_lists.size();
    }


}
