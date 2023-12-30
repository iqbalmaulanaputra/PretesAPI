package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText Inputname;
    private EditText palindrome;
    private Button btnCheck;

    private  Button btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Inputname = findViewById(R.id.name);
        palindrome = findViewById(R.id.polindrome);
        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPalindrome();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               TextInput();
            }
        });
        String savedName = sharedPreferences.getString("nama_key", "");
        Inputname.setText(savedName);
    }

    private void TextInput(){
        String name = Inputname.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama_key", name);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("nama", name); // Kirim data nama ke activity baru
        startActivity(intent);
    }

    private void checkPalindrome() {
        String name = Inputname.getText().toString();
        String inputString = palindrome.getText().toString().toLowerCase();

        inputString = inputString.replaceAll("\\s", "");


        boolean isPalindrom = isPalindrome(inputString);
        showResultDialog(name, isPalindrom);
    }

    private boolean isPalindrome(String str) {
        int start = 0;
        int end = str.length() - 1;

        while (start < end) {
            if (str.charAt(start) != str.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    private void showResultDialog(String name, boolean isPalindrom) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Palindrome");
        builder.setMessage( name +
                (isPalindrom ? " ini adalah palindrome" : " ini Bukan Palindrome"));
        builder.setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
