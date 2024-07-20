package com.example.chatzone.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzone.R;
import com.example.chatzone.User01;
import com.example.chatzone.adapters.FriendAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendsFragment extends Fragment
{
    RecyclerView recyclerView;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    LinearLayoutManager linearLayoutManager;
    FriendAdapter myAdapter;
    EditText Search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends,container,false);
        mAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference();
        recyclerView=v.findViewById(R.id.friendrecycler);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        Search=v.findViewById(R.id.searchText);

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                proccesssearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                proccesssearch(editable.toString());
            }
        });

        Query query = dbRef.child("Users");
        FirebaseRecyclerOptions<User01> allusername = new FirebaseRecyclerOptions.Builder<User01>().setQuery(query,User01.class).build();

        myAdapter=new FriendAdapter(allusername);
        recyclerView.setAdapter(myAdapter);
        return v;
    }

    private void proccesssearch(String s) {
        Query query = dbRef.child("Users").orderByChild("search").startAt(s.toLowerCase()).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<User01> allusername = new FirebaseRecyclerOptions.Builder<User01>().setQuery(query,User01.class).build();
        myAdapter=new FriendAdapter(allusername);
        myAdapter.startListening();
        recyclerView.setAdapter(myAdapter);
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
