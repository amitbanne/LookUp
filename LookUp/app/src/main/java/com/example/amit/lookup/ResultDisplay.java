package com.example.amit.lookup;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultDisplay extends ListActivity {
    ArrayAdapter placeAdapter;
    private ListView placeListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_result_display);
        placeListView = (ListView) findViewById(R.id.list);
        String jsonData =  getIntent().getStringExtra("resultData");
        String category =  getIntent().getStringExtra("category");
        String radius =  getIntent().getStringExtra("radius");
        String storeName =  getIntent().getStringExtra("storeName");
        System.out.println("ResultDisplay:  "+ jsonData);
        List<Place> placeList =  parseJSONData(jsonData);
        List<PlaceData> listTitle = new ArrayList();
        for(int i=0;i< placeList.size();i++){
            listTitle.add(i, new PlaceData(placeList.get(i).getName(),
                    "\nOpen Now: " + placeList.get(i).getOpen()
                            + "\nAddress:" + placeList.get(i).getAddress()));
        }

        //placeAdapter =  new ArrayAdapter(ResultDisplay.this, R.layout.row_layout, listTitle);
        //placeListView.setAdapter(placeAdapter);

        List<PlaceData> openlist = new ArrayList();
        for(PlaceData pd: listTitle){
            if(pd.getInfo().contains("YES"))
                openlist.add(pd);
        }

        if(0 == openlist.size()){
            if(null == storeName)
                openlist.add(new PlaceData("All '"+category+"' places within '" + (Double.parseDouble(radius) / 1609.34)+"' miles CLOSED or NOT FOUND.",""));
            else
                openlist.add(new PlaceData("All places matching name '"+storeName.replace("%20"," ")+"'  within '" + (Double.parseDouble(radius) / 1609.34)+"' miles CLOSED or NOT FOUND.",""));
        }



        PlaceAdapter placeAdapter = new PlaceAdapter(this, openlist);
        setListAdapter(placeAdapter);



    }
    public List parseJSONData(String jsonData){
        ArrayList<Place> temp = new ArrayList();
        try {
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(jsonData);
            // make an jsonObject in order to parse the response

            if (jsonObject.has("results")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Place poi = new Place();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setAddress(jsonArray.getJSONObject(i).optString("vicinity", " "));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpen("YES");
                                } else {
                                    poi.setOpen("NO");
                                }
                            }
                        } else {
                            poi.setOpen("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();

        }
        return temp;
    }

}
