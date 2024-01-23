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

public class UserCatCardFragment extends Fragment {
    private ImageView imageCat;
    private TextView textCatName, textBreed, textFurLength, textPersonality, textCausesAllergy;
    public UserCatCardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usercatcard, container, false);

        imageCat = view.findViewById(R.id.imageCat);
        textCatName = view.findViewById(R.id.textCatName);
        textBreed = view.findViewById(R.id.textBreed);
        textFurLength = view.findViewById(R.id.textFurLength);
        textPersonality = view.findViewById(R.id.textPersonality);
        textCausesAllergy = view.findViewById(R.id.textCausesAllergy);
        Bundle args = getArguments();
        if (args != null) {
            displayCatDetails(args);
        }
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
}

