package dev.foggies.prisoncore.item.impl;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.item.ClickableItem;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.items.Bomb;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BombClickable extends ClickableItem {

    public BombClickable() {
        super("bomb");
    }

    @Override
    public void onClick(PrisonCore plugin, ItemStack itemStack, Cell cell, TPlayer tPlayer, Mine mine, Pickaxe pickaxe) {

        Bomb bomb = new Bomb(getRadius(itemStack, plugin));
        bomb.throwBomb(itemStack, tPlayer.toBukkit(), mine);

    }

    public int getRadius(ItemStack itemStack, PrisonCore plugin){
        return itemStack.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(plugin, "bomb_radius"),
                PersistentDataType.INTEGER
        );
    }



}
