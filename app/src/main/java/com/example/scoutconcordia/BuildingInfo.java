package com.example.scoutconcordia;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidKeyException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class BuildingInfo
{
    private String name;
    private String address;
    private String iconName;
    private String openingTimes;
    private LinkedList<LatLng> coordinates;
    private LatLng center;
    
    public BuildingInfo()
    {
        name = null;
        address = null;
        iconName = "smiling.png";
        coordinates = new LinkedList<LatLng>(new LatLng(0,0));
        center = null;
    }

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

    public String getOpeningTimes() { return openingTimes; }
    
    
    public static void decryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);
        
            while (reader.hasNextLine())
            {
                String decryptMe = reader.nextLine();
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(decryptMe.getBytes("UTF8"));
                String writeMe = new String(textDecrypted);
                writer.println(writeMe);
            }
        }
        catch(Exception e)
        {
            Log.println(Log.WARN, "decrypting", "An Exception occured");
        }
        finally
        {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        }
    }
    
    public static void encryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);
            
            while (reader.hasNextLine())
            {
                String encryptMe = reader.nextLine();
                byte[] text = encryptMe.getBytes("UTF8");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                byte[] textEncrypted = desCipher.doFinal(text);
                String writeMe = new String(textEncrypted);
                writer.println(writeMe);
            }
        }
        catch(Exception e)
        {
            Log.println(Log.WARN, "decrypting", "An Exception occured");
        }
        finally
        {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        }
    }
    
    public static LinkedList<BuildingInfo> obtainBuildings(InputStream readMe)
    {
        int currentPos = 0;
        Scanner reader = null;
        String currentLine = null;
        BuildingInfo currentBuilding = null;
        BuildingInfo template = new BuildingInfo();
        LinkedList<BuildingInfo> returnMe = new LinkedList<BuildingInfo>(template);
        
        try
        {
            reader = new Scanner(readMe);
            while(reader.hasNext())
            {
                currentBuilding = new BuildingInfo();
                currentLine = reader.nextLine();
                Log.println(Log.WARN, "printing", currentLine);
                currentPos = currentLine.indexOf("Name: ");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected a name but didn't find one");
                currentBuilding.name = (currentLine.substring(currentPos + 5));
                reader.nextLine();

                currentLine = reader.nextLine();
                currentPos = currentLine.indexOf("Address: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected an address but didn't find one");
                currentBuilding.address = (currentLine.substring(currentPos+10,currentLine.length() - 1));
                
                currentLine = reader.nextLine();
                currentPos = currentLine.indexOf("Icon Name: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected an icon link but didn't find one");
                currentBuilding.iconName = (currentLine.substring(currentPos+12,currentLine.length() - 1));
                
                currentLine = reader.nextLine();
                currentPos = currentLine.indexOf("Opening Times: \"");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected opening times but didn't find one");
                currentBuilding.openingTimes = (currentLine.substring(currentPos+16,currentLine.length() - 1));
                reader.nextLine();
                currentLine = reader.nextLine();

                while (currentLine.charAt(currentLine.length()-1) != '}')
                {
                    int posOfhalfway = 0;
                    double x_coordinate = 0, y_coordinate = 0;
                    currentPos = currentLine.indexOf("{");
                    posOfhalfway = currentLine.indexOf(",");
                    x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfhalfway));
                    y_coordinate = Double.parseDouble(currentLine.substring(posOfhalfway+2, currentLine.length()-2));
                    currentBuilding.coordinates.add(new LatLng(x_coordinate, y_coordinate));
                    currentLine = reader.nextLine();
                }
                for (int i = 0; i < 2; i++)
                {
                    int posOfhalfway = 0;
                    double x_coordinate = 0, y_coordinate = 0;
                    currentPos = currentLine.indexOf("{");
                    posOfhalfway = currentLine.indexOf(",");
                    x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfhalfway));
                    y_coordinate = Double.parseDouble(currentLine.substring(posOfhalfway+2, currentLine.length()-2));
                    if (i < 1)
                        currentBuilding.coordinates.add(new LatLng(x_coordinate, y_coordinate));
                    else
                        currentBuilding.center = new LatLng(x_coordinate, y_coordinate);
                    currentLine = reader.nextLine();
                }
                returnMe.add(currentBuilding);
                if (reader.hasNextLine())
                    reader.nextLine();
            }
        }
        catch (InputMismatchException ime)
        {
            Log.println(Log.WARN, "printing", ime.getMessage());
        }
        catch (Exception e)
        {
            Log.println(Log.WARN, "printing", "An error occurred with the stream");
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
        return returnMe;
    }
    
    public static void writeCenters(double[][][] locations)
    {
        PrintWriter pw = null;
        try
        {
            pw = new PrintWriter("");
            for (int i = 0; i < locations.length; i++)
            {
                double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
                for (int j = 0; j < locations[i].length; j++)
                {
                    if (j == 0)
                    {
                        x1 = locations[i][j][0];
                        x2 = locations[i][j][0];
                        y1 = locations[i][j][1];
                        y2 = locations[i][j][1];
                    }
                    if (locations[i][j][0] < x1)
                        x1 = locations[i][j][0];
                    if (locations[i][j][0] > x2)
                        x2 = locations[i][j][0];
                    if (locations[i][j][1] < y1)
                        y1 = locations[i][j][1];
                    if (locations[i][j][1] > y2)
                        y2 = locations[i][j][1];
            
                }
                float center_x = (float) x1 + (float) ((x2 - x1) / 2);
                float center_y = (float) y1 + (float) ((y2 - y1) / 2);
                pw.println("{ " + center_x + ", " + center_y  + "}");
            }
        } catch (FileNotFoundException fnf)
        {
            System.out.println("Stop being Stupid!");
        }
        finally
        {
            if (pw != null)
                pw.close();
        }
    }

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
