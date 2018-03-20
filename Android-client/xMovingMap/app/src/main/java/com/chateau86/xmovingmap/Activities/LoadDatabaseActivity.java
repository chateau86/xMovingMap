package com.chateau86.xmovingmap.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chateau86.xmovingmap.R;
import com.chateau86.xmovingmap.model.NavaidDatabaseHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadDatabaseActivity extends AppCompatActivity {
    TextView textbox;
    private static final int READ_REQUEST_CODE = 42;
    InputStream text_in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_database);
        textbox = findViewById(R.id.txt_status);
        Button btn_select_file = findViewById(R.id.btn_select_db_file);
        btn_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("*/*");

                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
        Button btn_load_db = findViewById(R.id.btn_load_data);
        btn_load_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("xMovingMap", "Calling database init");
                //TODO: Actually init database
                if(text_in == null){
                    textbox.setText("No input file selected.");
                    return;
                }
                try {
                    BufferedReader in_txt = new BufferedReader(new InputStreamReader(text_in));
                    NavaidDatabaseHolder.getInstance().initDatabase(in_txt);
                    int count = NavaidDatabaseHolder.getInstance().navaidDAO().getRowCount();
                    textbox.setText("Row count: "+ count);
                } catch (IOException e){
                    textbox.setText("DB read error: "+ e);
                    //do nothing for now
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    Log.i("LoadDatabase", "Uri: " + uri.toString());
                    text_in = getContentResolver().openInputStream(uri);
                } catch(Exception e){
                    text_in = null;
                    Log.e("LoadDatabase", "URI received error: "+e);
                }
            }
        }
    }
}
