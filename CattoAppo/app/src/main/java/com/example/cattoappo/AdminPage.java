package com.example.cattoappo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPage extends AppCompatActivity {

    private Button btnInsertCat, btnDeleteCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpage);

        btnInsertCat = findViewById(R.id.button3);
        btnDeleteCat = findViewById(R.id.button4);

        btnInsertCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminPage.this, InsertCatActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, DeleteCat.class);
                startActivity(intent);
            }
        });
    }
}
