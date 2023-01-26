package dev.foggies.prisoncore.item;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.player.data.TPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public abstract class PlaceableItem {

    private final String identifier;

    public abstract void onClick(PrisonCore plugin, BlockPlaceEvent e, ItemStack itemStack, Cell cell, TPlayer tPlayer, Mine mine, Pickaxe pickaxe);

}
