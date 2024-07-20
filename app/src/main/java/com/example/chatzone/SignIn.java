package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    TextInputLayout Email;
    TextView SignUpL;
    TextInputLayout Pass01;
    Button SignIn01;
    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    ProgressBar PB;
    ImageButton Back;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Email=findViewById(R.id.Email01);
        Pass01=findViewById(R.id.Password);
        SignIn01=findViewById(R.id.Sign_In);
        PB=findViewById(R.id.progressBar02);
        SignUpL=findViewById(R.id.SignUpLink);
        Back=findViewById(R.id.Back);
        auth = FirebaseAuth.getInstance();

        SignIn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PB.setVisibility(View.VISIBLE);
                String email= Objects.requireNonNull(Email.getEditText()).getText().toString();
                String Pass= Objects.requireNonNull(Pass01.getEditText()).getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(Pass)){
                    Toast.makeText(SignIn.this,"Input Field can't be Empty",Toast.LENGTH_SHORT).show();
                }
                if(!email.matches(emailPattern)){
                    Toast.makeText(SignIn.this,"Enter valid Email Address",Toast.LENGTH_SHORT).show();
                }
                signInUser(email,Pass);
            }

            private void signInUser(String email, String pass) {
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                    PB.setVisibility(View.GONE);
                    if(task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        assert user != null;
                        if (user.isEmailVerified()) {
                            Intent i = new Intent(SignIn.this, MainActivity.class);
                            Toast.makeText(SignIn.this,"Singed in",Toast.LENGTH_SHORT).show();
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SignIn.this, VerificationActivity.class);
                            Toast.makeText(SignIn.this, "Verify Email", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        }
                    }
                });
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, WelcomeScreen.class);
                startActivity(i);
            }
        });

        SignUpL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
            }
        });

    }
}