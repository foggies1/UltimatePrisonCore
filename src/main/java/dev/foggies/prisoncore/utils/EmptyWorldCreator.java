package dev.foggies.prisoncore.utils;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class EmptyWorldCreator {

    public World createEmptyWorld(String worldName) {

        World world = new WorldCreator(worldName)
                .generator(new EmptyWorldGenerator())
                .createWorld();
        world.setSpawnFlags(false, false);
        world.setPVP(false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        return world;
    }

}
