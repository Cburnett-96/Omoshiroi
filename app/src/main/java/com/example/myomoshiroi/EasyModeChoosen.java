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

import com.example.myomoshiroi.other.Constants;
import com.example.myomoshiroi.other.SoundPoolManager;

public class EasyModeChoosen {
    //PopupWindow display method
    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_choosen, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        Button btn_Tenses = popupView.findViewById(R.id.btn_Tenses);
        Button btn_TorF = popupView.findViewById(R.id.btn_TorF);
        Button btn_MultipleChoice = popupView.findViewById(R.id.btn_MultipleChoice);
        Button btn_Spelling = popupView.findViewById(R.id.btn_Spelling);
        Button btn_Matching = popupView.findViewById(R.id.btn_Matching);
        Button btn_Close = popupView.findViewById(R.id.btn_close);
        Button btn_help = popupView.findViewById(R.id.btn_HelpEasyMode);

        btn_Tenses.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            EasyModeChoosen popUp = new EasyModeChoosen();
            popUp.showPopupTensesMode(v);
        });
        btn_TorF.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            EasyModeChoosen popUp = new EasyModeChoosen();
            popUp.showPopupTorFMode(v);
        });
        btn_MultipleChoice.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            EasyModeChoosen popUp = new EasyModeChoosen();
            popUp.showPopupMultipleMode(v);
        });
        btn_Spelling.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            EasyModeChoosen popUp = new EasyModeChoosen();
            popUp.showPopupSpellingMode(v);
        });
        btn_Matching.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            EasyModeChoosen popUp = new EasyModeChoosen();
            popUp.showPopupMatchingMode(v);
        });

        btn_Close.setOnClickListener(v -> popupWindow.dismiss());
        btn_help.setOnClickListener(v -> {
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

    public void showPopupMultipleMode(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_multiple_choice_stages, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button btn_Back = popupView.findViewById(R.id.btn_back);
        Button btn_Stage1 = popupView.findViewById(R.id.btn_Stage1);
        Button btn_Stage2 = popupView.findViewById(R.id.btn_Stage2);
        Button btn_Stage3 = popupView.findViewById(R.id.btn_Stage3);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btn_Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.multiple_stage_01));
                v.getContext().startActivity(intent);
            }
        });
        btn_Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(view.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, view.getContext().getString(R.string.multiple_stage_02));
                view.getContext().startActivity(intent);
            }
        });
        btn_Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(view.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, view.getContext().getString(R.string.multiple_stage_03));
                view.getContext().startActivity(intent);
            }
        });
    }
    public void showPopupTensesMode(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_tense_stages, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button btn_Back = popupView.findViewById(R.id.btn_back);
        Button btn_Stage1 = popupView.findViewById(R.id.btn_Stage1);
        Button btn_Stage2 = popupView.findViewById(R.id.btn_Stage2);
        Button btn_Stage3 = popupView.findViewById(R.id.btn_Stage3);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btn_Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.tenses_stage_01));
                v.getContext().startActivity(intent);
            }
        });
        btn_Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(view.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, view.getContext().getString(R.string.tenses_stage_02));
                view.getContext().startActivity(intent);
            }
        });
        btn_Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void showPopupTorFMode(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_torf_stages, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button btn_Back = popupView.findViewById(R.id.btn_back);
        Button btn_Stage1 = popupView.findViewById(R.id.btn_Stage1);
        Button btn_Stage2 = popupView.findViewById(R.id.btn_Stage2);
        Button btn_Stage3 = popupView.findViewById(R.id.btn_Stage3);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btn_Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.torf_stage_01));
                v.getContext().startActivity(intent);
            }
        });
        btn_Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void showPopupMatchingMode(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_matching_stages, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button btn_Back = popupView.findViewById(R.id.btn_back);
        Button btn_Stage1 = popupView.findViewById(R.id.btn_Stage1);
        Button btn_Stage2 = popupView.findViewById(R.id.btn_Stage2);
        Button btn_Stage3 = popupView.findViewById(R.id.btn_Stage3);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btn_Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.matching_stage_01));
                v.getContext().startActivity(intent);
            }
        });
        btn_Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void showPopupSpellingMode(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.easy_mode_spelling_stages, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        Button btn_Back = popupView.findViewById(R.id.btn_back);
        Button btn_Stage1 = popupView.findViewById(R.id.btn_Stage1);
        Button btn_Stage2 = popupView.findViewById(R.id.btn_Stage2);
        Button btn_Stage3 = popupView.findViewById(R.id.btn_Stage3);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                popupWindow.dismiss();
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btn_Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), EasyTopicActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.spelling_stage_01));
                v.getContext().startActivity(intent);
            }
        });
        btn_Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
