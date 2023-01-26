package dev.foggies.prisoncore.cell.defense;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.serialize.Position;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * A Defense Block is a block that can be placed in a Cell Raid Region,
 * to defend other player's from attacking the beacon which stores value.
 */

@Getter
@Setter
public class DefenseBlock {

    private final Cell cell;
    private final DefenseBlockTier tier;
    private final Position position;
    private int health;

    public DefenseBlock(Cell cell, PrisonCore plugin, ItemStack itemStack, Position position) {
        this.cell = cell;
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        String[] data = container.get(
                new NamespacedKey(
                        plugin,
                        "defence_block"
                ),
                PersistentDataType.STRING
        ).split(",");

        this.tier = DefenseBlockTier.valueOf(data[0]);
        this.health = Integer.parseInt(data[1]);
        this.position = position;
    }

    public DefenseBlock(DefenseBlockTier tier) {
        this.tier = tier;
        this.cell = null;
        this.position = null;
        this.health = tier.getMaxHealth();
    }

    public DefenseBlock(Cell cell, DefenseBlockTier tier, Position position) {
        this.cell = cell;
        this.tier = tier;
        this.position = position;
        this.health = tier.getMaxHealth();
    }

    public DefenseBlock(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split("/");
        this.tier = DefenseBlockTier.valueOf(data[0]);
        this.position = Position.of(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), data[1]);
        this.health = Integer.parseInt(data[5]);
    }

    public ItemStack toItem(PrisonCore plugin) {
        return ItemStackBuilder.of(tier.getMaterial())
                .name("&f&l" + SmallCaps.convert("Defence Block: ") + tier.getColor() + "" + SmallCaps.convert(tier.getDisplayName()))
                .lore(
                        "&f" + SmallCaps.convert("Health: ") + "&c" + SmallCaps.convert(String.valueOf(health)) + "&f/&c" + SmallCaps.convert(String.valueOf(tier.getMaxHealth())),
                        "",
                        "&f" + SmallCaps.convert("usage"),
                        "&7" + SmallCaps.convert("Place this block at a Cell Raid Region to"),
                        "&7" + SmallCaps.convert("defend other player's from attacking the"),
                        "&7" + SmallCaps.convert("beacon which stores value.")
                )
                .transformMeta(meta -> {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    container.set(
                            new NamespacedKey(plugin, "defence_block"),
                            PersistentDataType.STRING,
                            serialiseItem()
                    );
                    container.set(
                            new NamespacedKey(plugin, "placeable_item"),
                            PersistentDataType.STRING,
                            "defence_block"
                    );
                })
                .enchant(Enchantment.DIG_SPEED)
                .hideAttributes()
                .build();
    }

    /**
     * Damage the defense block.
     *
     * @param amount the amount of damage.
     * @return whether the defense block was destroyed.
     */
    public boolean damage(int amount) {
        if (this.health - amount <= 0) {
            return true;
        } else {
            this.health -= amount;
            return false;
        }
    }

    public String serialiseItem() {
        return String.format("%s,%s", this.tier.name(), this.health);
    }

    public String serialise() {
        return String.format("%s/%s/%s/%s/%s/%s", this.tier.name(), this.position.getWorld(), this.position.getX(), this.position.getY(), this.position.getZ(), this.health);
    }

}
