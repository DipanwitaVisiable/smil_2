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

import com.visiabletech.smilmobileapp.Adapter.HealthCardAdapter;
import com.visiabletech.smilmobileapp.Pogo.HealthCardModel;

import java.util.ArrayList;

public class HealthCardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<HealthCardModel> arrayList = new ArrayList<>();
    String[] text={"Name : Rakib Uddin","Name : Kazi Sani","Name : Rakib Uddin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_card);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Health Card");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HealthCardActivity.this,MainActivity.class));
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        for (int i=0; i<text.length; i++)
        {
            HealthCardModel chamberModel = new HealthCardModel(text[i]);
            arrayList.add(chamberModel);
        }

        recyclerView.setAdapter(new HealthCardAdapter(this,arrayList));
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(HealthCardActivity.this,MainActivity.class));
        finish();
    }
}
