package dev.foggies.prisoncore.cell.defense;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.utils.SmallCaps;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DefenseBomb {

    public ItemStack toItem(PrisonCore plugin) {
        return ItemStackBuilder.of(Material.TNT)
                .name("&c&l" + SmallCaps.convert("C4"))
                .lore(
                       "&7" + SmallCaps.convert("Used in Raiding Cells, place this next to a"),
                        "&7" + SmallCaps.convert("Defense Block to deplete it's health."),
                        "",
                        "&c&l" + SmallCaps.convert("NOTE: ") + "&7" + SmallCaps.convert("Some Defense Blocks are stronger"),
                        "&7" + SmallCaps.convert("than others, and will take more C4 to destroy.")
                )
                .transformMeta(meta -> {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    container.set(
                            new NamespacedKey(
                                    plugin,
                                    "placeable_item"
                            ),
                            PersistentDataType.STRING,
                            "defence_bomb"
                    );
                })
                .enchant(Enchantment.DIG_SPEED)
                .hideAttributes()
                .build();
    }

}
