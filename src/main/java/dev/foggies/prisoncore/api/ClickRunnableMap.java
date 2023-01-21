package dev.foggies.prisoncore.api;


import org.bukkit.event.inventory.ClickType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClickRunnableMap {

    private final Map<ClickType, Runnable> clickRunnableMap = new ConcurrentHashMap<>();

    public ClickRunnableMap add(ClickType clickType, Runnable runnable) {
        this.clickRunnableMap.put(clickType, runnable);
        return this;
    }

    public Map<ClickType, Runnable> build(){
        return this.clickRunnableMap;
    }

}
