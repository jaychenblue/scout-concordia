package com.example.scoutconcordia;

public class ShuttleInfo {

    // These attributes hold the shuttle times form specific campuses on given days
    String[] schedule_monToThurs_Loyola = {"07:30",
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
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
            "20:00",
            "20:30",
            "20:45",
            "21:05",
            "21:30",
            "22:00",
            "22:30",
            "23:00"};

    String[] schedule_friday_Loyola = {
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
            "12:20", "12:40", "12:55", "13:30", "14:05", "14:20", "14:40", "15:15", "15:30", "15:50", "16:25", "16:40", "17:00", "18:05", "18:40", "19:15", "19:50"};

    String[] schedule_MonToThurs_SGW = {"07:45",
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
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
            "20:00",
            "20:10",
            "20:30",
            "21:00",
            "21:25",
            "21:45",
            "22:00",
            "23:30",
            "23:00"};

    String[] schedule_friday_SGW = {
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
            "12:20", "12:55", "13:30", "13:45", "14:05", "14:40", "14:55", "15:15", "15:50", "16:05", "16:25", "17:15", "18:05", "18:40", "19:15", "19:50"};

    public ShuttleInfo() {
    }

    // This returns a double array, which holds the shuttle time of the requested day and location
    public double[] getLoyolaMondayToThursdayTimes() {

        String[] numRange = schedule_monToThurs_Loyola;

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

        String[] numRange = schedule_MonToThurs_SGW;

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
    public double[] getLoyolaFridayTimes() {

        String[] numRange = schedule_friday_Loyola;

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
    public double[] getSGWFridayTimes() {

        String[] numRange = schedule_friday_SGW;

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

}
