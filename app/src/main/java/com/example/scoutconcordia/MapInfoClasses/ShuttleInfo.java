package com.example.scoutconcordia.MapInfoClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.example.scoutconcordia.Activities.MainActivity;
import com.example.scoutconcordia.Activities.MapsActivity;
import com.example.scoutconcordia.R;
import com.example.scoutconcordia.R.*;
import com.google.api.client.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;




// To use this class, the app has to essentially create an object of 'ShuttleInfo'.
// Then, the app needs to call either of two methods, depending on the location of the user.
// If the user is at SGW, the object should call 'getNextEarliestTimeFromSGW()'.
// If the user is at Loyola, the object should call 'getNextEarliestTimeFromLoyola()'.


public class ShuttleInfo {

    private Context context = MapsActivity.mContext;

    // This method checks the time and day and finds the next relevant shuttle bus time, from SGW.
    // From there, it builds an estimate of how long it would take for the entire trip.

    public double getNextEarliestTimeFromSGW() {


        Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
        double hour = cal.get(Calendar.HOUR_OF_DAY);
        double minutes = cal.get(Calendar.MINUTE);

        String timeString = (int) hour + ":" + (int) minutes;

        // This variable keeps track of the current time.
        double timeOfDay = Math.round((hour + (minutes / 60)) * 100.0) / 100.0;

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        // This variable keeps track of the current day.
        String today = null;

        switch (currentDay) {

            case 1:
                today = "Sunday";
                break;

            case 2:
                today = "Monday";
                break;

            case 3:
                today = "Tuesday";
                break;

            case 4:
                today = "Wednesday";
                break;

            case 5:
                today = "Thursday";
                break;

            case 6:
                today = "Friday";
                break;

            case 7:
                today = "Saturday";
                break;
            default:
                today = "Monday";
                break;
        }


        // now we iterate through the schedule to check the next best time.

        String messageToUser = null;
        double nextShuttleTime = 0;
        double[] shuttleTimes;
        int indexPosition = 0;



        switch (today) {

            case "Sunday":
                messageToUser = "There are no bus shuttles on Sundays!";
                break;

            case "Monday":

                shuttleTimes = getSGWMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";


                break;

            case "Tuesday":


                shuttleTimes = getSGWMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursSGW()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Wednesday":
                shuttleTimes = getSGWMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursSGW()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Thursday":
                shuttleTimes = getSGWMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursSGW()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Friday":

                shuttleTimes = getSGWFridayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "Today is " + today + ". The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveFridaySGW()[indexPosition] + " (" + nextShuttleTime + ")";

                break;


            case "Saturday":
                messageToUser = "There are no bus shuttles on Saturdays!";
                break;


            default:
                shuttleTimes = getSGWMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursSGW()[indexPosition] + " (" + nextShuttleTime + ")";


                break;

        }

        // The shuttle time is hardcoded at 30 minutes, because of fluctuations, it is an estimation
        double estimatedTime = Math.round((((nextShuttleTime - timeOfDay) * 60) + 30) * 100.0) / 100.0;

        String overallEstimation = "The estimated route time is: " + estimatedTime;

        System.out.println((messageToUser + "\n" + overallEstimation + " minutes"));

//            return (messageToUser + "\n" + overallEstimation);

        return estimatedTime;

    }


    // This method checks the time and day and finds the next relevant shuttle bus time, from Loyola.
    // From there, it builds an estimate of how long it would take for the entire trip.

    public double getNextEarliestTimeFromLoyola() {


        Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
        double hour = cal.get(Calendar.HOUR_OF_DAY);
        double minutes = cal.get(Calendar.MINUTE);

        String timeString = (int) hour + ":" + (int) minutes;

        // This variable keeps track of the current time.
        double timeOfDay = Math.round((hour + (minutes / 60)) * 100.0) / 100.0;

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        // This variable keeps track of the current day.
        String today = null;

        switch (currentDay) {

            case 1:
                today = "Sunday";
                break;

            case 2:
                today = "Monday";
                break;

            case 3:
                today = "Tuesday";
                break;

            case 4:
                today = "Wednesday";
                break;

            case 5:
                today = "Thursday";
                break;

            case 6:
                today = "Friday";
                break;

            case 7:
                today = "Saturday";
                break;

            default:
                today = "Monday";
                break;
        }

//            System.out.println(today);
//            System.out.println("time is: " + timeOfDay);


        // now we iterate through the schedule to check the next best time.


        String messageToUser = null;
        double nextShuttleTime = 0;
        double[] shuttleTimes;
        int indexPosition = 0;


        switch (today) {

            case "Sunday":
                messageToUser = "There are no bus shuttles on Sundays!";
                break;

            case "Monday":

                shuttleTimes = getLoyolaMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";


                break;

            case "Tuesday":


                shuttleTimes = getLoyolaMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Wednesday":
                shuttleTimes = getLoyolaMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Thursday":
                shuttleTimes = getLoyolaMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";

                break;

            case "Friday":

                shuttleTimes = getLoyolaFridayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "Today is " + today + ". The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveFridayLoyola()[indexPosition] + " (" + nextShuttleTime + ")";

                break;


            case "Saturday":
                messageToUser = "There are no bus shuttles on Saturdays!";
                break;


            default:
                shuttleTimes = getLoyolaMondayToThursdayTimes();

                for (int i = 0; i < shuttleTimes.length; i++) {
                    if (timeOfDay < shuttleTimes[i]) {
                        nextShuttleTime = shuttleTimes[i];
                        indexPosition = i;
                        break;
                    }
                }

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + retrieveMonToThursLoyola()[indexPosition] + " (" + nextShuttleTime + ")";


                break;


        }

        // The shuttle time is hardcoded at 30 minutes, because of fluctuations, it is an estimation
        double estimatedTime = Math.round((((nextShuttleTime - timeOfDay) * 60) + 30) * 100.0) / 100.0;

        String overallEstimation = "The estimated route time is: " + estimatedTime;

        System.out.println((messageToUser + "\n" + overallEstimation + " minutes"));

//            return (messageToUser + "\n" + overallEstimation);

        return estimatedTime;

    }

    // This returns a double array, which holds the shuttle time of the requested day and location.
    // It essentially converts strings into doubles, for calculation purposoes.
    public double[] getLoyolaMondayToThursdayTimes() {

        String[] numRange = retrieveMonToThursLoyola();

        int[] hours = new int[numRange.length];
        double[] minutes = new double[numRange.length];

        for (int i = 0; i < numRange.length; i++) {
            String[] allTimes = numRange[i].split(":");
            hours[i] = Integer.parseInt(allTimes[0]);
            minutes[i] = Integer.parseInt(allTimes[1]);
        }

        double[] actualTimes = new double[hours.length];
        double calculatedMinutes = 0;
        for (int j = 0; j < actualTimes.length; j++) {

            calculatedMinutes = Math.round((minutes[j] / 60) * 100.0) / 100.0;
            actualTimes[j] = (hours[j] + (calculatedMinutes));

//                System.out.println(actualTimes[j]);
        }

        return actualTimes;
    }

    // This returns a double array, which holds the shuttle time of the requested day and location
    public double[] getSGWMondayToThursdayTimes() {

        String[] numRange = retrieveMonToThursSGW();

        int[] hours = new int[numRange.length];
        double[] minutes = new double[numRange.length];

        for (int i = 0; i < numRange.length; i++) {
            String[] allTimes = numRange[i].split(":");
            hours[i] = Integer.parseInt(allTimes[0]);
            minutes[i] = Integer.parseInt(allTimes[1]);
        }

        double[] actualTimes = new double[hours.length];
        double calculatedMinutes = 0;
        for (int j = 0; j < actualTimes.length; j++) {

            calculatedMinutes = Math.round((minutes[j] / 60) * 100.0) / 100.0;
            actualTimes[j] = (hours[j] + (calculatedMinutes));

        }
        return actualTimes;

    }

    // This returns a double array, which holds the shuttle time of the requested day and location
    public double[] getLoyolaFridayTimes() {

        String[] numRange = retrieveFridayLoyola();

        int[] hours = new int[numRange.length];
        double[] minutes = new double[numRange.length];

        for (int i = 0; i < numRange.length; i++) {
            String[] allTimes = numRange[i].split(":");
            hours[i] = Integer.parseInt(allTimes[0]);
            minutes[i] = Integer.parseInt(allTimes[1]);
        }

        double[] actualTimes = new double[hours.length];
        double calculatedMinutes = 0;
        for (int j = 0; j < actualTimes.length; j++) {

            calculatedMinutes = Math.round((minutes[j] / 60) * 100.0) / 100.0;
            actualTimes[j] = (hours[j] + (calculatedMinutes));

        }
        return actualTimes;

    }

    // This returns a double array, which holds the shuttle time of the requested day and location
    public double[] getSGWFridayTimes() {

        String[] numRange = retrieveFridaySGW();

        int[] hours = new int[numRange.length];
        double[] minutes = new double[numRange.length];

        for (int i = 0; i < numRange.length; i++) {
            String[] allTimes = numRange[i].split(":");
            hours[i] = Integer.parseInt(allTimes[0]);
            minutes[i] = Integer.parseInt(allTimes[1]);
        }

        double[] actualTimes = new double[hours.length];
        double calculatedMinutes = 0;
        for (int j = 0; j < actualTimes.length; j++) {

            calculatedMinutes = Math.round((minutes[j] / 60) * 100.0) / 100.0;
            actualTimes[j] = (hours[j] + (calculatedMinutes));

        }
        return actualTimes;
    }



    // These methods will handle the retrieval of times from external encrypted txt files,
    // And then convert them into a comprehensible String array which can be used by this class for calculations.
    public String[] retrieveMonToThursLoyola() {

        ArrayList<String> shuttleArrayList = new ArrayList<>();

            try
            {
                InputStream is = context.getResources().openRawResource(raw.schedule_montothurs_loyola);
                InputStreamReader readInput = new InputStreamReader(is);
                StringBuilder sb = new StringBuilder();
                BufferedReader bfr = new BufferedReader(readInput);

                String thisLine;

                // This while loop goes through the entire file line by line and analyzes it
                while ((thisLine = bfr.readLine()) != null) {
                    shuttleArrayList.add(thisLine);
                }

                readInput.close();
                bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] givenTimes = new String[shuttleArrayList.size()];

        for (int i = 0; i < givenTimes.length; i++){
            givenTimes[i] = shuttleArrayList.get(i);
        }

        return givenTimes;

    }


    public String[] retrieveFridayLoyola() {

        ArrayList<String> shuttleArrayList = new ArrayList<>();

        try {
            InputStream is = context.getResources().openRawResource(raw.schedule_friday_loyola);
            InputStreamReader readInput = new InputStreamReader(is);
            StringBuilder sb = new StringBuilder();
            BufferedReader bfr = new BufferedReader(readInput);

            String thisLine;

            // This while loop goes through the entire file line by line and analyzes it
            while ((thisLine = bfr.readLine()) != null) {
                shuttleArrayList.add(thisLine);
            }

            readInput.close();
            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] givenTimes = new String[shuttleArrayList.size()];

        for (int i = 0; i < givenTimes.length; i++){
            givenTimes[i] = shuttleArrayList.get(i);
        }

        return givenTimes;

    }


    public String[] retrieveMonToThursSGW() {

        ArrayList<String> shuttleArrayList = new ArrayList<>();

        try {
            InputStream is = context.getResources().openRawResource(raw.schedule_montothurs_sgw);
            InputStreamReader readInput = new InputStreamReader(is);
            StringBuilder sb = new StringBuilder();
            BufferedReader bfr = new BufferedReader(readInput);

            String thisLine;

            // This while loop goes through the entire file line by line and analyzes it
            while ((thisLine = bfr.readLine()) != null) {
                shuttleArrayList.add(thisLine);
            }

            readInput.close();
            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] givenTimes = new String[shuttleArrayList.size()];

        for (int i = 0; i < givenTimes.length; i++){
            givenTimes[i] = shuttleArrayList.get(i);
        }

        return givenTimes;

    }


    public String[] retrieveFridaySGW() {

        BufferedReader readDat = null;
        ArrayList<String> shuttleArrayList = new ArrayList<>();

        try {
            readDat = new BufferedReader(new FileReader("raw/schedule_friday_sgw.txt"));

            String thisLine;

            // This while loop goes through the entire file line by line and analyzes it
            while((thisLine = readDat.readLine()) != null)
            {
                shuttleArrayList.add(thisLine);
            }

            readDat.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] givenTimes = new String[shuttleArrayList.size()];

        for (int i = 0; i < givenTimes.length; i++){
            givenTimes[i] = shuttleArrayList.get(i);
        }

        return givenTimes;

    }



}

