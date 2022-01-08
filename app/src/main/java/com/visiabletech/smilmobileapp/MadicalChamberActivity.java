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

import com.visiabletech.smilmobileapp.Adapter.MedicalChamberAdapter;
import com.visiabletech.smilmobileapp.Pogo.MedicalChamberModel;

import java.util.ArrayList;

public class MadicalChamberActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<MedicalChamberModel> arrayList = new ArrayList<>();
    String[] text={"Dr. Saroj Khowala","Dr. Pawan Agarwal","Dr. Prasenjit Ganguly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madical_chamber);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Madical Chamber");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MadicalChamberActivity.this,MediWallentActivity.class));
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        for (int i=0; i<text.length; i++)
        {
            MedicalChamberModel chamberModel = new MedicalChamberModel(text[i]);
            arrayList.add(chamberModel);
        }

        recyclerView.setAdapter(new MedicalChamberAdapter(this,arrayList));

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(MadicalChamberActivity.this,MediWallentActivity.class));
        finish();
    }
}
