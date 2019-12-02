package com.example.quizfirebase.ViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizfirebase.Model.QuestionScore;
import com.example.quizfirebase.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetail extends AppCompatActivity {
    String viewUser="";

    FirebaseDatabase database;
    DatabaseReference question_score;
    private static final  String TAG = "ScoreDetail";

    private RecyclerView scoreList;

    FirebaseRecyclerAdapter<QuestionScore,ScoreDetailViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        scoreList = (RecyclerView)findViewById(R.id.score_list);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");


        scoreList.hasFixedSize();
        scoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if(!viewUser.isEmpty())
            loadScoreDetail(viewUser);
    }

    public void loadScoreDetail(String viewUser) {
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<QuestionScore>()
                .setQuery(question_score.orderByChild("user").equalTo(viewUser), QuestionScore.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(personsOptions) {
            @NonNull
            @Override
            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_detail_layout, parent, false);
                return new ScoreDetailViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder viewHolder, int i, @NonNull QuestionScore questionScore) {
                viewHolder.txt_name.setText(questionScore.getCategoryName());
                viewHolder.txt_score.setText(questionScore.getScore());
            }

        };

        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
