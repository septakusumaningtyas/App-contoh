package com.example.quizfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizfirebase.Common.Common;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener{
    final static long INTERVAL = 1000;//1sec
    final static long TIMEOUT = 60000;//7sec
    int progressValue = 0;

    CountDownTimer mCountDown;

    int index = 0,score = 0, thisQuestion=0,totalQuestion,correctAnswer;


    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore,txtQuestionNum,question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        //Views
        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);

        progressBar = findViewById(R.id.progressBar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        if(index < totalQuestion) //still have question in list
        {
            Button clickedButton = (Button)v;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                //Choose correct answer
                score+=10;
                correctAnswer++;
                showQuestion(++index);
            }
            else
            {
//                Choose wrong answer
//                Intent intent = new Intent(this, Done.class);
//                Bundle dataSend = new Bundle();
//                dataSend.putInt("SCORE",score);
//                dataSend.putInt("TOTAL",totalQuestion);
//                dataSend.putInt("CORRECT",correctAnswer);
//                intent.putExtras(dataSend);
//                startActivity(intent);
//                finish();
                score+=0;
                correctAnswer+=0;
                showQuestion(++index);
            }

            txtScore.setText(String.format("%d",score));

        }
    }

    private void showQuestion(int index) {
        if(index<totalQuestion)
        {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;

            if (Common.questionList.get(index).getIsImageQuestion().equals(true))
            {
                //if is image
                Glide.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else
            {
                question_text.setText(Common.questionList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start(); //set timer
        }
        else
        {
            //if it is final question
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
