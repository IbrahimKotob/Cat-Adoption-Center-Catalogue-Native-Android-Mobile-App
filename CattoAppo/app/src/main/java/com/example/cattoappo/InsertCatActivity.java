package com.example.cattoappo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InsertCatActivity extends AppCompatActivity {

    private EditText editCatName, editFurLength;
    private CheckBox checkBoxCausesAllergy;
    private Button btnInsertCat;
    private Spinner spinnerBreed;
    private RadioGroup radioGroupPersonality, radioGroupFurLength;
    private ImageView imagePreview;

    private DBHelper dbHelper;

    private List<String> breedList = Arrays.asList("Javanese", "Persian", "Munchkin", "Siamese", "Calico", "Ragamuffin", "Tabby", "Scottish Fold");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_cat);

        dbHelper = new DBHelper(this);

        editCatName = findViewById(R.id.editCatName);
        checkBoxCausesAllergy = findViewById(R.id.checkBoxCausesAllergy);
        spinnerBreed = findViewById(R.id.spinnerBreed);
        radioGroupPersonality = findViewById(R.id.radioGroupPersonality);
        radioGroupFurLength = findViewById(R.id.radioGroupFurLength);
        imagePreview = findViewById(R.id.imagePreview);
        btnInsertCat = findViewById(R.id.btnInsertCat);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, breedList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBreed.setAdapter(adapter);

        btnInsertCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCat();
            }
        });

        spinnerBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updatePreviewImage(spinnerBreed.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    private String getImagePath(String breed) {

        int breedIndex = breedList.indexOf(breed) + 1;
        return "cat" + breedIndex;
    }

    private void insertCat() {
        String catName = editCatName.getText().toString();
        String breed = spinnerBreed.getSelectedItem().toString();
        String furLength = getSelectedRadioButtonValue(radioGroupFurLength);
        String personality = getSelectedRadioButtonValue(radioGroupPersonality);
        int causesAllergy = checkBoxCausesAllergy.isChecked() ? 1 : 0;


        String imagePath = getImagePath(breed);

        long result = dbHelper.insertCat(catName, breed, furLength, personality, causesAllergy, imagePath);

        if (result != -1) {
            Toast.makeText(this, "Cat inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cat was not inserted", Toast.LENGTH_SHORT).show();
        }
    }


    private String getSelectedRadioButtonValue(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        return radioButton != null ? radioButton.getText().toString() : "";
    }

    private void updatePreviewImage(String breed) {
        String imageName = "cat" + (breedList.indexOf(breed) + 1);
        int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
        Log.d("ImageDebug", "Resource ID: " + imageResource);

        imagePreview.setImageResource(imageResource);
    }
}

