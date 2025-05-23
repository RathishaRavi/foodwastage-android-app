package com.example.foodwastage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FoodWasteHistoryFragment extends Fragment {
    private ListView historyListView;
    private ArrayAdapter<String> historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_waste_history, container, false);

        historyListView = view.findViewById(R.id.history_list_view);
        final EditText inputEditText = view.findViewById(R.id.history_input);
        Button addButton = view.findViewById(R.id.add_button);

        historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        historyListView.setAdapter(historyAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String historyItem = inputEditText.getText().toString();
                historyAdapter.add(historyItem);
                inputEditText.setText("");

                // Save the history item to shared preferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FoodWasteHistory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String history = sharedPreferences.getString("history", "");
                if (history.isEmpty()) {
                    editor.putString("history", historyItem);
                } else {
                    editor.putString("history", history + "," + historyItem);
                }
                editor.apply();
            }
        });

        // Get the food waste history from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FoodWasteHistory", Context.MODE_PRIVATE);
        String[] history = sharedPreferences.getString("history", "").split(",");

        // Add the history items to the adapter
        for (String item : history) {
            historyAdapter.add(item);
        }

        return view;
    }
}