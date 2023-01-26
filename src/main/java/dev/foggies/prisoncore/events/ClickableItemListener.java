package dev.foggies.prisoncore.events;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.CellManager;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.item.ClickableItem;
import dev.foggies.prisoncore.item.ClickableItemStorage;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class ClickableItemListener {

    public ClickableItemListener(PrisonCore plugin) {

        ClickableItemStorage itemStorage = plugin.getClickableItemStorage();
        PlayerManager playerManager = plugin.getPlayerManager();
        MineManager mineManager = plugin.getMineManager();
        PickaxeManager pickaxeManager = plugin.getPickaxeManager();
        CellManager cellManager = plugin.getCellManager();

        Events.subscribe(PlayerInteractEvent.class)
                .filter(PlayerInteractEvent::hasItem)
                .filter(event -> itemStorage.isClickable(event.getPlayer().getInventory().getItemInMainHand()))
                .handler(e -> {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();
                    ItemStack itemStack = player.getInventory().getItemInMainHand();

                    TPlayer tPlayer = playerManager.getPlayer(uuid);
                    Mine mine = mineManager.getMine(uuid);
                    Pickaxe pickaxe = pickaxeManager.getPickaxe(uuid);
                    Optional<Cell> cell = cellManager.getCell(uuid);

                    ClickableItem clickableItem = itemStorage.get(itemStack);

                    if (clickableItem == null) return;
                    if(cell.isEmpty()) return;

                    clickableItem.onClick(plugin, itemStack, cell.get(), tPlayer, mine, pickaxe);


                });


    }
}
