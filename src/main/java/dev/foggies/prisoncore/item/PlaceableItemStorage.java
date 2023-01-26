package dev.foggies.prisoncore.item;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.api.Storage;
import dev.foggies.prisoncore.item.impl.CellDefenseBlockPlaceable;
import dev.foggies.prisoncore.item.impl.CellDefenseBombPlaceable;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlaceableItemStorage extends Storage<String, PlaceableItem> {

    private final PrisonCore plugin;

    public PlaceableItemStorage(PrisonCore plugin) {
        this.plugin = plugin;
        add("defence_block", new CellDefenseBlockPlaceable());
        add("defence_bomb", new CellDefenseBombPlaceable());
    }

    public boolean isPlaceable(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "placeable_item"), PersistentDataType.STRING);
    }

    public PlaceableItem get(ItemStack itemStack) {
        return get(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "placeable_item"), PersistentDataType.STRING));
    }

}
