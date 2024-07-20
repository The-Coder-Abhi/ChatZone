package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    TextInputLayout UserName;
    TextInputLayout Email;
    TextInputLayout Pass01;
    TextInputLayout conPass01;
    Button signUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar PB1;
    ImageButton Back;
    static FirebaseAuth mAuth;
    static FirebaseUser mUser;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserName=findViewById(R.id.Username);
        Email=findViewById(R.id.Email);
        Pass01=findViewById(R.id.Password01);
        conPass01=findViewById(R.id.Password02);
        signUp=findViewById(R.id.Sign_Up02);
        PB1=findViewById(R.id.progressBar01);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        Back=findViewById(R.id.back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               creatUser();

            }

            private void creatUser() {
                Intent i = getIntent();
                String BD = i.getStringExtra(userInfo.t1);
                String FN = i.getStringExtra(userInfo.t2);
                String LN = i.getStringExtra(userInfo.t3);
                String PN = i.getStringExtra(userInfo.t4);
                String email=Email.getEditText().getText().toString();
                String UN=UserName.getEditText().getText().toString();
                String Pass=Pass01.getEditText().getText().toString();
                String conPass=conPass01.getEditText().getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(UN) || TextUtils.isEmpty(Pass) || TextUtils.isEmpty(conPass)){
                    Toast.makeText(SignUp.this,"Input Field can't be Empty",Toast.LENGTH_SHORT).show();
                }else if(Pass == conPass)
                {
                    Toast.makeText(SignUp.this,"Password and Confirm Password does not match",Toast.LENGTH_SHORT).show();
                }else if(Pass.length()<6){
                    Toast.makeText(SignUp.this,"Password length should be Atleast 6 digit long",Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern))
                {
                    Toast.makeText(SignUp.this,"Enter valid Email Address",Toast.LENGTH_SHORT).show();
                }else
                {
                   PB1.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                mUser=mAuth.getCurrentUser();
                                sendVerificationEmail();
                                dbRef=dbRef.child(mUser.getUid());
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("id",mUser.getUid());
                                hashMap.put("Email",email);
                                hashMap.put("User_Name",UN);
                                hashMap.put("First_Name",FN);
                                hashMap.put("Last_Name",LN);
                                hashMap.put("Birth_Date",BD);
                                hashMap.put("Phone_Number",PN);
                                hashMap.put("image_url","default");
                                hashMap.put("status","offline");
                                hashMap.put("search",FN.toLowerCase()+" "+LN.toLowerCase());
                                dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       PB1.setVisibility(View.GONE);
                                        if(task.isSuccessful())
                                        {
                                            Intent in = new Intent(SignUp.this,VerificationActivity.class);
                                            startActivity(in);
                                            finish();
                                        }
                                    }
                                });
                                //Toast.makeText(SignUp.this,"Registration Successful",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(SignUp.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
            /*private void nextAct() {
                Intent in = new Intent(SignUp.this,MainActivity.class);
                startActivity(in);
            }*/
        });
    }

    private void sendVerificationEmail() {
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignUp.this, "Verification email sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Verification email not sent", Toast.LENGTH_SHORT).show();
            }
        });
    }


}