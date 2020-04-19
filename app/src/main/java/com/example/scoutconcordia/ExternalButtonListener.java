package com.example.scoutconcordia;

import android.graphics.BitmapFactory;
import android.view.View;

import com.example.scoutconcordia.Activities.MapsActivity;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;

public class ExternalButtonListener extends MapsActivity {

    public static void setUpGroundOverlay(String image)
    {
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        float imgRotation = -56;
        float overlaySize = 75;
        BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier(image, DRAWABLE, getmContext().getPackageName()));

        hallGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(floorPlan)
                .position(hallOverlaySouthWest, overlaySize)
                .anchor(0, 1)
                .bearing(imgRotation));
    }

    public static void addfloor1ButtonListener()
    {
        floor1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                resetButtonColors();
                floor1.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floor1.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();
                setUpGroundOverlay("hall1p");
            }
        });
    }

    public static void addfloor2ButtonListener()
    {
        floor2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                resetButtonColors();
                floor2.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floor2.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();
                setUpGroundOverlay("hall2floor");
            }
        });
    }

    public static void addfloor8ButtonListener()
    {
        floor8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetButtonColors();
                floor8.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floor8.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();
                setUpGroundOverlay("hall8p");
            }
        });
    }

    public static void addfloor9ButtonListener()
    {
        floor9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetButtonColors();
                floor9.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floor9.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();
                setUpGroundOverlay("hall9p");
            }
        });
    }

    public static void addfloorCC1ButtonListener()
    {
        floorCC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorCC1.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorCC1.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = 29;
                float overlaySize = 82;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("cc_building1", DRAWABLE, getmContext().getPackageName()));

                ccGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(ccOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorCC2ButtonListener()
    {
        floorCC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorCC2.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorCC2.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = 29;
                float overlaySize = 82;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("cc_building2", DRAWABLE, getmContext().getPackageName()));

                ccGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(ccOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorVE2ButtonListener()
    {
        floorVE2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVE2.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorVE2.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = 29;
                float overlaySize = 50;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("ve_floor2", DRAWABLE, getmContext().getPackageName()));

                veGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(veBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorVL1ButtonListener()
    {
        floorVL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVL1.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorVL1.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = 209;
                float overlaySize = 71;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("vl_001", DRAWABLE, getmContext().getPackageName()));

                vlGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(vlBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorVL2ButtonListener()
    {
        floorVL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVL2.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorVL2.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = 209;
                float overlaySize = 71;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("vl_002", DRAWABLE, getmContext().getPackageName()));

                vlGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(vlBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorMB1ButtonListener()
    {
        floorMB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorMB1.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorMB1.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = -56;
                float overlaySize = 42;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("mb_01", DRAWABLE, getmContext().getPackageName()));

                mbGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(mbBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public static void addfloorMBS2ButtonListener()
    {
        floorMBS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorMBS2.setBackgroundColor(getmContext().getResources().getColor(R.color.burgandy));
                floorMBS2.setTextColor(getmContext().getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                float imgRotation = -56;
                float overlaySize = 42;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getmContext().getResources().getIdentifier("mb_s02", DRAWABLE, getmContext().getPackageName()));

                mbGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(mbBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }
}
