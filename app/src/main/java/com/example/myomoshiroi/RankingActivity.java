package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myomoshiroi.adapter.RankingAdapter;
import com.example.myomoshiroi.adapter.RankingTopOneAdapter;
import com.example.myomoshiroi.adapter.RankingTopTwoAdapter;
import com.example.myomoshiroi.model.UserRanking;
import com.example.myomoshiroi.model.UserRankingOne;
import com.example.myomoshiroi.model.UserRankingTwoThree;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {
    private LinearLayout linearProgress;
    private TextView totalUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    SharedPreferences prefs;
    String fullname, avatar;
    int point;

    private RecyclerView recyclerView, recyclerViewTop, recyclerViewTop2;
    private RankingAdapter adapter;
    private RankingTopOneAdapter adapterTop1;
    private RankingTopTwoAdapter adapterTop2;
    private List<UserRanking> userList;
    private List<UserRankingOne> userList1;
    private List<UserRankingTwoThree> userList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_ranking);

        linearProgress = findViewById(R.id.linearLoadingProgress);
        totalUser = findViewById(R.id.textViewTotalUser);
        recyclerViewTop2 = findViewById(R.id.recyclerViewTopTwoThree);
        recyclerViewTop = findViewById(R.id.recyclerViewTop);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerViewTop2.setHasFixedSize(true);
        recyclerViewTop2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTop.setHasFixedSize(true);
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userList1 = new ArrayList<>();
        userList2 = new ArrayList<>();
        adapterTop2 = new RankingTopTwoAdapter(this, userList2);
        adapterTop1 = new RankingTopOneAdapter(this,userList1);
        adapter = new RankingAdapter(this, userList);
        recyclerViewTop2.setAdapter(adapterTop2);
        recyclerViewTop.setAdapter(adapterTop1);
        recyclerView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserRanking");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Button buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(RankingActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearProgress.setVisibility(View.VISIBLE);
        saveData();
        Query query = FirebaseDatabase.getInstance().getReference("UserRanking")
                .orderByChild("point");
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    private void saveData(){
        String getName = prefs.getString("name",null);
        String getAvatar = prefs.getString("avatarName", null);
        int getPoints = prefs.getInt("points", 0);

        fullname = getName;
        avatar = getAvatar;
        point = getPoints;

        UserRanking data = new UserRanking(fullname, avatar, point);
        FirebaseDatabase.getInstance().getReference("UserRanking")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data);
        UserRankingOne data1 = new UserRankingOne(fullname, avatar, point);
        FirebaseDatabase.getInstance().getReference("UserRanking")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data1);
        UserRankingTwoThree data2 = new UserRankingTwoThree(fullname, avatar, point);
        FirebaseDatabase.getInstance().getReference("UserRanking")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data2);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userList.clear();
            userList1.clear();
            userList2.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserRanking user = snapshot.getValue(UserRanking.class);
                    UserRankingOne user1 = snapshot.getValue(UserRankingOne.class);
                    UserRankingTwoThree user2 = snapshot.getValue(UserRankingTwoThree.class);
                    userList.add(user);
                    userList1.add(user1);
                    userList2.add(user2);
                }
                int total = (int) dataSnapshot.getChildrenCount();
                totalUser.setText("Total Ranking Participants ("+total+")");
                adapterTop1.notifyDataSetChanged();
                adapterTop2.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                Collections.reverse(userList);
                Collections.reverse(userList1);
                Collections.reverse(userList2);
                adapterTop2.removeItem(0);
                adapter.removeItem(0);
                adapter.removeItem(0);
                adapter.removeItem(0);
                linearProgress.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}