package com.example.myomoshiroi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.example.myomoshiroi.other.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

public class EditProfile {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    SharedPreferences prefs;

    ImageView changeAvatar;
    String avatarName;
    Button buttonSave, buttonChange;
    RadioButton radioMale, radioFemale;
    EditText editName, editLRN;
    TextView Email;
    String email;

    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;

    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.edit_profile, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        changeAvatar = popupView.findViewById(R.id.changeAvatar);
        buttonSave = popupView.findViewById(R.id.saveButton);
        buttonChange = popupView.findViewById(R.id.changeButton);
        radioFemale = popupView.findViewById(R.id.rFemale);
        radioMale = popupView.findViewById(R.id.rMale);
        editName = popupView.findViewById(R.id.editFullname);
        editLRN = popupView.findViewById(R.id.editLRN);
        Email = popupView.findViewById(R.id.email);

        prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        avatarName = prefs.getString("avatarName",null);
        FirebaseAuth();
        GetAvatarName();

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );
        currentDate = Utils.formatDate(attempt.getCreatedTime());
        title = "Profile Changes";
        createdTime = currentDate;

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

        //Change Avatar Image
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                SoundPoolManager.playSound(1);
                Intent intent = new Intent(view.getContext(), ChangeAvatarActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        //Save button function for updating user information
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                if(TextUtils.isEmpty(editName.getText().toString()) | TextUtils.isEmpty(editLRN.getText().toString())){
                    Toast.makeText(view.getContext(), "Fill-up some field", Toast.LENGTH_LONG).show();
                }
                else {
                    if (radioFemale.isChecked()){
                        uidRef.child("Gender").setValue("Female");
                    }else {
                        uidRef.child("Gender").setValue("Male");
                    }
                    descriptions = "You've made a change to your profile information.";
                    SaveHistory();
                    uidRef.child("Fullname").setValue(editName.getText().toString());
                    uidRef.child("LRN").setValue(editLRN.getText().toString());
                    popupWindow.dismiss();
                }

            }
        });
        //Change button function for updating user password
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request to Change the Password with confirmation
                SoundPoolManager.playSound(0);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("Change Password")
                        .setMessage("Are you sure, want to change your Password?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        descriptions = "You requested that your password be changed.";
                                        SaveHistory();
                                        Toast.makeText(view.getContext(), "Proceed to your Email to change PASSWORD", Toast.LENGTH_LONG).show();
                                        popupWindow.dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }
    private void FirebaseAuth(){
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);

        user = mAuth.getCurrentUser();
        // Getting user information into EditText box
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("Fullname").getValue()).toString();
                email = Objects.requireNonNull(snapshot.child("EmailId").getValue()).toString();
                String lrn = Objects.requireNonNull(snapshot.child("LRN").getValue()).toString();
                String gender = Objects.requireNonNull(snapshot.child("Gender").getValue()).toString();
                editName.setHint(name);
                editLRN.setHint(lrn);
                Email.setText(email);
                if (gender.equals("Female")){
                    radioFemale.setChecked(true);
                }else{
                    radioMale.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
    private void GetAvatarName(){
        switch (avatarName) {
            case "avatar_bulb": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_bulb);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cactus": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_cactus);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_chick": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_chick);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cup": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_cup);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_hat": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_hat);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_pinaple": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_pinaple);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_android": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_android);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cliprobot": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_cliprobot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_head": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_head);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_monitor": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_monitor);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_robot": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_robot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_skull": {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.avatar_skull);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = changeAvatar.getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
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
