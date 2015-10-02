package pt.gngtv.utils;

import java.util.Locale;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Utils {

    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }

    public static String formatCurrency(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }
}
