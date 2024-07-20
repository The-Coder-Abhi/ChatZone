package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    Button Verify;
    ImageButton Back;
    FirebaseAuth auth;
    FirebaseUser usertask,user1;
    ProgressBar PB1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        PB1=findViewById(R.id.pB);
        auth=FirebaseAuth.getInstance();
        Back=findViewById(R.id.back01);
        usertask=auth.getCurrentUser();
        Verify=findViewById(R.id.verify01);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertask.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }
                    }
                });
            }
        });

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshUser();
            }
        });

    }
    public void refreshUser(){
        auth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user1 = auth.getCurrentUser();
                if(user1.isEmailVerified())
                {
                    PB1.setVisibility(View.VISIBLE);
                    Intent i = new Intent(VerificationActivity.this, ProfileImage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    PB1.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(VerificationActivity.this, "Email Not Verified !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
