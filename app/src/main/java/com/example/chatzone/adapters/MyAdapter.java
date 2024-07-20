package com.example.chatzone.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzone.Chat;
import com.example.chatzone.R;
import com.example.chatzone.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.time.LocalTime;

public class MyAdapter extends FirebaseRecyclerAdapter<User, MyAdapter.myViewHolder> {



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull User model) {
        holder.Username.setText(model.getUser_Name());
        holder.UserStatus.setText(model.getStatus());
        String uri=model.getImage_url();
        Picasso.get().load(uri).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalTime CurrentTime = LocalTime.now();
                LocalTime StartTime = LocalTime.of(02,00);
                LocalTime EndTime = LocalTime.of(06,00);

                    if (CurrentTime.isAfter(StartTime) && CurrentTime.isBefore(EndTime)) {
                        Toast.makeText(view.getContext(), "Freezed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(view.getContext(), Chat.class);
                        i.putExtra("Id",model.getId());
                        i.putExtra("Username",model.getUser_Name());
                        i.putExtra("imgUri",model.getImage_url());
                        view.getContext().startActivity(i);
                    }


            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
            return new myViewHolder(view);
    }



    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView Username,UserStatus;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.UserImage);
            Username=(TextView) itemView.findViewById(R.id.Username);
            UserStatus=(TextView) itemView.findViewById(R.id.UserStatus);
        }
    }
}
