package com.example.form;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.form.model.DisasterModel;
import com.example.form.model.WeatherRVModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class WeatherFragment extends Fragment {
    TextView cityTextView, dateTextView, tempTextView, rainTextView, humidityTextView;
    ImageView weatherImage;

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
        weatherImage = view.findViewById(R.id.weather_image_view);

        requestQueue = Volley.newRequestQueue(view.getContext());
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.country_edit_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, COUNTRIES);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            getWeather("https://api.weatherapi.com/v1/current.json?key=a9415893546545e2a38124125220707&q="+parent.getItemAtPosition(position).toString()+"&aqi=no",view.getContext());
        });
        getWeather("https://api.weatherapi.com/v1/current.json?key=a9415893546545e2a38124125220707&q=Yemen&aqi=no",view.getContext());
        return view;
    }
    void getWeather(String url, Context context) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        WeatherRVModel weatherRVModel =  WeatherRVModel.fromMap(response);
                        cityTextView.setText(weatherRVModel.getCity());
                        dateTextView.setText(weatherRVModel.getTime());
                        tempTextView.setText(weatherRVModel.getTemperature()+" °C");
                        rainTextView.setText("Rain:"+weatherRVModel.getUv()+"% Wind: "+weatherRVModel.getWindSpeed()+" mph");
                        new DownloadImageTask((ImageView) weatherImage)
                                .execute("https://"+weatherRVModel.getIcon());


                        Log.e("WeatherFragment-1",  weatherRVModel.getIcon());
                    } catch (Exception e) {
                        Log.e("WeatherFragment-1", e.getMessage());
                    }
                }, error -> {
            Log.e("WeatherFragment-2", error.getMessage());
        });
        requestQueue.add(request);
    }

}