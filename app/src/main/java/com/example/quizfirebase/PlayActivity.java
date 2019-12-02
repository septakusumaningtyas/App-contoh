package com.example.quizfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    public void play(View view) {
        Button button = (Button)findViewById(R.id.button_play);

        Intent intent =  new Intent(PlayActivity.this, CategoryPracticeActivity.class);
        startActivity(intent);
    }

    public void grade(View view) {
        Button button = (Button)findViewById(R.id.button_grade);

        Intent intent =  new Intent(PlayActivity.this, RankingActivity.class);
        startActivity(intent);
    }
}
