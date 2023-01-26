package dev.foggies.prisoncore.utils;

import java.util.List;

public enum SmallCaps {

    A("ᴀ"),
    B("ʙ"),
    C("ᴄ"),
    D("ᴅ"),
    E("ᴇ"),
    F("ꜰ"),
    G("ɢ"),
    H("ʜ"),
    I("ɪ"),
    J("ᴊ"),
    K("ᴋ"),
    L("ʟ"),
    M("ᴍ"),
    N("ɴ"),
    O("ᴏ"),
    P("ᴘ"),
    Q("ǫ"),
    R("ʀ"),
    S("ꜱ"),
    T("ᴛ"),
    U("ᴜ"),
    V("ᴠ"),
    W("ᴡ"),
    X("x"),
    Y("ʏ"),
    Z("ᴢ"),
    NUMBER_1("¹"),
    NUMBER_2("²"),
    NUMBER_3("³"),
    NUMBER_4("⁴"),
    NUMBER_5("⁵"),
    NUMBER_6("⁶"),
    NUMBER_7("⁷"),
    NUMBER_8("⁸"),
    NUMBER_9("⁹"),
    NUMBER_0("⁰");

    private final String conversion;

    SmallCaps(String conversion) {
        this.conversion = conversion;
    }

    public String getConversion() {
        return conversion;
    }

    public static List<String> convert(List<String> lore) {
        return lore.stream().map(SmallCaps::convert).toList();
    }

    public static String convert(String original) {
        String[] split = original.split("");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (!s.matches("[A-Za-z0-9]")) {
                sb.append(s);
            } else {
                if (s.matches("[0-9]")) {
                    sb.append(SmallCaps.valueOf("NUMBER_" + s).getConversion());
                } else {
                    sb.append(SmallCaps.valueOf(s.toUpperCase()).getConversion());
                }
            }
        }
        return sb.toString();
    }

}
