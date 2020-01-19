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

import java.security.acl.LastOwnerException;
import java.util.concurrent.LinkedTransferQueue;


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


    }

} //end of Maps Activity Class