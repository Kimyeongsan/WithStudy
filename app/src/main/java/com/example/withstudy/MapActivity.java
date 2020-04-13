package com.example.withstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* 디버깅 키 해시 뽑기
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.withstudy", PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                Log.d("keyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        */

        createMapView();
    }

    // MapView 객체를 생성하고 View 띄우기
    private void createMapView() {
        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        mapViewContainer.addView(mapView);
    }
}
