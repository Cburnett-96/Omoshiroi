package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserData;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.model.UserItem;
import com.example.myomoshiroi.model.UserMission;
import com.example.myomoshiroi.other.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private EditText edit_txt_Fullname, edit_txt_LRN, edit_txt_Email, edit_txt_Pass, edit_txt_CoPass;
    com.google.android.material.textfield.TextInputLayout passwordHint, coPassHint, emailHint;
    private RadioButton radioMale, radioFemale;
    Button button_register;
    TextView text_view_login;
    LinearLayout linearProgress, linearLayout;

    DatabaseReference databaseReference, uidRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    SharedPreferences prefs;

    String fullname, lrn, email, password, co_password, avatar;
    boolean Andry, Bulby, Cackytus, Chicky, Clibot, Cupies, Hatty, Headyglass, Jobot, Monisad, Pineglass, Skullyhead;
    int level, point, coin, totalocoins;
    int timerAdd, timerStop, timerFreeze;
    int easyCount;
    String gender = "";

    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup);
        //        get all view id from XML
        linearProgress = findViewById(R.id.linearLoadingProgress);
        linearLayout = findViewById(R.id.linearLayout);
        edit_txt_Fullname = findViewById(R.id.edit_txt_Fullname);
        edit_txt_LRN = findViewById(R.id.edit_txt_LRN);
        edit_txt_Email = findViewById(R.id.edit_txt_Email);
        emailHint = findViewById(R.id.edit_txt_EmailHint);
        edit_txt_Pass = findViewById(R.id.edit_txt_Pass);
        edit_txt_CoPass = findViewById(R.id.edit_txt_CoPass);
        passwordHint = findViewById(R.id.edit_text_PasswordHint);
        coPassHint = findViewById(R.id.edit_text_CoPasswordHint);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        text_view_login = findViewById(R.id.text_view_login);
        button_register = findViewById(R.id.button_register);
        //starting level, point and coin value
        avatar = "profile";
        level = 1;
        point = 300;
        coin = 30;
        totalocoins = 30;

        //starting mission items
        easyCount = 0;

        //starting items of the new user
        Andry = false;
        Bulby = false;
        Cackytus = false;
        Chicky = false;
        Clibot = false;
        Cupies = false;
        Hatty = false;
        Headyglass = false;
        Jobot = false;
        Monisad = false;
        Pineglass = false;
        Skullyhead = false;
        timerAdd = 1;
        timerStop = 1;
        timerFreeze = 1;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");

        //        History handle
        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );
        currentDate = Utils.formatDate(attempt.getCreatedTime());
        title = "Account Registration";
        descriptions = "You've created your omoshiroi account and earned initial reward 300 experience points and 30 Ocoins " +
                "with extra power-ups that can use in-game.";
        createdTime = currentDate;

        ValidateUserInput();

        text_view_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //        handle user SignUp button
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateFullname() | !validateLRN() | !validateEmail() | !validatePassword() | checkUserGender()) {
                    return;
                }

                if (password.equals(co_password)) {
                    //    progressbar VISIBLE
                    linearProgress.setVisibility(View.VISIBLE);

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    uidRef = databaseReference.child("UserData");
                    Query query = uidRef.orderByChild("EmailId").equalTo(email);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                                        (new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {

                                                    UserData data = new UserData(fullname, lrn, email, gender, level, point, coin, avatar, totalocoins);

                                                    FirebaseDatabase.getInstance().getReference("UserData")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).
                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    SharedPreferences.Editor editor = prefs.edit();
                                                                    editor.putString("avatarName", "profile");
                                                                    editor.putString("music setting", "on");
                                                                    editor.putString("sound setting", "on");
                                                                    editor.putString("walkthrough", "on");
                                                                    editor.apply();

                                                                    //    progressbar GONE
                                                                    linearProgress.setVisibility(View.GONE);
                                                                    linearLayout.setVisibility(View.GONE);
                                                                    Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                                                                    alert.setTitle("WELCOME TO OMOSHIROI!")
                                                                            .setMessage("First and foremost, thank you for signing up. For a new member, the initial reward are 300 experience points and 30 ocoins " +
                                                                                    "with extra power-ups that can use in-game.\n" +
                                                                                    "Good luck, and enjoy yourself while playing the game.")
                                                                            .setIcon(android.R.drawable.ic_menu_info_details)
                                                                            .setCancelable(false)
                                                                            .setPositiveButton("Okay!", (dialog, which) -> {
                                                                                SaveItems();
                                                                                SaveMission();
                                                                                SaveHistory();
                                                                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            });
                                                                    AlertDialog dialog = alert.create();
                                                                    dialog.show();
                                                                }
                                                            });
                                                } else {
                                                    //    progressbar GONE
                                                    linearProgress.setVisibility(View.GONE);
                                                    Toast.makeText(SignupActivity.this, "Connection Error!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                //    progressbar GONE
                                linearProgress.setVisibility(View.GONE);
                                Toast.makeText(SignupActivity.this, "Your email address is already in use!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);
                } else {
                    Toast.makeText(SignupActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ValidateUserInput(){
        edit_txt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_txt_Email.getText().toString().trim())) {
                    emailHint.setHelperText("*Enter Your Email Address");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_txt_Email.getText().toString().trim()).matches()) {
                    emailHint.setHelperText("*Please Enter Valid Email Address");
                } else {
                    emailHint.setHelperTextEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_txt_Pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_txt_Pass.length() >= 8){
                    passwordHint.setHelperTextEnabled(false);
                } else
                {
                    passwordHint.setHelperText("*Password must be at least 8 characters");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_txt_CoPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_txt_Pass.getText().toString().trim().equals(edit_txt_CoPass.getText().toString().trim())){
                    coPassHint.setHelperTextEnabled(false);
                } else
                {
                    coPassHint.setHelperText("*Password didn't match");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean validateFullname() {
        fullname = edit_txt_Fullname.getText().toString().trim();
        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(SignupActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLRN() {
        lrn = edit_txt_LRN.getText().toString().trim();
        if (TextUtils.isEmpty(lrn)) {
            Toast.makeText(SignupActivity.this, "Enter Your LRN or Student Number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        email = edit_txt_Email.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignupActivity.this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Please enter valid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        password = edit_txt_Pass.getText().toString().trim();
        co_password = edit_txt_CoPass.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(co_password)) {
            Toast.makeText(SignupActivity.this, "Enter Your Co-Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() <= 7) {
            Toast.makeText(SignupActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkUserGender() {

        if (radioMale.isChecked()) {
            gender = "Male";
            return false;

        }
        if (radioFemale.isChecked()) {
            gender = "Female";
            return false;

        } else {
            Toast.makeText(SignupActivity.this, "Select Your Gender", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void SaveItems() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserItem");
        UserItem items = new UserItem(Andry, Bulby, Cackytus, Chicky, Clibot, Cupies, Hatty, Headyglass, Jobot,
                Monisad, Pineglass, Skullyhead, timerAdd, timerFreeze, timerStop);
        FirebaseDatabase.getInstance().getReference("UserItem")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(items);
    }

    private void SaveMission() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserMission");
        UserMission mission = new UserMission(easyCount);
        FirebaseDatabase.getInstance().getReference("UserMission")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mission);
    }

    private void SaveHistory() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
}