package com.example.scoutconcordia;

import java.util.Calendar;


// To use this class, the app has to essentially create an object of 'ShuttleInfo'.
// Then, the app needs to call either of two methods, depending on the location of the user.
// If the user is at SGW, the object should call 'getNextEarliestTimeFromSGW()'.
// If the user is at Loyola, the object should call 'getNextEarliestTimeFromLoyola()'.


public class ShuttleInfo {

    // These attributes hold the shuttle times form specific campuses on given days
    private String[] schedule_monToThurs_Loyola = {
            "07:30",
            "07:40",
            "07:55",
            "08:20",
            "08:35",
            "08:55",
            "09:10",
            "09:30",
            "09:45",
            "10:20",
            "10:35",
            "10:55",
            "11:10",
            "11:30",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:00",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
            "18:00",
            "18:30",
            "19:00",
            "19:30",
            "20:00",
            "20:30",
            "20:45",
            "21:05",
            "21:30",
            "22:00",
            "22:30",
            "23:00"};

    private String[] schedule_friday_Loyola = {
            "07:40",
            "08:15",
            "08:55",
            "09:10",
            "09:30",
            "10:20",
            "10:35",
            "11:10",
            "11:30",
            "11:45",
            "12:20",
            "12:40",
            "12:55",
            "13:30",
            "14:05",
            "14:20",
            "14:40",
            "15:15",
            "15:30",
            "15:50",
            "16:25",
            "16:40",
            "17:00",
            "18:05",
            "18:40",
            "19:15",
            "19:50"};

    private String[] schedule_MonToThurs_SGW = {
            "07:45",
            "08:05",
            "08:20",
            "08:35",
            "08:55",
            "09:10",
            "09:30",
            "09:45",
            "10:05",
            "10:20",
            "10:55",
            "11:10",
            "11:45",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:00",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
            "18:00",
            "18:30",
            "19:00",
            "19:30",
            "20:00",
            "20:10",
            "20:30",
            "21:00",
            "21:25",
            "21:45",
            "22:00",
            "23:30",
            "23:00"};

    private String[] schedule_friday_SGW = {
            "07:45",
            "08:20",
            "08:55",
            "09:30",
            "09:45",
            "10:05",
            "10:55",
            "11:10",
            "11:45",
            "12:05",
            "12:20",
            "12:55",
            "13:30",
            "13:45",
            "14:05",
            "14:40",
            "14:55",
            "15:15",
            "15:50",
            "16:05",
            "16:25",
            "17:15",
            "18:05",
            "18:40",
            "19:15",
            "19:50"};

    public String[] getSchedule_monToThurs_Loyola() {
        return schedule_monToThurs_Loyola;
    }

    public void setSchedule_monToThurs_Loyola(String[] schedule_monToThurs_Loyola) {
        this.schedule_monToThurs_Loyola = schedule_monToThurs_Loyola;
    }

    public String[] getSchedule_friday_Loyola() {
        return schedule_friday_Loyola;
    }

    public void setSchedule_friday_Loyola(String[] schedule_friday_Loyola) {
        this.schedule_friday_Loyola = schedule_friday_Loyola;
    }

    public String[] getSchedule_MonToThurs_SGW() {
        return schedule_MonToThurs_SGW;
    }

    public void setSchedule_MonToThurs_SGW(String[] schedule_MonToThurs_SGW) {
        this.schedule_MonToThurs_SGW = schedule_MonToThurs_SGW;
    }

    public String[] getSchedule_friday_SGW() {
        return schedule_friday_SGW;
    }

    public void setSchedule_friday_SGW(String[] schedule_friday_SGW) {
        this.schedule_friday_SGW = schedule_friday_SGW;
    }


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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";


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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "Today is " + today + ". The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_friday_SGW[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";


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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_monToThurs_Loyola[indexPosition] + " (" + nextShuttleTime + ")";


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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_monToThurs_Loyola[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_monToThurs_Loyola[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_monToThurs_Loyola[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "Today is " + today + ". The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_friday_Loyola[indexPosition] + " (" + nextShuttleTime + ")";

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

                messageToUser = "The current time is: " + timeString + " (" + timeOfDay + ")" + " and the next bus shuttle comes at: " + schedule_MonToThurs_SGW[indexPosition] + " (" + nextShuttleTime + ")";


                break;


        }

        // The shuttle time is hardcoded at 30 minutes, because of fluctuations, it is an estimation
        double estimatedTime = Math.round((((nextShuttleTime - timeOfDay) * 60) + 30) * 100.0) / 100.0;

        String overallEstimation = "The estimated route time is: " + estimatedTime;

        System.out.println((messageToUser + "\n" + overallEstimation + " minutes"));

//            return (messageToUser + "\n" + overallEstimation);

        return estimatedTime;

    }

    // This returns a double array, which holds the shuttle time of the requested day and location
    public double[] getLoyolaMondayToThursdayTimes() {

        String[] numRange = getSchedule_monToThurs_Loyola();

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

        String[] numRange = getSchedule_MonToThurs_SGW();

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

        String[] numRange = getSchedule_friday_Loyola();

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

        String[] numRange = getSchedule_friday_SGW();

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

}

