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

import com.visiabletech.smilmobileapp.Adapter.ExamDetailsAdapter;
import com.visiabletech.smilmobileapp.Pogo.ExamDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class ExamDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_details);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Examination");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExamDetailsActivity.this,ExamActivity.class));
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ExamDetailsAdapter(this,timeTableList()));

    }

    private List<ExamDetailsModel> timeTableList()
    {
        List<ExamDetailsModel> list = new ArrayList<>();

        list.add( new ExamDetailsModel("English","Half-Yearly","11:30 am","08/03/2020"));
        list.add( new ExamDetailsModel("Moral Science","Half-Yearly","11:30 am","09/03/2020"));
        list.add( new ExamDetailsModel("Hindi","Half-Yearly","11:30 am","10/03/2020"));


        return list;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExamDetailsActivity.this,ExamActivity.class));
        finish();
    }
}
