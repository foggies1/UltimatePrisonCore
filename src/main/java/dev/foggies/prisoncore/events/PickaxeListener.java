package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.ui.EnchantUI;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class PickaxeListener {

    public PickaxeListener(PickaxeManager pickaxeManager, PlayerManager playerManager) {

        Events.subscribe(PlayerInteractEvent.class)
                .handler(e -> {

                    if(e.getAction() != Action.RIGHT_CLICK_AIR) return;

                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    Pickaxe pickaxe = pickaxeManager.getPickaxe(uuid);
                    TPlayer tPlayer = playerManager.getPlayer(uuid);

                    if (pickaxe == null) return;
                    if (tPlayer == null) return;

                    new EnchantUI(player, pickaxe, tPlayer).open();
                });

    }
}
