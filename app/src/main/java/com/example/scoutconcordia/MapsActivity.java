package com.example.scoutconcordia;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.content.DialogInterface;
import android.graphics.Color;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.security.KeyStore;
import java.security.acl.LastOwnerException;
import java.util.concurrent.LinkedTransferQueue;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    private float zoomLevel = 16.0f;
    private final int ACCESS_FINE_LOCATION = 9001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    final private LatLng concordiaLatLngDowntownCampus = new LatLng(45.494619, -73.577376);
    final private LatLng concordiaLatLngLoyolaCampus = new LatLng(45.458423, -73.640460);

    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addListenerOnToggle();
    }

    public void addListenerOnToggle() {
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleCampus();
            }
        });
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        }
        else{
            // request for user's permission for location services
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
            requestPermissions();
        }

        mMap.setOnMyLocationChangeListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        addSGWPolygons();  //adds the polygons for the SGW campus
        addLoyolaPolygons(); //adds the polygons for the Loyola campus
        // Add a marker in Concordia and move the camera
        LatLng coco = new LatLng(45.494619, -73.577376); // Concordia's coordinates
        mMap.addMarker(new MarkerOptions().position(coco).title("Marker in Concordia"));
        float zoomLevel = 16.0f; // max 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coco, zoomLevel));


    } //end of onMapReady

    // moves the camera to keep on user's location on any change in its location
    @Override
    public void onMyLocationChange(Location location){
        LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
        animateCamera(loc, zoomLevel);
    }

    //detects camera movement and the cause for the movement
    @Override
    public void onCameraMoveStarted(int reason) {
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

        int strokeColor = Color.parseColor("#000000");
        int fillColor = Color.parseColor("#FF74091F"); //color format is #AARRGGBB where AA is for the opacity. 00 is fully transparent. FF is opaque

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                strokeColor = Color.parseColor("#BB000000");
                fillColor = Color.parseColor("#BB74091F");
                break;
        }
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
        polygon.setClickable(true);
    }

    /**
     *  This method is used to indicate all of the polygons that will overlay the buildings on the
     *  SGW campus
     */
    public void addSGWPolygons() {
        // (B) 2160, Bishop
        Polygon B_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497741, -73.579635),
                        new LatLng(45.497921, -73.579455),
                        new LatLng(45.497884, -73.579376),
                        new LatLng(45.497708, -73.579562)
                ));
        B_building.setTag("alpha");
        stylePolygon(B_building);

        // (CI) 2149, Mackay
        Polygon CI_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497406, -73.580015),
                        new LatLng(45.497583, -73.579837),
                        new LatLng(45.497546, -73.579760),
                        new LatLng(45.497365, -73.579932)
                ));
        CI_building.setTag("alpha");
        stylePolygon(CI_building);

        // (CL) 1665, Sainte-Catherine O.
        Polygon CL_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.494165, -73.579656),
                        new LatLng(45.494471, -73.579282),
                        new LatLng(45.494260, -73.578937),
                        new LatLng(45.494035, -73.579214),
                        new LatLng(45.494012, -73.579249),
                        new LatLng(45.493989, -73.579293),
                        new LatLng(45.493978, -73.579332),
                        new LatLng(45.493978, -73.579332)
                ));
        CL_building.setTag("alpha");
        stylePolygon(CL_building);

        // (D) 2140, Bishop
        Polygon D_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497742, -73.579411),
                        new LatLng(45.497847, -73.579308),
                        new LatLng(45.497808, -73.579222),
                        new LatLng(45.497707, -73.579342)
                ));
        D_building.setTag("alpha");
        stylePolygon(D_building);

        // (EN) 2070, Mackay
        Polygon EN_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496699, -73.579784),
                        new LatLng(45.496931, -73.579553),
                        new LatLng(45.496891, -73.579469),
                        new LatLng(45.496788, -73.579574),
                        new LatLng(45.496802, -73.579606),
                        new LatLng(45.496676, -73.579733)
                ));
        EN_building.setTag("alpha");
        stylePolygon(EN_building);

        // (ER) 2155, Guy
        Polygon ER_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.496260, -73.580351),
                        new LatLng(45.496677, -73.579968),
                        new LatLng(45.496522, -73.579631),
                        new LatLng(45.496216, -73.579905),
                        new LatLng(45.496247, -73.579972),
                        new LatLng(45.496162, -73.580068)
                ));
        ER_building.setTag("alpha");
        stylePolygon(ER_building);

        // (EV) Engineering, Computer Science and Visual Arts Integrated Complex. 1515, Sainte-Catherine O.
        Polygon EV_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.495595, -73.578762),
                        new LatLng(45.495863, -73.578490),
                        new LatLng(45.495668, -73.578073),
                        new LatLng(45.496047, -73.577708),
                        new LatLng(45.495831, -73.577249),
                        new LatLng(45.495539, -73.577530),
                        new LatLng(45.495550, -73.577555),
                        new LatLng(45.495462, -73.577639),
                        new LatLng(45.495447, -73.577609),
                        new LatLng(45.495355, -73.577695),
                        new LatLng(45.495353, -73.577742),
                        new LatLng(45.495237, -73.577877),
                        new LatLng(45.495234, -73.577913),
                        new LatLng(45.495248, -73.577914),
                        new LatLng(45.495264, -73.578001),
                        new LatLng(45.495247, -73.578021)
                ));
        EV_building.setTag("alpha");
        stylePolygon(EV_building);

        // (FA) 2060, Mackay
        Polygon FA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.496777, -73.579586),
                        new LatLng(45.496871, -73.579488),
                        new LatLng(45.496835, -73.579402),
                        new LatLng(45.496735, -73.579503)
                ));
        FA_building.setTag("alpha");
        stylePolygon(FA_building);

        // (FB) Faubourg Building 1250, Guy. 1600 Sainte-Catherine O.
        Polygon FB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                     new LatLng(45.494696, -73.578039),
                        new LatLng(45.494912, -73.577785),
                        new LatLng(45.494869, -73.577713),
                        new LatLng(45.494876, -73.577704),
                        new LatLng(45.494835, -73.577633),
                        new LatLng(45.494842, -73.577624),
                        new LatLng(45.494798, -73.577549),
                        new LatLng(45.494806, -73.577541),
                        new LatLng(45.494763, -73.577465),
                        new LatLng(45.494774, -73.577453),
                        new LatLng(45.494692, -73.577310),
                        new LatLng(45.494700, -73.577299),
                        new LatLng(45.494654, -73.577217),
                        new LatLng(45.494396, -73.577521)
                ));
        FB_building.setTag("alpha");
        stylePolygon(FB_building);

        // (FG) Faubourg Sainte-Catherine Building 1610, Sainte-Catherine O.
        Polygon FG_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.493822, -73.579067),
                        new LatLng(45.494294, -73.578512),
                        new LatLng(45.494300, -73.578523),
                        new LatLng(45.494369, -73.578444),
                        new LatLng(45.494361, -73.578426),
                        new LatLng(45.494694, -73.578039),
                        new LatLng(45.494452, -73.577617),
                        new LatLng(45.494387, -73.577692),
                        new LatLng(45.494426, -73.577762),
                        new LatLng(45.494392, -73.577805),
                        new LatLng(45.494371, -73.577768),
                        new LatLng(45.494186, -73.577987),
                        new LatLng(45.494202, -73.578018),
                        new LatLng(45.494111, -73.578126),
                        new LatLng(45.494103, -73.578113),
                        new LatLng(45.493911, -73.578342),
                        new LatLng(45.493922, -73.578364),
                        new LatLng(45.493891, -73.578400),
                        new LatLng(45.493882, -73.578385),
                        new LatLng(45.493833, -73.578440),
                        new LatLng(45.493847, -73.578464),
                        new LatLng(45.493624, -73.578727)
                ));
        FG_building.setTag("alpha");
        stylePolygon(FG_building);

        // (GA) Grey Nuns Annex 1211-1215, St-Mathieu
        Polygon GA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                   new LatLng(45.493850, -73.578355),
                        new LatLng(45.494134, -73.578011),
                        new LatLng(45.494119, -73.577984),
                        new LatLng(45.494345, -73.577736),
                        new LatLng(45.494284, -73.577622),
                        new LatLng(45.494054, -73.577870),
                        new LatLng(45.494075, -73.577908),
                        new LatLng(45.493790, -73.578254)
                ));
        GA_building.setTag("alpha");
        stylePolygon(GA_building);

        // (GM) Guy-De Maisonneuve Building
        Polygon GM_building = mMap.addPolygon(new PolygonOptions()
                .add(
                     new LatLng(45.495781, -73.579147),
                        new LatLng(45.496128, -73.578806),
                        new LatLng(45.496111, -73.578767),
                        new LatLng(45.496109, -73.578767),
                        new LatLng(45.495947, -73.578435),
                        new LatLng(45.495616, -73.578744),
                        new LatLng(45.495779, -73.579090),
                        new LatLng(45.495763, -73.579108)
                ));
        GM_building.setTag("alpha");
        stylePolygon(GM_building);

        // (GN) Grey Nuns Building 1190, Guy. 1175, St-Mathieu
        Polygon GN_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.492593, -73.576534),
                        new LatLng(45.492614, -73.576577),
                        new LatLng(45.492607, -73.576584),
                        new LatLng(45.492746, -73.576869),
                        new LatLng(45.492727, -73.576873),
                        new LatLng(45.492676, -73.576923),
                        new LatLng(45.492677, -73.576965),
                        new LatLng(45.492710, -73.577033),
                        new LatLng(45.492773, -73.576973),
                        new LatLng(45.492794, -73.576977),
                        new LatLng(45.492810, -73.576962),
                        new LatLng(45.492896, -73.577141),
                        new LatLng(45.492838, -73.577198),
                        new LatLng(45.492925, -73.577379),
                        new LatLng(45.492993, -73.577314),
                        new LatLng(45.493088, -73.577511),
                        new LatLng(45.493198, -73.577401),
                        new LatLng(45.493108, -73.577213),
                        new LatLng(45.493363, -73.576961),
                        new LatLng(45.493353, -73.576936),
                        new LatLng(45.493438, -73.576853),
                        new LatLng(45.493478, -73.576935),
                        new LatLng(45.493450, -73.576963),
                        new LatLng(45.493529, -73.577128),
                        new LatLng(45.493576, -73.577081),
                        new LatLng(45.493601, -73.577131),
                        new LatLng(45.493579, -73.577153),
                        new LatLng(45.493634, -73.577267),
                        new LatLng(45.493746, -73.577157),
                        new LatLng(45.493671, -73.576999),
                        new LatLng(45.493727, -73.576944),
                        new LatLng(45.493667, -73.576822),
                        new LatLng(45.493613, -73.576873),
                        new LatLng(45.493551, -73.576747),
                        new LatLng(45.493838, -73.576465),
                        new LatLng(45.493837, -73.576669),
                        new LatLng(45.493717, -73.576784),
                        new LatLng(45.493902, -73.577173),
                        new LatLng(45.494065, -73.577015),
                        new LatLng(45.493897, -73.576660),
                        new LatLng(45.493929, -73.576472),
                        new LatLng(45.494193, -73.577020),
                        new LatLng(45.493868, -73.577339),
                        new LatLng(45.493975, -73.577559),
                        new LatLng(45.494124, -73.577413),
                        new LatLng(45.494094, -73.577348),
                        new LatLng(45.494391, -73.577055),
                        new LatLng(45.494018, -73.576282),
                        new LatLng(45.494131, -73.576168),
                        new LatLng(45.494043, -73.575991),
                        new LatLng(45.493933, -73.576093),
                        new LatLng(45.493713, -73.575638),
                        new LatLng(45.493570, -73.575782),
                        new LatLng(45.493792, -73.576245),
                        new LatLng(45.493492, -73.576540),
                        new LatLng(45.493472, -73.576497),
                        new LatLng(45.493340, -73.576624),
                        new LatLng(45.493364, -73.576673),
                        new LatLng(45.493024, -73.577006),
                        new LatLng(45.492941, -73.576833),
                        new LatLng(45.492948, -73.576794),
                        new LatLng(45.492926, -73.576747),
                        new LatLng(45.492899, -73.576743),
                        new LatLng(45.492733, -73.576396)
                ));
        GN_building.setTag("alpha");
        stylePolygon(GN_building);

        // (GS) 1538, Sherbrooke
        Polygon GS_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496627, -73.581460),
                        new LatLng(45.496785, -73.581300),
                        new LatLng(45.496711, -73.581126),
                        new LatLng(45.496653, -73.581171),
                        new LatLng(45.496518, -73.580803),
                        new LatLng(45.496477, -73.580844),
                        new LatLng(45.496487, -73.580872),
                        new LatLng(45.496415, -73.580941),
                        new LatLng(45.496578, -73.581436)));
        GS_building.setTag("alpha");
        stylePolygon(GS_building);

        // (H) Henry F. Hall Building. 1455, De Maisonneuve O.
        Polygon H_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497163, -73.579557),
                        new LatLng(45.497718, -73.579039),
                        new LatLng(45.497374, -73.578319),
                        new LatLng(45.496820, -73.578841)));
        H_building.setTag("alpha");
        stylePolygon(H_building);

        // (K) 2150, Bishop
        Polygon K_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.497627, -73.579634),
                        new LatLng(45.497881, -73.579379),
                        new LatLng(45.497847, -73.579307),
                        new LatLng(45.497754, -73.579401),
                        new LatLng(45.497772, -73.579438),
                        new LatLng(45.497697, -73.579510),
                        new LatLng(45.497685, -73.579487),
                        new LatLng(45.497597, -73.579571)
                ));
        K_building.setTag("alpha");
        stylePolygon(K_building);

        // (LB) J.W McConnell Building. 1400, De Maisonneuve O.
        Polygon LB_building = mMap.addPolygon(new PolygonOptions()
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
        LB_building.setTag("alpha");
        stylePolygon(LB_building);

        // (LD) 1424, Bishop
        Polygon LD_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496564, -73.577470),
                        new LatLng(45.496858, -73.577170),
                        new LatLng(45.496818, -73.577086),
                        new LatLng(45.496522, -73.577386)
                ));
        LD_building.setTag("alpha");
        stylePolygon(LD_building);

        // (LS) 1535, De Maisonneuve O.
        Polygon LS_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496200, -73.579633),
                        new LatLng(45.496283, -73.579551),
                        new LatLng(45.496339, -73.579731),
                        new LatLng(45.496496, -73.579564),
                        new LatLng(45.496327, -73.579162),
                        new LatLng(45.496095, -73.579363)
                ));
        LS_building.setTag("alpha");
        stylePolygon(LS_building);

        // (M) 2135, Mackay
        Polygon M_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497323, -73.579844),
                        new LatLng(45.497426, -73.579734),
                        new LatLng(45.497390, -73.579683),
                        new LatLng(45.497290, -73.579780)
                ));
        M_building.setTag("alpha");
        stylePolygon(M_building);

        // (MB) John Molson Building. 1450, Guy
        Polygon MB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.495358, -73.579367),
                        new LatLng(45.495519, -73.579200),
                        new LatLng(45.495439, -73.578960),
                        new LatLng(45.495187, -73.578525),
                        new LatLng(45.495004, -73.578736),
                        new LatLng(45.495037, -73.578790),
                        new LatLng(45.495006, -73.578824),
                        new LatLng(45.495166, -73.579171),
                        new LatLng(45.495221, -73.579115)
                ));
        MB_building.setTag("alpha");
        stylePolygon(MB_building);

        // (MI) 2130, Bishop
        Polygon MI_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497647, -73.579403),
                        new LatLng(45.497808, -73.579222),
                        new LatLng(45.497778, -73.579167),
                        new LatLng(45.497683, -73.579260),
                        new LatLng(45.497694, -73.579284),
                        new LatLng(45.497623, -73.579354)
                ));
        MI_building.setTag("alpha");
        stylePolygon(MI_building);

        // (MU) 2170, Bishop
        Polygon MU_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497785, -73.579703),
                        new LatLng(45.497964, -73.579533),
                        new LatLng(45.497921, -73.579455),
                        new LatLng(45.497749, -73.579628)
                ));
        MU_building.setTag("alpha");
        stylePolygon(MU_building);

        // (P) 2020, Mackay
        Polygon P_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496625, -73.579262),
                        new LatLng(45.496723, -73.579170),
                        new LatLng(45.496677, -73.579083),
                        new LatLng(45.496578, -73.579171)
                ));
        P_building.setTag("alpha");
        stylePolygon(P_building);

        // (PR) 2100, Mackay
        Polygon PR_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496946, -73.579938),
                        new LatLng(45.497029, -73.579852),
                        new LatLng(45.496980, -73.579748),
                        new LatLng(45.496895, -73.579833)
                ));
        PR_building.setTag("alpha");
        stylePolygon(PR_building);

        // (Q) 2010, Mackay
        Polygon Q_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496578, -73.579170),
                        new LatLng(45.496675, -73.579082),
                        new LatLng(45.496644, -73.579009),
                        new LatLng(45.496553, -73.579120)
                ));
        Q_building.setTag("alpha");
        stylePolygon(Q_building);

        // (R) 2050, Mackay
        Polygon R_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496745, -73.579492),
                        new LatLng(45.496837, -73.579400),
                        new LatLng(45.496802, -73.579329),
                        new LatLng(45.496711, -73.579420)
                ));
        R_building.setTag("alpha");
        stylePolygon(R_building);

        // (RR) 2040, Mackay
        Polygon RR_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496651, -73.579480),
                        new LatLng(45.496796, -73.579331),
                        new LatLng(45.496759, -73.579254),
                        new LatLng(45.496612, -73.579401)
                ));
        RR_building.setTag("alpha");
        stylePolygon(RR_building);

        // (S) 2145, Mackay
        Polygon S_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.497363, -73.579927),
                        new LatLng(45.497491, -73.579799),
                        new LatLng(45.497460, -73.579753),
                        new LatLng(45.497437, -73.579773),
                        new LatLng(45.497426, -73.579734),
                        new LatLng(45.497323, -73.579844)
                ));
        S_building.setTag("alpha");
        stylePolygon(S_building);

        // (SB) Samuel Bronfman Building. 1590, Docteur-Penfield
        Polygon SB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496435, -73.586209),
                        new LatLng(45.496501, -73.586208),
                        new LatLng(45.496506, -73.586135),
                        new LatLng(45.496521, -73.586136),
                        new LatLng(45.496519, -73.586168),
                        new LatLng(45.496538, -73.586212),
                        new LatLng(45.496683, -73.586087),
                        new LatLng(45.496652, -73.586014),
                        new LatLng(45.496658, -73.586008),
                        new LatLng(45.496582, -73.585830),
                        new LatLng(45.496572, -73.585835),
                        new LatLng(45.496553, -73.585792),
                        new LatLng(45.496509, -73.585792),
                        new LatLng(45.496511, -73.585779),
                        new LatLng(45.496491, -73.585739),
                        new LatLng(45.496467, -73.585737)
                ));
        SB_building.setTag("alpha");
        stylePolygon(SB_building);

        // (T) 2030, Mackay
        Polygon T_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496668, -73.579345),
                        new LatLng(45.496751, -73.579260),
                        new LatLng(45.496711, -73.579183),
                        new LatLng(45.496627, -73.579264)
                ));
        T_building.setTag("alpha");
        stylePolygon(T_building);

        // (TD) Toronto-Dominion Building
        Polygon TD_building = mMap.addPolygon(new PolygonOptions()
                .add(
                      new LatLng(45.495128, -73.578501),
                     new LatLng(45.495189, -73.578427),
                        new LatLng(45.495037, -73.578072),
                        new LatLng(45.494943, -73.578177),
                        new LatLng(45.495023, -73.578325),
                        new LatLng(45.495043, -73.578300),
                        new LatLng(45.495065, -73.578341),
                        new LatLng(45.495047, -73.578364)
                ));
        TD_building.setTag("alpha");
        stylePolygon(TD_building);

        // (V) 2110, Mackay
        Polygon V_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496984, -73.580018),
                        new LatLng(45.497089, -73.579916),
                        new LatLng(45.497048, -73.579834),
                        new LatLng(45.496946, -73.579938)
                ));
        V_building.setTag("alpha");
        stylePolygon(V_building);

        // (VA) Visual Arts Building. 1395, Rene-Levesque O.
        Polygon VA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                    new LatLng(45.495401, -73.573767),
                        new LatLng(45.495669, -73.574309),
                        new LatLng(45.496186, -73.573795),
                        new LatLng(45.496069, -73.573560),
                        new LatLng(45.495817, -73.573810),
                        new LatLng(45.495675, -73.573490)
                ));
        VA_building.setTag("alpha");
        stylePolygon(VA_building);

        // (X) 2080, Mackay
        Polygon X_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496857, -73.579758),
                        new LatLng(45.496949, -73.579665),
                        new LatLng(45.496907, -73.579578),
                        new LatLng(45.496815, -73.579671)
                ));
        X_building.setTag("alpha");
        stylePolygon(X_building);

        // (Z) 2090, Mackay
        Polygon Z_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.496911, -73.579816),
                        new LatLng(45.496986, -73.579741),
                        new LatLng(45.496949, -73.579665),
                        new LatLng(45.496850, -73.579763),
                        new LatLng(45.496874, -73.579812),
                        new LatLng(45.496897, -73.579791)
                ));
        Z_building.setTag("alpha");
        stylePolygon(Z_building);
    } //end of addSGWPolygons

    public void addLoyolaPolygons() {
        // (AD) 7141, Sherbrooke O. (Administration Building)
        Polygon AD_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.457911, -73.640124),
                        new LatLng(45.457985, -73.640067),
                        new LatLng(45.457962, -73.640006),
                        new LatLng(45.458086, -73.639912),
                        new LatLng(45.458089, -73.639920),
                        new LatLng(45.458154, -73.639869),
                        new LatLng(45.458151, -73.639862),
                        new LatLng(45.458277, -73.639767),
                        new LatLng(45.458298, -73.639821),
                        new LatLng(45.458371, -73.639765),
                        new LatLng(45.458327, -73.639649),
                        new LatLng(45.458331, -73.639644),
                        new LatLng(45.458313, -73.639595),
                        new LatLng(45.458307, -73.639598),
                        new LatLng(45.458259, -73.639469),
                        new LatLng(45.458242, -73.639483),
                        new LatLng(45.458230, -73.639476),
                        new LatLng(45.458210, -73.639494),
                        new LatLng(45.458207, -73.639510),
                        new LatLng(45.458183, -73.639527),
                        new LatLng(45.458200, -73.639574),
                        new LatLng(45.458191, -73.639582),
                        new LatLng(45.458203, -73.639617),
                        new LatLng(45.458097, -73.639697),
                        new LatLng(45.458086, -73.639691),
                        new LatLng(45.458065, -73.639634),
                        new LatLng(45.458060, -73.639638),
                        new LatLng(45.458040, -73.639581),
                        new LatLng(45.457983, -73.639626),
                        new LatLng(45.458004, -73.639679),
                        new LatLng(45.458000, -73.639682),
                        new LatLng(45.458022, -73.639743),
                        new LatLng(45.458023, -73.639755),
                        new LatLng(45.457915, -73.639837),
                        new LatLng(45.457903, -73.639808),
                        new LatLng(45.457893, -73.639817),
                        new LatLng(45.457875, -73.639771),
                        new LatLng(45.457846, -73.639791),
                        new LatLng(45.457841, -73.639788),
                        new LatLng(45.457822, -73.639795),
                        new LatLng(45.457819, -73.639812),
                        new LatLng(45.457799, -73.639827)
                ));
        AD_building.setTag("alpha");
        stylePolygon(AD_building);

        // (BB) 3502, Belmore
        Polygon BB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459731, -73.639267),
                        new LatLng(45.459828, -73.639192),
                        new LatLng(45.459797, -73.639118),
                        new LatLng(45.459702, -73.639192)
                ));
        BB_building.setTag("alpha");
        stylePolygon(BB_building);

        // (BH) 3500, Belmore
        Polygon BH_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459702, -73.639192),
                        new LatLng(45.459797, -73.639118),
                        new LatLng(45.459770, -73.639044),
                        new LatLng(45.459676, -73.639116)
                ));
        BH_building.setTag("alpha");
        stylePolygon(BH_building);

        // (CC) 7141, Sherbrooke O. (Central Building)
        Polygon CC_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.458379, -73.640794),
                        new LatLng(45.458518, -73.640684),
                        new LatLng(45.458429, -73.640450),
                        new LatLng(45.458436, -73.640444),
                        new LatLng(45.458321, -73.640142),
                        new LatLng(45.458313, -73.640146),
                        new LatLng(45.458221, -73.639906),
                        new LatLng(45.458166, -73.639948),
                        new LatLng(45.458140, -73.639881),
                        new LatLng(45.458108, -73.639906),
                        new LatLng(45.458122, -73.639944),
                        new LatLng(45.458095, -73.63996),
                        new LatLng(45.458104, -73.639995),
                        new LatLng(45.458081, -73.640015),
                        new LatLng(45.458173, -73.640255),
                        new LatLng(45.458167, -73.640262),
                        new LatLng(45.458280, -73.640563),
                        new LatLng(45.458289, -73.640557)
                ));
        CC_building.setTag("alpha");
        stylePolygon(CC_building);

        // (CJ) 7141, Sherbrooke O. (Communication Studies and Journalism Building)
        Polygon CJ_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.457333, -73.640717),
                        new LatLng(45.457597, -73.640503),
                        new LatLng(45.457649, -73.640631),
                        new LatLng(45.457831, -73.640486),
                        new LatLng(45.457755, -73.640291),
                        new LatLng(45.457726, -73.640315),
                        new LatLng(45.457622, -73.640045),
                        new LatLng(45.457484, -73.640149),
                        new LatLng(45.457435, -73.64003),
                        new LatLng(45.457446, -73.639945),
                        new LatLng(45.457464, -73.639954),
                        new LatLng(45.457479, -73.639819),
                        new LatLng(45.457431, -73.639772),
                        new LatLng(45.457387, -73.639760),
                        new LatLng(45.457359, -73.639761),
                        new LatLng(45.457333, -73.639769),
                        new LatLng(45.457305, -73.639784),
                        new LatLng(45.457279, -73.639802),
                        new LatLng(45.457259, -73.639828),
                        new LatLng(45.457229, -73.639883),
                        new LatLng(45.457211, -73.639984),
                        new LatLng(45.457213, -73.640018),
                        new LatLng(45.457305, -73.640073),
                        new LatLng(45.457311, -73.640049),
                        new LatLng(45.457361, -73.640076),
                        new LatLng(45.457411, -73.640206),
                        new LatLng(45.457175, -73.640393),
                        new LatLng(45.457280, -73.640658),
                        new LatLng(45.457304, -73.640639)
                ));
        CJ_building.setTag("alpha");
        stylePolygon(CJ_building);

        // (DO) 7200, Sherbrooke O. (Stinger Dome)
        Polygon DO_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.457389, -73.637103),
                        new LatLng(45.458364, -73.635969),
                        new LatLng(45.457932, -73.635218),
                        new LatLng(45.456959, -73.636361)
                ));
        DO_building.setTag("alpha");
        stylePolygon(DO_building);

        // (FC) 7141, Sherbrooke O. ( F.C. Smith Building)
        Polygon FC_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.458599, -73.639588),
                        new LatLng(45.458627, -73.639665),
                        new LatLng(45.458665, -73.639683),
                        new LatLng(45.458741, -73.639624),
                        new LatLng(45.458750, -73.639571),
                        new LatLng(45.458721, -73.639493),
                        new LatLng(45.458734, -73.639485),
                        new LatLng(45.458709, -73.639417),
                        new LatLng(45.458717, -73.639412),
                        new LatLng(45.458702, -73.639372),
                        new LatLng(45.458720, -73.639360),
                        new LatLng(45.458717, -73.639352),
                        new LatLng(45.458743, -73.639333),
                        new LatLng(45.458727, -73.639295),
                        new LatLng(45.458702, -73.639315),
                        new LatLng(45.458697, -73.639303),
                        new LatLng(45.458680, -73.639316),
                        new LatLng(45.458670, -73.639290),
                        new LatLng(45.458661, -73.639295),
                        new LatLng(45.458601, -73.639136),
                        new LatLng(45.458610, -73.639128),
                        new LatLng(45.458615, -73.639095),
                        new LatLng(45.458603, -73.639066),
                        new LatLng(45.458582, -73.639058),
                        new LatLng(45.458575, -73.639065),
                        new LatLng(45.458569, -73.639051),
                        new LatLng(45.458558, -73.639058),
                        new LatLng(45.458537, -73.639003),
                        new LatLng(45.458520, -73.639016),
                        new LatLng(45.458507, -73.638982),
                        new LatLng(45.458528, -73.638965),
                        new LatLng(45.458518, -73.638935),
                        new LatLng(45.458382, -73.639040),
                        new LatLng(45.458392, -73.639070),
                        new LatLng(45.458415, -73.639052),
                        new LatLng(45.458427, -73.639086),
                        new LatLng(45.458410, -73.639102),
                        new LatLng(45.458430, -73.639154),
                        new LatLng(45.458424, -73.639161),
                        new LatLng(45.458432, -73.639181),
                        new LatLng(45.458419, -73.639192),
                        new LatLng(45.458442, -73.639255),
                        new LatLng(45.458457, -73.639244),
                        new LatLng(45.458517, -73.639408),
                        new LatLng(45.458511, -73.639411),
                        new LatLng(45.458514, -73.639423),
                        new LatLng(45.458487, -73.639442),
                        new LatLng(45.458512, -73.639520),
                        new LatLng(45.458525, -73.639509),
                        new LatLng(45.458535, -73.639540),
                        new LatLng(45.458531, -73.639561),
                        new LatLng(45.458539, -73.639585),
                        new LatLng(45.458556, -73.639594),
                        new LatLng(45.458580, -73.639574),
                        new LatLng(45.458590, -73.639597)
                ));
        FC_building.setTag("alpha");
        stylePolygon(FC_building);

        // (GE) 7141, Sherbrooke O. (Centre for Structural and Functional Genomics)
        Polygon GE_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.456945, -73.640742),
                        new LatLng(45.457174, -73.640570),
                        new LatLng(45.457130, -73.640452),
                        new LatLng(45.457142, -73.640443),
                        new LatLng(45.457040, -73.640164),
                        new LatLng(45.456798, -73.640346),
                        new LatLng(45.456896, -73.640612),
                        new LatLng(45.456872, -73.640630),
                        new LatLng(45.456894, -73.640689),
                        new LatLng(45.456918, -73.640671)
                ));
        GE_building.setTag("alpha");
        stylePolygon(GE_building);


        // (HA) 7141, Sherbrooke O. (Hingston Hall, wing HA)
        Polygon HA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459410, -73.641485),
                        new LatLng(45.459432, -73.641543),
                        new LatLng(45.459676, -73.641352),
                        new LatLng(45.459652, -73.641290),
                        new LatLng(45.459663, -73.641281),
                        new LatLng(45.459529, -73.640931),
                        new LatLng(45.459515, -73.640941),
                        new LatLng(45.459492, -73.640882),
                        new LatLng(45.459252, -73.641070),
                        new LatLng(45.459276, -73.641133),
                        new LatLng(45.459260, -73.641146),
                        new LatLng(45.459395, -73.641495)
                ));
        HA_building.setTag("alpha");
        stylePolygon(HA_building);

        // (HB) 7141, Sherbrooke O. (Hingston Hall, wing HB)
        Polygon HB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459140, -73.642244),
                        new LatLng(45.459161, -73.642303),
                        new LatLng(45.459360, -73.642145),
                        new LatLng(45.459339, -73.642089),
                        new LatLng(45.459362, -73.642070),
                        new LatLng(45.459358, -73.642057),
                        new LatLng(45.459502, -73.641943),
                        new LatLng(45.459527, -73.642003),
                        new LatLng(45.459556, -73.641978),
                        new LatLng(45.459368, -73.641496),
                        new LatLng(45.458972, -73.641806),
                        new LatLng(45.458997, -73.641878),
                        new LatLng(45.458963, -73.641906),
                        new LatLng(45.459105, -73.642271)
                ));
        HB_building.setTag("alpha");
        stylePolygon(HB_building);

        // (HC) 7141, Sherbrooke O. (Hingston Hall, wing HC)
        Polygon HC_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.459623, -73.642271),
                        new LatLng(45.459794, -73.642134),
                        new LatLng(45.459781, -73.642100),
                        new LatLng(45.459893, -73.642010),
                        new LatLng(45.459825, -73.641833),
                        new LatLng(45.459713, -73.641920),
                        new LatLng(45.459698, -73.641885),
                        new LatLng(45.459525, -73.642023)
                ));
        HC_building.setTag("alpha");
        stylePolygon(HC_building);

        // (HU) 7141, Sherbrooke O. (Applied Science hub)
        Polygon HU_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.458290, -73.641709),
                        new LatLng(45.458530, -73.642346),
                        new LatLng(45.458847, -73.642096),
                        new LatLng(45.458625, -73.641470)
                ));
        HU_building.setTag("alpha");
        stylePolygon(HU_building);

        // (JR) 7141, Sherbrooke O. (Jesuit Residence)
        Polygon JR_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.458495, -73.643402),
                        new LatLng(45.458559, -73.643348),
                        new LatLng(45.458566, -73.643364),
                        new LatLng(45.458634, -73.643308),
                        new LatLng(45.458606, -73.643236),
                        new LatLng(45.458624, -73.643221),
                        new LatLng(45.458583, -73.643119),
                        new LatLng(45.458570, -73.643130),
                        new LatLng(45.458539, -73.643055),
                        new LatLng(45.458484, -73.643099),
                        new LatLng(45.458489, -73.643112),
                        new LatLng(45.458478, -73.643121),
                        new LatLng(45.458469, -73.643096),
                        new LatLng(45.458397, -73.643153),
                        new LatLng(45.458427, -73.643227),
                        new LatLng(45.458410, -73.643242),
                        new LatLng(45.458448, -73.643343),
                        new LatLng(45.458466, -73.643329)
                ));
        JR_building.setTag("alpha");
        stylePolygon(JR_building);

        // (PC) 7200, Sherbrooke O. (PERFORM Centre)
        Polygon PC_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.457013, -73.637885),
                        new LatLng(45.457279, -73.637674),
                        new LatLng(45.456942, -73.636805),
                        new LatLng(45.456675, -73.637024)
                ));
        PC_building.setTag("alpha");
        stylePolygon(PC_building);

        // (PS) 7141, Sherbrooke O. (Physical Services Building)
        Polygon PS_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459706, -73.640312),
                        new LatLng(45.459851, -73.640198),
                        new LatLng(45.459864, -73.640228),
                        new LatLng(45.459981, -73.640134),
                        new LatLng(45.459664, -73.639317),
                        new LatLng(45.459624, -73.639349),
                        new LatLng(45.459576, -73.639229),
                        new LatLng(45.459287, -73.639457),
                        new LatLng(45.459333, -73.639579),
                        new LatLng(45.459403, -73.639525),
                        new LatLng(45.459444, -73.639631),
                        new LatLng(45.459414, -73.639655),
                        new LatLng(45.459607, -73.640158),
                        new LatLng(45.459638, -73.640135)
                ));
        PS_building.setTag("alpha");
        stylePolygon(PS_building);

        // (PT) 7141, Sherbrooke O. (Oscar Peterson Concert Hall)
        Polygon PT_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459372, -73.639253),
                        new LatLng(45.459505, -73.639149),
                        new LatLng(45.459470, -73.639054),
                        new LatLng(45.459458, -73.639071),
                        new LatLng(45.459312, -73.638696),
                        new LatLng(45.459170, -73.638811),
                        new LatLng(45.459314, -73.639189),
                        new LatLng(45.459322, -73.639183),
                        new LatLng(45.459340, -73.639234),
                        new LatLng(45.459361, -73.639220)
                ));
        PT_building.setTag("alpha");
        stylePolygon(PT_building);

        // (PY) 7141, Sherbrooke O. (Psychology Building)
        Polygon PY_building = mMap.addPolygon(new PolygonOptions()
                .add(
                      new LatLng(45.458848, -73.640835),
                        new LatLng(45.459180, -73.640573),
                        new LatLng(45.459200, -73.640623),
                        new LatLng(45.459283, -73.640560),
                        new LatLng(45.459233, -73.640432),
                        new LatLng(45.459238, -73.640428),
                        new LatLng(45.459216, -73.640372),
                        new LatLng(45.459212, -73.640375),
                        new LatLng(45.459120, -73.640134),
                        new LatLng(45.459087, -73.640159),
                        new LatLng(45.459084, -73.640152),
                        new LatLng(45.459053, -73.640179),
                        new LatLng(45.459057, -73.640183),
                        new LatLng(45.459024, -73.640209),
                        new LatLng(45.459029, -73.640222),
                        new LatLng(45.458996, -73.640248),
                        new LatLng(45.458985, -73.640220),
                        new LatLng(45.458758, -73.640398),
                        new LatLng(45.458764, -73.640418),
                        new LatLng(45.458731, -73.640446),
                        new LatLng(45.458823, -73.640695),
                        new LatLng(45.458801, -73.640713)
                ));
        PY_building.setTag("alpha");
        stylePolygon(PY_building);

        // (RA) 7200, Sherbrooke O. (Recreation and Athletics Complex)
        Polygon RA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.456887, -73.638561),
                        new LatLng(45.457018, -73.638455),
                        new LatLng(45.457031, -73.638486),
                        new LatLng(45.457055, -73.638466),
                        new LatLng(45.457042, -73.638432),
                        new LatLng(45.457158, -73.638339),
                        new LatLng(45.457036, -73.638033),
                        new LatLng(45.457008, -73.638055),
                        new LatLng(45.456957, -73.637927),
                        new LatLng(45.457027, -73.637872),
                        new LatLng(45.456726, -73.637102),
                        new LatLng(45.456682, -73.636989),
                        new LatLng(45.456415, -73.637368),
                        new LatLng(45.456397, -73.637385),
                        new LatLng(45.456692, -73.638140),
                        new LatLng(45.456794, -73.638060),
                        new LatLng(45.456844, -73.638188),
                        new LatLng(45.456774, -73.638244),
                        new LatLng(45.456802, -73.638316),
                        new LatLng(45.456767, -73.638345),
                        new LatLng(45.456782, -73.638386),
                        new LatLng(45.456794, -73.638378),
                        new LatLng(45.456797, -73.638387),
                        new LatLng(45.456786, -73.638398),
                        new LatLng(45.456803, -73.638441),
                        new LatLng(45.456833, -73.638417)
                ));
        RA_building.setTag("alpha");
        stylePolygon(RA_building);

        // (RF) 7141, Sherbrooke O. (Loyola Jesuit Hall and Conference Centre)
        Polygon RF_building = mMap.addPolygon(new PolygonOptions()
                .add(
                      new LatLng(45.458508, -73.641375),
                        new LatLng(45.458641, -73.641270),
                        new LatLng(45.458647, -73.641284),
                        new LatLng(45.458806, -73.641160),
                        new LatLng(45.458801, -73.641145),
                        new LatLng(45.458821, -73.641129),
                        new LatLng(45.458785, -73.641036),
                        new LatLng(45.458763, -73.641050),
                        new LatLng(45.458692, -73.640854),
                        new LatLng(45.458699, -73.640851),
                        new LatLng(45.458683, -73.640807),
                        new LatLng(45.458587, -73.640880),
                        new LatLng(45.458539, -73.640756),
                        new LatLng(45.458513, -73.640776),
                        new LatLng(45.458487, -73.640709),
                        new LatLng(45.458439, -73.640749),
                        new LatLng(45.458464, -73.640814),
                        new LatLng(45.458414, -73.640854),
                        new LatLng(45.458473, -73.641007),
                        new LatLng(45.458383, -73.641077),
                        new LatLng(45.458428, -73.641199),
                        new LatLng(45.458511, -73.641137),
                        new LatLng(45.458548, -73.641232),
                        new LatLng(45.458526, -73.641245),
                        new LatLng(45.458537, -73.641278),
                        new LatLng(45.458486, -73.641317)
                ));
        RF_building.setTag("alpha");
        stylePolygon(RF_building);

        // (SC) 7141, Sherbrooke O. (Student Center)
        Polygon SC_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459109, -73.639425),
                        new LatLng(45.459226, -73.639336),
                        new LatLng(45.459244, -73.639385),
                        new LatLng(45.459310, -73.639330),
                        new LatLng(45.459271, -73.639230),
                        new LatLng(45.459293, -73.639210),
                        new LatLng(45.459205, -73.638977),
                        new LatLng(45.459157, -73.639001),
                        new LatLng(45.459141, -73.638962),
                        new LatLng(45.459076, -73.639010),
                        new LatLng(45.459089, -73.639055),
                        new LatLng(45.458997, -73.639130)
                ));
        SC_building.setTag("alpha");
        stylePolygon(SC_building);

        // (SH) 7141, Sherbrooke O. (Solar House)
        Polygon SH_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.459257, -73.642559),
                        new LatLng(45.459327, -73.642556),
                        new LatLng(45.459323, -73.642372),
                        new LatLng(45.459252, -73.642374)
                ));
        SH_building.setTag("alpha");
        stylePolygon(SH_building);

        // (SI) 4455, West Broadway (St. Ignatius of Loyola Church)
        Polygon SI_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.457965, -73.642684),
                        new LatLng(45.458167, -73.642525),
                        new LatLng(45.458116, -73.642399),
                        new LatLng(45.458097, -73.642414),
                        new LatLng(45.458040, -73.642266),
                        new LatLng(45.458052, -73.642234),
                        new LatLng(45.457995, -73.642082),
                        new LatLng(45.457969, -73.642082),
                        new LatLng(45.457940, -73.642008),
                        new LatLng(45.457878, -73.642053),
                        new LatLng(45.457856, -73.641996),
                        new LatLng(45.457823, -73.642024),
                        new LatLng(45.457833, -73.642049),
                        new LatLng(45.457765, -73.642104),
                        new LatLng(45.457777, -73.642135),
                        new LatLng(45.457721, -73.642180),
                        new LatLng(45.457729, -73.642200),
                        new LatLng(45.457679, -73.642241),
                        new LatLng(45.457689, -73.642270),
                        new LatLng(45.457648, -73.642302),
                        new LatLng(45.457643, -73.642291),
                        new LatLng(45.457633, -73.642299),
                        new LatLng(45.457640, -73.642319),
                        new LatLng(45.457622, -73.642333),
                        new LatLng(45.457630, -73.642353),
                        new LatLng(45.457569, -73.642399),
                        new LatLng(45.457631, -73.642574),
                        new LatLng(45.457655, -73.642557),
                        new LatLng(45.457658, -73.642564),
                        new LatLng(45.457729, -73.642507),
                        new LatLng(45.457739, -73.642533),
                        new LatLng(45.457781, -73.642501),
                        new LatLng(45.457794, -73.642533),
                        new LatLng(45.457844, -73.642492),
                        new LatLng(45.457856, -73.642521),
                        new LatLng(45.457911, -73.642476),
                        new LatLng(45.457926, -73.642515),
                        new LatLng(45.457931, -73.642511),
                        new LatLng(45.457941, -73.642537),
                        new LatLng(45.457916, -73.642558)
                ));
        SI_building.setTag("alpha");
        stylePolygon(SI_building);

        // (SP) 7141, Sherbrooke O. (Richard J. Renaud Science Complex)
        Polygon SP_building = mMap.addPolygon(new PolygonOptions()
                .add(
                       new LatLng(45.457438, -73.642003),
                        new LatLng(45.457642, -73.641846),
                        new LatLng(45.457672, -73.641928),
                        new LatLng(45.458327, -73.641412),
                        new LatLng(45.458276, -73.641283),
                        new LatLng(45.458208, -73.641337),
                        new LatLng(45.458180, -73.641262),
                        new LatLng(45.458256, -73.641201),
                        new LatLng(45.458194, -73.641037),
                        new LatLng(45.458339, -73.640922),
                        new LatLng(45.458316, -73.640862),
                        new LatLng(45.457998, -73.641114),
                        new LatLng(45.457979, -73.641066),
                        new LatLng(45.457893, -73.641132),
                        new LatLng(45.457908, -73.641170),
                        new LatLng(45.457524, -73.641472),
                        new LatLng(45.457202, -73.640657),
                        new LatLng(45.456983, -73.640829),
                        new LatLng(45.457024, -73.640935),
                        new LatLng(45.456996, -73.640960),
                        new LatLng(45.457017, -73.641015),
                        new LatLng(45.457041, -73.640992),
                        new LatLng(45.457158, -73.641295),
                        new LatLng(45.457148, -73.641303),
                        new LatLng(45.457179, -73.641382),
                        new LatLng(45.457169, -73.641392),
                        new LatLng(45.457185, -73.641432),
                        new LatLng(45.457210, -73.641414)
                ));
        SP_building.setTag("alpha");
        stylePolygon(SP_building);

        // (TA) 7079, de Terrebonne
        Polygon TA_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.459982, -73.640990),
                        new LatLng(45.460031, -73.640950),
                        new LatLng(45.459998, -73.640851),
                        new LatLng(45.459943, -73.640890)
                ));
        TA_building.setTag("alpha");
        stylePolygon(TA_building);

        // (TB) 7075, de Terrebonne
        Polygon TB_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.460031, -73.640950),
                        new LatLng(45.460087, -73.640906),
                        new LatLng(45.460049, -73.640809),
                        new LatLng(45.459998, -73.640851)
                ));
        TB_building.setTag("alpha");
        stylePolygon(TB_building);

        // (VE) 7141, Sherbrooke O. (Vanier extension)
        Polygon VE_building = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(45.458836, -73.639003),
                        new LatLng(45.459086, -73.638803),
                        new LatLng(45.458887, -73.638292),
                        new LatLng(45.458629, -73.638469)
                ));
        VE_building.setTag("alpha");
        stylePolygon(VE_building);

        // (VL) 7141, Sherbrooke O. (Vanier Library Building)
        Polygon VL_building = mMap.addPolygon(new PolygonOptions()
                .add(
                    new LatLng(45.459168, -73.638807),
                        new LatLng(45.459323, -73.638688),
                        new LatLng(45.459146, -73.638226),
                        new LatLng(45.459221, -73.638167),
                        new LatLng(45.459107, -73.637872),
                        new LatLng(45.458907, -73.638026),
                        new LatLng(45.458914, -73.638051),
                        new LatLng(45.458883, -73.638079),
                        new LatLng(45.458899, -73.638121),
                        new LatLng(45.458869, -73.638146),
                        new LatLng(45.458889, -73.638195),
                        new LatLng(45.458856, -73.638221),
                        new LatLng(45.458886, -73.638297),
                        new LatLng(45.459087, -73.638800),
                        new LatLng(45.459151, -73.638767)
                ));
        VL_building.setTag("alpha");
        stylePolygon(VL_building);
    } //end of addLoyolaPolygons



    // listener method for when my location button is clicke, resets setMyLocationEnable to true
    // so the camera can stay on the user's location ( camera is disabled to stay on user's location
    // when user gesture moves the camera). Check onCameraMoveStarted listener method
    @Override
    public boolean onMyLocationButtonClick () {
        mMap.setMyLocationEnabled(true);
        return false; // returning false calls the super method, returning true does not
    }


    private void animateCamera(LatLng latLng, float zoomLevel){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    private void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location currentLocation = (Location) task.getResult();
                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        animateCamera(currentLatLng, zoomLevel);
                    }
                }
            });
        }catch (SecurityException e){
            // some problem occurred, return Concordia downtown Campus Location
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
        }
    }

//    private void viewDowntownCampus() {
//        animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
//    }
//
//    private void viewLoyolaCampus() {
//        animateCamera(concordiaLatLngLoyolaCampus, zoomLevel);
//    }

    private void toggleCampus() {
        if (toggleButton.isChecked()) {
            animateCamera(concordiaLatLngLoyolaCampus, zoomLevel);
        } else {
            animateCamera(concordiaLatLngDowntownCampus, zoomLevel);
        }
    }

    private void requestPermissions(){
        // permission not granted, request for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
        } else {
            // Permission has already been granted, DO NOTHING
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted, move the camera to current location
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            }
        }
    }

} //end of Maps Activity Class
