package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileInfo extends AppCompatActivity {

    TextView UN,FN,LN,BD,PN;
    ImageView Img;
    Button SignOut;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        UN = findViewById(R.id.UserName01);
        FN = findViewById(R.id.FirstName01);
        LN = findViewById(R.id.LastName01);
        BD = findViewById(R.id.BirthDate01);
        PN = findViewById(R.id.Phone_Number);
        Img = findViewById(R.id.UpladImage01);
        Back = findViewById(R.id.imageButton01);
        SignOut = findViewById(R.id.Edit);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        ref = database.getReference().child("Users");
        ref.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {
                        Toast.makeText(ProfileInfo.this, "Success", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String User_Name = String.valueOf(dataSnapshot.child("User_Name").getValue());
                        String First_Name = String.valueOf(dataSnapshot.child("First_Name").getValue());
                        String Last_Name = String.valueOf(dataSnapshot.child("Last_Name").getValue());
                        String Birth_Date = String.valueOf(dataSnapshot.child("Birth_Date").getValue());
                        String Phone_Number = String.valueOf(dataSnapshot.child("Phone_Number").getValue());
                        String image_url = String.valueOf(dataSnapshot.child("image_url").getValue());
                        UN.setText(User_Name);
                        FN.setText(First_Name);
                        LN.setText(Last_Name);
                        BD.setText(Birth_Date);
                        PN.setText(Phone_Number);
                        Picasso.get().load(image_url).into(Img);
                    }else{
                        Toast.makeText(ProfileInfo.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProfileInfo.this, "Failed to read information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileInfo.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileInfo.this, WelcomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileInfo.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}