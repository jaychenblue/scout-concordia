package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.scoutconcordia.DataStructures.Graph;
import com.example.scoutconcordia.FileAccess.DES;
import com.example.scoutconcordia.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.android.gms.common.api.Status;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
    private BottomAppBar popUpBar;
    private ToggleButton toggleButton;
    private boolean isInfoWindowShown = false;
    private Marker searchMarker;
    DES encrypter = new DES();

    // Concordia buildings list
    public static final List<String> locations = new ArrayList<>();

    // Displays the Map
    @Override protected void onCreate(Bundle savedInstanceState)
    {
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
                    System.out.println("STATUS CODE: "+ status.getStatusMessage());
            }
        });
*/
        addListenerOnToggle();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar_activity_maps);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
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

        // lets encrypt all of the files before using them
        encryptAllInputFiles();

        // Lets try creating a graph for Hall 8th Floor
        Graph hall_8_floor = new Graph(1);
        createGraph(hall_8_floor, "encrypted_hall8nodes.txt");
        
        
        Graph g1 = new Graph(20);
        LatLng p0 = new LatLng(0,0);
        LatLng p1 = new LatLng(1,1);
        LatLng p2 = new LatLng(2,2);
        LatLng p3 = new LatLng(3,3);
        LatLng p4 = new LatLng(4,4);
        LatLng p5 = new LatLng(5,5);
        LatLng p6 = new LatLng(6,6);
        LatLng p7 = new LatLng(7,7);
        LatLng p8 = new LatLng(8,8);
        g1.insertVertex(p0);
        g1.insertVertex(p1);
        g1.insertVertex(p2);
        g1.insertVertex(p3);
        g1.insertVertex(p4);
        g1.insertVertex(p5);
        g1.insertVertex(p6);
        g1.insertVertex(p7);
        g1.insertVertex(p8);
        g1.insertEdge(p3, p1);
        g1.insertEdge(p3, p5);
        g1.insertEdge(p1, p7);
        g1.insertEdge(p5, p4);
        g1.insertEdge(p5, p2);
        g1.insertEdge(p7, p2);
        g1.insertEdge(p7, p8);
        g1.insertEdge(p2, p6);
        g1.insertEdge(p4, p6);
        g1.insertEdge(p6, p8);
        Object[] path = g1.breathFirstSearch(p4,p8);
        if (path != null)
        {
            Log.w("BFS", "Final Path");
            for (int i = 0; i < path.length; i++)
            {
                Log.w("BFS", path[i].toString());
            }
        }
        // Playing with the Tree
        /*
        N_aryTree tree = new N_aryTree();
        N_aryTree.TreeNode n = tree.getHead();
        n.setElement(new LatLng(0,0));
        n.addToChildren(new LatLng(1,1));
        n.addToChildren(new LatLng(2,2));
        n.addToChildren(new LatLng(3,3));
        N_aryTree.TreeNode n1 = tree.findSpecifiedNode(tree.getHead(), new LatLng(1,1));
        n1.addToChildren(new LatLng(6,6));
        n1.addToChildren(new LatLng(5,5));
        n1.addToChildren(new LatLng(4,4));
        N_aryTree.TreeNode n2 = tree.findSpecifiedNode(tree.getHead(), new LatLng(2,2));
        n2.addToChildren(new LatLng(7,7));
        n2.addToChildren(new LatLng(8,8));
        n2.addToChildren(new LatLng(9,9));
        N_aryTree.TreeNode n3 = tree.findSpecifiedNode(tree.getHead(), new LatLng(3,3));
        n3.addToChildren(new LatLng(10,10));
        n3.addToChildren(new LatLng(11,11));
        n3.addToChildren(new LatLng(12,12));
        N_aryTree.TreeNode n4 = tree.findSpecifiedNode(tree.getHead(), new LatLng(4,4));
        n4.addToChildren(new LatLng(13,13));
        n4.addToChildren(new LatLng(14,14));
        N_aryTree.TreeNode n8 = tree.findSpecifiedNode(n2, new LatLng(4,4));
        if (n8 != null)
        {
            //Log.println(Log.WARN, "Tree", n4.getElement().toString());
            //Log.println(Log.WARN, "Tree", n4.getParent().getElement().toString());
        }
        
        Object[] path = tree.getPath(new LatLng(0,0), new LatLng(12,12));
        for (int i = 0; i < path.length; i++)
        {
            Log.println(Log.WARN, "Tree", path[i].toString());
        }
        */
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
            }
        });
    }
    // onClick listener for when "Find your way" button is clicked
    public void onFindYourWayButtonClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this  );
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.outdoor_buildings_diections_ui, null);
        builder.setView(dialogView);

        Spinner fromSearchBar = dialogView.findViewById(R.id.fromSearchBar);
        Spinner toSearchBar = dialogView.findViewById(R.id.toSearchBar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSearchBar.setAdapter(adapter);
        toSearchBar.setAdapter(adapter);

        fromSearchBar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toSearchBar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        // can add a functionality here that gives us the directions when we press on the button
    }

    // this is the listener for the pop up bar at the bottom of the screen.
    public void addPopUpBarListener()
    {
        popUpBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        // can add functionality here if we click on the pop up bar
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

        addLocationsToMap(getResources().openRawResource(getResources().getIdentifier("downtownlocations", "raw", getPackageName())));  //adds the polygons for the SGW campus
        addLocationsToMap(getResources().openRawResource(getResources().getIdentifier("loyolalocations", "raw", getPackageName()))); //adds the polygons for the Loyola campus
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
    public void encryptAllInputFiles()
    {
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
    public void createGraph(Graph graphName, String encryptedFileName)
    {
        String tempDecryptedFile = "tempDecryptedFile.txt";
        InputStream fis = null;
        OutputStream fos = null;
        try
        {
            // First we need to decrypt the file to have access to the locations
            fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), encryptedFileName));  // input the encrypted file
            fos = new FileOutputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile)); // output the decrypted file
            encrypter.decryptFile(fis, fos);

            // with the decrypted file, we can add the nodes to the graph
            fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile));  // input the encrypted file
            graphName = Graph.addNodesToGraph(fis);

            // close the input and the output streams
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // delete the temp file which was decrypted
            File deleteMe = new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile);
            deleteMe.delete();
        }
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
                isInfoWindowShown = false;

                // move the camera to the marker location
                animateCamera(marker.getPosition(), zoomLevel);

                if (!isInfoWindowShown) {
                    marker.showInfoWindow();

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


                    isInfoWindowShown = true;
                } else {
                    marker.hideInfoWindow();
                    directionButton.setVisibility(View.INVISIBLE);
                    exploreInsideButton.setVisibility(View.INVISIBLE);
                    popUpBar.setVisibility(View.INVISIBLE);
                    isInfoWindowShown = false;
                }

                // change the icon of the marker (this was for testing purposes)
                //marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.smiling)));
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                directionButton.setVisibility(View.INVISIBLE);
                exploreInsideButton.setVisibility(View.INVISIBLE);
                popUpBar.setVisibility(View.INVISIBLE);
                isInfoWindowShown = false;
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

    private void addLocationsToMap(InputStream location)
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

    private void createFloorGraphs()
    {
        // Lets try creating a graph for Hall 8th Floor
        InputStream fis = null;
        try
        {
            fis = getResources().openRawResource(getResources().getIdentifier("hall8nodes", "raw", getPackageName()));
            Graph hall_8_floor = new Graph(10);
            hall_8_floor.addNodesToGraph(fis);
            LatLng[] tempArray = hall_8_floor.vertices();
            for (int i = 0; i < tempArray.length; i++)
            {
                System.out.println(tempArray[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

}
