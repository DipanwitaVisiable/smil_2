package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.visiabletech.smilmobileapp.Adapter.PathologyServicesAdapter;
import com.visiabletech.smilmobileapp.Pogo.PathologyServicesModel;

import java.util.ArrayList;

public class PathologyServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<PathologyServicesModel> arrayList = new ArrayList<>();
    String[] text={"ROUTINE BLOOD TEST","CT SCAN","DOPPLER STUDY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_services);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        recyclerView = findViewById(R.id.recycler_view);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pathology Services");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PathologyServicesActivity.this,PathologyActivity.class));
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        for (int i=0; i<text.length; i++)
        {
            PathologyServicesModel chamberModel = new PathologyServicesModel(text[i]);
            arrayList.add(chamberModel);
        }

        recyclerView.setAdapter(new PathologyServicesAdapter(this,arrayList));

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(PathologyServicesActivity.this,PathologyActivity.class));
        finish();
    }
}
