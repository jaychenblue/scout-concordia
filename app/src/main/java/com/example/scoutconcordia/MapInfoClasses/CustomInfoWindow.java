package com.example.scoutconcordia.MapInfoClasses;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.example.scoutconcordia.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/** Classed used to create custom info windows for markers displayed on the map. */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Activity context;

    public CustomInfoWindow(Activity context)
    {
        this.context = context;
    }
    
    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    /** Overrides the getInfoContents method.
     * @param marker A marker from the map
     * @return Returns an info window view with the custom marker properties
     */
    @Override
    public View getInfoContents(Marker marker)
    {
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
