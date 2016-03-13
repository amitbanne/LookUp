package com.example.amit.lookup;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    Button lookUpButton;
    TextView locationText;
    MyLocationListener locListener;
    Spinner radiusSpinner;
    AutoCompleteTextView categoryTextView;
    String latitude, longitude;
    String radius, category;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lookUpButton = (Button) findViewById(R.id.lookUpButton);
        radiusSpinner = (Spinner) findViewById(R.id.radiusSpinner);
        categoryTextView = (AutoCompleteTextView) findViewById(R.id.categoryTextView);

        setCurrentLocation();
        String[] radiusValues = new String[]{"0.5", "1", "2", "5", "10", "20", "50"};
        final String[] categoryValues =
                new String[]{"airport",
                        "accounting",
                        "amusement_park",
                        "aquarium",
                        "art_gallery",
                        "atm",
                        "bakery",
                        "bank",
                        "bar",
                        "beauty_salon",
                        "bicycle_store",
                        "book_store",
                        "bowling_alley",
                        "bus_station",
                        "cafe",
                        "campground",
                        "car_dealer",
                        "car_rental",
                        "car_repair",
                        "car_wash",
                        "casino",
                        "cemetery",
                        "church",
                        "city_hall",
                        "clothing_store",
                        "convenience_store",
                        "courthouse",
                        "dentist",
                        "department_store",
                        "doctor",
                        "electrician",
                        "electronics_store",
                        "embassy",
                        "finance",
                        "fire_station",
                        "florist",
                        "food",
                        "funeral_home",
                        "furniture_store",
                        "gas_station",
                        "grocery_or_supermarket",
                        "gym",
                        "hair_care",
                        "hardware_store",
                        "hindu_temple",
                        "home_goods_store",
                        "hospital",
                        "insurance_agency",
                        "jewelry_store",
                        "laundry",
                        "lawyer",
                        "library",
                        "liquor_store",
                        "local_government_office",
                        "locksmith",
                        "lodging",
                        "meal_delivery",
                        "meal_takeaway",
                        "mosque",
                        "movie_rental",
                        "movie_theater",
                        "moving_company",
                        "museum",
                        "night_club",
                        "painter",
                        "park",
                        "parking",
                        "pet_store",
                        "pharmacy",
                        "physiotherapist",
                        "plumber",
                        "police",
                        "post_office",
                        "real_estate_agency",
                        "restaurant",
                        "roofing_contractor",
                        "school",
                        "shoe_store",
                        "shopping_mall",
                        "spa",
                        "stadium",
                        "storage",
                        "store",
                        "subway_station",
                        "synagogue",
                        "taxi_stand",
                        "train_station",
                        "travel_agency",
                        "university",
                        "veterinary_care",
                        "zoo"};

        ArrayAdapter<String> radiusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, radiusValues);
        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(radiusAdapter);
        radiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        radius = "" + (0.5 * 1609.34);
                        break;
                    case 1:
                        radius = "" + (1 * 1609.34);
                        break;
                    case 2:
                        radius = "" + (2 * 1609.34);
                        break;
                    case 3:
                        radius = "" + (5 * 1609.34);
                        break;
                    case 4:
                        radius = "" + (10 * 1609.34);
                        break;
                    case 5:
                        radius = "" + (20 * 1609.34);
                        break;
                    case 6:
                        radius = "" + (0.5 * 1609.34);
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryValues);
        categoryTextView.setAdapter(categoryAdapter);
        category =  this.categoryTextView.getEditableText().toString();
        categoryTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position Selected: " + position);
                category = (String) parent.getItemAtPosition(position);
            }


        });
        System.out.println("Category Selected: "+category);


        lookUpButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setCurrentLocation();
                new DataRetrieveActivity().execute();
            }
        });
    }

    public void setCurrentLocation(){

        locListener = new MyLocationListener(MainActivity.this);

        // check if GPS enabled
        if(locListener.canGetLocation()){

            latitude = ""+locListener.getLatitude();
            longitude = "" + locListener.getLongitude();
            // \n is for new line
            // String displayText = "Latitude: "+ latitude+"\nLongitude: "+longitude;

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            locListener.showSettingsAlert();
        }
    }



    class DataRetrieveActivity extends AsyncTask {

        String jsonData;

        @Override
        protected Object doInBackground(Object[] params) {
            String GOOGLE_KEY = "AIzaSyA1EmFgzWrZEZXLi-Y-YxRgNTDol2VitUs";
            String baseURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
            String searchURL =baseURL+latitude+","+longitude+"&radius="+radius+"&opennow=true&type="+category+"&key="+GOOGLE_KEY;
            System.out.println("URL: "+ searchURL);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " +longitude);
            try{
                URL url = new URL(searchURL);
                jsonData = IOUtils.toString(new InputStreamReader(
                        url.openStream()));

            }catch(IOException e){
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(Object o) {
            System.out.println("MainActivity: " + jsonData);
            Intent resultIntent =  new Intent(MainActivity.this, ResultDisplay.class);
            resultIntent.putExtra("resultData", jsonData);
            resultIntent.putExtra("category", category);
            resultIntent.putExtra("radius", radius);
            String temp = null;
            resultIntent.putExtra("storeName", temp);
            startActivity(resultIntent);

        }
    }
}
