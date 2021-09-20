package com.example.myomoshiroi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.other.EasyModeTenses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

public class ChangeAvatarActivity extends AppCompatActivity {

    Button buttonSave;
    ImageView changeAvatar, avatar1, avatar2, avatar3, avatar4, avatar5, avatar6,
            avatar7, avatar8, avatar9, avatar10, avatar11, avatar12;
    TextView avatarName;

    boolean Andry, Bulby, Cackytus, Chicky, Clibot, Cupies, Hatty, Headyglass, Jobot,
            Monisad, Pineglass, Skullyhead;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, uidRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    SharedPreferences prefs;
    String editAvatarName;

    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_change_avatar);

        buttonSave = findViewById(R.id.saveButton);
        changeAvatar = findViewById(R.id.changeAvatar);
        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        avatar3 = findViewById(R.id.avatar3);
        avatar4 = findViewById(R.id.avatar4);
        avatar5 = findViewById(R.id.avatar5);
        avatar6 = findViewById(R.id.avatar6);
        avatar7 = findViewById(R.id.avatar7);
        avatar8 = findViewById(R.id.avatar8);
        avatar9 = findViewById(R.id.avatar9);
        avatar10 = findViewById(R.id.avatar10);
        avatar11 = findViewById(R.id.avatar11);
        avatar12 = findViewById(R.id.avatar12);
        avatarName = findViewById(R.id.avatarName);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Andry = prefs.getBoolean("Andry", false);
        Bulby = prefs.getBoolean("Bulby", false);
        Cackytus = prefs.getBoolean("Cackytus", false);
        Chicky = prefs.getBoolean("Chicky", false);
        Clibot = prefs.getBoolean("Clibot", false);
        Cupies = prefs.getBoolean("Cupies", false);
        Hatty = prefs.getBoolean("Hatty", false);
        Headyglass = prefs.getBoolean("Headyglass", false);
        Jobot = prefs.getBoolean("Jobot", false);
        Monisad = prefs.getBoolean("Monisad", false);
        Pineglass = prefs.getBoolean("Pineglass", false);
        Skullyhead = prefs.getBoolean("Skullyhead", false);

        FirebaseAuth();

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );
        currentDate = EasyModeTenses.formatDate(attempt.getCreatedTime());
        title = "Profile Changes";
        descriptions = "You've made a change to your profile avatar.";
        createdTime = currentDate;

        editAvatarName = "profile";
        avatar1.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_bulb);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_bulb";
            avatarName.setText("Bulby");
            if (Bulby) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar2.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_cactus);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_cactus";
            avatarName.setText("Cackytus");
            if (Cackytus) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar3.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_chick);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_chick";
            avatarName.setText("Chicky");
            if (Chicky) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar4.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_cup);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_cup";
            avatarName.setText("Cupies");
            if (Cupies) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar5.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_hat);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_hat";
            avatarName.setText("Hatty");
            if (Hatty) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar6.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_pinaple);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_pinaple";
            avatarName.setText("Pineglass");
            if (Pineglass) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar7.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_android);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_android";
            avatarName.setText("Andry");
            if (Andry) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar8.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_cliprobot);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_cliprobot";
            avatarName.setText("Clibot");
            if (Clibot) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar9.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_head);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_head";
            avatarName.setText("Headyglass");
            if (Headyglass) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar10.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_monitor);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_monitor";
            avatarName.setText("Monisad");
            if (Monisad) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar11.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_robot);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_robot";
            avatarName.setText("Jobot");
            if (Jobot) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        avatar12.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_skull);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_skull";
            avatarName.setText("Skullyhead");
            if (Skullyhead) {
                buttonSave.setText("Save");
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
                buttonSave.setTextColor(Color.BLACK);
                buttonSave.setText("Purchase first at the avatar store!");
            }
        });
        buttonSave.setOnClickListener(v -> {
            uidRef.child("Avatar").setValue(editAvatarName);
            SaveHistory();
            Intent intent = new Intent(ChangeAvatarActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void FirebaseAuth() {
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        user = mAuth.getCurrentUser();
    }

    private void SaveHistory() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
}