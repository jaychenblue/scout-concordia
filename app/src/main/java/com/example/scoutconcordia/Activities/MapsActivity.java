package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.scoutconcordia.DataStructures.Graph;
import com.example.scoutconcordia.Directions;
import com.example.scoutconcordia.ExternalButtonListener;
import com.example.scoutconcordia.FileAccess.FileAccessor;
import com.example.scoutconcordia.Locations;
import com.example.scoutconcordia.MapInfoClasses.ShuttleInfo;
import com.example.scoutconcordia.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.PolyUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.example.scoutconcordia.MapInfoClasses.CustomInfoWindow;
import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;
import static android.content.ContentValues.TAG;
import static android.view.View.VISIBLE;
import static com.example.scoutconcordia.Directions.displaySearchResults;
import static com.example.scoutconcordia.Directions.getDirections;
import static com.example.scoutconcordia.Locations.addLocationsToMap;
import static com.example.scoutconcordia.Locations.addRestaurantsToMap;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    protected static GoogleMap mMap;
    protected static float zoomLevel = 16.0f;
    // different req code for handling result depending on why permission was asked
    private final int ACCESS_FINE_LOCATION = 9001; // req code for user location permission when starting app
    private final int ACCESS_FINE_LOCATION_DRAW_PATH = 9002; // Req code asking for permission when user selects current location as origin but has not enabled permission
    private final int RC_CALENDAR_ACTIVITY = 9003;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng concordiaLatLngDowntownCampus = new LatLng(45.494619, -73.577376);
    private final LatLng concordiaLatLngLoyolaCampus = new LatLng(45.458423, -73.640460);
    private Button directionButton;
    protected static Button exploreInsideButton;
    protected static Button nextStep;

    protected static List<Object[]> searchResults = new ArrayList<>();
    protected static int searchResultsIndex;
    protected static Polyline searchPath;
    protected static Marker pathMarker;

    protected static Button floor1;
    protected static Button floor2;
    protected static Button floor8;
    protected static Button floor9;
    protected static Button floorCC1;
    protected static Button floorCC2;
    protected static Button floorVE2;
    protected static Button floorVL1;
    protected static Button floorVL2;
    protected static Button floorMB1;
    protected static Button floorMBS2;

    private BottomAppBar popUpBar;
    private ToggleButton toggleButton;
    private boolean isInfoWindowShown = false;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext; // This context variable is necessary in order for non-activity classes to read resource files.

    protected static Marker searchMarker;
    private String activeInfoWindow = null;
    protected static List<Polygon> polygonBuildings = new ArrayList<>();  // list of polygons, representing all of the campus buildings
    protected static List<Marker> markerBuildings = new ArrayList<>();  // List of markers for all of the campus buildings
    protected static List<Marker> restaurantMarkers = new ArrayList<>();  // List of markers for all of the restaurants (POI)
    protected static List<Graph> floorGraphs = new ArrayList<>();  // List of graphs, representing the graphs used for indoor directions.
    public static final List<String> locations = new ArrayList<>();    // Concordia buildings list

    // We use this for image overlay of Hall building
    protected static final LatLng hallOverlaySouthWest = new LatLng(45.496827, -73.578849);
    protected static final LatLng veBuildingOverlaySouthWest = new LatLng(45.458849, -73.639018);
    protected static final LatLng vlBuildingOverlaySouthWest = new LatLng(45.459106, -73.637831);
    protected static final LatLng mbBuildingOverlaySouthWest = new LatLng(45.494962, -73.578783);
    protected static GroundOverlay hallGroundOverlay;
    protected static final LatLng ccOverlaySouthWest = new LatLng(45.458380, -73.640795);
    protected static GroundOverlay ccGroundOverlay;
    protected static GroundOverlay veGroundOverlay;
    protected static GroundOverlay vlGroundOverlay;
    protected static GroundOverlay mbGroundOverlay;

    protected static final Map<String, LatLng> locationMap = new TreeMap<>(); // maps building names to their location
    protected static String startingPoint; // Concordia Place user selects as starting point. Used to get LatLng form locationMap
    protected static String destination; // Concordia Place user selects as destination. Used to get LatLng form locationMap
    protected static String startingBuilding;  // The letter of the building. ex: "H"
    protected static String destinationBuilding;  // The letter of the building. ex: "H"
    protected static LatLng origin; //origin of directions search
    protected static Polyline pathPolyline = null; // polyline for displaying the map
    private Dialog setOriginDialog;
    protected static boolean useMyLocation = true; // whether useMyCurrentLocation button is checked
    protected static Marker startLocationMarker;
    protected static Marker destinationMarker = null;
    protected static int travelMode = 1; // 1 = walking (default), 2 = car, 3 = transit, 4 = shuttle
    private BottomNavigationView travelOptionsMenu = null;
    private boolean shuttleAvailable = false;

//    protected static boolean disabilityPreference = false; //false for no disability, true for disability
    protected static boolean needMoreDirections = false; //this boolean will be used when getting directions from class to class in another building
    protected static boolean classToClass = false; //this boolean determines if we are searching from a class in 1 building to a class in another building

    protected static TextView travelTime;  //estimated travel time
    protected static GeoApiContext geoApiContext = null;

    /**
     * Defining all literals that are used multiple times as constants as to reduce potential errors
     * if the code ever needs to be modified
     */
    protected static final String DRAWABLE = "drawable";
    protected static final String BUILDING = "building"; //building type
    protected static final String BUILDING_NAME = "Building"; //building name
    protected static final String H100 = "H-100";
    protected static final String CC150 = "CC-150";
    protected static final int VISIBLE_INT = 0;
    protected static final int INVISIBLE_INT = 4;

    // Displays the Map
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setmContext(this);

        //Toolbar on top of the page
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //toggle for different campuses
        addListenerOnToggle();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar_activity_maps);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_schedule:
                        Intent calendarIntent = new Intent(MapsActivity.this, CalendarActivity.class);
                        calendarIntent.putStringArrayListExtra("locations", (ArrayList<String>) locations);
                        startActivity(calendarIntent);
                        MapsActivity.this.overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_shuttle:
                        Intent shuttleIntent = new Intent(MapsActivity.this, ShuttleScheduleActivity.class);
                        startActivity(shuttleIntent);
                        MapsActivity.this.overridePendingTransition(0, 0);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });

        exploreInsideButton = findViewById(R.id.exploreInsideButton);
        ExternalButtonListener.addExploreInsideButtonListener();

        addDirectionButtonListener();
        addPopUpBarListener();

        // adding button listeners for all of the floors.
        floor1 = findViewById(R.id.floor1);
        floor2 = findViewById(R.id.floor2);
        floor8 = findViewById(R.id.floor8);
        floor9 = findViewById(R.id.floor9);
        floorCC1 = findViewById(R.id.floorCC1);
        floorCC2 = findViewById(R.id.floorCC2);
        floorVE2 = findViewById(R.id.floorVE2);
        floorVL1 = findViewById(R.id.floorVL1);
        floorVL2 = findViewById(R.id.floorVL2);
        floorMB1 = findViewById(R.id.floorMB1);
        floorMBS2 = findViewById(R.id.floorMBS2);
        nextStep = findViewById(R.id.nextStep);
        ExternalButtonListener.addfloor8ButtonListener();
        ExternalButtonListener.addfloor9ButtonListener();
        ExternalButtonListener.addfloor1ButtonListener();
        ExternalButtonListener.addfloor2ButtonListener();
        ExternalButtonListener.addfloorCC1ButtonListener();
        ExternalButtonListener.addfloorCC2ButtonListener();
        ExternalButtonListener.addfloorVE2ButtonListener();
        ExternalButtonListener.addfloorVL1ButtonListener();
        ExternalButtonListener.addfloorVL2ButtonListener();
        ExternalButtonListener.addfloorMB1ButtonListener();
        ExternalButtonListener.addfloorMBS2ButtonListener();
        ExternalButtonListener.addNextStepListener();

        createFloorGraphs();

        travelTime = findViewById((R.id.estimatedTravelTime));  //this is for the drawDirectionsPath method of the Directions class
        geoApiContext = new GeoApiContext.Builder()         //this is for the drawDirectionsPath method of the Directions class
                .apiKey(getString(R.string.google_maps_key))
                .build();
    }

    public void createFloorGraphs()
    {
        Graph hall_1_floor = createGraph("encryptedhall1nodes", true);
        Graph hall_2_floor = createGraph("encryptedhall2nodes", true);
        Graph hall_8_floor = createGraph("encryptedhall8nodes", true);
        Graph hall_9_floor = createGraph("encryptedhall9nodes", true);
        Graph cc_1_floor = createGraph("encryptedcc1nodes", true);
        Graph cc_2_floor = createGraph("encryptedcc2nodes", true);

        floorGraphs.add(hall_1_floor);
        floorGraphs.add(hall_2_floor);
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

    // If button pushed change Campus
    public void addListenerOnToggle()
    {
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleCampus();
            }
        });
    }

    private void initializeSearchBar(){
        final AutoCompleteTextView searchBar = findViewById(R.id.search_bar);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, locations);
        searchBar.setAdapter(adapter);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchBar.getText().toString().equals("restaurants near me"))
                {
                    setRestaurantMarkersVisibility(true);
                }
                else
                {
                    setOriginDialog = setOriginDialog();
                    destination = searchBar.getText().toString(); //set destination name, to be used to get LatLng from locationMap
                    searchBar.setText(null);
                    setOriginDialog.show(); // create dialog asking user to select a starting point
                }
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
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.travel_modes_nav_bar);
        MenuItem shuttleMenuItem = bottomNavigationView.getMenu().getItem(3);
        shuttleMenuItem.setEnabled(enabled);
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

    public void useMyLocationButtonClicked(View v){
        RadioButton btn = v.findViewById(R.id.useMyLocationButton);
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
        directionButton = findViewById(R.id.directionsButton);
        // can add a functionality here that gives us the directions when we press on the button

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOriginDialog = setOriginDialog();
                destination = searchMarker.getTitle(); //set destination name
                setOriginDialog.show();
            }
        });
    }

    // this is the listener for the pop up bar at the bottom of the screen.
    public void addPopUpBarListener()
    {
        popUpBar = findViewById(R.id.bottomAppBar);
    }

    // method for setting the visibility of the polygons on the map.
    public void setPolygonBuildingsVisibility(Boolean visibility)
    {
        for (Polygon poly : polygonBuildings)
            poly.setVisible(visibility);
    }

    // method for setting the visibility of the building markers on the map.
    public void setBuildingMarkersVisibility(Boolean visibility)
    {
        for (Marker mar : markerBuildings)
            mar.setVisible(visibility);
    }

    // method for setting the visibility of the restaurant markers.
    public void setRestaurantMarkersVisibility(Boolean visibility)
    {
        for (Marker mar : restaurantMarkers)
            mar.setVisible(visibility);
    }

    /**
     * This method sets the visibility parameters for the Hall Buttons which show the floors and call the floor maps when clicked.
     * @param visibility boolean which holds whether a button should be viewable
     */
    public static void setHallButtonsVisibility(Boolean visibility)
    {
        int vis;
        if (visibility)
            vis = VISIBLE_INT;
        else
            vis = INVISIBLE_INT;
        floor1.setVisibility(vis);
        floor2.setVisibility(vis);
        floor8.setVisibility(vis);
        floor9.setVisibility(vis);
    }

    /**
     * This method sets the visibility parameters for the VE Building Buttons which show the floors and call the floor maps when clicked.
     * @param visibility boolean which holds whether a button should be viewable
     */
    public static void setVEButtonsVisibility(Boolean visibility)
    {
        int vis;
        if (visibility)
            vis = VISIBLE_INT;
        else
            vis = INVISIBLE_INT;
        floorVE2.setVisibility(vis);
    }

    /**
     * This method sets the visibility parameters for the VL Building Buttons which show the floors and call the floor maps when clicked.
     * @param visibility boolean which holds whether a button should be viewable
     */
    public static void setVLButtonsVisibility(Boolean visibility)
    {
        int vis;
        if (visibility)
            vis = VISIBLE_INT;
        else
            vis = INVISIBLE_INT;
        floorVL1.setVisibility(vis);
        floorVL2.setVisibility(vis);
    }

    /**
     * This method sets the visibility parameters for the MB Building Buttons which show the floors and call the floor maps when clicked.
     * @param visibility boolean which holds whether a button should be viewable
     */
    public static void setMBButtonsVisibility(Boolean visibility)
    {
        int vis;
        if (visibility)
            vis = VISIBLE_INT;
        else
            vis = INVISIBLE_INT;
        floorMB1.setVisibility(vis);
        floorMBS2.setVisibility(vis);
    }

    /**
     * This method sets the visibility parameters for the CC Building Buttons which show the floors and call the floor maps when clicked.
     * @param visibility boolean which holds whether a button should be viewable
     */
    public static void setCCButtonsVisibility(Boolean visibility)
    {
        int vis;
        if (visibility)
            vis = VISIBLE_INT;
        else
            vis = INVISIBLE_INT;
        floorCC1.setVisibility(vis);
        floorCC2.setVisibility(vis);
    }

    /**
     * This method essentially sweeps all the families of floor maps, grouped by buldings,
     * and removes the overlays displayed on the google maps.
     */
    public static void removeAllFloorOverlays()
    {
        if (hallGroundOverlay != null)
            hallGroundOverlay.remove();
        if (ccGroundOverlay != null)
            ccGroundOverlay.remove();
        if (vlGroundOverlay != null)
            vlGroundOverlay.remove();
        if (veGroundOverlay != null)
            veGroundOverlay.remove();
        if (mbGroundOverlay != null)
            mbGroundOverlay.remove();
    }

    /**
     * This method resets button colours to the default, considering they might have been changed
     * following a click event triggered by the user.
     */
    public static void resetButtonColors()
    {
        floor1.setBackgroundResource(android.R.drawable.btn_default);
        floor1.setTextColor(getmContext().getResources().getColor(R.color.black));
        floor2.setBackgroundResource(android.R.drawable.btn_default);
        floor2.setTextColor(getmContext().getResources().getColor(R.color.black));
        floor8.setBackgroundResource(android.R.drawable.btn_default);
        floor8.setTextColor(getmContext().getResources().getColor(R.color.black));
        floor9.setBackgroundResource(android.R.drawable.btn_default);
        floor9.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorCC1.setBackgroundResource(android.R.drawable.btn_default);
        floorCC1.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorCC2.setBackgroundResource(android.R.drawable.btn_default);
        floorCC2.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorVE2.setBackgroundResource(android.R.drawable.btn_default);
        floorVE2.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorVL1.setBackgroundResource(android.R.drawable.btn_default);
        floorVL1.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorVL2.setBackgroundResource(android.R.drawable.btn_default);
        floorVL2.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorMB1.setBackgroundResource(android.R.drawable.btn_default);
        floorMB1.setTextColor(getmContext().getResources().getColor(R.color.black));
        floorMBS2.setBackgroundResource(android.R.drawable.btn_default);
        floorMBS2.setTextColor(getmContext().getResources().getColor(R.color.black));
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
        locationAccessor.resetObject();
        locationAccessor.setInputStream(getStreamFromFileName("encrypted_restaurants_sgw"));
        locationAccessor.decryptFile(true);
        addRestaurantsToMap(locationAccessor.obtainContents());  //adds the restaurants to the SGW campus
        locationAccessor.resetObject();
        locationAccessor.setInputStream(getStreamFromFileName("encrypted_restaurants_loyola"));
        locationAccessor.decryptFile(true);
        addRestaurantsToMap(locationAccessor.obtainContents());  //adds the restaurants to the loyola campus
        locations.add("restaurants near me");

        // Add a marker in Concordia and move the camera
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
        searchMarker = mMap.addMarker(new MarkerOptions().position(concordiaLatLngDowntownCampus).visible(false));
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

    protected static TravelMode getTravelMode()
    {
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
    protected static void resetPath()
    {
        if(pathPolyline != null) {
            try {
                startLocationMarker.remove();
                destinationMarker.remove();
                pathPolyline.remove(); //remove previous path
            }catch(Throwable t){t.printStackTrace();}
        }
    }

    private void resetGetDirectionParams()
    {
        startingPoint = null;
        destination = null;
        travelMode = 1;
    }

    // this method will be used for creating the floor graphs by reading form a node encrypted text file.
    public Graph createGraph(String encryptedFileName, boolean isEncrypted)
    {
        FileAccessor useMeToRead = new FileAccessor();
        useMeToRead.setInputStream(getStreamFromFileName(encryptedFileName));
        Graph graphName;
        // First we need to decrypt the file to have access to the locations
        useMeToRead.decryptFile(isEncrypted);

        // with the decrypted file, we can add the nodes to the graph
        graphName = Graph.addNodesToGraph(useMeToRead.obtainContents());
        graphName.addAdjacentNodes();
        return graphName;
    }

    public void setClickListeners()
    {
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
                    animateCamera(marker.getPosition(), zoomLevel);       // move the camera to the marker location
                    if (!isInfoWindowShown)
                    {
                        marker.showInfoWindow();
                        activeInfoWindow = marker.getTitle();
                        // this sets the parameters for the button that appears on click. (The direction button)
                        directionButton.setVisibility(VISIBLE);
                        LinearLayout.LayoutParams directionButtonLayoutParams = (LinearLayout.LayoutParams) directionButton.getLayoutParams();
                        directionButton.setLayoutParams(directionButtonLayoutParams);
                        // this sets the parameters for the button that appears on click. (The explore inside button)
                        exploreInsideButton.setVisibility(VISIBLE);
                        LinearLayout.LayoutParams exploreButtonLayoutParams = (LinearLayout.LayoutParams) exploreInsideButton.getLayoutParams();
                        exploreInsideButton.setLayoutParams(exploreButtonLayoutParams);
                        popUpBar.setVisibility(VISIBLE); // this sets the parameters for the pop up bar that appears on click
                        isInfoWindowShown = true;
                    }
                    else
                    {
                        marker.hideInfoWindow();
                        directionButton.setVisibility(View.INVISIBLE);
                        exploreInsideButton.setVisibility(View.INVISIBLE);
                        popUpBar.setVisibility(View.INVISIBLE);
                        isInfoWindowShown = false;
                        activeInfoWindow = null;
                    }
                    setHallButtonsVisibility(false);
                    setCCButtonsVisibility(false);
                    setVEButtonsVisibility(false);
                    setVLButtonsVisibility(false);
                    setMBButtonsVisibility(false);
                    removeAllFloorOverlays();
                }
                else if (restaurantMarkers.contains(marker))
                {
                    isInfoWindowShown = false;
                    searchMarker = marker;  // set the global search marker to the marker that has most recently been clicked

                    animateCamera(marker.getPosition(), zoomLevel);      // move the camera to the marker location

                    if (!isInfoWindowShown)
                    {
                        marker.showInfoWindow();
                        activeInfoWindow = marker.getTitle();
                        directionButton.setVisibility(VISIBLE);
                        LinearLayout.LayoutParams directionButtonLayoutParams = (LinearLayout.LayoutParams) directionButton.getLayoutParams();
                        directionButton.setLayoutParams(directionButtonLayoutParams);

                        exploreInsideButton.setVisibility(VISIBLE);
                        LinearLayout.LayoutParams exploreButtonLayoutParams = (LinearLayout.LayoutParams) exploreInsideButton.getLayoutParams();
                        exploreInsideButton.setLayoutParams(exploreButtonLayoutParams);

                        popUpBar.setVisibility(VISIBLE);
                        isInfoWindowShown = true;
                    }
                }
                return true;
            }
        });

        // when we click on the map, we want to reset back to all the defaults
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    directionButton.setVisibility(View.INVISIBLE);
                    exploreInsideButton.setVisibility(View.INVISIBLE);

                    setHallButtonsVisibility(false);
                    setCCButtonsVisibility(false);
                    setVEButtonsVisibility(false);
                    setVLButtonsVisibility(false);
                    setMBButtonsVisibility(false);
                    removeAllFloorOverlays();

                    popUpBar.setVisibility(View.INVISIBLE);
                    isInfoWindowShown = false;
                    setPolygonBuildingsVisibility(true);
                    setBuildingMarkersVisibility(true);
                    resetGetDirectionParams();
                    resetPath();  //erase the path from outdoor directions
                    setRestaurantMarkersVisibility(false);

                    travelTime.setVisibility(View.INVISIBLE);
                }
                catch (Throwable t){
                    t.printStackTrace();
                }
            }
        });
    }

    // listener method for when my location button is clicke, resets setMyLocationEnable to true
    // so the camera can stay on the user's location ( camera is disabled to stay on user's location
    // when user gesture moves the camera). Check onCameraMoveStarted listener method
    @Override public boolean onMyLocationButtonClick()
    {
        mMap.setMyLocationEnabled(true);
        return false; // returning false calls the super method, returning true does not
    }

    protected static void animateCamera(LatLng latLng, float zoomLevel)
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
                        // we will assume that the starting point is a building as there is no way to determine which floor someone is on
                        // we also need to look at the destination to find where we are going. (this will be a building we know)
                        origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        startingPoint = BUILDING;
                        startingBuilding = BUILDING;

                        getDirections();
                    }
                }
            });
        }catch (SecurityException e){
            // some problem occurred, return Concordia downtown Campus Location
            e.printStackTrace();
        }
    }

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

    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(intent.getIntExtra("requestCode", -1) == RC_CALENDAR_ACTIVITY ) {
            destination = intent.getStringExtra("location");
            setOriginDialog = setOriginDialog();
            setOriginDialog.show();
        }
    }

    // This sets the context and is called during the onCreate method.

    /**
     * This gets the context from the calling class which is required for passing forward access to resource files
     * @param mContext this is the context that is passed down
     */
    public static void setmContext(Context mContext) {
        MapsActivity.mContext = mContext;
    }

    /**
     * This gets the context from the calling class which is required for passing forward access to resource files
     * (THis is for non-activity or non-fragment classes to use in order to pull the context of this class)
     */
    public static Context getmContext() {
        return mContext;
    }

    public InputStream getStreamFromFileName(String fileName)
    {
        return getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));
    }


    //inflates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Handling menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main_home:
                break;

            case R.id.main_schedule:
                Intent calendarIntent = new Intent(MapsActivity.this, CalendarActivity.class);
                startActivity(calendarIntent);
                MapsActivity.this.overridePendingTransition(0, 0);
                break;

            case R.id.main_settings:
                Intent settingsIntent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                MapsActivity.this.overridePendingTransition(0,0);
                break;

            case R.id.main_shuttle:
                Intent shuttleIntent = new Intent(MapsActivity.this, ShuttleScheduleActivity.class);
                startActivity(shuttleIntent);
                MapsActivity.this.overridePendingTransition(0, 0);
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return false;
    }
}