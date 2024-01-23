package com.example.cattoappo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class catalogue extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogue);

        ImageButton Javanese = findViewById(R.id.cat1);
        ImageButton Persian = findViewById(R.id.cat2);
        ImageButton Munchkin = findViewById(R.id.cat3);
        ImageButton Siamese = findViewById(R.id.cat4);
        ImageButton Calico = findViewById(R.id.cat5);
        ImageButton Ragamuffin = findViewById(R.id.cat6);
        ImageButton Tabby = findViewById(R.id.cat7);
        ImageButton ScottishFold = findViewById(R.id.cat8);

        Javanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Javanese");
            }
        });

        Persian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Persian");
            }
        });

        Munchkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Munchkin");
            }
        });

        Siamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Siamese");
            }
        });

        Calico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Calico");
            }
        });

        Ragamuffin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Ragamuffin");
            }
        });

        Tabby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Tabby");
            }
        });

        ScottishFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSearchActivity("Scottish Fold");
            }
        });
    }

    private void startUserSearchActivity(String breed) {
        Intent intent = new Intent(catalogue.this, UserSearchActivity.class);
        intent.putExtra("breed", breed);
        startActivity(intent);
    }
}
