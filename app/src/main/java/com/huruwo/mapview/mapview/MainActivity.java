package com.huruwo.mapview.mapview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapFrameLayout mapfra;
    private MapView mapview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapfra = (MapFrameLayout) findViewById(R.id.mapfra);
        mapview = (MapView) findViewById(R.id.mapview);


        mapview.setOnZteClickListener(new MapView.OnPathClickListener() {
            @Override
            public void onPathClick(PathItem p) {
                Toast.makeText(getBaseContext(),p.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });


        mapview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mapview.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mapview.loadMap(R.raw.jx_maps,"江西地图");
                mapview.setScacle(mapview.getMinScale());


            }
        });

        mapview.setOnPathDrawListener(new MapView.OnPathDrawListener() {
            @Override
            public void onPathDrawFinish(List<float[]> pos) {
                mapfra.setPoints(pos);
            }
        });
    }
}
