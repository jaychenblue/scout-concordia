package com.example.scoutconcordia;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Activity context;

    public CustomInfoWindow(Activity context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker){
        return null;
    }

    @Override
    public View getInfoContents(Marker marker){
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);

        BuildingInfo building = (BuildingInfo)marker.getTag();


        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());
        tvAddress.setText(building.getAddress());




        return view;
    }

}
