package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.scoutconcordia.DataStructures.Graph;
import com.example.scoutconcordia.FileAccess.FileAccessor;
import com.example.scoutconcordia.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.maps.android.PolyUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.android.gms.common.api.Status;

import java.io.OutputStream;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.example.scoutconcordia.MapInfoClasses.CustomInfoWindow;
import com.google.android.material.button.MaterialButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    private float zoomLevel = 16.0f;
    private final int ACCESS_FINE_LOCATION = 9001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng concordiaLatLngDowntownCampus = new LatLng(45.494619, -73.577376);
    private final LatLng concordiaLatLngLoyolaCampus = new LatLng(45.458423, -73.640460);
    private Button directionButton;
    private Button exploreInsideButton;

    private Button floor1;
    private Button floor2;
    private Button floor8;
    private Button floor9;


    private BottomAppBar popUpBar;
    private ToggleButton toggleButton;
    private boolean isInfoWindowShown = false;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext; // This context variable is necessary in order for non-activity classes to read resource files.

    private Marker searchMarker;
    private String activeInfoWindow = null;
    private List<Polygon> polygonBuildings = new ArrayList<>();
    private List<Marker> markerBuildings = new ArrayList<>();
    private List<Marker> hall8floorMarkers = new ArrayList<>();
    private List<Marker> hall9floorMarkers = new ArrayList<>();
    private List<Graph> floorGraphs = new ArrayList<>();

    // We use this for image overlay of Hall building
    private final LatLng hallOverlaySouthWest = new LatLng(45.496827, -73.578849);
    private final LatLng hallOverlayNorthEast = new LatLng(45.497711, -73.579033);
    private GroundOverlay hallGroundOverlay;

    // Concordia buildings list
    public static final List<String> locations = new ArrayList<>();

    // Displays the Map
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

/**        Places.initialize(this, getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // RectangularBounds bounds = RectangularBounds.newInstance(
        //         new LatLng(-33.880490, 151.184363),
        //         new LatLng(-33.858754, 151.229596));

        //autocompleteFragment.setLocationBias(bounds); will return places within this area, may return results outside the aread

        // Select here whatever infortmation you want to retrieve for the selected place
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        //the types of places search should display
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        //restricts searches to Canada only so that we do not get random locations when searching
        autocompleteFragment.setCountry("CA");

        //autocompleteFragment.set
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                searchMarker.remove();
                searchMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), zoomLevel));
                mMap.setOnMyLocationChangeListener(null);
            }

            @Override
            public void onError(@NonNull Status status) {
                System.out.println("STATUS CODE: " + status.getStatusMessage());
            }
        });
*/
        addListenerOnToggle();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar_activity_maps);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_map:
                        break;

                    case R.id.nav_schedule:
                        Intent calendarIntent = new Intent(MapsActivity.this, CalendarActivity.class);
                        startActivity(calendarIntent);
                        MapsActivity.this.overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_shuttle:
                        Intent shuttleIntent = new Intent(MapsActivity.this, ShuttleScheduleActivity.class);
                        startActivity(shuttleIntent);
                        MapsActivity.this.overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        addDirectionButtonListener();
        addExploreInsideButtonListener();
        addPopUpBarListener();
        addfloor8ButtonListener();
        addfloor9ButtonListener();
        addfloor1ButtonListener();
        addfloor2ButtonListener();

        createFloorGraphs();

        floorGraphs.indexOf(hall_8_floor);
        LatLng point1 = hall_8_floor.searchByClassName("ESCALATOR");
        LatLng point2 = hall_8_floor.searchByClassName("ELEVATOR");

        String fromMe = "";
        String toMe = "";
        searchForClass(fromMe, toMe);

        // lets encrypt all of the files before using them
        //encryptAllInputFiles();
    }


    public void addfloor1ButtonListener()
    {

        if (hallGroundOverlay != null) {
            hallGroundOverlay.remove();
        }

        floor1 = (Button) findViewById(R.id.floor1);
        floor1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {

                // THis code handles the map overlay of the floor plans.
                // Map overlay of the Hall image over the building
                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.bluesquare, dimensions);
                int imgHeightPixels = dimensions.outHeight;

                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(R.drawable.hall1p);

                GroundOverlayOptions goo1 = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);

                hallGroundOverlay = mMap.addGroundOverlay(goo1);



//                // Lets try creating a graph for Hall 8th Floor
//                Graph hall_8_floor = createGraph("encryptedhall8nodes");
//                //Graph hall_8_floor = createGraph("hall8nodes");
//
//                for (Graph.Node node : hall_8_floor.nodes())
//                {
//                    if (node.getType() > 0) {  // if it is a hall node
//                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        hall8floorMarkers.add(polyMarker);
//                    } else { // if it is a class node
//                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        hall8floorMarkers.add(polyMarker);
//                    }
//                }
//                for (LatLng vertices : hall_8_floor.vertices())
//                {
//                    if (hall_8_floor.incidentVerticies(vertices) != null)
//                        Log.w("Adjacency List", Arrays.toString(hall_8_floor.incidentVerticies(vertices)));
//                    else
//                        Log.w("Adjacency List", "Failed!");
//                }
//
//                LatLng point1 = hall_8_floor.searchByClassName("H-810");
//                LatLng point2 = hall_8_floor.searchByClassName("ELEVATOR");
//
//                Log.w("Point 1:", point1.toString());
//                Log.w("Point 2:", point2.toString());
//
//                Object[] path = hall_8_floor.breathFirstSearch(point1, point2);
//
//                LatLng[] dest = new LatLng[path.length];
//                System.arraycopy(path, 0, dest, 0, path.length);
//
//
//                Polyline searchPath = mMap.addPolyline(new PolylineOptions());
//                List<LatLng> listOfPoints = new ArrayList<>();
//                searchPath.setPoints(Arrays.asList(dest));
//
//                if (path != null)
//                {
//                    Log.w("BFS", "Final Path");
//                    for (int i = 0; i < path.length; i++)
//                    {
//                        Log.w("BFS", path[i].toString());
//
//                        // lets highlight the path.
//                        for (Marker markers : hall8floorMarkers)
//                        {
//                            if (markers.getPosition().equals(path[i]))
//                            {
//                                if (path[i].equals(point1))
//                                {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // start is green
//                                } else if (path[i].equals(point2))
//                                {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // end is blue
//                                } else {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // path is yellow
//                                }
//                            }
//                        }
//                    }
//                }
            }
        });
    }



    public void addfloor2ButtonListener()
    {

        if (hallGroundOverlay != null) {
            hallGroundOverlay.remove();
        }

        floor2 = (Button) findViewById(R.id.floor2);
        floor2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // THis code handles the map overlay of the floor plans.
                // Map overlay of the Hall image over the building
                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;

                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(R.drawable.hall2floor);

                GroundOverlayOptions goo2 = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);

                hallGroundOverlay = mMap.addGroundOverlay(goo2);


//                // Lets try creating a graph for Hall 8th Floor
//                Graph hall_8_floor = createGraph("encryptedhall8nodes");
//                //Graph hall_8_floor = createGraph("hall8nodes");
//
//                for (Graph.Node node : hall_8_floor.nodes())
//                {
//                    if (node.getType() > 0) {  // if it is a hall node
//                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        hall8floorMarkers.add(polyMarker);
//                    } else { // if it is a class node
//                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        hall8floorMarkers.add(polyMarker);
//                    }
//                }
//                for (LatLng vertices : hall_8_floor.vertices())
//                {
//                    if (hall_8_floor.incidentVerticies(vertices) != null)
//                        Log.w("Adjacency List", Arrays.toString(hall_8_floor.incidentVerticies(vertices)));
//                    else
//                        Log.w("Adjacency List", "Failed!");
//                }
//
//                LatLng point1 = hall_8_floor.searchByClassName("H-810");
//                LatLng point2 = hall_8_floor.searchByClassName("ELEVATOR");
//
//                Log.w("Point 1:", point1.toString());
//                Log.w("Point 2:", point2.toString());
//
//                Object[] path = hall_8_floor.breathFirstSearch(point1, point2);
//
//                LatLng[] dest = new LatLng[path.length];
//                System.arraycopy(path, 0, dest, 0, path.length);
//
//
//                Polyline searchPath = mMap.addPolyline(new PolylineOptions());
//                List<LatLng> listOfPoints = new ArrayList<>();
//                searchPath.setPoints(Arrays.asList(dest));
//
//                if (path != null)
//                {
//                    Log.w("BFS", "Final Path");
//                    for (int i = 0; i < path.length; i++)
//                    {
//                        Log.w("BFS", path[i].toString());
//
//                        // lets highlight the path.
//                        for (Marker markers : hall8floorMarkers)
//                        {
//                            if (markers.getPosition().equals(path[i]))
//                            {
//                                if (path[i].equals(point1))
//                                {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // start is green
//                                } else if (path[i].equals(point2))
//                                {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // end is blue
//                                } else {
//                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // path is yellow
//                                }
//                            }
//                        }
//                    }
//                }
            }
        });
    }

    public void searchForClass(String fromMe, String toMe)
    {

    }
    public void createFloorGraphs()
    {
        // Lets try creating a graph for Hall 8th Floor
        Graph hall_8_floor = createGraph("encryptedhall8nodes");
        Graph hall_9_floor = createGraph("emcryptedhall9nodes");

        floorGraphs.add(hall_8_floor);
        floorGraphs.add(hall_9_floor);

        //LatLng point1 = hall_8_floor.searchByClassName("ESCALATOR");
        //LatLng point2 = hall_8_floor.searchByClassName("ELEVATOR");

        //Log.w("Point 1:", point1.toString());
        //Log.w("Point 2:", point2.toString());

        //Object[] path = hall_8_floor.breathFirstSearch(point1, point2);

        //LatLng[] dest = new LatLng[path.length];
        //System.arraycopy(path, 0, dest, 0, path.length);


        //Polyline searchPath = mMap.addPolyline(new PolylineOptions());
        //List<LatLng> listOfPoints = new ArrayList<>();
        //searchPath.setPoints(Arrays.asList(dest));

        //if (path != null)
        //{
        //    Log.w("BFS", "Final Path");
        //    for (int i = 0; i < path.length; i++)
        //    {
        //        Log.w("BFS", path[i].toString());

        //        // lets highlight the path.
        //        for (Marker markers : hall8floorMarkers)
        //        {
        //            if (markers.getPosition().equals(path[i]))
        //            {
        //                if (path[i].equals(point1))
        //                {
        //                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // start is green
        //                } else if (path[i].equals(point2))
        //                {
        //                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // end is blue
        //                } else {
        //                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // path is yellow
        //                }
        //            }
        //        }
        //    }
       // }
    }

    public void addfloor8ButtonListener()
    {

        if (hallGroundOverlay != null) {
            hallGroundOverlay.remove();
        }

        floor8 = (Button) findViewById(R.id.floor8);
        floor8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // THis code handles the map overlay of the floor plans.
                // Map overlay of the Hall image over the building
                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.bluesquare, dimensions);
                int imgHeightPixels = dimensions.outHeight;

                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(R.drawable.hall8p);

                GroundOverlayOptions goo8 = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);

                hallGroundOverlay = mMap.addGroundOverlay(goo8);


                // For future reference, this will be necessary in order to remove the overlay once
                // the app moves away contextually from the inside of the building.

//                hall8.remove();

                // Lets try creating a graph for Hall 8th Floor
                Graph hall_8_floor = createGraph("encryptedhall8nodes");
                //Graph hall_8_floor = createGraph("hall8nodes");

                for (Graph.Node node : hall_8_floor.nodes())
                {
                    if (node.getType() > 0) {  // if it is a hall node
                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        hall8floorMarkers.add(polyMarker);
                    } else { // if it is a class node
                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        hall8floorMarkers.add(polyMarker);
                    }
                }

                LatLng point1 = hall_8_floor.searchByClassName("ESCALATOR");
                LatLng point2 = hall_8_floor.searchByClassName("ELEVATOR");

                Log.w("Point 1:", point1.toString());
                Log.w("Point 2:", point2.toString());

                Object[] path = hall_8_floor.breathFirstSearch(point1, point2);

                LatLng[] dest = new LatLng[path.length];
                System.arraycopy(path, 0, dest, 0, path.length);


                Polyline searchPath = mMap.addPolyline(new PolylineOptions());
                List<LatLng> listOfPoints = new ArrayList<>();
                searchPath.setPoints(Arrays.asList(dest));

                if (path != null)
                {
                    Log.w("BFS", "Final Path");
                    for (int i = 0; i < path.length; i++)
                    {
                        Log.w("BFS", path[i].toString());

                          // lets highlight the path.
                          for (Marker markers : hall8floorMarkers)
                          {
                              if (markers.getPosition().equals(path[i]))
                              {
                                  if (path[i].equals(point1))
                                  {
                                      markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // start is green
                                  } else if (path[i].equals(point2))
                                  {
                                      markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // end is blue
                                  } else {
                                      markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // path is yellow
                                  }
                              }
                          }
                    }
                }
            }
        });
    }

    public void addfloor9ButtonListener()
    {

        if (hallGroundOverlay != null) {
            hallGroundOverlay.remove();
        }

        floor9 = (Button) findViewById(R.id.floor9);
        floor9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // THis code handles the map overlay of the floor plans.
                // Map overlay of the Hall image over the building
                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.bluesquare, dimensions);
                int imgHeightPixels = dimensions.outHeight;

                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(R.drawable.hall9p);

                GroundOverlayOptions goo9 = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);
//                mMap.addGroundOverlay(goo);

//                GroundOverlay hall9 = mMap.addGroundOverlay(goo);
                hallGroundOverlay = mMap.addGroundOverlay(goo9);

                // Lets try creating a graph for Hall 8th Floor
                Graph hall_9_floor = createGraph("encryptedhall9nodes");
                //Graph hall_9_floor = createGraph("hall9nodes");

                for (Graph.Node node : hall_9_floor.nodes())
                {
                    if (node.getType() > 0) {  // if it is a hall node
                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        hall9floorMarkers.add(polyMarker);
                    } else { // if it is a class node
                        Marker polyMarker = mMap.addMarker(new MarkerOptions().position(node.getElement()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        hall9floorMarkers.add(polyMarker);
                    }
                }

                LatLng point1 = hall_9_floor.searchByClassName("H-927");
                LatLng point2 = hall_9_floor.searchByClassName("ELEVATOR");

                Log.w("Point 1:", point1.toString());
                Log.w("Point 2:", point2.toString());

                Object[] path = hall_9_floor.breathFirstSearch(point1, point2);

                if (path != null)
                {
                    Log.w("BFS", "Final Path");
                    for (int i = 0; i < path.length; i++)
                    {
                        Log.w("BFS", path[i].toString());

                        // lets highlight the path.
                        for (Marker markers : hall9floorMarkers)
                        {
                            if (markers.getPosition().equals(path[i]))
                            {
                                if (path[i].equals(point1))
                                {
                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // start is green
                                } else if (path[i].equals(point2))
                                {
                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // end is blue
                                } else {
                                    markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // path is yellow
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    // If button pushed change Campus
    public void addListenerOnToggle()
    {
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleCampus();
            }
        });
    }


    public void initializeSearchBar(){
        final AutoCompleteTextView searchBar = findViewById(R.id.search_bar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, locations);
        searchBar.setAdapter(adapter);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFindYourWayButtonClick(searchBar.getText().toString());
            }
        });
    }
    // onClick listener for when "Find your way" button is clicked
    public void onFindYourWayButtonClick(final String destination){
        AlertDialog.Builder builder = new AlertDialog.Builder(this  );
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.outdoor_buildings_diections_ui, null);
        builder.setView(dialogView);

        final AutoCompleteTextView startingLocation = dialogView.findViewById(R.id.starting_location);
        RadioButton useCurrentLocationButton = dialogView.findViewById(R.id.useMyLocationButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startingLocation.setAdapter(adapter);

        startingLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        builder.create().show();
    }

    // get directions button clicked in dialog for getting directions (in onFindYourWayButtonClick)
    public void onGetDirectionsClick(View v){
    }

    // walking option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onWalkingSelected(MenuItem m){
        m.setChecked(true);
    }

    // car option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onCarSelected(MenuItem m){
        m.setChecked(true);
    }

    // transit option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onTransitSelected(MenuItem m){
        m.setChecked(true);
    }

    // shuttle option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onShuttleSelected(MenuItem m){
        m.setChecked(true);
    }

    // this is the listener for the get directions button.
    // Once we get the search bar working, we can add a method for search here so that when the button is clicked it searches for location and gives the directions.
    public void addDirectionButtonListener()
    {
        directionButton = (Button) findViewById(R.id.directionsButton);
        // can add a functionality here that gives us the directions when we press on the button
    }

    // this is the listener for the explore inside button.
    public void addExploreInsideButtonListener()
    {
        exploreInsideButton = (Button) findViewById(R.id.exploreInsideButton);

        exploreInsideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activeInfoWindow != null)
                {
                    // we want to remove the building outline from the map so we can see the indoor floor plan
                    LatLng loc = searchMarker.getPosition();  // this is the location of the marker

                    // we look at the list of all polygons. If the marker is within the polygon then we want to hide the polygon from the map.
                    for (Polygon poly : polygonBuildings) {
                        if (PolyUtil.containsLocation(loc, poly.getPoints(), true))
                        {
                            poly.setVisible(false);  // hide the polygon
                            searchMarker.setVisible(false);  // hide the marker

                            floor1.setVisibility(View.VISIBLE);
                            floor2.setVisibility(View.VISIBLE);
                            floor8.setVisibility(View.VISIBLE);
                            floor9.setVisibility(View.VISIBLE);
                        }
                    }

                    // we want to zoom in onto the center of the building.
                    animateCamera(loc, 19.0f);
                }
            };
        });
    }

    // method for hiding all of the polygons on the map
    public void hideAllPolygons()
    {
        for (Polygon poly : polygonBuildings) {
            poly.setVisible(false);
        }
    }

    // method for showing all of the polygons on the map
    public void showAllPolygons()
    {
        for (Polygon poly : polygonBuildings) {
            poly.setVisible(true);
        }
    }

    // this is the listener for the pop up bar at the bottom of the screen.
    public void addPopUpBarListener()
    {
        popUpBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        // can add functionality here if we click on the pop up bar
    }

    // method for hiding all of the markers on the map
    public void hideAllMarkers()
    {
        for (Marker mar : markerBuildings) {
            mar.setVisible(false);
        }
    }

    // method for showing all of the markers on the map
    public void showAllMarkers()
    {
        for (Marker mar : markerBuildings) {
            mar.setVisible(true);
        }
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
    @Override public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        }
        else
        {
            // request for user's permission for location services
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
            requestPermissions();
        }

        mMap.setOnMyLocationChangeListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        FileAccessor locationAccessor = new FileAccessor();
        locationAccessor.setInputStream(getStreamFromFileName("encrypteddtown"));
        locationAccessor.decryptFile(true);
        addLocationsToMap(locationAccessor.obtainContents());  //adds the polygons for the SGW campus
        locationAccessor.resetObject();
        locationAccessor.setInputStream(getStreamFromFileName("encryptedloyola"));
        locationAccessor.decryptFile(true);
        addLocationsToMap(locationAccessor.obtainContents()); //adds the polygons for the Loyola campus

        // Add a marker in Concordia and move the camera
        searchMarker = mMap.addMarker(new MarkerOptions().position(concordiaLatLngDowntownCampus).title("Marker in Concordia"));
        float zoomLevel = 16.0f; // max 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concordiaLatLngDowntownCampus, zoomLevel));
        // Refresh to fix Map not displaying properly
        toggleCampus();
        toggleButton.setChecked(false);
        getCurrentLocation();

        setClickListeners(); // sets the polygon listeners

        //Set custom InfoWindow Adapter
        CustomInfoWindow adapter = new CustomInfoWindow(MapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        initializeSearchBar();
    }

    // moves the camera to keep on user's location on any change in its location
    @Override
    public void onMyLocationChange(Location location)
    {
        LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
        animateCamera(loc, zoomLevel);
    }

    //detects camera movement and the cause for the movement
    @Override
    public void onCameraMoveStarted(int reason)
    {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {   //user gestured on the map
            // remove the onMyLocationChangeListener when user is moving the map otherwise the map is automatically centered
            mMap.setOnMyLocationChangeListener(null);
        }
        else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) { // user tapped somethig on the map
            // logic here
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) { // app moved the camera
            // logic here
        }
    }

    // This is the method that will be used to encrypt all of the input files during the app startup.
    // This method encrypts all of the files that are in the "filestoencrypt" file
    // *** we need to keep this here until the end as i am using it to get the encrypted files for the raw folder ***
    public void encryptAllInputFiles()
    {
        //DES encrypter = new DES();
        InputStream fis = null;
        OutputStream fos = null;
        String filename = "";
        String encryptedFilename = "";
        try {
            InputStream readMe = getResources().openRawResource(getResources().getIdentifier("filestoencrypt", "raw", getPackageName()));
            Scanner reader = new Scanner(readMe);
            while (reader.hasNext())
            {
                filename = reader.nextLine();
                encryptedFilename = "encrypted_" + filename;
                fis = getResources().openRawResource(getResources().getIdentifier(filename, "raw", getPackageName()));
                fos = new FileOutputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), encryptedFilename));
                //encrypter.encryptFile(fis, fos);

                // this is some code that we can use to get the text in the encrypted file
                if (filename.equals("hall9nodes"))
                {
                    fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), encryptedFilename));
                   Scanner readEncrypted = new Scanner(fis);
                    while (readEncrypted.hasNext())
                    {
                        System.out.println(readEncrypted.nextLine());
                    }
                    System.out.println("DONE");
                }
            }
            // close the input and the output streams
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this method will be used for creating the floor graphs by reading form a node encrypted text file.
    public Graph createGraph(String encryptedFileName)
    {
        FileAccessor useMeToRead = new FileAccessor();
        useMeToRead.setInputStream(getStreamFromFileName(encryptedFileName));
        Graph graphName = null;
        // First we need to decrypt the file to have access to the locations
        useMeToRead.decryptFile(true);

        // with the decrypted file, we can add the nodes to the graph
        graphName = Graph.addNodesToGraph(useMeToRead.obtainContents());
        graphName.addAdjacentNodes();
        return graphName;
    }

    public void setClickListeners() {
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                System.out.println("You clicked on this polygon:" + polygon);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // we only want to perform these actions if the marker we clicked on is one of the custom markers.
                if (markerBuildings.contains(marker)) {

                    isInfoWindowShown = false;
                    searchMarker = marker;  // set the global search marker to the marker that has most recently been clicked

                    // move the camera to the marker location
                    animateCamera(marker.getPosition(), zoomLevel);

                    if (!isInfoWindowShown) {
                        marker.showInfoWindow();

                        activeInfoWindow = marker.getTitle();

                        // this sets the parameters for the button that appears on click. (The direction button)
                        directionButton.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams directionButtonLayoutParams = (LinearLayout.LayoutParams) directionButton.getLayoutParams();
                        //directionButtonLayoutParams.topMargin = 200;
                        //directionButtonLayoutParams.leftMargin = -toggleButton.getWidth() + 200;
                        directionButton.setLayoutParams(directionButtonLayoutParams);

                        // this sets the parameters for the button that appears on click. (The explore inside button)
                        exploreInsideButton.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams exploreButtonLayoutParams = (LinearLayout.LayoutParams) exploreInsideButton.getLayoutParams();
                        //exploreButtonLayoutParams.topMargin = 200;
                        //exploreButtonLayoutParams.leftMargin = 400;
                        exploreInsideButton.setLayoutParams(exploreButtonLayoutParams);

                        // this sets the parameters for the pop up bar that appears on click
                        popUpBar.setVisibility(View.VISIBLE);

                    floor1.setVisibility(View.INVISIBLE);
                    floor2.setVisibility(View.INVISIBLE);
                    floor8.setVisibility(View.INVISIBLE);
                    floor9.setVisibility(View.INVISIBLE);


                    if (hallGroundOverlay != null) {
                        hallGroundOverlay.remove();
                    }


                    isInfoWindowShown = true;
                } else {
                    marker.hideInfoWindow();
                    directionButton.setVisibility(View.INVISIBLE);
                    exploreInsideButton.setVisibility(View.INVISIBLE);
                    popUpBar.setVisibility(View.INVISIBLE);
                    floor1.setVisibility(View.INVISIBLE);
                    floor2.setVisibility(View.INVISIBLE);
                    floor8.setVisibility(View.INVISIBLE);
                    floor9.setVisibility(View.INVISIBLE);

                    if (hallGroundOverlay != null) {
                        hallGroundOverlay.remove();
                    }

                        isInfoWindowShown = false;
                        activeInfoWindow = null;
                    }
                }
                return true;
            }
        });

        // when we click on the map, we want to reset back to all the defaults
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                directionButton.setVisibility(View.INVISIBLE);
                exploreInsideButton.setVisibility(View.INVISIBLE);

                floor1.setVisibility(View.INVISIBLE);
                floor2.setVisibility(View.INVISIBLE);
                floor8.setVisibility(View.INVISIBLE);
                floor9.setVisibility(View.INVISIBLE);

                if (hallGroundOverlay != null) {
                    hallGroundOverlay.remove();
                }

                for (Marker m : hall8floorMarkers)
                {
                    m.setVisible(false);
                }
                for (Marker m : hall9floorMarkers)
                {
                    m.setVisible(false);
                }

                popUpBar.setVisibility(View.INVISIBLE);
                isInfoWindowShown = false;
                showAllPolygons();
                showAllMarkers();
            }
        });
    }

    /**
     *
     * This method is used to apply styling to the polygons that will be displayed on the map.
     * Different styles can be created by assigning different tags to the polygons
     * If you need polygons with the same styling just assign them the same tags
     */
    private void stylePolygon(Polygon polygon)
    {
        int strokeColor, fillColor; //color format is #AARRGGBB where AA is for the opacity. 00 is fully transparent. FF is opaque
        String type = "";
        if (polygon.getTag() != null)
            type = polygon.getTag().toString();

        switch (type)
        {
            case "alpha":
                strokeColor = Color.parseColor("#FF000000");
                fillColor = Color.parseColor("#FF74091F");
                break;
            default:
                strokeColor = Color.parseColor("#BB000000");
                fillColor = Color.parseColor("#FF74091F");
        }
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
        polygon.setClickable(true);
    }

    private void addLocationsToMap(String[] location)
    {
        LinkedList<BuildingInfo> buildings = BuildingInfo.obtainBuildings(location);
        //BuildingInfo.encryptFile();
        LinkedList.Node currentBuilding = buildings.getHead();
        for (int i = 0; i < buildings.size(); i++)
        {
            PolygonOptions po = new PolygonOptions();
            LinkedList<LatLng> coordinates = ((BuildingInfo)currentBuilding.getEle()).getCoordinates();
            LinkedList.Node currentCoordinate = coordinates.getHead();

            // add building name to list
            locations.add(((BuildingInfo) currentBuilding.getEle()).getName().trim());

            for (int j = 0; j < coordinates.size(); j++)
            {
                po.add((LatLng)currentCoordinate.getEle());
                currentCoordinate = currentCoordinate.getNext();
            }
            Polygon justAddedPolygon = mMap.addPolygon(po);
            polygonBuildings.add(justAddedPolygon); // add the polygon to the list of polygons
            Resources res = this.getResources();
            int resID = res.getIdentifier(((BuildingInfo)currentBuilding.getEle()).getIconName(), "drawable", this.getPackageName());
            Marker polyMarker = mMap.addMarker(new MarkerOptions()
                    .position(((BuildingInfo)currentBuilding.getEle()).getCenter())
                    .title(((BuildingInfo)currentBuilding.getEle()).getName())
                    .visible(true)
                    .flat(true)
                    .alpha(1)
                    //.snippet("This is a test piece of text to see how it will look like in the window")
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .zIndex(44));
            markerBuildings.add(polyMarker); // add the marker to the list of markers

            if (resID != 0)
            {
                int height = 90;
                int width = 90;
                Bitmap b = BitmapFactory.decodeResource(getResources(), resID);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                polyMarker.setIcon(smallMarkerIcon);
            }
            BuildingInfo Hall_Building = new BuildingInfo(((BuildingInfo)currentBuilding.getEle()).getName(), ((BuildingInfo)currentBuilding.getEle()).getAddress(), ((BuildingInfo)currentBuilding.getEle()).getOpeningTimes());
            polyMarker.setTag(Hall_Building);
            justAddedPolygon.setTag("alpha");
            stylePolygon(justAddedPolygon);
            currentBuilding = currentBuilding.getNext();
        }
    }

    // listener method for when my location button is clicke, resets setMyLocationEnable to true
    // so the camera can stay on the user's location ( camera is disabled to stay on user's location
    // when user gesture moves the camera). Check onCameraMoveStarted listener method
    @Override public boolean onMyLocationButtonClick()
    {
        mMap.setMyLocationEnabled(true);
        return false; // returning false calls the super method, returning true does not
    }

    private void animateCamera(LatLng latLng, float zoomLevel)
    {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    private void getCurrentLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener()
            {
                @Override public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Location currentLocation = (Location) task.getResult();
                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        animateCamera(currentLatLng, zoomLevel);
                    }
                }
            });
        }catch (SecurityException e){
            // some problem occurred, return Concordia downtown Campus Location
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
            e.printStackTrace();
        }
    }

//    private void viewDowntownCampus() {
//        animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
//    }
//
//    private void viewLoyolaCampus() {
//        animateCamera(concordiaLatLngLoyolaCampus, zoomLevel);
//    }

    private void toggleCampus()
    {
        mMap.setOnMyLocationChangeListener(null);
        if (toggleButton.isChecked())
            animateCamera(concordiaLatLngLoyolaCampus, zoomLevel);
        else
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
    }

    private void requestPermissions()
    {
        // permission not granted, request for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted, move the camera to current location
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // This sets the context and is called during the onCreate method.
    public static void setmContext(Context mContext) {
        MapsActivity.mContext = mContext;
    }

    // THis is for non-activity or non-fragment classes to use in order to pull the context of this class, which will
    // then allow them to access resource files, this cannot be done otherwise.
    public static Context getmContext() {
        return mContext;
    }

    public InputStream getStreamFromFileName(String fileName)
    {
        return getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));
    }
}

