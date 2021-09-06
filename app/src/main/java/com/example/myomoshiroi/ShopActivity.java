package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.example.myomoshiroi.other.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, uidRef, uidRefItem;
    SharedPreferences prefs;
    int getocoins, getTimerAdd, getTimerFreeze, getTimerStop;
    boolean addOneMin = false;
    boolean freeze = false;
    boolean stop = false;
    boolean Andry, Bulby, Cackytus, Chicky, Clibot, Cupies, Hatty, Headyglass, Jobot,
            Monisad, Pineglass, Skullyhead;
    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;

    ColorStateList def;
    TextView select, itemPowerUps, itemAvatar, timerAddCount, timerFreezeCount, timerStopCount, powerUpName,
            price, descriptionPowerUps, Ocoins;
    Button buttonBack, changeAvatar;
    LinearLayout layoutPowerUps, layoutAvatars;
    ImageView imageShop, imagePowerUps;
    Button buyAdd,buyStop,buyFreeze, buyButton;
    Button buyAddInfo,buyStopInfo,buyFreezeInfo;
    Button buyAndry,buyBulby,buyCackytus, buyChicky, buyClibot, buyCupies, buyHatty, buyHeadyglass, buyMonisad,
            buyPineglass, buyJobot, buySkullyhead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_shop);

        layoutPowerUps = findViewById(R.id.linearLayoutPowerUps);
        layoutAvatars = findViewById(R.id.linearLayoutAvatars);
        imageShop = findViewById(R.id.imageShop);
        buttonBack = findViewById(R.id.backButton);
        changeAvatar = findViewById(R.id.btn_changeAvatar);
        select = findViewById(R.id.select);
        itemPowerUps = findViewById(R.id.itemPowerUps);
        itemAvatar = findViewById(R.id.itemAvatar);
        Ocoins = findViewById(R.id.textOcoin);

        timerAddCount = findViewById(R.id.timerAddOneMinCount);
        timerFreezeCount = findViewById(R.id.timerFreezeCount);
        timerStopCount = findViewById(R.id.timerStopCount);
        buyAdd = findViewById(R.id.btn_buyAdd);
        buyAddInfo = findViewById(R.id.btn_buyAddInfo);
        buyStop = findViewById(R.id.btn_buyStop);
        buyStopInfo = findViewById(R.id.btn_buyStopInfo);
        buyFreeze = findViewById(R.id.btn_buyFreeze);
        buyFreezeInfo = findViewById(R.id.btn_buyFreezeInfo);
        buyAndry = findViewById(R.id.btn_buyAndry);
        buyBulby = findViewById(R.id.btn_buyBulby);
        buyCackytus = findViewById(R.id.btn_buyCackytus);
        buyChicky = findViewById(R.id.btn_buyChicky);
        buyClibot = findViewById(R.id.btn_buyClibot);
        buyCupies = findViewById(R.id.btn_buyCupies);
        buyHatty = findViewById(R.id.btn_buyHatty);
        buyHeadyglass = findViewById(R.id.btn_buyHeadyglass);
        buyMonisad = findViewById(R.id.btn_buyMonisad);
        buyPineglass = findViewById(R.id.btn_buyPineglass);
        buyJobot = findViewById(R.id.btn_buyJobot);
        buySkullyhead = findViewById(R.id.btn_buySkullyhead);

        buyAdd.setOnClickListener(this);
        buyStop.setOnClickListener(this);
        buyFreeze.setOnClickListener(this);
        buyAndry.setOnClickListener(this);
        buyBulby.setOnClickListener(this);
        buyCackytus.setOnClickListener(this);
        buyChicky.setOnClickListener(this);
        buyClibot.setOnClickListener(this);
        buyCupies.setOnClickListener(this);
        buyHatty.setOnClickListener(this);
        buyHeadyglass.setOnClickListener(this);
        buyMonisad.setOnClickListener(this);
        buyPineglass.setOnClickListener(this);
        buyJobot.setOnClickListener(this);
        buySkullyhead.setOnClickListener(this);

        def = itemAvatar.getTextColors();
        itemAvatar.setOnClickListener(this);
        itemPowerUps.setOnClickListener(this);

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
        getocoins = prefs.getInt("ocoins", 0);
        Ocoins.setText(String.valueOf(getocoins));
        getTimerAdd = prefs.getInt("timerAddOneMin", 0);
        getTimerFreeze = prefs.getInt("timerFreeze", 0);
        getTimerStop = prefs.getInt("timerStop", 0);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRefItem = databaseReference.child("UserItem").child(uid);

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );
        currentDate = Utils.formatDate(attempt.getCreatedTime());
        createdTime = currentDate;
        title = "Bought Power-Ups";

        buyAddInfo.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            addOneMin = true;
            freeze = false;
            stop = false;
            showPopupWindow(v);
        });
        buyFreezeInfo.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            freeze = true;
            addOneMin = false;
            stop = false;
            showPopupWindow(v);
        });
        buyStopInfo.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            stop = true;
            addOneMin = false;
            freeze = false;
            showPopupWindow(v);
        });
        changeAvatar.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(ShopActivity.this, ChangeAvatarActivity.class);
            startActivity(intent);
            finish();
        });

        buttonBack.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
        UpdateCount();
        AvatarValue();
    }
    @Override
    public void onClick(View view) {
        //TABS
        if (view.getId() == R.id.itemPowerUps){
            SoundPoolManager.playSound(1);
            select.animate().x(0).setDuration(100);
            itemPowerUps.setTextColor(getResources().getColor(R.color.forest_green));
            itemAvatar.setTextColor(def);
            layoutPowerUps.setVisibility(View.VISIBLE);
            layoutAvatars.setVisibility(View.GONE);
            InputStream imageStream = getResources().openRawResource(R.raw.shop_powerups);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageShop.setImageBitmap(bitmap);
            title = "Bought Power-Ups";
        } else if (view.getId() == R.id.itemAvatar){
            SoundPoolManager.playSound(1);
            itemPowerUps.setTextColor(def);
            itemAvatar.setTextColor(getResources().getColor(R.color.forest_green));
            int size = itemAvatar.getWidth();
            select.animate().x(size).setDuration(100);
            layoutPowerUps.setVisibility(View.GONE);
            layoutAvatars.setVisibility(View.VISIBLE);
            InputStream imageStream = getResources().openRawResource(R.raw.shop_avatar);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageShop.setImageBitmap(bitmap);
            title = "Bought Profile Avatar";
        }
        //POWER UPS BUY BUTTONS
        if (view.getId() == R.id.btn_buyAdd) {
            if(500 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerAddOnemin").setValue(getTimerAdd + 1);
                uidRef.child("Ocoin").setValue(getocoins - 500);
                descriptions = "You purchased add one (1) minute.";
                UpdateData();
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyFreeze) {
            if(800 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerFreeze").setValue(getTimerFreeze + 1);
                uidRef.child("Ocoin").setValue(getocoins - 800);
                descriptions = "You purchased freeze timer.";
                UpdateData();
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyStop) {
            if(1200 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerStop").setValue(getTimerStop + 1);
                uidRef.child("Ocoin").setValue(getocoins - 1200);
                descriptions = "You purchased stop timer.";
                UpdateData();
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        }
        //BUY BUTTON OF AVATARS
        if (view.getId() == R.id.btn_buyAndry) {
            if(5000 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Andry").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5000);
                descriptions = "You purchased Andry avatar.";
                buyAndry.setText("Owned");
                buyAndry.setEnabled(false);
                buyAndry.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyBulby) {
            if(5200 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Bulby").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5200);
                descriptions = "You purchased Bulby avatar.";
                buyBulby.setText("Owned");
                buyBulby.setEnabled(false);
                buyBulby.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyCackytus) {
            if(5000 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Cackytus").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5000);
                descriptions = "You purchased Cackytus avatar.";
                buyCackytus.setText("Owned");
                buyCackytus.setEnabled(false);
                buyCackytus.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyChicky) {
            if(5100 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Chicky").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5100);
                descriptions = "You purchased Chicky avatar.";
                buyChicky.setText("Owned");
                buyChicky.setEnabled(false);
                buyChicky.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyClibot) {
            if(5000 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Clibot").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5000);
                descriptions = "You purchased Clibot avatar.";
                buyClibot.setText("Owned");
                buyClibot.setEnabled(false);
                buyClibot.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyCupies) {
            if(7500 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Cupies").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 7500);
                descriptions = "You purchased Cupies avatar.";
                buyCupies.setText("Owned");
                buyCupies.setEnabled(false);
                buyCupies.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyHatty) {
            if(7500 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Hatty").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 7500);
                descriptions = "You purchased Hatty avatar.";
                buyHatty.setText("Owned");
                buyHatty.setEnabled(false);
                buyHatty.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyHeadyglass) {
            if(6200 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Headyglass").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 6200);
                descriptions = "You purchased Headyglass avatar.";
                buyHeadyglass.setText("Owned");
                buyHeadyglass.setEnabled(false);
                buyHeadyglass.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyJobot) {
            if(5000 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Jobot").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5000);
                descriptions = "You purchased Jobot avatar.";
                buyJobot.setText("Owned");
                buyJobot.setEnabled(false);
                buyJobot.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyMonisad) {
            if(5100 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Monisad").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5100);
                descriptions = "You purchased Monisad avatar.";
                buyMonisad.setText("Owned");
                buyMonisad.setEnabled(false);
                buyMonisad.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buyPineglass) {
            if(5200 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Pineglass").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 5200);
                descriptions = "You purchased Pineglass avatar.";
                buyPineglass.setText("Owned");
                buyPineglass.setEnabled(false);
                buyPineglass.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btn_buySkullyhead) {
            if(6400 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("Skullyhead").setValue(true);
                uidRef.child("Ocoin").setValue(getocoins - 6400);
                descriptions = "You purchased Skullyhead avatar.";
                buySkullyhead.setText("Owned");
                buySkullyhead.setEnabled(false);
                buySkullyhead.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void UpdateCount(){
        uidRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timerAdd = Objects.requireNonNull(snapshot.child("TimerAddOnemin").getValue()).toString();
                String timerStop = Objects.requireNonNull(snapshot.child("TimerStop").getValue()).toString();
                String timerFreeze = Objects.requireNonNull(snapshot.child("TimerFreeze").getValue()).toString();
                timerAddCount.setText(timerAdd);
                timerFreezeCount.setText(timerFreeze);
                timerStopCount.setText(timerStop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
    private void UpdateData(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void AvatarValue(){
        if(Andry){
            buyAndry.setText("Owned");
            buyAndry.setEnabled(false);
            buyAndry.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }else if (Bulby){
            buyBulby.setText("Owned");
            buyBulby.setEnabled(false);
            buyBulby.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }else if(Cackytus) {
            buyCackytus.setText("Owned");
            buyCackytus.setEnabled(false);
            buyCackytus.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Chicky) {
            buyChicky.setText("Owned");
            buyChicky.setEnabled(false);
            buyChicky.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Clibot) {
            buyClibot.setText("Owned");
            buyClibot.setEnabled(false);
            buyClibot.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Cupies) {
            buyCupies.setText("Owned");
            buyCupies.setEnabled(false);
            buyCupies.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Hatty) {
            buyHatty.setText("Owned");
            buyHatty.setEnabled(false);
            buyHatty.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Headyglass) {
            buyHeadyglass.setText("Owned");
            buyHeadyglass.setEnabled(false);
            buyHeadyglass.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Jobot) {
            buyJobot.setText("Owned");
            buyJobot.setEnabled(false);
            buyJobot.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Monisad) {
            buyMonisad.setText("Owned");
            buyMonisad.setEnabled(false);
            buyMonisad.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Pineglass) {
            buyPineglass.setText("Owned");
            buyPineglass.setEnabled(false);
            buyPineglass.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
        else if(Skullyhead) {
            buySkullyhead.setText("Owned");
            buySkullyhead.setEnabled(false);
            buySkullyhead.setBackgroundTintList(ContextCompat.getColorStateList(ShopActivity.this, R.color.dark_gray));
        }
    }
    //PopupWindow display method
    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.power_ups_more_info, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        buyButton = popupView.findViewById(R.id.buyButton);
        imagePowerUps = popupView.findViewById(R.id.imageViewPowerUps);
        powerUpName = popupView.findViewById(R.id.textViewPowerUpName);
        price = popupView.findViewById(R.id.textViewPrice);
        descriptionPowerUps = popupView.findViewById(R.id.textViewDescriptionPowerUps);

        if(addOneMin){
            InputStream imageStream = getResources().openRawResource(R.raw.timer_addonemin);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imagePowerUps.setImageBitmap(bitmap);
            powerUpName.setText("Add 1 minute");
            price.setText("200");
            descriptionPowerUps.setText("They can utilize this power-up on any mode to add one (1) minute to the timer.");
            buyButton.setOnClickListener(v -> {
            if(200 < getocoins){
                    SoundPoolManager.playSound(1);
                uidRefItem.child("TimerAddOnemin").setValue(getTimerAdd + 1);
                    uidRef.child("Ocoin").setValue(getocoins - 200);
                    descriptions = "You purchased add one (1) minute.";
                    UpdateData();
                    SaveHistory();
                }
            else{
                    SoundPoolManager.playSound(0);
                    Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
                }
        });
        }else if(freeze){
            InputStream imageStream = getResources().openRawResource(R.raw.timer_freeze);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imagePowerUps.setImageBitmap(bitmap);
            powerUpName.setText("Freeze Timer");
            price.setText("400");
            descriptionPowerUps.setText("They can use this power-up in any mode to freeze or pause the timer per question.");
            buyButton.setOnClickListener(v -> {
            if(400 < getocoins){
                    SoundPoolManager.playSound(1);
                uidRefItem.child("TimerFreeze").setValue(getTimerFreeze + 1);
                    uidRef.child("Ocoin").setValue(getocoins - 400);
                    descriptions = "You purchased freeze timer.";
                    UpdateData();
                    SaveHistory();
            }
            else{
                    SoundPoolManager.playSound(0);
                    Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
                }
            });
        }else if (stop){
            InputStream imageStream = getResources().openRawResource(R.raw.timer_stop);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imagePowerUps.setImageBitmap(bitmap);
            powerUpName.setText("Stop Timer");
            price.setText("800");
            descriptionPowerUps.setText("This power-up can be used in any mode to pause the timer for the whole question stage.");
            buyButton.setOnClickListener(v -> {
            if(800 < getocoins){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerStop").setValue(getTimerStop + 1);
                uidRef.child("Ocoin").setValue(getocoins - 800);
                descriptions = "You purchased stop timer.";
                UpdateData();
                SaveHistory();
            }
            else{
                SoundPoolManager.playSound(0);
                Toast.makeText(ShopActivity.this, "You don't have enough Ocoins to purchase this!", Toast.LENGTH_LONG).show();
            }
            });
        }

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }
    private  void SaveHistory(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
}