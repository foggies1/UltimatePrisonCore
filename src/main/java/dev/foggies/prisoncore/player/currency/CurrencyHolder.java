package dev.foggies.prisoncore.player.currency;

import dev.foggies.prisoncore.player.data.TPlayer;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyHolder {

    private TPlayer player;
    private final Map<CurrencyType, Long> currencies = new ConcurrentHashMap<>();

    public CurrencyHolder(TPlayer player) {
        this.player = player;
        Arrays.stream(CurrencyType.values())
                .forEach(type -> {
                    currencies.put(type, Long.MAX_VALUE);
                });
    }

    public CurrencyHolder(TPlayer player, String serialisedData) {
        this.player = player;
        String[] data = serialisedData.split(";");
        Arrays.stream(data)
                .forEach(type -> {
                    String[] typeData = type.split(":");
                    currencies.put(CurrencyType.valueOf(typeData[0]), Long.parseLong(typeData[1]));
                });
        checkAndInsert();
    }

    public long getCurrency(CurrencyType currencyType) {
        if (!currencies.containsKey(currencyType)) {
            currencies.put(currencyType, 0L);
        }
        return this.currencies.get(currencyType);
    }

    public void setCurrency(CurrencyType currencyType, long amount) {
        if (!hasCurrency(currencyType)) {
            this.currencies.put(currencyType, amount);
        } else {
            this.currencies.replace(currencyType, amount);
        }
    }

    public void addCurrency(CurrencyType currencyType, long amount) {
        setCurrency(currencyType, getCurrency(currencyType) + amount);
    }

    public void removeCurrency(CurrencyType currencyType, long amount) {
        setCurrency(currencyType, getCurrency(currencyType) - amount);
    }

    public boolean hasEnough(CurrencyType currencyType, long amount) {
        return getCurrency(currencyType) >= amount;
    }

    public boolean hasCurrency(CurrencyType currencyType) {
        return this.currencies.containsKey(currencyType);
    }

    public void checkAndInsert() {
        Arrays.stream(CurrencyType.values())
                .forEach(type -> {
                    if (!hasCurrency(type)) {
                        currencies.put(type, 0L);
                    }
                });
    }

    public String serialise() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<CurrencyType, Long> entry : currencies.entrySet()) {
            builder.append(entry.getKey().name()).append(":").append(entry.getValue()).append(";");
        }
        return builder.toString();
    }


}
