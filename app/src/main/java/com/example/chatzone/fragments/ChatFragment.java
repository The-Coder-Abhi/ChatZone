package com.example.chatzone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzone.R;
import com.example.chatzone.User;
import com.example.chatzone.adapters.MyAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatFragment extends Fragment
{
    RecyclerView recyclerView;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    LinearLayoutManager linearLayoutManager;
    MyAdapter myAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat,container,false);
        mAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference();
        recyclerView=v.findViewById(R.id.recyclerview);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = dbRef.child("Users");
        FirebaseRecyclerOptions<User> allusername = new FirebaseRecyclerOptions.Builder<User>().setQuery(query,User.class).build();

        myAdapter=new MyAdapter(allusername);
        recyclerView.setAdapter(myAdapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}
