package com.example.smartglove;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SairSystem extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                finishAffinity();
//            }
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Por favor clique de novo para sair", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }
}
