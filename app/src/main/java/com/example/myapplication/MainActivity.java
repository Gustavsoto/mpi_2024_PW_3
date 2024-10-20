package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int selectedTheme = sharedPreferences.getInt("selected_theme", R.style.Base_Theme_MyApplication); // Default theme
        setTheme(selectedTheme);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button saveButton = findViewById(R.id.saveButton);
        Button goToSecondButton = findViewById(R.id.goToSecondButton);

        Spinner themeSpinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        themeSpinner.setAdapter(adapter);
        int savedPosition = sharedPreferences.getInt("theme_spinner_position", 0);
        themeSpinner.setSelection(savedPosition);

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int themeToApply;
                switch (position) {
                    case 0: // Default or Light Theme
                    case 2:
                        themeToApply = R.style.Base_Theme_MyApplication;
                        break;
                    case 1: // Dark Theme
                        themeToApply = R.style.Base_Theme_MyApplication_Dark;
                        break;
                    default:
                        themeToApply = R.style.Base_Theme_MyApplication;
                }

                // Store selected theme in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("selected_theme", themeToApply);
                editor.putInt("theme_spinner_position", position);  // Save spinner position
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        TextInputEditText inputText = findViewById(R.id.inputText);
        String savedString = sharedPreferences.getString("entered_text", "No string has been stored"); // Default to empty string
        inputText.setText(savedString);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = String.valueOf(inputText.getText());
                saveToSharedPreferences(input);
                recreate();
            }
        });
        goToSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private void saveToSharedPreferences(String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("entered_text", value);
        editor.apply();
    }
}