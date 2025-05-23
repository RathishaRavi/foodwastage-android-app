package com.example.foodwastage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

public class GoalSettingFragment extends Fragment {
    private TextView goalTextView;
    private EditText goalEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goal_setting, container, false);

        // Find the views
        goalTextView = view.findViewById(R.id.goal_text_view);
        goalEditText = view.findViewById(R.id.goal_edit_text);
        saveButton = view.findViewById(R.id.save_button);

        // Set the text for the goal text view
        goalTextView.setText("Set your daily food waste reduction goal:");

        // Set the click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the goal value from the edit text
                String goalValue = goalEditText.getText().toString();

                // Save the goal value to shared preferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GoalPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("goal", goalValue);
                editor.apply();

                // Display a toast message to confirm the goal has been saved
                Toast.makeText(getActivity(), "Goal saved!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}