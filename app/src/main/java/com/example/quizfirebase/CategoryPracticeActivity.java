package com.example.quizfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizfirebase.Common.Common;
import com.example.quizfirebase.Interface.ItemClickListener;
import com.example.quizfirebase.Model.Category;
import com.example.quizfirebase.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryPracticeActivity extends AppCompatActivity {
    Dialog myDialog;

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories, questionScore;


    public static CategoryPracticeActivity newInstances()
    {
        CategoryPracticeActivity categoryPracticeActivity = new CategoryPracticeActivity();
        return categoryPracticeActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_practice);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
        questionScore = database.getReference("Question_Score").child("user");

        listCategory = findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        listCategory.setLayoutManager(new GridLayoutManager(this, 3));

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(categories, Category.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options)
        {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vh  = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
//                return new CategoryViewHolder(inflater.inflate(R.layout.category_layout, parent, false));

                final CategoryViewHolder viewHolder = new CategoryViewHolder(vh);
                myDialog = new Dialog(parent.getContext());
                myDialog.setContentView(R.layout.popup);

                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder viewHolder, int position, @NonNull final Category model) {
                viewHolder.category_name.setText(model.getName());
                Glide.with(viewHolder.itemView)
                        .load(model.getImage())
                        .into(viewHolder.category_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(position == 0)
                        {
                            Intent startGame = new Intent(view.getContext() ,Start.class);
                            Common.categoryId = adapter.getRef(position).getKey();
                            Common.categoryName = model.getName();
                            startActivity(startGame);
                        }
                        else
                        {
//                            Glide.with(view.getContext())
//                                    .load(viewHolder.category_image)
//                                    .into();
                            TextView text = myDialog.findViewById(R.id.popup_text);
                            myDialog.show();
                        }
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
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
