package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.scoutconcordia.DataStructures.Graph;
import com.example.scoutconcordia.FileAccess.DES;
import com.example.scoutconcordia.FileAccess.FileAccessor;
import com.example.scoutconcordia.MapInfoClasses.ShuttleInfo;
import com.example.scoutconcordia.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
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
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.example.scoutconcordia.MapInfoClasses.CustomInfoWindow;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    private float zoomLevel = 16.0f;
    // different req code for handling result depending on why permission was asked
    private final int ACCESS_FINE_LOCATION = 9001; // req code for user location permission when starting app
    private final int ACCESS_FINE_LOCATION_DRAW_PATH = 9002; // Req code asking for permission when user selects current location as origin but has not enabled permission
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
    //private Button floor2;
    private Button floor8;
    private Button floor9;
    private Button floorCC1;
    private Button floorCC2;

    private Button floorVE2;
    private Button floorVL1;
    private Button floorVL2;

    private Button floorMB1;
    private Button floorMBS2;

    private BottomAppBar popUpBar;
    private ToggleButton toggleButton;
    private boolean isInfoWindowShown = false;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext; // This context variable is necessary in order for non-activity classes to read resource files.

    private Marker searchMarker;
    private String activeInfoWindow = null;
    private List<Polygon> polygonBuildings = new ArrayList<>();
    private List<Marker> markerBuildings = new ArrayList<>();
    private List<Graph> floorGraphs = new ArrayList<>();
    public static final List<String> locations = new ArrayList<>();    // Concordia buildings list

    // We use this for image overlay of Hall building
    private final LatLng hallOverlaySouthWest = new LatLng(45.496827, -73.578849);
    private final LatLng hallOverlayNorthEast = new LatLng(45.497711, -73.579033);
    private final LatLng veBuildingOverlaySouthWest = new LatLng(45.458849, -73.639018);
    private final LatLng vlBuildingOverlaySouthWest = new LatLng(45.459106, -73.637831);
    private final LatLng mbBuildingOverlaySouthWest = new LatLng(45.494962, -73.578783);
    private GroundOverlay hallGroundOverlay;
    private final LatLng ccOverlaySouthWest = new LatLng(45.458380, -73.640795);
    private GroundOverlay ccGroundOverlay;
    private GroundOverlay veGroundOverlay;
    private GroundOverlay vlGroundOverlay;
    private GroundOverlay mbGroundOverlay;


    private static final Map<String, LatLng> locationMap = new TreeMap<>(); // maps building names to their location
    private String startingPoint; // Concordia Place user selects as starting point. Used to get LatLng form locationMap
    private String destination; // Cooncordia Place user selects as destination. Used to get LatLng form locationMap
    private String startingBuilding;
    private String destinationBuilding;
    private LatLng origin; //origin of directions search
    private Polyline pathPolyline = null; // polyline for displaying the map
    private Dialog setOriginDialog;
    private boolean useMyLocation = true; // whether useMyCurrentLocation button is checked
    private Marker startLocationMarker, destinationMarker = null;
    private int travelMode = 1; // 1 = walking (default), 2 = car, 3 = transit, 4 = shuttle
    private BottomNavigationView travelOptionsMenu = null;
    private boolean shuttleAvailable = false;

    private boolean disabilityPreference = false; //false for no disability, true for disability
    private boolean needMoreDirections = false; //this boolean will be used when getting directions from class to class in another building
    private boolean classToClass = false; //this boolean determines if we are searching from a class in 1 building to a class in another building

    // Displays the Map
    @Override protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setmContext(this);

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
        //addfloor2ButtonListener();
        addfloorCC1ButtonListener();
        addfloorCC2ButtonListener();
        addfloorVE2ButtonListener();
        addfloorVL1ButtonListener();
        addfloorVL2ButtonListener();
        addfloorMB1ButtonListener();
        addfloorMBS2ButtonListener();
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
            nextStep.setVisibility(VISIBLE);
            Object[] path = graph1.breathFirstSearch(point1, point2);
            results.add(path);
            return results;
        }
        else {
            // we have to get more creative with the search and break it down
            // we need to search from class -> escalator, then from escalator -> class on the right floor
            if (disabilityPreference)
            {
                nextStep.setVisibility(VISIBLE); // enable the next step button
                Object[] path1 = graph1.breathFirstSearch(point1, graph1.searchByClassName("ELEVATOR"));
                Object[] path2 = graph2.breathFirstSearch(graph2.searchByClassName("ELEVATOR"), point2);
                results.add(path1);
                results.add(path2);
                return results;
            } else
            {
                nextStep.setVisibility(VISIBLE); // enable the next step button
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
                showHallButtons();
                hideCCButtons();
                floor1.performClick();
                break;
            //case "H-2":
            //    showHallButtons();
            //    hideCCButtons();
            //    floor2.performClick();
            //    break;
            case "H-8":
                showHallButtons();
                hideCCButtons();
                floor8.performClick();
                break;
            case "H-9":
                showHallButtons();
                hideCCButtons();
                floor9.performClick();
                break;
            case "CC-1":
                showCCButtons();
                hideHallButtons();
                floorCC1.performClick();
                break;
            case "CC-2":
                showCCButtons();
                hideHallButtons();
                floorCC2.performClick();
                break;
        }
    }

    public void createFloorGraphs()
    {
        Graph hall_1_floor = createGraph("encryptedhall1nodes", true);
        //Graph hall_2_floor = createGraph("hall2nodes", false);
        Graph hall_8_floor = createGraph("encryptedhall8nodes", true);
        Graph hall_9_floor = createGraph("encryptedhall9nodes", true);
        Graph cc_1_floor = createGraph("encryptedcc1nodes", true);
        Graph cc_2_floor = createGraph("encryptedcc2nodes", true);

        floorGraphs.add(hall_1_floor);
        //floorGraphs.add(hall_2_floor);
        floorGraphs.add(hall_8_floor);
        floorGraphs.add(hall_9_floor);
        floorGraphs.add(cc_1_floor);
        floorGraphs.add(cc_2_floor);

        for (Graph graph : floorGraphs)
        {
            for (Graph.Node node: graph.nodes())
            {
                if (node.getType() == 0)
                {
                    locations.add(node.getRoom());
                    locationMap.put(node.getRoom(), node.getElement());
                }
            }
        }
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

                resetButtonColors();
                floor1.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floor1.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();
                setUpGroundOverlay("hall1p");

                //for (Graph graph : floorGraphs)
                //{
                //    System.out.println(graph.id);
                //    if ((graph.id).equals("Hall 1 floor"))
                //    {
                //        for (Graph.Node node : graph.nodes())
                //        {
                //            mMap.addMarker(new MarkerOptions().position(node.getElement()));
                //        }
                //    }
                //}
            }
        });
    }

//    public void addfloor2ButtonListener()
//    {
//        floor2 = (Button) findViewById(R.id.floor2);
//        floor2.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view) {
//                resetButtonColors();
//                floor2.setBackgroundColor(getResources().getColor(R.color.burgandy));
//                floor2.setTextColor(getResources().getColor((R.color.faintGray)));
//                removeAllFloorOverlays();
//                setUpGroundOverlay("hall2floor");
//            }
//        });
//    }

    public void addfloor8ButtonListener()
    {
        floor8 = (Button) findViewById(R.id.floor8);
        floor8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                resetButtonColors();
                floor8.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floor8.setTextColor(getResources().getColor((R.color.faintGray)));

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

                resetButtonColors();
                floor9.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floor9.setTextColor(getResources().getColor((R.color.faintGray)));
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

                resetButtonColors();
                floorCC1.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorCC1.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 29;
                float overlaySize = 82;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cc_building1", "drawable", getPackageName()));

                ccGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
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

                resetButtonColors();
                floorCC2.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorCC2.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 29;
                float overlaySize = 82;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cc_building2", "drawable", getPackageName()));

                ccGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(ccOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorVE2ButtonListener()
    {
        floorVE2 = (Button) findViewById(R.id.floorVE2);
        floorVE2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVE2.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorVE2.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 29;
                float overlaySize = 50;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("ve_floor2", "drawable", getPackageName()));

                veGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(veBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorVL1ButtonListener()
    {
        floorVL1 = (Button) findViewById(R.id.floorVL1);
        floorVL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVL1.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorVL1.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 209;
                float overlaySize = 71;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("vl_001", "drawable", getPackageName()));

                vlGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(vlBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorVL2ButtonListener()
    {
        floorVL2 = (Button) findViewById(R.id.floorVL2);
        floorVL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorVL2.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorVL2.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = 209;
                float overlaySize = 71;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("vl_002", "drawable", getPackageName()));

                vlGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(vlBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorMB1ButtonListener()
    {
        floorMB1 = (Button) findViewById(R.id.floorMB1);
        floorMB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorMB1.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorMB1.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 42;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("mb_01", "drawable", getPackageName()));

                mbGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(mbBuildingOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation));
            }
        });
    }

    public void addfloorMBS2ButtonListener()
    {
        floorMBS2 = (Button) findViewById(R.id.floorMBS2);
        floorMBS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetButtonColors();
                floorMBS2.setBackgroundColor(getResources().getColor(R.color.burgandy));
                floorMBS2.setTextColor(getResources().getColor((R.color.faintGray)));
                removeAllFloorOverlays();

                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                int imgHeightPixels = dimensions.outHeight;
                float imgHeightInPixels;
                float imgRotation = -56;
                float overlaySize = 42;
                BitmapDescriptor floorPlan = BitmapDescriptorFactory.fromResource(getResources().getIdentifier("mb_s02", "drawable", getPackageName()));

                mbGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(mbBuildingOverlaySouthWest, overlaySize)
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
                if (pathMarker != null)
                {
                    pathMarker.remove();
                }
                searchResultsIndex++; //increment the search result index

                if (searchResultsIndex == searchResults.size())
                {
                    if (needMoreDirections)
                    {
                        /// NEED TO CHECK THIS OUT
                        LatLng[] dest = new LatLng[searchResults.get(searchResultsIndex - 1).length];
                        System.arraycopy(searchResults.get(searchResultsIndex - 1), 0, dest, 0, searchResults.get(searchResultsIndex - 1).length);
                        origin = dest[dest.length - 1];
                        startingPoint = startingBuilding + " Building";
                        getDirections(); // call get directions again to continue getting directions
                    } else
                    {
                        // we want to reset the app back to the initial state
                        searchResultsIndex = 0;
                        searchPath.setVisible(false);
                        nextStep.setVisibility(View.INVISIBLE);
                    }
                } else if (searchResultsIndex == 0) {
                    resetPath();  //erase the path from outdoor directions
                    exploreInsideButton.performClick();
                    displaySearchResults(searchResults.get(searchResultsIndex));
                } else if (searchResultsIndex == 100) {
                        resetPath();
                        nextStep.setVisibility(View.INVISIBLE);  //we have reached the end of the search
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

    private void initializeSearchBar(){
        final AutoCompleteTextView searchBar = findViewById(R.id.search_bar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, locations);
        searchBar.setAdapter(adapter);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setOriginDialog = setOriginDialog();
                destination = searchBar.getText().toString(); //set destination name, to be used to get LatLng from locationMap
                searchBar.setText(null);
                setOriginDialog.show(); // create dialog asking user to select a starting point
            }
        });

        //set autocomplete textview text to null and hide keyboard when dismissed
        searchBar.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                searchBar.setText("");
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    // onClick listener for when "Find your way" button is clicked
    private AlertDialog setOriginDialog(){
        useMyLocation = true; // reset useMyLocation to false
        AlertDialog.Builder builder = new AlertDialog.Builder(this  );
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.outdoor_buildings_diections_ui, null);
        builder.setView(dialogView);

        final AutoCompleteTextView startingLocation = dialogView.findViewById(R.id.starting_location);
        travelOptionsMenu = dialogView.findViewById(R.id.travel_modes_nav_bar);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startingLocation.setAdapter(adapter);

        ///when user clicks to enter starting point, uncheck useMyLocationButton if enabled
        startingLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(useMyLocation){ useMyLocationButtonClicked(dialogView); }
                return false;
            }
        });

        startingLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 startingPoint = startingLocation.getText().toString();
                 if(!enableShuttle(dialogView, locationsInDifferentCampuses(startingPoint, destination))){
                     setModeToWalkIfShuttleSelected();
                 }
            }
        });
        return builder.create();
    }

    private boolean locationsInDifferentCampuses(String startingPoint, String destination){
        LatLng origin = locationMap.get(startingPoint);
        LatLng dest = locationMap.get(destination);

        float[] results = new float[1];
        Location.distanceBetween(origin.latitude, origin.longitude, dest.latitude, dest.longitude, results);
        return results[0] > 2000.0;
    }

    private boolean enableShuttle(View v, boolean enabled){
        BottomNavigationItemView shuttleModeItem = v.findViewById(R.id.shuttle);
        shuttleModeItem.setEnabled(enabled);
        shuttleAvailable = enabled;
        return enabled;
    }

    public void onGetDirectionsClick(View v){
        if(useMyLocation) {
            setOriginDialog.dismiss();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getDirectionsFromCurrentLocation();
            }
            else{
                requestPermissions(ACCESS_FINE_LOCATION_DRAW_PATH);
            }
        }
        else { //starting point not current location
            origin = null;
            // if place is not in locationMap, exception will be thrown
            // No need to check for destination as setOrigin only opens when one of the given options is selected
            try{
               origin = locationMap.get(startingPoint);
            }
            catch(Throwable t){
                Log.d(TAG, ""+t.getMessage());
                Toast.makeText(this, "Invalid starting location", Toast.LENGTH_LONG).show();
            }

            if (startingPoint != null)
            {
                setOriginDialog.dismiss();
                getDirections();
            }
        }
    }

    public void getDirections()
    {
        if(startingPoint != null) {
            if (startingPoint.length() > 8 && startingPoint.substring(startingPoint.length() - 8).equals("Building")) //if the starting point is a building
            {
                if (destination.length() > 8 && destination.substring(destination.length() - 8).equals("Building")) //if the destination is a building
                {
                    if (needMoreDirections)
                    {
                        needMoreDirections = false;
                        searchResultsIndex = 99;
                    }
                    drawDirectionsPath(origin, locationMap.get(destination));
                } else {                                                                                            //if the destination is a classroom
                    String buildingName = destination.split("-")[0] + " Building";
                    String toMe = destination;

                    for (Marker marker : markerBuildings) {
                        if ((marker.getTitle()).equals(buildingName)) {
                            searchMarker.setPosition(marker.getPosition());
                            searchMarker.setVisible(false);
                        }
                    }
                    drawDirectionsPath(origin, locationMap.get(buildingName));

                    if (classToClass)  // if the general search is a class-> class search or just a building->class search
                    {
                        needMoreDirections = false;
                        if (destinationBuilding.equals("H"))
                        {
                            searchResults = searchForClass("H-100", toMe);
                        } else if (destinationBuilding.equals("CC"))
                        {
                            searchResults = searchForClass("CC-150", toMe);
                        }
                    } else
                    {
                        if (startingBuilding.equals("H")) {
                            searchResults = searchForClass("H-100", toMe);
                        } else if (startingBuilding.equals("CC")) {
                            searchResults = searchForClass("CC-150", toMe);
                        }
                    }
                    searchResultsIndex = -1;
                    searchPath.setVisible(true);
                }
            } else //if the starting point is a classroom
            {
                if (destination.length() > 8 && destination.substring(destination.length() - 8).equals("Building")) //if the destination is a building
                {
                    // need to go from class room to exit (we can hard code the exit for H and for CC)
                    startingBuilding = startingPoint.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"
                    for (Marker marker : markerBuildings) { //set the marker onto the desired building
                        if ((marker.getTitle()).equals(startingBuilding + " Building")) {
                            searchMarker.setPosition(marker.getPosition());
                            searchMarker.setVisible(false);
                        }
                    }
                    exploreInsideButton.performClick();

                    if (startingBuilding.equals("H")) {
                        searchResults = searchForClass(startingPoint, "H-100");
                    } else if (startingBuilding.equals("CC")) {
                        searchResults = searchForClass(startingPoint, "CC-150");  //directions to exit for CC building
                    }
                    searchResultsIndex = -1;
                    searchPath.setVisible(true);

                    needMoreDirections = true;

                    // need to go from exit to the building. Will be called in the needMoreDirections loop

                } else // if the destination is a classroom (class -> class)
                {
                    startingBuilding = startingPoint.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"
                    destinationBuilding = destination.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"

                    if (startingBuilding.equals(destinationBuilding))  // if both classes are in the same building
                    {
                        for (Marker marker : markerBuildings) { //set the marker onto the desired building
                            if ((marker.getTitle()).equals(startingBuilding + " Building")) {
                                searchMarker.setPosition(marker.getPosition());
                                searchMarker.setVisible(false);
                            }
                        }
                        exploreInsideButton.performClick();
                        searchResults = searchForClass(startingPoint, destination);
                        searchResultsIndex = -1;
                        searchPath.setVisible(true);
                    } else //if both classes are in different buildings
                    {
                        // go from the class to the building exit
                        for (Marker marker : markerBuildings) { //set the marker onto the desired building
                            if ((marker.getTitle()).equals(startingBuilding + " Building")) {
                                searchMarker.setPosition(marker.getPosition());
                                searchMarker.setVisible(false);
                            }
                        }
                        exploreInsideButton.performClick();

                        if (startingBuilding.equals("H")) {
                            searchResults = searchForClass(startingPoint, "H-100"); //directions to exit for H building
                        } else if (startingBuilding.equals("CC")) {
                            searchResults = searchForClass(startingPoint, "CC-150");  //directions to exit for CC building
                        }

                        searchResultsIndex = -1;
                        searchPath.setVisible(true);

                        needMoreDirections = true;
                        classToClass = true;

                        // go from building to building

                        // go from the building entrance to the class
                    }
                }
            }
        }
    }

    public void useMyLocationButtonClicked(View v){
        RadioButton btn = (RadioButton)v.findViewById(R.id.useMyLocationButton);
        AutoCompleteTextView startingLocation = ((ViewGroup)v.getParent().getParent()).findViewById(R.id.starting_location);

        setModeToWalkIfShuttleSelected();
        enableShuttle((ViewGroup)v.getParent().getParent(), false);
        btn.setChecked(!useMyLocation);
        useMyLocation = !useMyLocation;
    }

    // walking option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onWalkingSelected(MenuItem m){
        m.setChecked(true);
        travelMode = 1;
        if(shuttleAvailable) {
            travelOptionsMenu.findViewById(R.id.shuttle).setEnabled(true);
        }
    }

    // car option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onCarSelected(MenuItem m){
        m.setChecked(true);
        travelMode = 2;
        if(shuttleAvailable) {
            travelOptionsMenu.findViewById(R.id.shuttle).setEnabled(true);
        }
    }

    // transit option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onTransitSelected(MenuItem m){
        m.setChecked(true);
        travelMode = 3;
        if(shuttleAvailable) {
            travelOptionsMenu.findViewById(R.id.shuttle).setEnabled(true);
        }
    }

    // shuttle option selected in dialog for getting directions (in onFindYourWayButtonClick)
    public void onShuttleSelected(MenuItem m){
        m.setChecked(true);
        travelMode = 4;
    }

    private void setModeToWalkIfShuttleSelected(){
        if(travelOptionsMenu.findViewById(R.id.shuttle).isSelected()){
            travelOptionsMenu.getMenu().getItem(0).setChecked(true); // index 0 = MenuItem walking
        }
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
                //// TESTING INDOOR DIRECTIONS
                //String fromMe = "CC-215";
                //String toMe = "CC-219";
                //searchResults = searchForClass(fromMe, toMe);
                //searchResultsIndex = 0;
                //searchPath.setVisible(true);
                //displaySearchResults(searchResults.get(searchResultsIndex));
            }
        });
    }

    // this is the listener for the explore inside button.
    public void addExploreInsideButtonListener()
    {
        exploreInsideButton = (Button) findViewById(R.id.exploreInsideButton);

        exploreInsideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if (activeInfoWindow != null)
                //{
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
                                showHallButtons();
                            } else if (poly.getTag().equals("CC Building"))
                            {
                                showCCButtons();
                            }
                            else if (poly.getTag().equals("VE Building"))
                            {
                                showVEButtons();
                            }
                            else if (poly.getTag().equals("VL Building"))
                            {
                                LatLng ve_location = new LatLng(45.458850, -73.638660);

                                for (Polygon poly2 : polygonBuildings)
                                {
                                    if (PolyUtil.containsLocation(ve_location, poly2.getPoints(), true)){
                                        poly2.setVisible(false);
                                    }
                                }
                                showVLButtons();
                            }
                            else if (poly.getTag().equals("MB Building"))
                            {
                                showMBButtons();
                            }
                        }
                    }
                    // we want to zoom in onto the center of the building.
                    animateCamera(loc, 19.0f);
                //}
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

    public void showHallButtons()
    {
        floor1.setVisibility(View.VISIBLE);
        //floor2.setVisibility(View.VISIBLE);
        floor8.setVisibility(View.VISIBLE);
        floor9.setVisibility(View.VISIBLE);
    }

    public void hideHallButtons()
    {
        floor1.setVisibility(View.INVISIBLE);
        //floor2.setVisibility(View.INVISIBLE);
        floor8.setVisibility(View.INVISIBLE);
        floor9.setVisibility(View.INVISIBLE);
    }

    public void showVEButtons()
    {
        floorVE2.setVisibility(View.VISIBLE);
    }

    public void hideVEButtons()
    {
        floorVE2.setVisibility(View.INVISIBLE);
    }

    public void showVLButtons()
    {
        floorVL1.setVisibility(View.VISIBLE);
        floorVL2.setVisibility(View.VISIBLE);
    }

    public void hideVLButtons()
    {
        floorVL1.setVisibility(View.INVISIBLE);
        floorVL2.setVisibility(View.INVISIBLE);
    }

    public void showMBButtons()
    {
        floorMB1.setVisibility(View.VISIBLE);
        floorMBS2.setVisibility(View.VISIBLE);
    }

    public void hideMBButtons()
    {
        floorMB1.setVisibility(View.INVISIBLE);
        floorMBS2.setVisibility(View.INVISIBLE);
    }

    public void showCCButtons()
    {
        floorCC1.setVisibility(View.VISIBLE);
        floorCC2.setVisibility(View.VISIBLE);
    }

    public void hideCCButtons()
    {
        floorCC1.setVisibility(View.INVISIBLE);
        floorCC2.setVisibility(View.INVISIBLE);
    }


    public void removeAllFloorOverlays(){
        if (hallGroundOverlay != null){
            hallGroundOverlay.remove();
        }
        if (ccGroundOverlay != null){
            ccGroundOverlay.remove();
        }
        if (vlGroundOverlay != null){
            vlGroundOverlay.remove();
        }
        if (veGroundOverlay != null){
            veGroundOverlay.remove();
        }
        if (mbGroundOverlay != null){
            mbGroundOverlay.remove();
        }

    }

    public void resetButtonColors() {
        floor1.setBackgroundResource(android.R.drawable.btn_default);
        floor1.setTextColor(getResources().getColor(R.color.black));
        //floor2.setBackgroundResource(android.R.drawable.btn_default);
        //floor2.setTextColor(getResources().getColor(R.color.black));
        floor8.setBackgroundResource(android.R.drawable.btn_default);
        floor8.setTextColor(getResources().getColor(R.color.black));
        floor9.setBackgroundResource(android.R.drawable.btn_default);
        floor9.setTextColor(getResources().getColor(R.color.black));
        floorCC1.setBackgroundResource(android.R.drawable.btn_default);
        floorCC1.setTextColor(getResources().getColor(R.color.black));
        floorCC2.setBackgroundResource(android.R.drawable.btn_default);
        floorCC2.setTextColor(getResources().getColor(R.color.black));

        floorVE2.setBackgroundResource(android.R.drawable.btn_default);
        floorVE2.setTextColor(getResources().getColor(R.color.black));
        floorVL1.setBackgroundResource(android.R.drawable.btn_default);
        floorVL1.setTextColor(getResources().getColor(R.color.black));
        floorVL2.setBackgroundResource(android.R.drawable.btn_default);
        floorVL2.setTextColor(getResources().getColor(R.color.black));

        floorMB1.setBackgroundResource(android.R.drawable.btn_default);
        floorMB1.setTextColor(getResources().getColor(R.color.black));
        floorMBS2.setBackgroundResource(android.R.drawable.btn_default);
        floorMBS2.setTextColor(getResources().getColor(R.color.black));

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
            requestPermissions(ACCESS_FINE_LOCATION);
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

    /*
    // This is the method that will be used to encrypt all of the input files during the app startup.
    // This method encrypts all of the files that are in the "filestoencrypt" file
    // *** we need to keep this here until the end as i am using it to get the encrypted files for the raw folder ***
    public void encryptAllInputFiles()
    {
        DES encrypter = new DES();
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
                encrypter.encryptFile(fis, fos);

                // this is some code that we can use to get the text in the encrypted file
                if (filename.equals("hall1nodes2"))
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
    } */

    private void drawDirectionsPath(LatLng origin, LatLng dest){
        resetPath();
        List<LatLng> path = new ArrayList();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(context);
        TravelMode mode = getTraveMode();
        if(mode != null) {
            request.mode(mode).origin(origin.latitude + "," + origin.longitude).destination(dest.latitude + "," + dest.longitude);
        }

        try {
            DirectionsResult res = request.await();
            // adds coordinates of each step on the first route give to the List<LatLng> for drawing the polyline
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0]; // take the first route
                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) { //loop through all the legs
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {    // loop through all the steps
                                DirectionsStep step = leg.steps[j];
                                EncodedPolyline polyline = step.polyline;
                                if (polyline != null) {
                                    List<com.google.maps.model.LatLng> coordinates = polyline.decodePath();
                                    for (com.google.maps.model.LatLng coord : coordinates) {
                                        path.add(new LatLng(coord.lat, coord.lng));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            startLocationMarker = mMap.addMarker(new MarkerOptions().position(origin));
            destinationMarker = mMap.addMarker(new MarkerOptions().position(dest));
            //draw path
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            pathPolyline = mMap.addPolyline(opts);
            animateCamera(origin, zoomLevel);
            // set starting point and destination to null
            //resetGetDirectionParams();
        }
        catch(Throwable t){
            Log.d(TAG, t.getMessage());
        }
    }

    private TravelMode getTraveMode(){
        TravelMode mode = null;
        switch (travelMode){
            case 1:
                mode = TravelMode.WALKING;
                break;
            case 2:
                mode = TravelMode.DRIVING;
                break;
            case 3:
            case 4:
                mode = TravelMode.TRANSIT;
                break;
        }
        return mode;
    }

    // remove previous path and markers
    private void resetPath(){
        if(pathPolyline != null) {
            try {
                startLocationMarker.remove();
                destinationMarker.remove();
                pathPolyline.remove(); //remove previous path
            }catch(Throwable t){t.printStackTrace();}
        }
    }

    private void resetGetDirectionParams(){
        startingPoint = null;
        destination = null;
        travelMode = 1;
    }

    // this method will be used for creating the floor graphs by reading form a node encrypted text file.
    public Graph createGraph(String encryptedFileName, boolean isEncrypted)
    {
        FileAccessor useMeToRead = new FileAccessor();
        useMeToRead.setInputStream(getStreamFromFileName(encryptedFileName));
        Graph graphName = null;
        // First we need to decrypt the file to have access to the locations
        useMeToRead.decryptFile(isEncrypted);

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
                if (markerBuildings.contains(marker))
                {
                    isInfoWindowShown = false;
                    searchMarker = marker;  // set the global search marker to the marker that has most recently been clicked

                    // move the camera to the marker location
                    animateCamera(marker.getPosition(), zoomLevel);

                    if (!isInfoWindowShown) {
                        marker.showInfoWindow();

                        activeInfoWindow = marker.getTitle();

                    // this sets the parameters for the button that appears on click. (The direction button)
                    directionButton.setVisibility(VISIBLE);
                    LinearLayout.LayoutParams directionButtonLayoutParams = (LinearLayout.LayoutParams) directionButton.getLayoutParams();
                    //directionButtonLayoutParams.topMargin = 200;
                    //directionButtonLayoutParams.leftMargin = -toggleButton.getWidth() + 200;
                    directionButton.setLayoutParams(directionButtonLayoutParams);

                    // this sets the parameters for the button that appears on click. (The explore inside button)
                    exploreInsideButton.setVisibility(VISIBLE);
                    LinearLayout.LayoutParams exploreButtonLayoutParams = (LinearLayout.LayoutParams) exploreInsideButton.getLayoutParams();
                    //exploreButtonLayoutParams.topMargin = 200;
                    //exploreButtonLayoutParams.leftMargin = 400;
                    exploreInsideButton.setLayoutParams(exploreButtonLayoutParams);

                    // this sets the parameters for the pop up bar that appears on click
                    popUpBar.setVisibility(VISIBLE);

                        hideHallButtons();
                        hideCCButtons();
                        hideVEButtons();
                        hideVLButtons();
                        hideMBButtons();

                        removeAllFloorOverlays();

                        isInfoWindowShown = true;
                } else {
                        marker.hideInfoWindow();
                        directionButton.setVisibility(View.INVISIBLE);
                        exploreInsideButton.setVisibility(View.INVISIBLE);
                        popUpBar.setVisibility(View.INVISIBLE);

                        hideHallButtons();
                        hideCCButtons();
                        hideVEButtons();
                        hideVLButtons();
                        hideMBButtons();


                        removeAllFloorOverlays();

                        isInfoWindowShown = false;
                        activeInfoWindow = null;
                    }
                } else {
                    System.out.println(marker.getPosition());
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

                hideHallButtons();
                hideCCButtons();
                hideVEButtons();
                hideVLButtons();
                hideMBButtons();



                removeAllFloorOverlays();

                popUpBar.setVisibility(View.INVISIBLE);
                isInfoWindowShown = false;
                showAllPolygons();
                showAllMarkers();
                resetGetDirectionParams();

                //System.out.println(latLng);
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
            // add building name adn coordinates to the map
            locationMap.put(((BuildingInfo) currentBuilding.getEle()).getName().trim(), (LatLng)currentCoordinate.getEle());

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

    // animates camera to user's current location upon successfully retrieving its location
    private void getCurrentLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            final Task location = fusedLocationProviderClient.getLastLocation();
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

    // calls drawDirectionsPath upon successfully retrieving user's location
    private void getDirectionsFromCurrentLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener()
            {
                @Override public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Location currentLocation = (Location) task.getResult();
                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        drawDirectionsPath(currentLatLng, locationMap.get(destination));
                    }
                }
            });
        }catch (SecurityException e){
            // some problem occurred, return Concordia downtown Campus Location
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

    private void requestPermissions(int requestCode)
    {
        // permission not granted, request for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted, move the camera to current location
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
                break;
            case ACCESS_FINE_LOCATION_DRAW_PATH:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted,
                    getDirectionsFromCurrentLocation();
                }
                break;
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

