package com.example.amit.lookup;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SearchByName extends Activity {

    Spinner radiusSpinner;
    EditText storeNameText;
    Button lookUpButton;
    String radius, storeName;
    MyLocationListener locListener;
    String latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);


        radiusSpinner = (Spinner) findViewById(R.id.radiusSpinner);
        storeNameText = (EditText) findViewById(R.id.storeName);
        lookUpButton = (Button) findViewById(R.id.lookUpButton);
        String[] radiusValues = new String[]{"0.5", "1", "2", "5", "10", "20", "50"};

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

        lookUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeName = storeNameText.getEditableText().toString().replace(" ","%20");
                System.out.println("Store Name: "+storeName);
                setCurrentLocation();
                new DataRetrieveActivity().execute();

            }
        });


    }

    public void setCurrentLocation(){

        locListener = new MyLocationListener(SearchByName.this);

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
            String searchURL =baseURL+latitude+","+longitude+"&radius="+radius+"&opennow=true&name="+storeName+"&key="+GOOGLE_KEY;
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
            System.out.println("Search By Name : " + jsonData);
            Intent resultIntent =  new Intent(SearchByName.this, ResultDisplay.class);
            resultIntent.putExtra("resultData", jsonData);
            resultIntent.putExtra("storeName", storeName);
            resultIntent.putExtra("radius", radius);
            startActivity(resultIntent);

        }
    }

}
