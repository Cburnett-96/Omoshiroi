package com.example.myomoshiroi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myomoshiroi.GrammarCheck.*;
import com.example.myomoshiroi.SpellSearch.*;
import com.example.myomoshiroi.other.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class TestingAlgorithm extends AppCompatActivity {
    // Build new root
    public static Dictionary root = new Dictionary();
    public static HashSet<String> set = new HashSet<>();
    public Bigram n;
    public BuildRule rule;
    LinearLayout linearProgress;
    ImageView loading;
    private EditText speechText;
    Button loadData, spell, grammar, clear;

    public static int SPLASH_TIME_OUT = 1000;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_testing_algorithm);
        linearProgress = findViewById(R.id.linearLoadingProgress);
        loading = findViewById(R.id.loading);
        loadData = findViewById(R.id.RECORD_btn);
        spell = findViewById(R.id.CK_btn);
        grammar = findViewById(R.id.CORRECT_btn);
        clear = findViewById(R.id.CLEAR_btn);

        // Set notation label
        TextView note = findViewById((R.id.Note));
        note.setText(Html.fromHtml("<b><font color='red'>" + "SPELL ISSUE" + "</font></b>" + "  |  " + "<font color='blue'>" + "GRAMMAR ISSUE" + "</font>"));

        speechText = findViewById(R.id.input);
    }
    public void btn_check(View v){
        // Dismiss keyboard on Button Click
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Get input and processed, then print to output
        EditText input = findViewById(R.id.input);
        TextView output = findViewById((R.id.output));
        TextView label = findViewById(R.id.tip);

        String instr = input.getText().toString();
        instr = instr.replaceAll("[^a-zA-Z0-9]", " ");

        if(instr.length() > 0) {
            // Spell Check for user input
            checker check = new checker(new StringBuilder(instr), root, n);
            check.SpellCheck();
            HashMap<Integer,String> map = check.Correction;
            StringBuilder sb  = new StringBuilder();
            int cnt = 0;

            String[] strs = instr.split("\\s+");
            for (String str : strs) {
                if(map.containsKey(cnt++)) // Word along with current index has Spell Issues
                {
                    if(!Objects.equals(map.get(cnt - 1), ""))
                    {
                        sb.append("<b><font color='red'>").append(map.get(cnt - 1)).append("</font></b>").append(" ");
                    }else{
                        sb.append("<font color='red'>" + "⚠⚠⚠" + "</font>" + " ");
                    }
                }else {
                    sb.append(str).append(" ");
                }
            }
            if(map.size() > 0)
            {
                label.setText("Did you mean...?"); // Means there is Spell Issues
            }else{
                label.setText("Looks like all good...!"); // Means there is NOT Spell Issues
            }
            output.setText(Html.fromHtml(sb.toString()));
        }else{
            label.setText("");
            output.setText("Did you really type anything...?");
        }
    }

    public void btn_correct(View v){
        // Dismiss keyboard on Button Click
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Get input and processed, then print to output
        EditText input = findViewById(R.id.input);
        TextView output = findViewById((R.id.output));
        TextView label = findViewById(R.id.tip);

        label.setText("");

        String instr = input.getText().toString();
        instr = instr.replaceAll("[^a-zA-Z0-9]", " ");

        if(instr.length() > 0)
        {
            checker check = new checker(new StringBuilder(instr), root, n);
            check.SpellCheck();
            HashMap<Integer,String> map = check.Correction;

            if(check.GrammarCheck)
            {
                ArrayList<Double> GrammarCount = rule.getCount(check.original);
                ArrayList<ArrayList<Integer>> GrammarSuspect = new ArrayList<>();
                Double preCount = GrammarCount.get(0);
                ArrayList<Integer> inner = new ArrayList<>();
                boolean connection = false;
                if(preCount == 0.0)
                    inner.add(0);
                for(int i = 1; i< GrammarCount.size();i++){
                    if(GrammarCount.get(i) < 3.0){
                        if(preCount < 3.0){
                            if(inner.size()==3 && !connection )
                                inner.remove(0);
                            inner.add(i);
                            connection = true;
                        }
                        else
                        {
                            connection = false;
                            inner = new ArrayList<>();
                            if(i-2 >= 0) inner.add(i-2);
                            inner.add(i-1);
                            inner.add(i);
                        }
                    }
                    else{
                        if(!inner.isEmpty())
                            GrammarSuspect.add(new ArrayList<>(inner));
                        inner = new ArrayList<>();
                        connection = false;
                    }
                    preCount = GrammarCount.get(i);
                }
                if(!inner.isEmpty())
                    GrammarSuspect.add(new ArrayList<>(inner));

                if(GrammarSuspect.isEmpty()) {
                    if(map.size() > 0) {
                        output.setText("Grammar seems contain wrong spelling");
                    }else {
                        output.setText("Grammar seems good...");
                    }
                }
                else{
                    HashSet<Integer> indexset = new HashSet<>();
                    for(ArrayList<Integer> arls : GrammarSuspect)
                    {
                        indexset.addAll(arls);
                    }
                    System.out.println(indexset + "MAY GRAMMAR ERRORS");
                    // Highlight Grammar Issues
                    StringBuilder sb = new StringBuilder();
                    int cnt = 0;
                    String[] strs = instr.split("\\s+");
                    for (String str : strs) {
                        if(indexset.contains(cnt++)) // Word along with current index has Spell Issues
                        {
                            sb.append("<font color='blue'>").append(str).append("</font>").append(" ");
                        }else {
                            sb.append(str).append(" ");
                        }
                    }
                    label.setText("Grammar seems NOT good...");
                    output.setText(Html.fromHtml(sb.toString()));
                }
            }
        }else{
            output.setText("Did you really type anything...?");
        }
    }

    public void btn_clear(View v){
        // Dismiss keyboard on Button Click
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Get input and processed, then print to output
        EditText input = findViewById(R.id.input);
        TextView output = findViewById((R.id.output));
        TextView label = findViewById(R.id.tip);

        label.setText("");

        if(input.getText().toString().length() > 0)
        {
            ((EditText) findViewById(R.id.input)).getText().clear();
            output.setText("Did you mean...?");
        }else {
            output.setText("It is empty already...");
        }
    }
    public void btn_record(View v){
        linearProgress.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.raw.splash_screen_loading)
                .into(loading);
        loadData.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Read big text into Dictionary
                try {
                    System.out.println("STARTING LOAD THE WORD TAGGED DATA");
                    BufferedReader readerone = new BufferedReader(new InputStreamReader(getAssets().open("word_tagged.txt")));
                    String mLine = readerone.readLine();
                    while (mLine != null) {
                        String[] str = mLine.split("\\_");
                        root.insert(str[0],str[1]);
                        mLine = readerone.readLine();
                    }
                    readerone.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                // Read from dataset for training
                try {
                    System.out.println("STARTING LOAD THE GRAMMAR DATA");
                    BufferedReader readertwo = new BufferedReader(new InputStreamReader(getAssets().open("grammar.txt")));
                    String Line = readertwo.readLine();
                    int count = 0;
                    while (Line != null) {
                        String[] str = Line.split(",");
                        set.addAll(Arrays.asList(str));
                        count++;
                        Line = readertwo.readLine();
                        if(count == 20000)
                            break;
                    }
                    readertwo.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                n = new Bigram(set);
                n.train();
                System.out.println(n.BigramCount.size() + " TOTAL OF BIGRAM");

                rule = new BuildRule(set,3,root);
                rule.train();

                linearProgress.setVisibility(View.GONE);
                Glide.with(v).clear(loading);

                Toast.makeText(TestingAlgorithm.this, "Dataset has been added.", Toast.LENGTH_SHORT).show();
                spell.setEnabled(true);
                grammar.setEnabled(true);
                clear.setEnabled(true);
            }
        },SPLASH_TIME_OUT);

    }
}