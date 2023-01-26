package dev.foggies.prisoncore.cell.region;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.serialize.Position;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Getter
@Setter
public class CellRegion {

    private final Cell cell;
    private Position pos1;
    private Position pos2;

    public CellRegion(Cell cell, World cellWorld, int lastCellLocation) {
        this.cell = cell;
        this.pos1 = Position.of(lastCellLocation, 50, lastCellLocation, cellWorld);
        this.pos2 = Position.of(lastCellLocation + 100, 100, lastCellLocation + 100, cellWorld);
        make();
    }

    public CellRegion(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(":");
        this.pos1 = Position.of(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[0]);
        this.pos2 = Position.of(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), data[0]);
        make();
    }

    public void make() {
        Schedulers.async()
                .run(() -> {
                    Vector3 center = toCuboidRegion().getCenter();
                    int centerX = center.getBlockX();
                    double Y = pos1.getY();
                    int centerZ = center.getBlockZ();

                    File schematic = new File(PrisonCore.getProvidingPlugin(PrisonCore.class).getDataFolder(), "cell.schem");
                    ClipboardFormat format = ClipboardFormats.findByFile(schematic);

                    if (format != null) {
                        try (
                                EditSession editSession = WorldEdit.getInstance().newEditSession(toFawe());
                                FileInputStream fileInputStream = new FileInputStream(schematic);
                        ) {

                            ClipboardReader reader = format.getReader(fileInputStream);
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard)
                                    .createPaste(editSession)
                                    .to(BlockVector3.at(centerX, Y, centerZ))
                                    .ignoreAirBlocks(false)
                                    .build();

                            Operations.complete(operation);
                        } catch (MaxChangedBlocksException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    public Position getSpawnLocation() {
        CuboidRegion region = toCuboidRegion();

        return Position.of(
                region.getCenter().getX(),
                region.getCenter().getY() + 1,
                region.getCenter().getZ(),
                pos1.getWorld()
        );
    }

    public CuboidRegion toCuboidRegion() {
        return new CuboidRegion(
                BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ()),
                BlockVector3.at(pos2.getX(), pos2.getY(), pos2.getZ())
        );
    }

    public com.sk89q.worldedit.world.World toFawe() {
        return FaweAPI.getWorld(pos1.getWorld());
    }

    public String serialise() {
        return pos1.getWorld() + ":" + pos1.getX() + ":" + pos1.getY() + ":" + pos1.getZ() + ":" + pos2.getX() + ":" + pos2.getY() + ":" + pos2.getZ();
    }

}
