package com.example.chatzone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatzone.R;
import com.example.chatzone.WelcomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment
{
    TextView UN,FN,LN,BD,PN;
    ImageView Img;
    Button SignOut;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        UN = view.findViewById(R.id.UserName01);
        FN = view.findViewById(R.id.FirstName01);
        LN = view.findViewById(R.id.LastName01);
        BD = view.findViewById(R.id.BirthDate01);
        PN = view.findViewById(R.id.Phone_Number);
        Img = view.findViewById(R.id.UpladImage01);
        SignOut = view.findViewById(R.id.Edit);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        ref = database.getReference().child("Users");
        ref.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Failed to read information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), WelcomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return view;
    }

}
