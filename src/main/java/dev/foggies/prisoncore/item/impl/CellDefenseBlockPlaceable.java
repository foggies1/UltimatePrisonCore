package dev.foggies.prisoncore.item.impl;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.cell.defense.DefenseBlock;
import dev.foggies.prisoncore.cell.defense.DefenseBlocks;
import dev.foggies.prisoncore.cell.region.CellRaidRegion;
import dev.foggies.prisoncore.item.PlaceableItem;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.utils.SmallCaps;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.utils.Players;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CellDefenseBlockPlaceable extends PlaceableItem {

    public CellDefenseBlockPlaceable() {
        super(
                "defense_block"
        );
    }

    @Override
    public void onClick(PrisonCore plugin, BlockPlaceEvent e, ItemStack itemStack, Cell cell, TPlayer tPlayer, Mine mine, Pickaxe pickaxe) {

        CellRaidRegion cellRaidRegion = cell.getCellRaidRegion();
        Position position = Position.of(e.getBlockPlaced());

        if (!cellRaidRegion.contains(position)) {
            Players.msg(tPlayer.toBukkit(), "&c" + SmallCaps.convert("You can only place Defense Blocks in the Raidable Region!"));
            e.setCancelled(true);
            return;
        }

        DefenseBlock defenseBlock = new DefenseBlock(cell, plugin, itemStack, position);
        DefenseBlocks defenseBlocks = cellRaidRegion.getDefenseBlocks();
        defenseBlocks.addDefenseBlock(defenseBlock);
        Players.msg(tPlayer.toBukkit(), "&a" + SmallCaps.convert("You've placed a " + defenseBlock.getTier().name() + " Defense Block!"));
        itemStack.setAmount(itemStack.getAmount() - 1);

    }


}
