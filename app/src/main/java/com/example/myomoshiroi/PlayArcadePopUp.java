package com.example.myomoshiroi;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class PlayArcadePopUp {

    //PopupWindow display method
    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.arcade_choice, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        Button buttonTopic = popupView.findViewById(R.id.topicDifficultyButton);
        Button buttonStory = popupView.findViewById(R.id.storymodeButton);
        Button buttonClose = popupView.findViewById(R.id.closeButton);
        Button help = popupView.findViewById(R.id.btn_HelpPlayArcade);

        buttonTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("TOPIC DIFFICULTY")
                        .setMessage("On each question you have multiple choice, verb tense, flash card, " +
                                "fill in the blank, guest the image and construct a sentence to answer, earn points " +
                                "and ocoins on correct answer at the end you will show your summary result.")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                popupWindow.dismiss();
                                PlayArcadePopUp popUpClass = new PlayArcadePopUp();
                                popUpClass.showPopupTopic(view);
                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();

            }
        });
        buttonStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();

            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("Play Arcade Guide")
                        .setMessage("Topic Difficulty: On each question you have multiple choice, " +
                                "verb tense, flash card, fill in the blank, guest the image and construct a sentence " +
                                "to answer, earn points and ocoins on correct answer at the end you will show your " +
                                "summary result.\n" +
                                "\nStory Mode: ???")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }

    public void showPopupTopic(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater1 = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupViewTopic = inflater1.inflate(R.layout.arcade_difficulty, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupTopic = new PopupWindow(popupViewTopic, width, height, true);

        //Set the location of the window on the screen
        popupTopic.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button buttonEasy = popupViewTopic.findViewById(R.id.easyButton);
        Button buttonMedium = popupViewTopic.findViewById(R.id.mediumButton);
        Button buttonHard = popupViewTopic.findViewById(R.id.hardButton);
        Button buttonClose = popupViewTopic.findViewById(R.id.closeButton);
        Button help = popupViewTopic.findViewById(R.id.btn_HelpTopicDifficulty);

        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("Easy Mode")
                        .setMessage("On this mode you randomly answers (Verb Tense or complete the sentence) " +
                                "fill in the blank (Each correct answers equivalent of 20 points and 5 Ocoins " +
                                "also for incorrect answers you will minus points of 5) Good luck!")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), EasyTopicActivity.class);
                                view.getContext().startActivity(intent);
                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();

            }
        });
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();
            }
        });
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTopic.dismiss();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
                alertExit.setTitle("Topic Difficulty Guide")
                        .setMessage("Easy Mode:\n" +
                                "On this mode you randomly answers (Verb Tense or complete the sentence) " +
                                "fill in the blank (Each correct answers equivalent of 20 points and 5 Ocoins " +
                                "also for incorrect answers you will minus points of 5) Good luck!\n" +
                                "\nMedium Mode: ???\n" +
                                "\n" +
                                "Hard Mode: ???")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }
}
