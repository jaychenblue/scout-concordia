package com.example.scoutconcordia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class BuildingInfo implements java.io.Serializable {
    String name;
    String address;

    // constructor
    public BuildingInfo(String name, String address){
        this.name = name;
        this.address = address;
    }

    public static void getBuildingInfo() {}

    public static void writeToFile(String fileName, BuildingInfo buildingInfos) throws IOException {
        File f = new File(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(buildingInfos);
        oos.flush();
        oos.close();
    }
}
