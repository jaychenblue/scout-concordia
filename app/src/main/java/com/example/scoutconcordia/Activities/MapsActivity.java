package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
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
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
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
import java.util.List;
import java.util.Scanner;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.example.scoutconcordia.MapInfoClasses.CustomInfoWindow;

import static android.location.Location.distanceBetween;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    private float zoomLevel = 16.0f;
    private final int ACCESS_FINE_LOCATION = 9001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng concordiaLatLngDowntownCampus = new LatLng(45.494619, -73.577376);
    private final LatLng concordiaLatLngLoyolaCampus = new LatLng(45.458423, -73.640460);
    private Button directionButton;
    private Button exploreInsideButton;

    private Button floor8;
    private Button floor9;


    private BottomAppBar popUpBar;
    private ToggleButton toggleButton;
    private boolean isInfoWindowShown = false;
    private Marker searchMarker;
    DES encrypter = new DES();
    private String activeInfoWindow = null;
    private List<Polygon> polygonBuildings = new ArrayList<>();
    private List<Marker> markerBuildings = new ArrayList<>();
    private List<Marker> hall8floorMarkers = new ArrayList<>();


    // We use this for image overlay of Hall building
    private final LatLng hallOverlaySouthWest = new LatLng(45.496827, -73.578849);
    private final LatLng hallOverlayNorthEast = new LatLng(45.497711, -73.579033);

    private GroundOverlay googoo;


    // Displays the Map
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(this, getString(R.string.google_maps_key));
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

        // lets encrypt all of the files before using them
        encryptAllInputFiles();


        // Lets try creating a graph for Hall 8th Floor
        //Graph hall_8_floor = new Graph(1);
        //createGraph(hall_8_floor, "encrypted_hall8nodes.txt");
    
    
        Graph g1 = new Graph(20);
        LatLng p0 = new LatLng(0, 0);
        LatLng p1 = new LatLng(1, 1);
        LatLng p2 = new LatLng(2, 2);
        LatLng p3 = new LatLng(3, 3);
        LatLng p4 = new LatLng(4, 4);
        LatLng p5 = new LatLng(5, 5);
        LatLng p6 = new LatLng(6, 6);
        LatLng p7 = new LatLng(7, 7);
        LatLng p8 = new LatLng(8, 8);
        g1.insertVertex(p0, 1);
        g1.insertVertex(p1, 1);
        g1.insertVertex(p2, 1);
        g1.insertVertex(p3, 0);
        g1.insertVertex(p4, 0);
        g1.insertVertex(p5, 1);
        g1.insertVertex(p6, 1);
        g1.insertVertex(p7, 0);
        g1.insertVertex(p8, 0);
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
        Object[] path = g1.breathFirstSearch(p3, p8);
        if (path != null)
        {
            Log.w("BFS", "Final Path");
            for (int i = 0; i < path.length; i++)
            {
                Log.w("BFS", path[i].toString());
            }
        }
        else
            Log.w("BFS", "Failed!");


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

    public void addfloor8ButtonListener()
    {
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

                GroundOverlayOptions goo = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);

                googoo = mMap.addGroundOverlay(goo);


                // For future reference, this will be necessary in order to remove the overlay once
                // the app moves away contextually from the inside of the building.

//                hall8.remove();


                // Lets try creating a graph for Hall 8th Floor
                Graph hall_8_floor = createGraph("encrypted_classrooms");
                //System.out.println(hall_8_floor.vertices().length);

                // This is temporary to help in placing the markers for each floor
                for (LatLng vertices : hall_8_floor.vertices())
                {
                    Marker polyMarker = mMap.addMarker(new MarkerOptions().position(vertices));
                    hall8floorMarkers.add(polyMarker); // add the marker to the list of markers
                }

                for (LatLng vertices : hall_8_floor.vertices())
                {
                    Log.w("Adjacency List", Arrays.toString(hall_8_floor.incidentVerticies(vertices)));
                }

                //Marker tempMarker = hall8floorMarkers.get(10);
                //LatLng tempLatLng = tempMarker.getPosition();
                //tempMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // green is the selected node

                //for (LatLng vertice : hall_8_floor.vertices())
                // {
                //    if (hall_8_floor.areAdjacent(vertice, tempLatLng))
                //    {
                //        for (Marker markers : hall8floorMarkers)
                //            if (markers.getPosition().equals(vertice))
                //            {
                //                markers.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));  // blue is the closest node
                //            }
                //    }
                // }


                LatLng point1 = hall_8_floor.vertices()[20];  //start
                LatLng point2 = hall_8_floor.vertices()[27];  //end
                Log.w("Point 1:", point1.toString());
                Log.w("Point 2:", point2.toString());

                Object[] path = hall_8_floor.breathFirstSearch(point1, point2);

                if (path != null) {
                    Log.w("BFS", "Final Path");
                    for (int i = 0; i < path.length; i++) {
                        Log.w("BFS", path[i].toString());

                          // lets highlight the path.
                          for (Marker markers : hall8floorMarkers)
                          {
                              if (markers.getPosition().equals(path[i]))
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
        });

    }

    public void addfloor9ButtonListener()
    {

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

                GroundOverlayOptions goo = new GroundOverlayOptions()
                        .image(floorPlan)
                        .position(hallOverlaySouthWest, overlaySize)
                        .anchor(0, 1)
                        .bearing(imgRotation);
//                mMap.addGroundOverlay(goo);

//                GroundOverlay hall9 = mMap.addGroundOverlay(goo);
                googoo = mMap.addGroundOverlay(goo);



//                // Lets try creating a graph for Hall 9th Floor
//                Graph hall_8_floor = createGraph("encrypted_classrooms");
//                //System.out.println(hall_8_floor.vertices().length);
//
//                // This is temporary to help in placing the markers for each floor
//                for (LatLng vertices : hall_8_floor.vertices())
//                {
//                    mMap.addMarker(new MarkerOptions().position(vertices));
//                }

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

        // THIS IS SOME CODE TO TEST OUT THE FILEACCESSOR METHODS
        FileAccessor downtownLocations = new FileAccessor();
        //The 2 commented lines are an example of reading a file that is not encrypted
        //downtownLocations.setFileName("downtownlocations");
        //Object[] output = downtownLocations.obtainContents(false);
        // reading a file that is encrypted
        downtownLocations.setFileName("encrypteddtown");
        Object[] output = downtownLocations.obtainContents(true);
        Log.w("FileAccessor", Integer.toString(output.length));
        for (int i = 0; i < output.length; i++)
        {
            Log.w("FileAcccessor", output[i].toString());
        }


        FileAccessor loyolaLocations = new FileAccessor();
        loyolaLocations.setFileName("encryptedloyola");
        output = loyolaLocations.obtainContents(true);
        Log.w("FileAccessor", Integer.toString(output.length));
        for (int i = 0; i < output.length; i++)
        {
            Log.w("FileAcccessor", output[i].toString());
        }
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
                //if (filename.equals("loyolalocations"))
                //{
                //    fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), encryptedFilename));
                //   Scanner readEncrypted = new Scanner(fis);
                //    while (readEncrypted.hasNext())
                //    {
                //        System.out.println(readEncrypted.nextLine());
                //    }
                //}

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
        String tempDecryptedFile = "tempDecryptedFile";
        InputStream fis = null;
        OutputStream fos = null;
        Graph graphName = null;
        try
        {
            // First we need to decrypt the file to have access to the locations
            fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), encryptedFileName));  // input the encrypted file
            fos = new FileOutputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile)); // output the decrypted file
            encrypter.decryptFile(fis, fos);

            // with the decrypted file, we can add the nodes to the graph
            fis = new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile));  // input the encrypted file
            graphName = Graph.addNodesToGraph(fis);
            graphName.addAjacentNodes();

            // close the input and the output streams
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // delete the temp file which was decrypted
            File deleteMe = new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), tempDecryptedFile);
            deleteMe.delete();
            return graphName;
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

                    floor8.setVisibility(View.INVISIBLE);
                    floor9.setVisibility(View.INVISIBLE);


                    if (googoo != null) {
                        googoo.remove();
                    }


                    isInfoWindowShown = true;
                } else {
                    marker.hideInfoWindow();
                    directionButton.setVisibility(View.INVISIBLE);
                    exploreInsideButton.setVisibility(View.INVISIBLE);
                    popUpBar.setVisibility(View.INVISIBLE);
                    floor8.setVisibility(View.INVISIBLE);
                    floor9.setVisibility(View.INVISIBLE);

                    if (googoo != null) {
                        googoo.remove();
                    }


                    isInfoWindowShown = false;
                    activeInfoWindow = null;
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

                floor8.setVisibility(View.INVISIBLE);
                floor9.setVisibility(View.INVISIBLE);

                if (googoo != null) {
                    googoo.remove();
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

    class FileAccessor
    {
        String fileName;

        public FileAccessor()
        {
            fileName = null;
        }

        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }

        // Used first to be able to read the file
        public void decryptFile()
        {
            InputStream readme = null;
            OutputStream writeToMe = null;
            try
            {
                readme = getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));
                writeToMe = new FileOutputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), fileName));
                encrypter.decryptFile(readme, writeToMe);
            }
            catch (FileNotFoundException fnf)
            {
                Log.println(Log.WARN, "FileAccessor", "The output file was moved during the transfer");
            }
            catch (Resources.NotFoundException e)
            {
                Log.println(Log.WARN, "FileAccessor", "The input file could not be located");
            }
        }

        // returns an array with every line as a string from the file
        public Object[] obtainContents(boolean isEncrypted)
        {
            LinkedList<String> contents = new LinkedList<String>("");
            Scanner reader = null;
            if (!isEncrypted)  // if the file is not encrypted
            {
                InputStream input = getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));  // we can read the file directly
                reader = new Scanner(input);
            }
            else // if the file is encrypted
            {
                this.decryptFile();  //decrypt the file
                try
                {
                    reader = new Scanner(new FileInputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), fileName)));  // read the decrypted file
                } catch (FileNotFoundException fnf)
                {
                    Log.w("FileAccessor", fnf.getMessage());
                }
            }
            while (reader.hasNextLine())
            {
                contents.add(reader.nextLine());
            }
            return contents.toArray();
        }

        // used when done to make sure no information is potentially leaked
        public boolean closeFile()
        {
            File deleteMe = new File(getFilesDir().getAbsoluteFile(), fileName);
            return deleteMe.delete();
        }
    }
}

