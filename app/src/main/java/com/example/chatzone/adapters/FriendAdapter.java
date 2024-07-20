package com.example.chatzone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzone.R;
import com.example.chatzone.User01;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class FriendAdapter extends FirebaseRecyclerAdapter<User01,FriendAdapter.friendViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FriendAdapter(@NonNull FirebaseRecyclerOptions<User01> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendAdapter.friendViewHolder holder, int position, @NonNull User01 model) {
        holder.Username.setText(model.getUser_Name());
        holder.Firstname.setText(model.getFirst_Name());
        holder.Surname.setText(model.getLast_Name());
        String uri = model.getImage_url();
        Picasso.get().load(uri).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Friends", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public FriendAdapter.friendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlayout,parent,false);
        return new friendViewHolder(view);
    }

    public static class friendViewHolder extends RecyclerView.ViewHolder{

        TextView Firstname,Surname,Username;
        ImageView img;
        public friendViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.UserImage);
            Firstname=(TextView) itemView.findViewById(R.id.name);
            Surname=(TextView) itemView.findViewById(R.id.surname);
            Username=(TextView) itemView.findViewById(R.id.Username);
        }
    }
}
