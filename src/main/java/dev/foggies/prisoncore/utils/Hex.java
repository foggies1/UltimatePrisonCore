package dev.foggies.prisoncore.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hex {

    public static String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }

        return message;
    }

    public static List<String> translate(List<String> lore) {
        List<String> finalLore = new ArrayList<>();
        for (String line : lore) {
            finalLore.add(translate(line));
        }
        return finalLore;
    }

}
