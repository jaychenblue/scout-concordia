package com.example.scoutconcordia;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.scoutconcordia.Activities.MapsActivity;
import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Locations extends MapsActivity {

    /**This method is used to apply styling to the polygons that will be displayed on the map.
     * Different styles can be created by assigning different tags to the polygons
     * If you need polygons with the same styling just assign them the same tags
     */
    protected static void stylePolygon(Polygon polygon)
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

    public static void addLocationsToMap(String[] location)
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
            Resources res = getmContext().getResources();
            //Resources res = this.getResources();
            int resID = res.getIdentifier(((BuildingInfo)currentBuilding.getEle()).getIconName(), DRAWABLE, getmContext().getPackageName());
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
                Bitmap b = BitmapFactory.decodeResource(getmContext().getResources(), resID);
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

    public static void addRestaurantsToMap(String[] location)
    {
        int currentPos = 0;
        //String markerName = "";
        String markerNameWithLocation = "";
        LatLng markerLocation = null;
        String markerAddress = "";
        for (int i = 2; i < location.length - 1; i++)  // skip the first 2 lines of the file and the last line of the file
        {
            int posOfhalfway = 0;
            int posOfEnd = 0;
            double x_coordinate = 0, y_coordinate = 0;

            posOfhalfway = location[i].indexOf(",");
            posOfEnd = location[i].indexOf("}");

            //currentPos = location[i].indexOf("(");
            //markerName = location[i].substring(1, currentPos);
            currentPos = location[i].indexOf("{");
            markerNameWithLocation = location[i].substring(1, currentPos);

            x_coordinate = Double.parseDouble(location[i].substring(currentPos+1,posOfhalfway));
            y_coordinate = Double.parseDouble(location[i].substring(posOfhalfway+2, posOfEnd));
            markerLocation = new LatLng(x_coordinate, y_coordinate);

            currentPos = location[i].indexOf("}");
            markerAddress = location[i].substring(currentPos + 1);

            Marker restaurantMarker = mMap.addMarker(new MarkerOptions()
                    .position(markerLocation)
                    .title(markerNameWithLocation)
                    .visible(false)
                    .flat(true)
                    .alpha(1)
                    .zIndex(44)
            );

            BuildingInfo restaurantInfo = new BuildingInfo(markerNameWithLocation, markerAddress, "");
            restaurantMarker.setTag(restaurantInfo);

            // set the icon for the restaurant marker
            int resID = getmContext().getResources().getIdentifier("restaurant_icon", DRAWABLE, getmContext().getPackageName());
            Bitmap smallMarker = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getmContext().getResources(), resID), 90, 90, false);
            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
            restaurantMarker.setIcon(smallMarkerIcon);

            restaurantMarkers.add(restaurantMarker);

            locations.add(markerNameWithLocation);     // add restaurant name to list for the search bar
            locationMap.put((markerNameWithLocation), markerLocation);   // add restaurant name and coordinate to the map
        }
    }
}
