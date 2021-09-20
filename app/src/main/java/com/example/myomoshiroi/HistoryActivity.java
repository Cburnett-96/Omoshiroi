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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myomoshiroi.adapter.HistoryAdapter;
import com.example.myomoshiroi.adapter.RankingAdapter;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.model.UserRanking;
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
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    LinearLayout linearProgress;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<UserHistory> userHistoryList;
    ImageView loading;
    TextView totalActivities, totalXP, totalOcoins;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_history);

        linearProgress = findViewById(R.id.linearLoadingProgress);
        loading = findViewById(R.id.loading);
        totalActivities = findViewById(R.id.textViewTotalActivities);
        totalXP = findViewById(R.id.textViewTotalXp);
        totalOcoins = findViewById(R.id.textViewTotalOCoin);
        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userHistoryList = new ArrayList<>();
        adapter = new HistoryAdapter(this, userHistoryList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");

        Button buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(HistoryActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        linearProgress.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.raw.splash_screen_loading)
                .into(loading);
        Query query = FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("CreatedTime");
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userHistoryList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserHistory user = snapshot.getValue(UserHistory.class);
                    userHistoryList.add(user);
                }
                int total = (int) dataSnapshot.getChildrenCount();
                totalActivities.setText("Total Activities ("+total+")");
                adapter.notifyDataSetChanged();
                Collections.reverse(userHistoryList);
                linearProgress.setVisibility(View.GONE);
                Glide.with(HistoryActivity.this).clear(loading);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void FirebaseAuth(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int getPoints = prefs.getInt("points", 0);

        totalXP.setText(String.valueOf(getPoints));
        //        Get Firebase auth instance
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("UserData").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String total = Objects.requireNonNull(snapshot.child("TotalOcoins").getValue()).toString();
                totalOcoins.setText(total);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
}