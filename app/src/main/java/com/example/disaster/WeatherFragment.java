package com.example.disaster;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class WeatherFragment extends Fragment {
    TextView cityTextView;
    TextView dateTextView;
    TextView tempTextView;
    TextView rainTextView;
    TextView humidityTextView;

    RequestQueue requestQueue;;
   private static final String[] COUNTRIES=new String[]{
            "Yemen",
            "Saudi Arabia",
            "United Arab Emirates",
            "Kuwait",
            "Oman",
            "Qatar",
            "Bahrain",
            "Jordan",
            "Lebanon",

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        cityTextView = view.findViewById(R.id.city_text_view);
        dateTextView = view.findViewById(R.id.date_text_view);
        tempTextView = view.findViewById(R.id.temp_text_view);
        rainTextView = view.findViewById(R.id.rain_text_view);
        humidityTextView = view.findViewById(R.id.humidity_text_view);

        requestQueue = Volley.newRequestQueue(view.getContext());
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.country_edit_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, COUNTRIES);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            getWeather("https://api.weatherapi.com/v1/current.json?key=a9415893546545e2a38124125220707&q="+parent.getItemAtPosition(position).toString()+"&aqi=no");
        });
        getWeather("https://api.weatherapi.com/v1/current.json?key=a9415893546545e2a38124125220707&q=Yemen&aqi=no");
        return view;
    }
    void getWeather(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        WeatherRVModel weatherRVModel =  WeatherRVModel.fromMap(response);
                        cityTextView.setText(weatherRVModel.getCity());
                        dateTextView.setText(weatherRVModel.getTime());
                        tempTextView.setText(weatherRVModel.getTemperature()+" °C");
                        rainTextView.setText("Rain:"+weatherRVModel.getUv()+"% Wind: "+weatherRVModel.getWindSpeed()+" mph");
                        humidityTextView.setText(weatherRVModel.getHumidity()+"% :Humidity");
                    } catch (Exception e) {
                        Log.e("WeatherFragment-1", e.getMessage());
                    }
                }, error -> {
            Log.e("WeatherFragment-2", error.getMessage());
        });
        requestQueue.add(request);
    }
}