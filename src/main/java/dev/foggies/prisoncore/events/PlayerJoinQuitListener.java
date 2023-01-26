package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.CellManager;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoinQuitListener {

    public PlayerJoinQuitListener(PrisonCore plugin) {
        MineManager mineManager = plugin.getMineManager();
        PlayerManager playerManager = plugin.getPlayerManager();
        PickaxeManager pickaxeManager = plugin.getPickaxeManager();
        CellManager cellManager = plugin.getCellManager();

        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    mineManager.loadMine(uuid);
                    playerManager.loadPlayer(uuid);
                    pickaxeManager.loadPickaxe(uuid);
                    cellManager.createCell(player, "testing");
                });

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    mineManager.unloadMine(uuid);
                    playerManager.unloadPlayer(uuid);
                    pickaxeManager.unloadPickaxe(uuid);
                });

    }


}
