package project.ium.tweb.ium_tweb_android.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    public static String convertDay(String day){
        if (day == null) return null;

        switch (day) {
            case "Lun":
                return "Lunedì";
            case "Mar":
                return "Martedì";
            case "Mer":
                return "Mercoledì";
            case "Gio":
                return "Giovedì";
            case "Ven":
                return "Venerdì";
            default:
                return null;
        }
    }

    public static String convertHour(int hour){
        return Integer.toString(hour) + ":00";
    }

    public static String convertTimestamp(double timestamp) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss");
        return formatter.format(new Date((long) timestamp));
    }

}
