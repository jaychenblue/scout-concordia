package com.example.scoutconcordia.MapInfoClasses;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.scoutconcordia.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;

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
        TextView tvOpeningTimes = (TextView) view.findViewById(R.id.tv_openingTimes);

        BuildingInfo building = (BuildingInfo)marker.getTag();

        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());
        tvAddress.setText(building.getAddress());
        tvOpeningTimes.setText("Opening hours: \n" + Html.fromHtml(building.getOpeningTimes()));

        return view;
    }

}
