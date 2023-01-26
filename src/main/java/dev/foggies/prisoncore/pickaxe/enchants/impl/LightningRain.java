package dev.foggies.prisoncore.pickaxe.enchants.impl;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.pickaxe.enchants.api.EnchantResult;
import dev.foggies.prisoncore.player.data.TPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class LightningRain extends AbstractEnchant {

    public LightningRain() {
        super(
                "lightnin_grain",
                "Lightning Rain",
                "#C1C17B",
                "DIAMOND_ORE",
                "1",
                new ArrayList<>(),
                1,
                1000,
                4393,
                2f,
                0.5f
        );
    }

    @Override
    public EnchantResult onBlockBreak(Player player, Pickaxe pickaxe, Mine mine, TPlayer tPlayer, BlockBreakEvent e) {

//        Block block = e.getBlock();
//        Location location = block.getLocation();
//        List<Location> circle = getCircle(location);
//        List<Region> spheres = toSpheres(circle);
//        MineRegion mineRegion = mine.getMineRegion();
//        mineRegion.setRegions(spheres, BlockTypes.AIR);

        return new EnchantResult() {
            @Override
            public void apply() {

            }
        };
    }

    public List<Region> toSpheres(List<Location> locations) {
        List<Region> spheres = new ArrayList<>();
        for (Location location : locations) {
            spheres.add(
                    new SphereRegionFactory().createCenteredAt(
                            BlockVector3.at(location.getX(), location.getY(), location.getZ()),
                            3
                    )
            );
        }
        return spheres;
    }

    public List<Location> getCircle(Location location) {
        List<Location> locations = new ArrayList<>();
        int radius = 10;
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        for (int i = 0; i < 360; i++) {
            double angle = i * Math.PI / 180;
            double x1 = x + (radius * Math.cos(angle));
            double z1 = z + (radius * Math.sin(angle));
            locations.add(new Location(location.getWorld(), x1, y, z1));
        }
        return locations;
    }

}
