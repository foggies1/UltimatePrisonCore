package dev.foggies.prisoncore.mine.data;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import dev.foggies.prisoncore.api.PrisonRegion;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Getter
public class MineRegion extends PrisonRegion {

    public MineRegion(Mine mine, String data) {
        super(mine, data);
    }

    public MineRegion(Mine mine, Position pos1, Position pos2) {
        super(mine, pos1, pos2);
    }

    public long setRegions(List<Region> regions, BlockType blockType) {

        AtomicLong blocksChanged = new AtomicLong(0);

        Schedulers.async().run(() -> {

            RegionMask regionMask = new RegionMask(toCuboidRegion());

            try (
                    EditSession editSession = WorldEdit.getInstance().newEditSession(toFaweWorld());
            ) {

                editSession.setMask(regionMask);
                regions.forEach(region -> {
                    blocksChanged.addAndGet(editSession.setBlocks(region, blockType));
                });
                editSession.flushQueue();

            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        });

        return blocksChanged.get();
    }

    public long setRegion(Region region, BlockType blockType) {
        AtomicLong blocksChanged = new AtomicLong(0);

        Schedulers.async().run(() -> {

            RegionMask regionMask = new RegionMask(toCuboidRegion());

            try (
                    EditSession editSession = WorldEdit.getInstance().newEditSession(toFaweWorld());
            ) {

                editSession.setMask(regionMask);
                blocksChanged.set(editSession.setBlocks(region, blockType));
                editSession.flushQueue();

            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        });

        return blocksChanged.get();
    }

    @Override
    public void reset() {
        Schedulers.async()
                .run(() -> {
                    MineBlock mineBlock = getMine().getMineBlock();
                    try (
                            EditSession editSession = WorldEdit.getInstance().newEditSession(toFaweWorld())
                    ) {

                        editSession.setBlocks((Region) toCuboidRegion(), mineBlock.getType());
                        editSession.flushQueue();

                    } catch (MaxChangedBlocksException e) {
                        e.printStackTrace();
                    }
                });
        getMine().getOuterRegion().reset();
        teleportPlayers();
    }

    public Location getSpawn() {
        CuboidRegion cuboidRegion = toCuboidRegion();
        return new Location(
                getPos1().toLocation().getWorld(),
                cuboidRegion.getCenter().getX(),
                cuboidRegion.getMaximumY() + 1,
                cuboidRegion.getCenter().getZ()
        );
    }

    public void teleportPlayers() {
        getPlayersInMine().forEach(player -> {
            if (player == null) return;
            Players.msg(player, "&aTeleporting you to the mine spawn...");
            player.teleport(getSpawn());
        });
    }

    public List<Player> getPlayersInMine() {
        CuboidRegion mineRegion = toCuboidRegion();
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> {
                    if (mineRegion.contains(
                            player.getLocation().getBlockX(),
                            player.getLocation().getBlockY(),
                            player.getLocation().getBlockZ()
                    )) {
                        return player;
                    }
                    return null;
                }).collect(Collectors.toList());
    }

}
