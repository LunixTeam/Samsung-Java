package ru.myitschool.mte;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tv_result);
        tvResult.setBackgroundColor(ContextCompat.getColor(this, R.color.normal));

        tvResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkNumber(s.toString());
            }
        });
    }

    private void checkNumber(String input) {
        try {
            int number = Integer.parseInt(input);

            if (number < 0) {
                tvResult.setBackgroundColor(ContextCompat.getColor(this, R.color.cautious));
            } else {
                tvResult.setBackgroundColor(ContextCompat.getColor(this, R.color.normal));
            }
        } catch (NumberFormatException e) {
            // Пустое тело
        }
    }
}