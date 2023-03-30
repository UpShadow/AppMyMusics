package br.unoeste.appmymusics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageview;
    private Runnable r;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acitivty);
        imageview=findViewById(R.id.imageView);
        handler = new Handler();
        handler.postDelayed(r=()->{entrar();},3000);
        imageview.setOnClickListener(e->{entrar();});

    }
    private void entrar()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        handler.removeCallbacks(r);
    }
}