package com.example.quizfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizfirebase.Common.Common;
import com.example.quizfirebase.Interface.ItemClickListener;
import com.example.quizfirebase.Interface.RankingCallBack;
import com.example.quizfirebase.Model.QuestionScore;
import com.example.quizfirebase.Model.Ranking;
import com.example.quizfirebase.ViewHolder.RankingViewHolder;
import com.example.quizfirebase.ViewHolder.ScoreDetail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingActivity extends AppCompatActivity {
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;
    int sum = 0;

    public static RankingActivity newInstance(){
        RankingActivity rankingActivity = new RankingActivity();
        return rankingActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

        //Init View
        rankingList = findViewById(R.id.ranking_list);
        layoutManager = new LinearLayoutManager(this);
        rankingList.setHasFixedSize(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking();
            }
        });

        //Set Adapter
        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(rankingTbl.orderByChild("score"), Ranking.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RankingViewHolder(inflater.inflate(R.layout.layout_ranking, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder rankingViewHolder, int position, @NonNull final Ranking model) {
                rankingViewHolder.txt_name.setText(model.getUserName());
                rankingViewHolder.txt_score.setText(String.valueOf(model.getScore()));

                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(view.getContext(), ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum+= Integer.parseInt(ques.getScore());

                }
                Ranking ranking = new Ranking(userName,sum);
                callback.callBack(ranking);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
