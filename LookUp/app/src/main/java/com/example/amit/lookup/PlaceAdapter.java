package com.example.amit.lookup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 11-03-2016.
 */
public class PlaceAdapter extends ArrayAdapter<PlaceData> {
    private final Context context;
    private final List<PlaceData> itemsArrayList;

    public PlaceAdapter(Context context, List<PlaceData> itemsArrayList) {

        super(context, R.layout.row_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView;
        TextView placeName, placeInfo;
        System.out.println("Display position: "+position);
        // 1. Create inflater
         inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
         rowView = inflater.inflate(R.layout.row_layout, parent, false);

        // 3. Get the two text view from the rowView
         placeName = (TextView) rowView.findViewById(R.id.placeName);
         placeInfo = (TextView) rowView.findViewById(R.id.placeInfo);

        // 4. Set the text for textView
        placeName.setText(itemsArrayList.get(position).getName());
        placeInfo.setText(itemsArrayList.get(position).getInfo());

        // 5. retrn rowView
        return rowView;
    }
}
