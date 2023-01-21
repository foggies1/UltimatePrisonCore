package dev.foggies.prisoncore.mine.data;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.foggies.prisoncore.api.PrisonRegion;
import me.lucko.helper.Schedulers;
import me.lucko.helper.serialize.Position;

public class OuterMineRegion extends PrisonRegion {

    public OuterMineRegion(Mine mine, Position pos1, Position pos2) {
        super(mine, pos1, pos2);
    }

    public OuterMineRegion(Mine mine, String data) {
        super(mine, data);
    }

    @Override
    public void reset() {
        Schedulers.async()
                .run(() -> {
                    try (
                            EditSession editSession = WorldEdit.getInstance().newEditSession(toFaweWorld())
                    ) {


                        CuboidRegion region = toCuboidRegion();
                        CuboidRegion floor = new CuboidRegion(
                                BlockVector3.at(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ()),
                                BlockVector3.at(region.getMaximumPoint().getX(), region.getMinimumPoint().getY(), region.getMaximumPoint().getZ())
                        );

                        editSession.makeWalls(region, BlockTypes.BEDROCK);
                        editSession.setBlocks((Region) floor, BlockTypes.BEDROCK);
                        editSession.flushQueue();

                    } catch (MaxChangedBlocksException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void expand(int amount) {
        super.expand(amount);
    }
}
