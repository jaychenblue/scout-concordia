package com.example.scoutconcordia;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.example.scoutconcordia.Activities.MapsActivity;
import com.example.scoutconcordia.DataStructures.Graph;
import com.example.scoutconcordia.MapInfoClasses.ShuttleInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.content.ContentValues.TAG;
import static android.view.View.VISIBLE;

/** This class is a helper class for the MapsActivity and is used when obtaining the directions
 * from 1 point to another on the map.*/
public class Directions extends MapsActivity {

    /** This method searches for a path between 2 classes.
     * @param fromMe The starting location as a string. e.g CC-105
     * @param toMe The destination location as a string. e.g H-100
     * @return Returns a list of object arrays, each containing a path for the desired step in the
     * overall directions to the location.
     */
    public static List<Object[]> searchForClass(String fromMe, String toMe) {
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
            }
            else
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

    /** This method is used to display the search results from the searchForClass method
     * @param results Takes in the Object array from the searchForClass method to draw the path.
     */
    public static void displaySearchResults(Object[] results)
    {
        LatLng[] dest = new LatLng[results.length];
        System.arraycopy(results, 0, dest, 0, results.length);

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
                }
                else if (point2 == node.getElement() && node.getType() == 0) //if the location matches and it is a classroom node
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
                setHallButtonsVisibility(true);
                setCCButtonsVisibility(false);
                floor1.performClick();
                break;
            case "H-2":
                setHallButtonsVisibility(true);
                setCCButtonsVisibility(false);
                floor2.performClick();
                break;
            case "H-8":
                setHallButtonsVisibility(true);
                setCCButtonsVisibility(false);
                floor8.performClick();
                break;
            case "H-9":
                setHallButtonsVisibility(true);
                setCCButtonsVisibility(false);
                floor9.performClick();
                break;
            case "CC-1":
                setCCButtonsVisibility(true);
                setHallButtonsVisibility(false);
                floorCC1.performClick();
                break;
            case "CC-2":
                setCCButtonsVisibility(true);
                setHallButtonsVisibility(false);
                floorCC2.performClick();
                break;
        }
    }

    /** This is the main method for getting the directions from a starting location to a destination.
     * This method is split up into many different possibilities and is called recursively when possible.
     * The possibilities are class -> class, building -> class, building -> building, and class->building.
     */
    public static void getDirections()
    {
        String startingPointType = "class"; // can be type class or building
        String destinationType = "class";  // can be type class or building

        for (int i = 0; i < restaurantMarkers.size(); i++)  // Check if either the destination or the starting point is a restaurant
        {
            if (restaurantMarkers.get(i).getTitle().equals(startingPoint))
            {
                startingPointType = BUILDING;
            }
            else if (restaurantMarkers.get(i).getTitle().equals(destination))
            {
                destinationType = BUILDING;
            }
        }

        if (startingPoint.length() > 8 && startingPoint.substring(startingPoint.length() - 8).equals(BUILDING_NAME))  // check if the starting point is a building
        {
            startingBuilding = startingPoint.split(" ")[0];
            startingPointType = BUILDING;
        }

        if (destination.length() > 8 && destination.substring(destination.length() - 8).equals(BUILDING_NAME))  // check if the destination is a building
        {
            destinationBuilding = destination.split(" ")[0];
            destinationType = BUILDING;
        }

        if(startingPoint != null) {
            if (startingPointType.equals(BUILDING) || startingPoint.equals(BUILDING)) //if the starting point is a building
            {
                if (destinationType.equals(BUILDING)) //if the destination is a building  (building -> building)
                {
                    directionsBuildingToBuilding();
                }
                else //if the destination is a classroom  (building -> classroom)
                {
                    directionsBuildingToClass();
                }
            }
            else //if the starting point is a classroom
            {
                if (destinationType.equals(BUILDING)) //if the destination is a building  (classroom -> building)
                {
                    directionsClassToBuilding();
                }
                else // if the destination is a classroom (class -> class)
                {
                    directionsClassToClass();
                }
            }
        }
    }

    /** Helper method for getting directions from class to class */
    public static void directionsClassToClass()
    {
        startingBuilding = startingPoint.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"
        destinationBuilding = destination.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"

        if (startingBuilding.equals(destinationBuilding))  // if both classes are in the same building
        {
            for (Marker marker : markerBuildings) { //set the marker onto the desired building
                if ((marker.getTitle()).equals(startingBuilding + " Building"))
                {
                    searchMarker.setPosition(marker.getPosition());
                    searchMarker.setVisible(false);
                }
            }
            exploreInsideButton.performClick();
            searchResults = searchForClass(startingPoint, destination);
            searchResultsIndex = -1;
            searchPath.setVisible(true);
        }
        else //if both classes are in different buildings
        {
            // go from the class to the building exit
            for (Marker marker : markerBuildings)   //set the marker onto the desired building
            {
                if ((marker.getTitle()).equals(startingBuilding + " Building"))
                {
                    searchMarker.setPosition(marker.getPosition());
                    searchMarker.setVisible(false);
                }
            }
            exploreInsideButton.performClick();

            if (startingBuilding.equals("H"))
            {
                searchResults = searchForClass(startingPoint, H100); //directions to exit for H building
            }
            else if (startingBuilding.equals("CC"))
            {
                searchResults = searchForClass(startingPoint, CC150);  //directions to exit for CC building
            }

            searchResultsIndex = -1;
            searchPath.setVisible(true);

            needMoreDirections = true;
            classToClass = true;

            // go from building to building

            // go from the building entrance to the class
        }
    }

    /** Helper method for getting directions from class to building */
    public static void directionsClassToBuilding()
    {
        // need to go from class room to exit (we can hard code the exit for H and for CC)
        startingBuilding = startingPoint.split("-")[0]; //this will obtain the beginning characters e.g "H" or "CC"
        for (Marker marker : markerBuildings) { //set the marker onto the desired building
            if ((marker.getTitle()).equals(startingBuilding + " Building"))
            {
                searchMarker.setPosition(marker.getPosition());
                searchMarker.setVisible(false);
            }
        }
        exploreInsideButton.performClick();

        if (startingBuilding.equals("H"))
        {
            searchResults = searchForClass(startingPoint, H100);
        }
        else if (startingBuilding.equals("CC"))
        {
            searchResults = searchForClass(startingPoint, CC150);  //directions to exit for CC building
        }
        searchResultsIndex = -1;
        searchPath.setVisible(true);
        needMoreDirections = true;
        // need to go from exit to the building. Will be called in the needMoreDirections loop
    }

    /** Helper method for getting directions from building to building. */
    public static void directionsBuildingToBuilding()
    {
        if (needMoreDirections)
        {
            needMoreDirections = false;
            searchResultsIndex = 99;
        }
        drawDirectionsPath(origin, locationMap.get(destination));
    }

    /** Helper method for getting directions from building to class. */
    public static void directionsBuildingToClass()
    {
        destinationBuilding = destination.split("-")[0];
        String destinationBuildingName = destination.split("-")[0] + " Building";
        String toMe = destination;

        for (Marker marker : markerBuildings)
        {
            if ((marker.getTitle()).equals(destinationBuildingName))
            {
                searchMarker.setPosition(marker.getPosition());
                searchMarker.setVisible(false);
            }
        }
        drawDirectionsPath(origin, locationMap.get(destinationBuildingName));  //draws the path from the start building to the destination building (This takes care of the building to building part)

        if (classToClass)  // if the general search is a class-> class search or just a building->class search
        {
            needMoreDirections = false;
            if (destinationBuilding.equals("H"))
            {
                searchResults = searchForClass(H100, toMe);
            }
            else if (destinationBuilding.equals("CC"))
            {
                searchResults = searchForClass(CC150, toMe);
            }
        }
        else
        {
            if (destinationBuilding.equals("H"))
            {
                searchResults = searchForClass(H100, toMe);  // search from the front door of the destination building to the destination classroom
            }
            else if (destinationBuilding.equals("CC"))
            {
                searchResults = searchForClass(CC150, toMe);   // search from the front door of the destination building to the destination classroom
            }
        }
        searchResultsIndex = -1;
        searchPath.setVisible(true);
    }

    /**
     * This is the method that draw the outdoor direction path between 2 points.
     * @param origin This is the first paramter to drawDirectionsPath method.
     * @param dest This is the second paramter to drawDirectionsPath method.
     * @return void.
     * @exception catch Throwable error.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected static void drawDirectionsPath(LatLng origin, LatLng dest){
        resetPath();
        List<LatLng> path = new ArrayList();
        //GeoApiContext context = new GeoApiContext.Builder()
        //        .apiKey(getString(R.string.google_maps_key))
        //        .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext);
        float time = 0;
        //travelTime = (TextView) findViewById((R.id.estimatedTravelTime));
        TravelMode mode = getTravelMode();

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
                        time = time + leg.duration.inSeconds;
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

        //to rounding up integers when it is 0
        BigDecimal timeRoundUp = BigDecimal.valueOf(time/60);
        int estimatedTime = new BigDecimal(String.valueOf(timeRoundUp)).setScale(0, RoundingMode.HALF_UP).intValue();
        //if shuttle not selected as travel mode
        if(travelMode != 4) {
            String estimateOutput = "";
            if (useMyLocation)
            {
                estimateOutput += "My location to ";
            }
            else if (startingPoint.length() > 8 && startingPoint.substring(startingPoint.length() - 8).equals(BUILDING_NAME)) //if the starting point is a building
            {
                estimateOutput += startingPoint+" to  ";
            }

            if (destination.length() > 8 && destination.substring(destination.length() - 8).equals(BUILDING_NAME)) //if the destination is a building
            {
                estimateOutput += destination +" ~";
            }
            travelTime.setText(estimateOutput + String.valueOf(estimatedTime) + " mins");
            travelTime.setVisibility(View.VISIBLE);
        }
        else {
            ShuttleInfo getShuttleEstimate = new ShuttleInfo();
            String buildingGiven = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                buildingGiven = startingPoint.toString();
            }
            if (buildingGiven.contains("GB") || buildingGiven.contains("H") || buildingGiven.contains("MB")
                    || buildingGiven.contains("LB") || buildingGiven.contains("EV") || buildingGiven.contains("GM")
                    || buildingGiven.contains("ER") || buildingGiven.contains("LS") || buildingGiven.contains("FB")
                    || buildingGiven.contains("FG") || buildingGiven.contains("GN") || buildingGiven.contains("CL")
                    || buildingGiven.contains("GA")) {
                travelTime.setText(getShuttleEstimate.getEstimatedRouteTimeFromLoyola());
                travelTime.setVisibility(View.VISIBLE);
            }
            else {
                travelTime.setText(getShuttleEstimate.getEstimatedRouteTimeFromSGW());
                travelTime.setVisibility(View.VISIBLE);
            }


        }
    }
}
