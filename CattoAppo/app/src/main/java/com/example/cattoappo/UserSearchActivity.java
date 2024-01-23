package com.example.cattoappo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity {
    private int selectedPosition = ListView.INVALID_POSITION;
    private ListView listViewCats;
    private Button btnMainMenu;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersearch);

        dbHelper = new DBHelper(this);

        listViewCats = findViewById(R.id.listViewCats);
        btnMainMenu = findViewById(R.id.MainMenu);


        Intent intent = getIntent();
        if (intent.hasExtra("breed")) {
            String selectedBreed = intent.getStringExtra("breed");
            setTitle("Cats - " + selectedBreed);

            loadCatsByBreed(selectedBreed);
        } else {
            Toast.makeText(this, "No cat breed selected", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (selectedPosition != ListView.INVALID_POSITION) {
            listViewCats.setItemChecked(selectedPosition, true);
            displayCatDetails(selectedPosition);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView))
                    .commit();
        }
        btnMainMenu.setOnClickListener(v -> finish());
    }

    private void loadCatsByBreed(String breed) {
        Cursor cursor = dbHelper.getCatsByBreed(breed);

        List<String> catNames = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String catName = cursor.getString(cursor.getColumnIndex("cat_name"));
                catNames.add(catName);
            } while (cursor.moveToNext());

            cursor.close();
        }
        listViewCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleCatSelection(position);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, catNames);
        listViewCats.setAdapter(adapter);
    }

    @SuppressLint("Range")
    private void displayCatDetails(int position) {
        String selectedBreed = getIntent().getStringExtra("breed");

        Cursor cursor = dbHelper.getCatsByBreed(selectedBreed);

        if (cursor != null && cursor.moveToPosition(position)) {
            Bundle args = new Bundle();
            args.putInt("catId", cursor.getInt(cursor.getColumnIndex("cat_id")));
            args.putString("catName", cursor.getString(cursor.getColumnIndex("cat_name")));
            args.putString("breed", cursor.getString(cursor.getColumnIndex("breed")));
            args.putString("furLength", cursor.getString(cursor.getColumnIndex("fur_length")));
            args.putString("personality", cursor.getString(cursor.getColumnIndex("personality")));
            args.putBoolean("causesAllergy", cursor.getInt(cursor.getColumnIndex("causes_allergy")) == 1);

            String imageName = cursor.getString(cursor.getColumnIndex("image_path"));
            Log.d("ImageName", imageName);
            int imageResourceId = getImageResource(imageName);
            args.putInt("imageResource", imageResourceId);

            UserCatCardFragment catCardFragment = new UserCatCardFragment();
            catCardFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, catCardFragment)
                    .addToBackStack(null)
                    .commit();

            cursor.close();
        }
    }

    private int getImageResource(String imageName) {


        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        Log.d("ImageDebug", "Resource ID: " + resourceId);

        if (resourceId == 0) {
            Log.e("ImageResourceError", "Resource not found for image: "+ imageName);
        }

        return resourceId;
    }
    private void handleCatSelection(int position) {
        selectedPosition = position;

        listViewCats.clearChoices();
        listViewCats.requestLayout();
        listViewCats.setItemChecked(position, true);

        displayCatDetails(position);
    }
}
