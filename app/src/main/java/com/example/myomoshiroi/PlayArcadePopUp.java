package com.example.myomoshiroi;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myomoshiroi.other.SoundPoolManager;

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

        buttonTopic.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            /*AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
            alertExit.setTitle("TOPIC DIFFICULTY")
                    .setMessage("Each question has multiple choice, verb tense, flash card, " +
                            "fill in the blank, guest the image and create a phrase to answer, " +
                            "get experience points and ocoins for right answers, and at the end you will see your summary result. ")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setCancelable(false)
                    .setPositiveButton("Okay", (dialog, which) -> {

                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();*/
            popupWindow.dismiss();
            PlayArcadePopUp popUpClass = new PlayArcadePopUp();
            popUpClass.showPopupTopic(view);
        });
        buttonStory.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();

        });
        buttonClose.setOnClickListener(v -> popupWindow.dismiss());
        help.setOnClickListener(v -> {
            AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
            alertExit.setTitle("Play Arcade Guide")
                    .setMessage("Topic Difficulty: Each question has multiple choice, verb tense, flash card, " +
                            "fill in the blank, guest the image and create a phrase to answer, " +
                            "get experience points and ocoins for right answers, and at the end you will see your summary result.\n" +
                            "\nStory Mode: ???")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, which) -> {

                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();
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

        buttonEasy.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            /*AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
            alertExit.setTitle("Easy Mode")
                    .setMessage("In this mode, you can randomly responses; verb tenses, or complete sentences " +
                            "with a time restriction of 1 minute, and each correct answer is worth 20 experience points " +
                            "and 5 ocoins; wrong answers are worth -5 points. Best wishes!")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setCancelable(false)
                    .setPositiveButton("Okay", (dialog, which) -> {

                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();*/
            popupTopic.dismiss();
            Intent intent = new Intent(view.getContext(), EasyTopicActivity.class);
            view.getContext().startActivity(intent);
        });
        buttonMedium.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();
        });
        buttonHard.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Toast.makeText(view.getContext(), "Under-development", Toast.LENGTH_SHORT).show();
        });
        buttonClose.setOnClickListener(v -> popupTopic.dismiss());
        help.setOnClickListener(v -> {
            AlertDialog.Builder alertExit = new AlertDialog.Builder(view.getContext());
            alertExit.setTitle("Topic Difficulty Guide")
                    .setMessage("Easy Mode:\n" +
                            "In this mode, you can randomly responses; verb tenses, or complete sentences " +
                            "with a time restriction of 1 minute and 30 seconds, and each correct answer is worth 20 experience points " +
                            "and 5 ocoins; wrong answers are worth -5 points. Best wishes!\n" +
                            "\nMedium Mode: ???\n" +
                            "\n" +
                            "Hard Mode: ???")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, which) -> {

                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();
        });
    }
}
