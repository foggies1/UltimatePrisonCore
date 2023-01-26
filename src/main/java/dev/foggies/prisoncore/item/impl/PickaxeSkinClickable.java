package dev.foggies.prisoncore.item.impl;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.item.ClickableItem;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.skin.PickaxeSkin;
import dev.foggies.prisoncore.player.data.TPlayer;
import me.lucko.helper.utils.Players;
import org.bukkit.inventory.ItemStack;

public class PickaxeSkinClickable extends ClickableItem {

    public PickaxeSkinClickable() {
        super(
                "pickaxe_skin"
        );
    }

    @Override
    public void onClick(PrisonCore plugin, ItemStack itemStack, Cell cell, TPlayer tPlayer, Mine mine, Pickaxe pickaxe) {
        PickaxeSkin pickaxeSkin = new PickaxeSkin(pickaxe, itemStack, plugin);

//        if (pickaxe.getPickaxeSkin() != null) {
//            Players.msg(pickaxe.toBukkit(), "&2&lPICKAXE &8» &7You have already applied a skin to your pickaxe!");
//            return;
//        }

        pickaxe.setPickaxeSkin(pickaxeSkin);
        Players.msg(pickaxe.toBukkit(), "&2&lPICKAXE &8» &7You have applied a skin to your pickaxe!");
        itemStack.setAmount(itemStack.getAmount() - 1);
    }
}
