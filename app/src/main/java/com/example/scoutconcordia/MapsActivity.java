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



        //adds the polygons for the SGW campus
        addSGWPolygons();

    }//end of onMapReady

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
        int fillColor = Color.parseColor("#74091F");

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                strokeColor = Color.parseColor("#000000");
                fillColor = Color.parseColor("#74091F");
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

        // (ER) 2155, Guy

        // (EV) Engineering, Computer Science and Visual Arts Integrated Complex. 1515, Sainte-Catherine O.

        // (FA) 2060, Mackay

        // (FB) Faubourg Building 1250, Guy. 1600 Sainte-Catherine O.

        // (FG) Faubourg Sainte-Catherine Building 1610, Sainte-Catherine O.

        // (GA) Grey Nuns Annex 1211-1215, St-Mathieu

        // (GM) Guy-De Maisonneuve Building

        // (GN) Grey Nuns Building 1190, Guy. 1175, St-Mathieu

        // (GS) 1538, Sherbrooke

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

        // (PR) 2100, Mackay

        // (Q) 2010, Mackay

        // (R) 2050, Mackay

        // (RR) 2040, Mackay

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

        // (T) 2030, Mackay

        // (TD) Toronto-Dominion Building

        // (V) 2110, Mackay

        // (VA) Visual Arts Building. 1395, Rene-Levesque O.

        // (X) 2080, Mackay

        // (Z) 2090, Mackay


    }

} //end of Maps Activity Class