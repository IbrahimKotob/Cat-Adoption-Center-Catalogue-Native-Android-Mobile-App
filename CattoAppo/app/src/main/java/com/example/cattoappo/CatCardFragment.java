package com.example.cattoappo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CatCardFragment extends Fragment {
    private ImageView imageCat;
    private TextView textCatName, textBreed, textFurLength, textPersonality, textCausesAllergy;
    private Button btnUpdateCat;
    public CatCardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catcard, container, false);

        imageCat = view.findViewById(R.id.imageCat);
        textCatName = view.findViewById(R.id.textCatName);
        textBreed = view.findViewById(R.id.textBreed);
        textFurLength = view.findViewById(R.id.textFurLength);
        textPersonality = view.findViewById(R.id.textPersonality);
        textCausesAllergy = view.findViewById(R.id.textCausesAllergy);
        btnUpdateCat = view.findViewById(R.id.button7);
        Bundle args = getArguments();
        if (args != null) {
            displayCatDetails(args);
        }
       btnUpdateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUpdateCatActivity(args);
            }
        });

        return view;
    }

    private void displayCatDetails(Bundle args) {
        String catName = args.getString("catName", "");
        String breed = args.getString("breed", "");
        String furLength = args.getString("furLength", "");
        String personality = args.getString("personality", "");
        boolean causesAllergy = args.getBoolean("causesAllergy", false);


        int imageResource = args.getInt("imageResource", getResources().getIdentifier("cat1", "drawable", requireActivity().getPackageName()));



        imageCat.setImageResource(imageResource);
        textCatName.setText("Cat Name: " + catName);
        textBreed.setText("Breed: " + breed);
        textFurLength.setText("Fur Length: " + furLength);
        textPersonality.setText("Personality: " + personality);
        textCausesAllergy.setText("Causes Allergy: " + (causesAllergy ? "Yes" : "No"));
    }
    private void launchUpdateCatActivity(Bundle args) {

        int catId = args.getInt("catId", -1);

        if (catId != -1) {

            Intent updateCatIntent = new Intent(requireActivity(), UpdateCatActivity.class);
            updateCatIntent.putExtra("catId", catId);
            startActivity(updateCatIntent);
        } else {
            Log.e("CatCardFragment", "Cat ID not found in arguments");
        }
    }
}

