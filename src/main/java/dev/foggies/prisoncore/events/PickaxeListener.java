package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.EnchantResult;
import dev.foggies.prisoncore.pickaxe.enchants.ui.EnchantUI;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import me.lucko.helper.Events;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PickaxeListener {

    public PickaxeListener(PrisonCore plugin, PickaxeManager pickaxeManager, PlayerManager playerManager, MineManager mineManager) {

        Events.subscribe(PlayerInteractEvent.class)
                .handler(e -> {

                    if (e.getAction() != Action.RIGHT_CLICK_AIR) return;

                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();

                    if(!isPickaxeItem(itemInHand, plugin)) return;

                    Pickaxe pickaxe = pickaxeManager.getPickaxe(uuid);
                    TPlayer tPlayer = playerManager.getPlayer(uuid);

                    if (pickaxe == null) return;
                    if (tPlayer == null) return;

                    new EnchantUI(player, pickaxe, tPlayer).open();
                });

        Events.subscribe(BlockBreakEvent.class)
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();

                    Pickaxe pickaxe = pickaxeManager.getPickaxe(uuid);
                    TPlayer tPlayer = playerManager.getPlayer(uuid);
                    Mine mine = mineManager.getMine(uuid);

                    if (pickaxe == null) return;
                    if (tPlayer == null) return;
                    if (mine == null) return;

                    pickaxe.getPickaxeEnchants()
                            .getEnchants()
                            .values()
                            .forEach(enchant -> {

                                if (enchant.getLevel() <= 0) return;

                                EnchantResult result = enchant.getEnchant().onBlockBreak(player, pickaxe, mine, tPlayer, e);
                                result.apply();

                            });

                });

    }

    public boolean isPickaxeItem(ItemStack itemStack, PrisonCore plugin){
        return itemStack.getItemMeta().getPersistentDataContainer().has(
                new NamespacedKey(plugin, "pickaxe"),
                PersistentDataType.STRING
        );
    }

}
