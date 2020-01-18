package com.example.scoutconcordia;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     *
     * This method is used to apply styling to the polygons that will be displayed on the map.
     * Different styles can be created by assigning different tags to the polygons
     * If you need polygons with the same styling just assign them the same tags
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        int strokeColor = Color.parseColor("#74091F");
        int fillColor = Color.parseColor("#74091F");

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                strokeColor = Color.parseColor("#74091F");
                fillColor = Color.parseColor("#74091F");
                break;
        }
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
        polygon.setClickable(true);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Concordia (downtown campus).
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Concordia and move the camera
        LatLng coco = new LatLng(45.494619, -73.577376); // Concordia's coordinates
        mMap.addMarker(new MarkerOptions().position(coco).title("Marker in Concordia"));
        float zoomLevel = 16.0f; // max 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coco, zoomLevel));


         //Hall Building
        Polygon hallBuilding = googleMap.addPolygon(new PolygonOptions()
                .add(
                    new LatLng(45.497163, -73.579557),
                    new LatLng(45.497718, -73.579039),
                    new LatLng(45.497374, -73.578319),
                    new LatLng(45.496820, -73.578841)));
        hallBuilding.setTag("alpha");
        stylePolygon(hallBuilding);

        //Pavillion JM McConnell Building
        Polygon pavillionJWMcConnell = googleMap.addPolygon(new PolygonOptions()
            .add(
                    new LatLng(45.496706, -73.578530),
                    new LatLng(45.496729, -73.578578),
                    new LatLng(45.496887, -73.578417),
                    new LatLng(45.496870, -73.578373),
                    new LatLng(45.496913, -73.578331),
                    new LatLng(45.496895, -73.578293),
                    new LatLng(45.496940, -73.578247),
                    new LatLng(45.496964, -73.578296),
                    new LatLng(45.497001, -73.578258),
                    new LatLng(45.497018, -73.578297),
                    new LatLng(45.497270, -73.578049),
                    new LatLng(45.497143, -73.577817),
                    new LatLng(45.497109, -73.577849),
                    new LatLng(45.497085, -73.577800),
                    new LatLng(45.497078, -73.577808),
                    new LatLng(45.497070, -73.577792),
                    new LatLng(45.497121, -73.577744),
                    new LatLng(45.497040, -73.577580),
                    new LatLng(45.496994, -73.577632),
                    new LatLng(45.496988, -73.577621),
                    new LatLng(45.496999, -73.577610),
                    new LatLng(45.496978, -73.577567),
                    new LatLng(45.497013, -73.577535),
                    new LatLng(45.496892, -73.577293),
                    new LatLng(45.496616, -73.577556),
                    new LatLng(45.496637, -73.577602),
                    new LatLng(45.496583, -73.577651),
                    new LatLng(45.496496, -73.577465),
                    new LatLng(45.496256, -73.577698),
                    new LatLng(45.496668, -73.578568)
            ));
        pavillionJWMcConnell.setTag("alpha");
        stylePolygon(pavillionJWMcConnell);

    }//end of onMapReady

} //end of Maps Activity Class