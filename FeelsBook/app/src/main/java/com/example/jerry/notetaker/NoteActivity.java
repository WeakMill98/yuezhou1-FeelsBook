package com.example.jerry.notetaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

public class NoteActivity extends AppCompatActivity {

    int emotionId;
    String emotionName;
    String fearName = "Fear";
    String joyName = "Joy";
    String loveName = "Love";
    String surpriseName = "Surprise";
    String angerName = "Anger";
    String sadnessName = "Sadness";
    private Spinner emotionSpinner;
    private Date currentDate = new Date(System.currentTimeMillis());
    Button saveButton;
    Button cancelButton;
    Button deleteButton;
    EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Get the Intent, as well as the comment field: edit text
        final EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();

        // Get the id of the object you are using
        // Get a reference to the spinner
        // Get the save, delete and cancel buttons
        emotionId = intent.getIntExtra("emotionId", -1);
        emotionName = intent.getStringExtra("emotionName");
        emotionSpinner = findViewById(R.id.emotionSpinner);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Get the edit text input box
        timeEditText = findViewById(R.id.timeEditText);

        // Populate the spinner, the comment section. and the date
        // This will vary based on whether the user clicked on an existing
        // emotion or decided to add in a new one
        List<Emotion> singleList = new ArrayList<>();
        if (emotionId != -1){
            Emotion emotion = MainActivity.emotionsArrayList.get(emotionId);
            singleList.add(emotion);
            timeEditText.setText(formatDateToISO());
            editText.setText(MainActivity.emotionsArrayList.get(emotionId).getComment());
        }

        // If we are adding in a new emotion
        // Firstly check the name of the emotion, that way we know which class to create
        // When we find a match, we can populate the spinner as a view for the user
        // We can also reference this object later from the spinner
        else{
            List<Emotion> allEmotions = populateAllEmotions();
            for (int i = 0; i< allEmotions.size(); i ++) {
                if (allEmotions.get(i).getEmotionName().equals(emotionName)) {
                singleList.add(allEmotions.get(i));
                }
            }
        }

        ArrayAdapter<Emotion> adapter = populateSpinner(singleList);
        emotionSpinner.setAdapter(adapter);

        // When the save Button is pressed
        // Will save all the changes
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();

                if (emotionId != -1){
                    // If the object is an existing one
                    // Simply update the time and the comment
                    // DO NOT let the user change the actual emotion
                    Emotion emotion = MainActivity.emotionsArrayList.get(emotionId);
                    emotion.setComment(comment);
                    saveData();
                }
                openMainActivity();
            }
        });

        // Set an on click for canceling
        // Will not save anything, only sends the viewer to the main activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        // Set a listener for delete
        // Will only delete an object that already exists, otherwise, it will just redirect to main
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emotionId != -1){
                    MainActivity.emotionsArrayList.remove(MainActivity.emotionsArrayList.get(emotionId));
                    saveData();
                }
                openMainActivity();
            }
        });
    }

    // Used to open the main Activity
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Formats the current Date to comply with iso 8601
    // Returns that as a string
    public String formatDateToISO(){
        //String tzStr = Calendar.getInstance().getTimeZone().getDisplayName();
        TimeZone tz = TimeZone.getTimeZone("MST");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    // Adds all possible emotions to a list
    // returns that list as output
    // Called only when a NEW emotion instance is added
    private List<Emotion> populateAllEmotions(){
        List<Emotion> emotionList = new ArrayList<>();
        Joy joy = new Joy(currentDate);
        Fear fear = new Fear(currentDate);
        emotionList.add(joy);
        emotionList.add(fear);
        return emotionList;
    }

    // Populates the spinner
    // List of emotions as input and an array adapter of those emotions as the output
    private ArrayAdapter<Emotion> populateSpinner(List<Emotion> emotionList){
        ArrayAdapter<Emotion> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, emotionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    // Checks to see if the Date time is valid
    public Boolean checkValidDate(){

        return true;
    }

    // Called to save changes
    // Code source listed in readme
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.emotionsArrayList);
        editor.putString("task list", json);
        editor.apply();
    }
}