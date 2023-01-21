package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.manager.MineManager;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class BlockBreakListener {

    public BlockBreakListener(PrisonCore plugin) {

        MineManager mineManager = plugin.getMineManager();

        Events.subscribe(BlockBreakEvent.class)
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    Mine mine = mineManager.getMine(uuid);
                    if (mine == null) return;
                    mine.getMineRegion().expand(1);
                });

    }
}
