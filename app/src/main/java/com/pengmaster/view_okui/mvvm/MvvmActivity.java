package com.pengmaster.view_okui.mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pengmaster.view_okui.R;

public class MvvmActivity extends AppCompatActivity {

    EditText etHi;
    EditText etHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        etHi = findViewById(R.id.etHi);
        etHello = findViewById(R.id.etHello);

        new ViewModel(new ViewBinder(),etHi,etHello).load();
    }
}
