package com.example.foodwastage;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Get the values from the bundle
        String name = getArguments().getString("name");
        String number = getArguments().getString("number");
        String address = getArguments().getString("address");
        String date = getArguments().getString("date");
        String location = getArguments().getString("location");
        String value = getArguments().getString("value");
        String type = getArguments().getString("type");
        String food = getArguments().getString("food");

        // Find the TextView
        textView = view.findViewById(R.id.dashboard_title);

        // Use these values to display the data in the fragment
        textView.setText("Name: " + name + "\nNumber: " + number + "\nAddress: " + address + "\nDate: " + date + "\nLocation: " + location + "\nValue: " + value + "\nType: " + type + "\nFood: " + food);

        return view;
    }
}
