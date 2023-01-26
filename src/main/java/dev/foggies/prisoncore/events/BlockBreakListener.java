package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.CellManager;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.cell.defense.DefenseBlock;
import dev.foggies.prisoncore.cell.defense.DefenseBlocks;
import dev.foggies.prisoncore.cell.region.CellRaidRegion;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.utils.SmallCaps;
import me.lucko.helper.Events;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.utils.Players;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class BlockBreakListener {

    public BlockBreakListener(PrisonCore plugin) {

        MineManager mineManager = plugin.getMineManager();
        CellManager cellManager = plugin.getCellManager();

        Events.subscribe(BlockBreakEvent.class)
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    Block block = e.getBlock();
                    Location loc = block.getLocation();
                    Optional<Cell> optional = cellManager.getCell(uuid);

                    if (optional.isPresent()) {
                        Cell cell = optional.get();
                        CellRaidRegion cellRaidRegion = cell.getCellRaidRegion();
                        DefenseBlocks defenseBlocks = cellRaidRegion.getDefenseBlocks();

                        Optional<DefenseBlock> defenseBlock = defenseBlocks.getDefenseBlock(Position.of(block));
                        defenseBlock.ifPresent(db -> {

                            if(!player.isSneaking()){
                                Players.msg(player, "&c" + SmallCaps.convert("You must be sneaking to break a defense block!"));
                                e.setCancelled(true);
                                return;
                            }

                            defenseBlocks.removeDefenseBlock(db);

                            ItemStack itemStack = db.toItem(plugin);
                            player.getInventory().addItem(itemStack);
                            block.setType(Material.AIR);

                            Players.msg(player, "&a" + SmallCaps.convert("You've destroyed a " + db.getTier().name() + " Defense Block!"));
                            e.setCancelled(true);
                        });

                    } else {
                        e.setCancelled(true);
                    }

                });

    }
}
