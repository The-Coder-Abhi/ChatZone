package com.example.chatzone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.chatzone.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ViewPager viewpager;
    TabLayout tablayout;
    androidx.appcompat.widget.Toolbar toolbar;
    TabItem mchat,mfriends,mprofile;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef,dbRefUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = findViewById(R.id.viewpager02);
        tablayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mchat = findViewById(R.id.chat);
        mfriends = findViewById(R.id.friends);
        mprofile = findViewById(R.id.profile);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Chats");
        dbRef.keepSynced(true);

        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_more_vert_24);
        toolbar.setOverflowIcon(drawable);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),tablayout.getTabCount());
        viewpager.setAdapter(viewPagerAdapter);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2)
                {
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profile:
                Intent i = new Intent(MainActivity.this,ProfileInfo.class);
                startActivity(i);
                finish();
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, WelcomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void status(String status)
    {
        dbRefUser = firebaseDatabase.getReference().child("Users").child(mUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        dbRefUser.updateChildren(hashMap);
    }
}








