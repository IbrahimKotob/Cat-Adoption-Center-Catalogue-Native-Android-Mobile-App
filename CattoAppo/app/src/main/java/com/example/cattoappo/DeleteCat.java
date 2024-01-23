package com.example.cattoappo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DeleteCat extends AppCompatActivity {

    private ListView listViewCats;
    private Button btnDeleteCat;
    private DBHelper dbHelper;
    private int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cat);

        dbHelper = new DBHelper(this);

        listViewCats = findViewById(R.id.listViewCats);
        btnDeleteCat = findViewById(R.id.btnDeleteCat);

        displayCatList();

        listViewCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleCatSelection(position);
            }
        });

        btnDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedCat();
            }
        });
    }

    private void displayCatList() {
        ArrayList<String> catEntries = new ArrayList<>();
        Cursor cursor = dbHelper.getCats();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int catId = cursor.getInt(cursor.getColumnIndex("cat_id"));
                @SuppressLint("Range") String catName = cursor.getString(cursor.getColumnIndex("cat_name"));
                catEntries.add(catName + " (ID: " + catId + ")");
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, catEntries);
        listViewCats.setAdapter(adapter);

        if (selectedPosition != ListView.INVALID_POSITION) {
            listViewCats.setItemChecked(selectedPosition, true);
            displayCatDetails(selectedPosition);
        } else {

            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView))
                    .commit();
        }
    }

    private void handleCatSelection(int position) {
        selectedPosition = position;

        listViewCats.clearChoices();
        listViewCats.requestLayout();

        listViewCats.setItemChecked(position, true);

        displayCatDetails(position);
    }

    private void deleteSelectedCat() {
        if (selectedPosition != ListView.INVALID_POSITION) {
            Cursor cursor = dbHelper.getCats();
            cursor.moveToPosition(selectedPosition);
            @SuppressLint("Range") int catId = cursor.getInt(cursor.getColumnIndex("cat_id"));

            int rowsAffected = dbHelper.deleteCat(catId);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Cat deleted successfully", Toast.LENGTH_SHORT).show();
                displayCatList();
                selectedPosition = ListView.INVALID_POSITION;
            } else {
                Toast.makeText(this, "Failed to delete cat", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No cat selected", Toast.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("Range")
    private void displayCatDetails(int position) {
        Cursor cursor = dbHelper.getCats();
        cursor.moveToPosition(position);

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
        CatCardFragment catCardFragment = new CatCardFragment();
        catCardFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, catCardFragment)
                .addToBackStack(null)
                .commit();
    }






    private int getImageResource(String imageName) {

        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        Log.d("ImageDebug", "Resource ID: " + resourceId);

        if (resourceId == 0) {
            Log.e("ImageResourceError", "Resource not found for image: "+ imageName);
        }

        return resourceId;
    }
}
