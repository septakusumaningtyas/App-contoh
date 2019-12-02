package com.example.quizfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quizfirebase.Common.Common;
import com.example.quizfirebase.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.chrono.MinguoChronology;

public class MainActivity extends AppCompatActivity {
//    Dialog myDialog;

    EditText edtNewUser, edtNewClass; //for sing up

    EditText edtUser; //for sign in;

    Button btnSingUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        myDialog = new Dialog(this);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUser = (EditText)findViewById(R.id.edtUserName);

        btnSignIn = (Button)findViewById(R.id.btn_sign_in);
        btnSingUp = (Button)findViewById(R.id.btn_sign_up);

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString());
            }
        });

    }

    private void signIn(final String user){
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists())
                {
                    if(!user.isEmpty()) {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getUserName().equals(user)) {
                            Intent homeActivity = new Intent(MainActivity.this, PlayActivity.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter your user name", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "User is not exists !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full the information");
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);

        edtNewUser = sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewClass = sign_up_layout.findViewById(R.id.edtNewKelas);

        alertDialog.setView(sign_up_layout);

        alertDialog.setNegativeButton("NO" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(edtNewUser.getText().toString(), edtNewClass.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists())
                        {
                            Toast.makeText(MainActivity.this,"User already exists!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this,"User registration success!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
