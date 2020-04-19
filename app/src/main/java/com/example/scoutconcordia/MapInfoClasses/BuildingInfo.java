package com.example.scoutconcordia.MapInfoClasses;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.example.scoutconcordia.DataStructures.LinkedList;

/** Class used to add information to a building */
public class BuildingInfo
{
    private String name;
    private String address;
    private String iconName;
    private String openingTimes;
    private LinkedList<LatLng> coordinates;
    private LatLng center;

    /** Default constructor for a BuildingInfo object */
    public BuildingInfo()
    {
        name = null;
        address = null;
        iconName = "smiling.png";
        coordinates = new LinkedList<>(new LatLng(0,0));
        center = null;
    }

    /** Parametrized constructor for a BuildingInfo object
     * @param name Name of the building
     * @param address Address of the building
     * @param openingTimes Opening hours of the building
     */
    public BuildingInfo(String name, String address, String openingTimes)
    {
        this.name = name;
        this.address = address;
        this.openingTimes = openingTimes;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getIconName()
    {
        return iconName;
    }
    
    public LinkedList<LatLng> getCoordinates()
    {
        return coordinates;
    }
    
    public LatLng getCenter()
    {
        return center;
    }

    public String getOpeningTimes()
    {
        return openingTimes;
    }

    /** Reads from a string of buildings with their corresponding information. Creates building
     * objects using this information for each building in the list.
     * @param readMe Information about the buildings.
     * @return Returns a LinkedList containing BuildingInfo objects for each of the buildings in
     * the file.
     */
    public static LinkedList<BuildingInfo> obtainBuildings(String[] readMe)
    {
        int currentPos;
        Scanner reader = null;
        String currentLine;
        BuildingInfo currentBuilding;
        BuildingInfo template = new BuildingInfo();
        LinkedList<BuildingInfo> returnMe = new LinkedList<>(template);
        
        try
        {
            for (int i = 0; i < readMe.length; i++)
            {
                currentBuilding = new BuildingInfo();
                currentLine = readMe[i];
                Log.println(Log.WARN, "printing", currentLine);
                currentPos = currentLine.indexOf("Name: ");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected a name but didn't find one");
                currentBuilding.name = (currentLine.substring(currentPos + 6));
                i++;

                i++;
                currentLine = readMe[i];
                currentPos = currentLine.indexOf("Address: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected an address but didn't find one");
                currentBuilding.address = (currentLine.substring(currentPos+10,currentLine.length() - 1));
                
                i++;
                currentLine = readMe[i];
                currentPos = currentLine.indexOf("Icon Name: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected an icon link but didn't find one");
                currentBuilding.iconName = (currentLine.substring(currentPos+12,currentLine.length() - 1));
                
                i++;
                currentLine = readMe[i];
                currentPos = currentLine.indexOf("Opening Times: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected opening times but didn't find one");
                currentBuilding.openingTimes = (currentLine.substring(currentPos+16,currentLine.length() - 1));
                i++; i++;
                currentLine = readMe[i];

                while (currentLine.charAt(currentLine.length()-1) != '}')
                {
                    int posOfhalfway;
                    double x_coordinate, y_coordinate;
                    currentPos = currentLine.indexOf("{");
                    posOfhalfway = currentLine.indexOf(",");
                    x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfhalfway));
                    y_coordinate = Double.parseDouble(currentLine.substring(posOfhalfway+2, currentLine.length()-2));
                    currentBuilding.coordinates.add(new LatLng(x_coordinate, y_coordinate));
                    i++;
                    currentLine = readMe[i];
                }
                for (int j = 0; j < 2; j++)
                {
                    int posOfhalfway;
                    double x_coordinate, y_coordinate;
                    currentPos = currentLine.indexOf("{");
                    posOfhalfway = currentLine.indexOf(",");
                    x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfhalfway));
                    y_coordinate = Double.parseDouble(currentLine.substring(posOfhalfway+2, currentLine.length()-2));
                    if (j < 1)
                        currentBuilding.coordinates.add(new LatLng(x_coordinate, y_coordinate));
                    else
                        currentBuilding.center = new LatLng(x_coordinate, y_coordinate);
                    i++;
                    currentLine = readMe[i];
                }
                returnMe.add(currentBuilding);
                if (i != readMe.length)
                    i++;
            }
        }
        catch (InputMismatchException ime)
        {
            Log.println(Log.WARN, "printing", ime.getMessage());
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
        return returnMe;
    }

    /** Override of the toString method
     * @return Returns a formatted print statement for the Building information
     */
    @Override public String toString()
    {
        String printMe = "";
        printMe += "Name: " + this.name + "\n";
        printMe += "Address: " + this.address + "\n";
        printMe += "IconName: " + this.iconName + "\n";
        printMe += "OpeningTimes: " + this.iconName + "\n";
        printMe += "Coordinates: " + coordinates.toString() + "\n";
        printMe += "Center: " + this.center;
        return printMe;
    }
}
