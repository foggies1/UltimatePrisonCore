package dev.foggies.prisoncore.item;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.api.Storage;
import dev.foggies.prisoncore.item.impl.BombClickable;
import dev.foggies.prisoncore.item.impl.PickaxeSkinClickable;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ClickableItemStorage extends Storage<String, ClickableItem> {

    private final PrisonCore plugin;

    public ClickableItemStorage(PrisonCore plugin) {
        this.plugin = plugin;
        add("pickaxe_skin", new PickaxeSkinClickable());
        add("bomb", new BombClickable());
    }

    public boolean isClickable(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "clickable_item"), PersistentDataType.STRING);
    }

    public ClickableItem get(ItemStack itemStack) {
        return get(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "clickable_item"), PersistentDataType.STRING));
    }

}
