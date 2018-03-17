package com.chateau86.xmovingmap.Activities.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chateau86.xmovingmap.R;
import com.chateau86.xmovingmap.model.Navaid;
import com.chateau86.xmovingmap.model.NavaidDatabaseHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mNavaidRView;
    private NavaidAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mNavaidRView = (RecyclerView) findViewById(R.id.NavaidListing);
        mLayoutManager = new LinearLayoutManager(this);
        mNavaidRView.setLayoutManager(mLayoutManager);
        mAdapter = new NavaidAdapter(new ArrayList<Navaid>());
        mNavaidRView.setAdapter(mAdapter);

        Button search_btn = findViewById(R.id.btn_search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText lat_box = findViewById(R.id.search_lat);
                EditText lon_box = findViewById(R.id.search_lon);
                double lat = Double.parseDouble(lat_box.getText().toString());
                double lon = Double.parseDouble(lon_box.getText().toString());
                List<Navaid> navaids = NavaidDatabaseHolder.getInstance().navaidDAO()
                                            .getAllNearby_sorted(
                                                    lat,
                                                    lon,
                                                    Math.sin(Math.toRadians(lat)),
                                                    Math.cos(Math.toRadians(lat)),
                                                    Math.sin(Math.toRadians(lon)),
                                                    Math.cos(Math.toRadians(lon))
                                            );
                mAdapter.updateData(navaids, lat, lon);
            }
        });
    }
}


