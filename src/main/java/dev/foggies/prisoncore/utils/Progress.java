package dev.foggies.prisoncore.utils;

import com.google.common.base.Strings;
import me.lucko.helper.text3.Text;

public class Progress {

    public static String getBar(long current, long max, int totalBars, char symbol, String completedColor,
                                String notCompletedColor) {
        if (current > max) current = max;
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Text.colorize(Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars));
    }

}
