package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzone.adapters.MessagesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chat extends AppCompatActivity {

    private static int Timer=180000;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref,databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText Writemessage;
    ImageButton SendTextMessage;
    CardView sendMessageCardView;
    androidx.appcompat.widget.Toolbar mtoolbarChat;
    ImageView UserImage;
    TextView UserTitle;
    private String enteredMessage;
    Intent i;
    String sendername,recievername,recieveruid,senderuid;
    String senderroom,recieverroom;
    RecyclerView messagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Writemessage = findViewById(R.id.getmessage);
        SendTextMessage = findViewById(R.id.SendButton);
        sendMessageCardView = findViewById(R.id.SendButtonCard);
        mtoolbarChat = findViewById(R.id.UserTitle);
        UserTitle = findViewById(R.id.ReceiverName);
        UserImage = findViewById(R.id.UserImage);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");



        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        senderuid = mAuth.getUid();
        recieveruid = getIntent().getStringExtra("Id");
        recievername = getIntent().getStringExtra("Username");
        senderroom = senderuid+recieveruid;
        recieverroom = recieveruid+senderuid;

        messagesArrayList = new ArrayList<>();
        messagerecyclerview = findViewById(R.id.messagerecycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(Chat.this,messagesArrayList);
        messagerecyclerview.setAdapter(messagesAdapter);
        //messagesAdapter.notifyDataSetChanged();
        i = getIntent();

        setSupportActionBar(mtoolbarChat);
        mtoolbarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Chat.this, "Toolbar is Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        UserTitle.setText(recievername);

        String Uri = getIntent().getStringExtra("imgUri");
        if(Uri.isEmpty()){
            Toast.makeText(this, "image path is null", Toast.LENGTH_SHORT).show();
        }
        else{
            Picasso.get().load(Uri).into(UserImage);
        }
        SendTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredMessage = Writemessage.getText().toString();
                if(enteredMessage.isEmpty()){
                    Toast.makeText(Chat.this, "Enter Message", Toast.LENGTH_SHORT).show();
                }else {
                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredMessage, mAuth.getUid(), date.getTime(), currenttime);
                    database.getReference().child("chats").child(senderroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.getReference().child("chats").child(recieverroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    messagesAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                    Writemessage.setText(null);
                }
            }
        });

        databaseReference = database.getReference().child("chats").child(senderroom).child("messages");
        messagesAdapter=new MessagesAdapter(Chat.this,messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messages=snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                //messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref = database.getReference().child("Users");
        ref.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String Phone_Number = String.valueOf(dataSnapshot.child("Phone_Number").getValue());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String Warning = "Your kid is using ChatZone since 30 min";
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(Phone_Number,null,Warning,null,null);
                            }
                        },Timer);
                    }
                }
            }
        });
        //messagesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}