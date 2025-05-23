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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DailyFoodwasteFragment extends Fragment {
    private static final String SHARED_PREFS_NAME = "GoalPreferences";
    private static final String HISTORY_KEY = "history";

    private SharedPreferences sharedPreferences;
    private List<String> historyList;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_food_waste, container, false);

        listView = view.findViewById(R.id.history_list_view);
        final EditText inputEditText = view.findViewById(R.id.daily_waste_input);
        Button addButton = view.findViewById(R.id.add_button);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        historyList = new ArrayList<>();
        loadHistoryFromSharedPreferences();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dailyWaste = inputEditText.getText().toString();
                addDailyWaste(dailyWaste);
                inputEditText.setText("");
            }
        });
        return view;
    }

    public void addDailyWaste(String dailyWaste) {
        historyList.add(dailyWaste);
        saveHistoryToSharedPreferences();
    }

    public List<String> getDailyWasteHistory() {
        return historyList;
    }

    private void loadHistoryFromSharedPreferences() {
        String historyString = sharedPreferences.getString(HISTORY_KEY, "");
        if (!historyString.isEmpty()) {
            String[] historyArray = historyString.split(",");
            for (String item : historyArray) {
                historyList.add(item);
            }
        }
    }

    private void saveHistoryToSharedPreferences() {
        StringBuilder historyStringBuilder = new StringBuilder();
        for (String item : historyList) {
            historyStringBuilder.append(item).append(",");
        }
        String historyString = historyStringBuilder.toString();
        sharedPreferences.edit().putString(HISTORY_KEY, historyString).apply();
    }
}