package com.example.myomoshiroi;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile {

    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Button buttonSave, buttonChange;
    RadioButton radioMale, radioFemale;
    EditText editName, editLRN;
    TextView Email;
    String email;

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
        buttonSave = popupView.findViewById(R.id.saveButton);
        buttonChange = popupView.findViewById(R.id.changeButton);
        radioFemale = popupView.findViewById(R.id.rFemale);
        radioMale = popupView.findViewById(R.id.rMale);
        editName = popupView.findViewById(R.id.editFullname);
        editLRN = popupView.findViewById(R.id.editLRN);
        Email = popupView.findViewById(R.id.email);

        FirebaseAuth();

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

        //Save button function for updating user information
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editName.getText().toString()) | TextUtils.isEmpty(editLRN.getText().toString())){
                    Toast.makeText(view.getContext(), "Fill-up some field", Toast.LENGTH_LONG).show();
                }
                else {
                    if (radioFemale.isChecked()){
                        uidRef.child("Gender").setValue("Female");
                    }else {
                        uidRef.child("Gender").setValue("Male");
                    }
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
}
