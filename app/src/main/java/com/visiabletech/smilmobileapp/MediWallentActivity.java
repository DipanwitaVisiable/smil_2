package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class MediWallentActivity extends AppCompatActivity {

    private SliderLayout sliderLayout;
    private LinearLayout lin_doctor,lin_pathology,lin_medical_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_wallent);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medi Wallent");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MediWallentActivity.this,MainActivity.class));
                finish();
            }
        });

        lin_doctor = findViewById(R.id.lin_doctor);
        lin_pathology = findViewById(R.id.lin_pathology);
        lin_medical_store = findViewById(R.id.lin_medical_store);

        sliderLayout = findViewById(R.id.slider_layout);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(5);

        setSliderViews();


        lin_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MediWallentActivity.this,MadicalChamberActivity.class));
                finish();
            }
        });


        lin_pathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MediWallentActivity.this,PathologyActivity.class));
                finish();
            }
        });

        lin_medical_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MediWallentActivity.this,MedicalStoreActivity.class));
                finish();
            }
        });

    }



    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.slider_1);
                    //sliderView.setImageUrl("https://www.guideposts.org/sites/guideposts.org/files/styles/bynder_webimage/public/story/doctorvisit_marquee.jpg");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.slider_2);
                    //sliderView.setImageUrl("https://i2-prod.mirror.co.uk/interactives/article12645227.ece/ALTERNATES/s615/doctor.jpg");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.slider_1);
                    //sliderView.setImageUrl("https://worklogichr.com/sites/default/files/8-Important-Tips-on-How-to-Choose-a-Primary-Care-Doctor-copy.jpg");
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.slider_3);
                    //sliderView.setImageUrl("https://www.pymnts.com/wp-content/uploads/2018/03/doctor.jpg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("Education for all Health" + (i + 1));
            final int finalI = i;

            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(MediWallentActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();

                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(MediWallentActivity.this,MainActivity.class));
        finish();
    }
}
