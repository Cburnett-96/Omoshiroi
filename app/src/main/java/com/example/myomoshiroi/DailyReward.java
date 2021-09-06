package com.example.myomoshiroi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.example.myomoshiroi.other.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

public class DailyReward {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, uidRef;
    FirebaseAuth mAuth;
    SharedPreferences prefs;

    Calendar calendar;
    int year, month, day, getocoins, getTotalOcoins, getpoints;
    boolean currentday = false;

    boolean firstDay;
    boolean secondDay;
    boolean thirdDay;
    boolean fourthDay;
    boolean fifthDay;
    boolean sixthDay;
    boolean seventhDay;

    Button claimButton, buttonClose, buttonHelpDailyReward;
    LinearLayout linearDailyReward, linearDay1, linearDay2, linearDay3, linearDay4, linearDay5, linearDay6, linearDay7;
    TextView ViewDay1, ViewDay2, ViewDay3, ViewDay4, ViewDay5, ViewDay6, ViewDay7;

    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;

    //PopupWindow display method
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.daily_reward, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        claimButton = popupView.findViewById(R.id.claimButton);
        buttonClose = popupView.findViewById(R.id.closeButton);
        buttonHelpDailyReward = popupView.findViewById(R.id.btn_Help);

        linearDailyReward = popupView.findViewById(R.id.linearDailyReward);
        linearDay1 = popupView.findViewById(R.id.linearDay1);
        linearDay2 = popupView.findViewById(R.id.linearDay2);
        linearDay3 = popupView.findViewById(R.id.linearDay3);
        linearDay4 = popupView.findViewById(R.id.linearDay4);
        linearDay5 = popupView.findViewById(R.id.linearDay5);
        linearDay6 = popupView.findViewById(R.id.linearDay6);
        linearDay7 = popupView.findViewById(R.id.linearDay7);

        ViewDay1 = popupView.findViewById(R.id.textViewDay1);
        ViewDay2 = popupView.findViewById(R.id.textViewDay2);
        ViewDay3 = popupView.findViewById(R.id.textViewDay3);
        ViewDay4 = popupView.findViewById(R.id.textViewDay4);
        ViewDay5 = popupView.findViewById(R.id.textViewDay5);
        ViewDay6 = popupView.findViewById(R.id.textViewDay6);
        ViewDay7 = popupView.findViewById(R.id.textViewDay7);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayString = year + "" + month + "" + day + "";
        prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        currentday = prefs.getBoolean(todayString, false);
        firstDay = prefs.getBoolean("firstDay", false);
        secondDay = prefs.getBoolean("secondDay", false);
        thirdDay = prefs.getBoolean("thirdDay", false);
        fourthDay = prefs.getBoolean("fourthDay", false);
        fifthDay = prefs.getBoolean("fifthDay", false);
        sixthDay = prefs.getBoolean("sixthDay", false);
        seventhDay = prefs.getBoolean("seventhDay", false);
        getocoins = prefs.getInt("ocoins", 0);
        getTotalOcoins = prefs.getInt("totalOcoins", 0);
        getpoints = prefs.getInt("points", 0);

        Random rand = new Random();
        int mysteryNum = rand.nextInt((800 - 100) + 1) + 100;

        int firstRewards = 50;
        int secondRewardsOcoin = 100;
        int thirdRewardsOcoin = 150;
        int fourthRewardsOcoin = 200;
        int fifthRewardsOcoin = 300;
        int sixthRewardsOcoin = 400;
        int seventhRewardsOcoin = mysteryNum;

        String claimed = "CLAIMED!";

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );
        currentDate = Utils.formatDate(attempt.getCreatedTime());
        title = "Daily Reward";
        createdTime = currentDate;

        //Daily reward
        if (!currentday && isZoneAutomatic(view.getContext()) && isTimeAutomatic(view.getContext())) { //currentday =false
            linearDailyReward.setVisibility(View.VISIBLE);
            SoundPoolManager.playSound(1);
            if (!firstDay) {
                ViewDay1.setText("TODAY");
                ViewDay2.setText("TOMORROW");
                ViewDay3.setText("DAY 3");
                ViewDay4.setText("DAY 4");
                ViewDay5.setText("DAY 5");
                ViewDay6.setText("DAY 6");
                ViewDay7.setText("DAY 7");
            } else if (!secondDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText("TODAY");
                ViewDay3.setText("TOMORROW");
                ViewDay4.setText("DAY 3");
                ViewDay5.setText("DAY 4");
                ViewDay6.setText("DAY 5");
                ViewDay7.setText("DAY 6");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            } else if (!thirdDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText(claimed);
                ViewDay3.setText("TODAY");
                ViewDay4.setText("TOMORROW");
                ViewDay5.setText("DAY 3");
                ViewDay6.setText("DAY 4");
                ViewDay7.setText("DAY 5");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay2.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            } else if (!fourthDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText(claimed);
                ViewDay3.setText(claimed);
                ViewDay4.setText("TODAY");
                ViewDay5.setText("TOMORROW");
                ViewDay6.setText("DAY 3");
                ViewDay7.setText("DAY 4");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay2.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay3.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            } else if (!fifthDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText(claimed);
                ViewDay3.setText(claimed);
                ViewDay4.setText(claimed);
                ViewDay5.setText("TODAY");
                ViewDay6.setText("TOMORROW");
                ViewDay7.setText("DAY 3");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay2.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay3.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay4.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            } else if (!sixthDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText(claimed);
                ViewDay3.setText(claimed);
                ViewDay4.setText(claimed);
                ViewDay5.setText(claimed);
                ViewDay6.setText("TODAY");
                ViewDay7.setText("TOMORROW");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay2.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay3.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay4.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay5.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            } else if (!seventhDay) {
                ViewDay1.setText(claimed);
                ViewDay2.setText(claimed);
                ViewDay3.setText(claimed);
                ViewDay4.setText(claimed);
                ViewDay5.setText(claimed);
                ViewDay6.setText(claimed);
                ViewDay7.setText("TODAY");

                linearDay1.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay2.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay3.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay4.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay5.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
                linearDay6.setForeground(AppCompatResources.getDrawable(view.getContext(), R.drawable.done));
            }
            claimButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SoundPoolManager.playSound(1);
                    mAuth = FirebaseAuth.getInstance();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    uidRef = databaseReference.child("UserData").child(uid);
                    // saving the date
                    SharedPreferences.Editor edt = prefs.edit();
                    edt.putBoolean(todayString, true);

                    if (!firstDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+firstRewards+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + firstRewards);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + firstRewards);
                        descriptions = "Earned "+firstRewards+" ocoins";
                        edt.putBoolean("firstDay", true);
                        edt.putBoolean("secondDay", false);
                    } else if (!secondDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+secondRewardsOcoin+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + secondRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+secondRewardsOcoin+" ocoins";
                        edt.putBoolean("secondDay", true);
                        edt.putBoolean("thirdDay", false);
                    } else if (!thirdDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+thirdRewardsOcoin+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + thirdRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+thirdRewardsOcoin+" ocoins";
                        edt.putBoolean("thirdDay", true);
                        edt.putBoolean("fourthDay", false);
                    } else if (!fourthDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+fourthRewardsOcoin+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + fourthRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+fourthRewardsOcoin+" ocoins";
                        edt.putBoolean("fourthDay", true);
                        edt.putBoolean("fifthDay", false);
                    } else if (!fifthDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+fifthRewardsOcoin+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + fifthRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+fifthRewardsOcoin+" ocoins";
                        edt.putBoolean("fifthDay", true);
                        edt.putBoolean("sixthDay", false);
                    } else if (!sixthDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+sixthRewardsOcoin+" ocoins", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + sixthRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+sixthRewardsOcoin+" ocoins";
                        edt.putBoolean("sixthDay", true);
                        edt.putBoolean("seventhDay", false);
                    } else if (!seventhDay) {
                        Toast.makeText(view.getContext(), "Successfully Claimed "+seventhRewardsOcoin+" ocoins on Mystery Box", Toast.LENGTH_SHORT).show();
                        uidRef.child("Ocoin").setValue(getocoins + seventhRewardsOcoin);
                        uidRef.child("TotalOcoins").setValue(getTotalOcoins + secondRewardsOcoin);
                        descriptions = "Earned "+seventhRewardsOcoin+" ocoins";
                        edt.putBoolean("seventhDay", true);
                        edt.putBoolean("firstDay", false);
                    }
                    edt.apply();
                    SaveHistory();
                    popupWindow.dismiss();
                }
            });
        } else {
            Toast.makeText(view.getContext(), "Your daily prize has already been claimed!", Toast.LENGTH_LONG).show();
            SoundPoolManager.playSound(0);
            linearDay1.setVisibility(View.GONE);
            linearDay2.setVisibility(View.GONE);
            linearDay3.setVisibility(View.GONE);
            linearDay4.setVisibility(View.GONE);
            linearDay5.setVisibility(View.GONE);
            linearDay6.setVisibility(View.GONE);
            linearDay7.setVisibility(View.GONE);
            claimButton.setEnabled(false);
            claimButton.setText("Come back tomorrow");
            //popupWindow.dismiss();
        }
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
            }
        });
        buttonHelpDailyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("OMOSHIRIO DAILY REWARD")
                        .setMessage("You can collect your daily prize every day." +
                                "\nNote: If you log out or clear the app's data, the daily prize will be reset to starting point (50 ocoins).")
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setCancelable(false)
                        .setPositiveButton("Okay", (dialog, which) -> {
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isZoneAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }
    private  void SaveHistory(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
}
