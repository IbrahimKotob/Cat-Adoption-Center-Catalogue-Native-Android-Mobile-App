package com.example.cattoappo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class UpdateCatActivity extends AppCompatActivity {

    private EditText editCatName;
    private CheckBox checkBoxCausesAllergy;
    private Button btnUpdateCat;
    private Spinner spinnerBreed;
    private RadioGroup radioGroupPersonality, radioGroupFurLength;
    private ImageView imagePreview;

    private DBHelper dbHelper;
    private int catId;

    private List<String> breedList = Arrays.asList("Javanese", "Persian", "Munchkin", "Siamese", "Calico", "Ragamuffin", "Tabby", "Scottish Fold");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cat);

        dbHelper = new DBHelper(this);

        editCatName = findViewById(R.id.editCatName);
        checkBoxCausesAllergy = findViewById(R.id.checkBoxCausesAllergy);
        spinnerBreed = findViewById(R.id.spinnerBreed);
        radioGroupPersonality = findViewById(R.id.radioGroupPersonality);
        radioGroupFurLength = findViewById(R.id.radioGroupFurLength);
        imagePreview = findViewById(R.id.imagePreview);
        btnUpdateCat = findViewById(R.id.btnUpdateCat);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, breedList);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBreed.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra("catId")) {
            catId = intent.getIntExtra("catId", -1);
            if (catId != -1) {
                // Load cat details and populate the UI
                loadCatDetails(catId);
            }
        }

        btnUpdateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCat();
            }
        });

        // Set a listener to the breed spinner to change the preview image on selection change
        spinnerBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updatePreviewImage(spinnerBreed.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void loadCatDetails(int catId) {
        Cursor cursor = dbHelper.getCatDetails(catId);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String catName = cursor.getString(cursor.getColumnIndex("cat_name"));
            @SuppressLint("Range") boolean causesAllergy = cursor.getInt(cursor.getColumnIndex("causes_allergy")) == 1;
            @SuppressLint("Range") String breed = cursor.getString(cursor.getColumnIndex("breed"));
            @SuppressLint("Range") String furLength = cursor.getString(cursor.getColumnIndex("fur_length"));
            @SuppressLint("Range") String personality = cursor.getString(cursor.getColumnIndex("personality"));

            editCatName.setText(catName);
            checkBoxCausesAllergy.setChecked(causesAllergy);
            spinnerBreed.setSelection(breedList.indexOf(breed));
            setRadioButtonByValue(radioGroupPersonality, personality);
            setRadioButtonByValue(radioGroupFurLength, furLength);
            updatePreviewImage(breed);

            cursor.close();
        }
    }

    private void updateCat() {
        String catName = editCatName.getText().toString();
        String breed = spinnerBreed.getSelectedItem().toString();
        String furLength = getSelectedRadioButtonValue(radioGroupFurLength);
        String personality = getSelectedRadioButtonValue(radioGroupPersonality);
        int causesAllergy = 0;
        if(checkBoxCausesAllergy.isChecked()) {
            causesAllergy = 1;
        }
        String imagePath = getImagePath(breed);

        int result = dbHelper.updateCat(catId, catName, breed, furLength, personality, causesAllergy, imagePath);

        if (result>0) {
            Toast.makeText(this, "Cat updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update cat", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(String breed) {
        int breedIndex = breedList.indexOf(breed) + 1;
        return "cat" + breedIndex;
    }

    private String getSelectedRadioButtonValue(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        return radioButton != null ? radioButton.getText().toString() : "";
    }

    private void updatePreviewImage(String breed) {
        String imageName = "cat" + (breedList.indexOf(breed) + 1);
        int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imagePreview.setImageResource(imageResource);
    }

    private void setRadioButtonByValue(RadioGroup radioGroup, String value) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View radioButton = radioGroup.getChildAt(i);
            if (radioButton instanceof RadioButton) {
                RadioButton currentRadioButton = (RadioButton) radioButton;
                if (currentRadioButton.getText().toString().equals(value)) {
                    currentRadioButton.setChecked(true);
                    break;
                }
            }
        }
    }
}
