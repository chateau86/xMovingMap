package com.chateau86.xmovingmap.Activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chateau86.xmovingmap.Activities.search.SearchActivity;
import com.chateau86.xmovingmap.R;
import com.chateau86.xmovingmap.model.NavaidDatabase;
import com.chateau86.xmovingmap.model.NavaidDatabaseHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashScreenActivity extends AppCompatActivity {

    TextView textbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try{
            NavaidDatabaseHolder.getInstance();
        } catch (IllegalStateException i){
            //Now init db
            NavaidDatabaseHolder.setInstance(Room.databaseBuilder(getApplicationContext(),
                    NavaidDatabase.class, "navaids").allowMainThreadQueries().build()); //TODO: Split to another thread
        }
        textbox = findViewById(R.id.txt_splash);
        Button loadBtn = findViewById(R.id.btn_load_data);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("xMovingMap", "Calling database init");
                //TODO: Actually init database
                BufferedReader in_txt = new BufferedReader(new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.earth_nav)));
                try {
                    NavaidDatabaseHolder.getInstance().initDatabase(in_txt);
                    int count = NavaidDatabaseHolder.getInstance().navaidDAO().getRowCount();
                    textbox.setText("Row count: "+ count);
                } catch (IOException e){
                    textbox.setText("DB read error: "+ e);
                    //do nothing for now
                }
            }
        });

        Button searchBtn = findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        int count = NavaidDatabaseHolder.getInstance().navaidDAO().getRowCount();
        textbox.setText("Row count: "+ count);
    }

}
