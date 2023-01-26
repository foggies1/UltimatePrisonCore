package dev.foggies.prisoncore.cell.region;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.cell.defense.DefenseBlocks;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CellRaidRegion {

    private final Cell cell;
    private Position pos1;
    private Position pos2;
    private Position beaconLocation;
    private DefenseBlocks defenseBlocks;
    private ArmorStand armorStand;

    public CellRaidRegion(Cell cell, CellRegion cellRegion) {
        this.cell = cell;

        CuboidRegion cellCuboidRegion = cellRegion.toCuboidRegion();

        int centerX = cellCuboidRegion.getCenter().getBlockX();
        int centerZ = cellCuboidRegion.getCenter().getBlockZ();

        this.pos1 = Position.of(centerX - 5, cellRegion.getPos1().getY() + 1, centerZ - 5, cellRegion.getPos1().getWorld());
        this.pos2 = Position.of(centerX + 5, cellRegion.getPos1().getY() + 10, centerZ + 5, cellRegion.getPos1().getWorld());

        CuboidRegion cuboidRegion = toCuboidRegion();
        centerX = cuboidRegion.getCenter().getBlockX();
        centerZ = cuboidRegion.getCenter().getBlockZ();
        this.beaconLocation = Position.of(centerX, pos1.getY(), centerZ, pos1.getWorld());

        this.defenseBlocks = new DefenseBlocks(cell, this);
        make();
        displayHologram();
    }

    public CellRaidRegion(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(":");
        this.pos1 = Position.of(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[0]);
        this.pos2 = Position.of(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), data[0]);
        this.defenseBlocks = new DefenseBlocks(cell, this, data[7]);

        CuboidRegion cuboidRegion = toCuboidRegion();
        int centerX = cuboidRegion.getCenter().getBlockX();
        int centerZ = cuboidRegion.getCenter().getBlockZ();
        this.beaconLocation = Position.of(centerX, pos1.getY(), centerZ, pos1.getWorld());

        make();
        displayHologram();
    }

    public void displayHologram() {
        Schedulers.builder()
                .sync()
                .every(1, TimeUnit.SECONDS)
                .run(() -> {
                    if (this.armorStand == null) {
                        this.armorStand = this.beaconLocation.toLocation().getWorld().spawn(this.beaconLocation.toLocation().add(0.5, 0.5, 0.5), ArmorStand.class);
                    }
                    this.armorStand.setCustomNameVisible(true);
                    this.armorStand.setCanMove(false);
                    this.armorStand.setVisible(false);
                    this.armorStand.setCustomName(Text.colorize("&c&l" + SmallCaps.convert("Cell Protection " + NumberFormat.getInstance().format(this.defenseBlocks.getTotalDefense()))));
                });
    }

    public void make() {
        Schedulers.async()
                .run(() -> {

                    try (
                            EditSession editSession = WorldEdit.getInstance().newEditSession(toFawe())
                    ) {

                        editSession.makeWalls(woolArea(), BlockTypes.RED_CARPET);
                        editSession.flushQueue();

                    } catch (MaxChangedBlocksException e) {
                        e.printStackTrace();
                    }

                });
        placeBeacon();
    }

    public boolean contains(Position position) {
        BlockVector3 vector3 = BlockVector3.at(position.getX(), position.getY(), position.getZ());
        Region raidableOuter = woolArea().getWalls();
        raidableOuter.getMaximumPoint().add(0, 50, 0);
        if (raidableOuter.contains(vector3)) return false;
        return toCuboidRegion().contains(vector3);
    }

    public void placeBeacon() {
        beaconLocation.toLocation().getBlock().setType(Material.BEACON);
    }

    public CuboidRegion woolArea() {
        return new CuboidRegion(
                BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ()),
                BlockVector3.at(pos2.getX(), pos1.getY(), pos2.getZ())
        );
    }

    public CuboidRegion toCuboidRegion() {
        return new CuboidRegion(
                BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ()),
                BlockVector3.at(pos2.getX(), pos2.getY(), pos2.getZ())
        );
    }

    public World toFawe() {
        return FaweAPI.getWorld(pos1.getWorld());
    }

    public String serialise() {
        return pos1.getWorld() + ":" + pos1.getX() + ":" + pos1.getY() + ":" + pos1.getZ() + ":" + pos2.getX() + ":" + pos2.getY() + ":" + pos2.getZ() + ":" + this.defenseBlocks.serialise();
    }

}
