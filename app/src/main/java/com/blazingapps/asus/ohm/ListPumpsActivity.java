package com.blazingapps.asus.ohm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPumpsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private List<Pump_List> pump_lists = new ArrayList<>();
    private PumpAdapter pumpAdapter;
    private Context context;
    public String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pumps);
        recyclerView = findViewById(R.id.pump_recycle);
        Bundle bundle = getIntent().getExtras();
        data = bundle.getString("nearby");
        try {
            JSONObject listpumps = new JSONObject(data);
            pumpAdapter = new PumpAdapter(listpumps.getJSONArray("pumps"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pumpAdapter);
        pumpAdapter.notifyDataSetChanged();


    }
}
