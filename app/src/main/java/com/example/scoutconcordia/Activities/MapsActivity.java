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

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.example.scoutconcordia.MapInfoClasses.CustomInfoWindow;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    private float zoomLevel = 16.0f;
    private final int ACCESS_FINE_LOCATION = 9001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng concordiaLatLngDowntownCampus = new LatLng(45.494619, -73.577376);
    private final LatLng concordiaLatLngLoyolaCampus = new LatLng(45.458423, -73.640460);
    private Button directionButton;
    private Button exploreInsideButton;
    private Button nextStep;

    private List<Object[]> searchResults = new ArrayList<>();
    private int searchResultsIndex;
    private Polyline searchPath;
    private Marker pathMarker;

    private Button floor1;
    private Button floor2;
    private Button floor8;
    private Button floor9;
    private Button floorCC1;
    private Button floorCC2;

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
    public static final List<String> locations = new ArrayList<>();    // Concordia buildings list

    // We use this for image overlay of Hall building
    private final LatLng hallOverlaySouthWest = new LatLng(45.496827, -73.578849);
    private final LatLng hallOverlayNorthEast = new LatLng(45.497711, -73.579033);
    private GroundOverlay hallGroundOverlay;
    private final LatLng ccOverlaySouthWest = new LatLng(45.458380, -73.640795);
    private GroundOverlay ccGroundOverlay;

    private GroundOverlayOptions goo1;
    private GroundOverlayOptions goo2;
    private GroundOverlayOptions goo8;
    private GroundOverlayOptions goo9;

    private boolean disabilityPreference = false; //false for no disability, true for disability

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
        addfloorCC1ButtonListener();
        addfloorCC2ButtonListener();
        addNextStepListener();

        createFloorGraphs();
        
        // lets encrypt all of the files before using them
        //encryptAllInputFiles();
    }

    public List<Object[]> searchForClass(String fromMe, String toMe) {
        // if we dont find the to me location on the same floor we need to send it to the escalator.
        LatLng point1 = null;
        LatLng point2 = null;
        String floor1 = "";
        String floor2 = "";
        Graph graph1 = null;
        Graph graph2 = null;
        List<Object[]> results = new ArrayList<>();

        for (Graph graph : floorGraphs)
        {
            point1 = graph.searchByClassName(fromMe);
            if (point1 != null)
            {
                graph1 = graph;
                floor1 = graph.id;
                break;
            }
        }

        for (Graph graph: floorGraphs)
        {
            point2 = graph.searchByClassName(toMe);
            if (point2 != null)
            {
                graph2 = graph;
                floor2 = graph.id;
                break;
            }
        }

        //if either of the points are null then we stop right away.
        if (point1 == null || point2 == null)
        {
            Log.w("ERROR: ", "The location you selected was not found");
            return null;
        }

        // we want to check if both of the locations are on the same floor
        if (floor1.equals(floor2))  //this is an easy search as they are on the same floor
        {
            Object[] path = graph1.breathFirstSearch(point1, point2);
            results.add(path);
            return results;
        }
        else {
            // we have to get more creative with the search and break it down
            // we need to search from class -> escalator, then from escalator -> class on the right floor
            if (disabilityPreference)
            {
                nextStep.setVisibility(View.VISIBLE); // enable the next step button
                Object[] path1 = graph1.breathFirstSearch(point1, graph1.searchByClassName("ELEVATOR"));
                Object[] path2 = graph2.breathFirstSearch(graph2.searchByClassName("ELEVATOR"), point2);
                results.add(path1);
                results.add(path2);
                return results;
            } else
            {
                nextStep.setVisibility(View.VISIBLE); // enable the next step button
                Object[] path1 = graph1.breathFirstSearch(point1, graph1.searchByClassName("ESCALATOR"));
                Object[] path2 = graph2.breathFirstSearch(graph2.searchByClassName("ESCALATOR"), point2);
                results.add(path1);
                results.add(path2);
                return results;
            }
        }
    }

    // this method is going to be used to display the search results from the searchForClass method
    public void displaySearchResults(Object[] results)
    {
        LatLng[] dest = new LatLng[results.length];
        System.arraycopy(results, 0, dest, 0, results.length);

        //List<LatLng> listOfPoints = new ArrayList<>();
        searchPath.setPoints(Arrays.asList(dest));
        
        // get the first point and the last point
        LatLng point1 = dest[0];
        LatLng point2 = dest[dest.length - 1];
        String point1Floor = "a";
        String point2Floor = "b";
        String showFloor = "";

        pathMarker = mMap.addMarker(new MarkerOptions()
                .position(point2)
                .visible(true));

        // we also need to consider if the point is an elevator/escalator so we need to check both points.
        for (Graph graph: floorGraphs)
        {
            for (Graph.Node node: graph.nodes())
            {
                if (point1 == node.getElement() && node.getType() == 0) //if the location matches and it is a classroom node
                {
                    point1Floor = node.getRoom().substring(0, node.getRoom().indexOf("-") + 2); // e.g "H-8" or "MB-1"
                    showFloor = point1Floor;
                    break;
                } else if (point2 == node.getElement() && node.getType() == 0) //if the location matches and it is a classroom node
                {
                    point2Floor = node.getRoom().substring(0, node.getRoom().indexOf("-") + 2); // e.g "H-8" or "MB-1"
                    showFloor = point2Floor;
                    break;
                }
            }
            if (!showFloor.equals(""))  //if they got a new value that isn't the default value we found the floor
            {
                break;
            }
        }

        // need to determine which floor map to show.
        switch (showFloor) {
            case "H-0":
            case "H-1":
                floor1.performClick();
                break;
            case "H-8":
                floor8.performClick();
                break;
            case "H-9":
                floor9.performClick();
                break;
        }
    }

    public void createFloorGraphs()
    {
        Graph hall_8_floor = createGraph("encryptedhall8nodes");
        Graph hall_9_floor = createGraph("encryptedhall9nodes");

        floorGraphs.add(hall_8_floor);
        floorGraphs.add(hall_9_floor);
    }

    public void setUpGroundOverlay(String image)
    {
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        int imgHeightPixels = dimensions.outHeight;
        float imgHeightInPixels;
        float imgRotation = -56;
        float overlaySize = 75;
        BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier(image, "drawable", getPackageName()));

        hallGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(floorPlan)
                .position(hallOverlaySouthWest, overlaySize)
                .anchor(0, 1)
                .bearing(imgRotation));
    }

    public void addfloor1ButtonListener()
    {
        floor1 = (Button) findViewById(R.id.floor1);
        floor1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                removeAllFloorOverlays();
                setUpGroundOverlay("hall1p");
            }
        });
    }

    public void addfloor2ButtonListener()
    {
        floor2 = (Button) findViewById(R.id.floor2);
        floor2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {

                removeAllFloorOverlays();
                setUpGroundOverlay("hall2floor");
            }
        });
    }

    public void addfloor8ButtonListener()
    {
        floor8 = (Button) findViewById(R.id.floor8);
        floor8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                removeAllFloorOverlays();
                setUpGroundOverlay("hall8p");
            }
        });
    }

    public void addfloor9ButtonListener()
    {
        floor9 = (Button) findViewById(R.id.floor9);
        floor9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                removeAllFloorOverlays();
                setUpGroundOverlay("hall9p");
            }
        });
    }

    public void addfloorCC1ButtonListener()
    {
        floorCC1 = (Button) findViewById(R.id.floorCC1);
        floorCC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 29;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cc_building1", "drawable", getPackageName()));

                hallGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(ccOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorCC2ButtonListener()
    {
        floorCC2 = (Button) findViewById(R.id.floorCC2);
        floorCC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 29;
                float overlaySize = 75;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cc_building2", "drawable", getPackageName()));

                hallGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(ccOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addNextStepListener()
    {
        nextStep = (Button) findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathMarker.remove();
                searchResultsIndex++; //increment the search result index

                if (searchResultsIndex == searchResults.size())
                {
                    // we want to reset the app back to the initial state
                    searchResultsIndex = 0;
                    searchPath.setVisible(false);
                    nextStep.setVisibility(View.INVISIBLE);
                } else
                {
                    displaySearchResults(searchResults.get(searchResultsIndex));
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

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TESTING INDOOR DIRECTIONS
                String fromMe = "H-811";
                String toMe = "H-927.04";
                searchResults = searchForClass(fromMe, toMe);
                searchResultsIndex = 0;
                searchPath.setVisible(true);
                displaySearchResults(searchResults.get(searchResultsIndex));
            }
        });
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

                            removeAllFloorOverlays();

                            if (poly.getTag().equals("H Building"))
                            {
                                floor1.setVisibility(View.VISIBLE);
                                floor2.setVisibility(View.VISIBLE);
                                floor8.setVisibility(View.VISIBLE);
                                floor9.setVisibility(View.VISIBLE);
                            } else if (poly.getTag().equals("CC Building"))
                            {
                                floorCC1.setVisibility(View.VISIBLE);
                                floorCC2.setVisibility(View.VISIBLE);
                            }
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

    public void removeAllFloorOverlays(){

        if (hallGroundOverlay != null){
            hallGroundOverlay.remove();
        }
//        if (goo2 != null) {
//            hallGroundOverlay.remove();
//        }
//        if (goo8 != null) {
//            hallGroundOverlay.remove();
//        }
//        if (goo1 != null) {
//            hallGroundOverlay.remove();
//        }
//        if (goo9 != null) {
//            hallGroundOverlay.remove();
//        }
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

        searchPath = mMap.addPolyline(new PolylineOptions());
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
                        floorCC1.setVisibility(View.INVISIBLE);
                        floorCC2.setVisibility(View.INVISIBLE);

                        removeAllFloorOverlays();

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
                        floorCC1.setVisibility(View.INVISIBLE);
                        floorCC2.setVisibility(View.INVISIBLE);

                        removeAllFloorOverlays();

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

                removeAllFloorOverlays();


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
            justAddedPolygon.setTag(((BuildingInfo) currentBuilding.getEle()).getName().trim());
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

