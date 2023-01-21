package dev.foggies.prisoncore.pickaxe.data;

import dev.foggies.prisoncore.pickaxe.booster.PickaxeBooster;
import dev.foggies.prisoncore.pickaxe.booster.PickaxeBoosterType;
import dev.foggies.prisoncore.pickaxe.enchants.storage.EnchantStorage;
import dev.foggies.prisoncore.pickaxe.enchants.data.PickaxeEnchants;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Pickaxe {

    private final UUID uuid;
    private PickaxeBooster pickaxeBooster;
    private PickaxeInfo pickaxeInfo;
    private PickaxeEnchants pickaxeEnchants;

    public Pickaxe(UUID uuid, EnchantStorage enchantStorage) {
        this.uuid = uuid;
        this.pickaxeBooster = new PickaxeBooster(this, PickaxeBoosterType.ALL, 10000, 100);
        this.pickaxeInfo = new PickaxeInfo(this);
        this.pickaxeEnchants = new PickaxeEnchants(this, enchantStorage);
    }

    public Pickaxe(UUID uuid, EnchantStorage enchantStorage, String serialisedData) {
        this.uuid = uuid;
        String[] data = serialisedData.split(",");
        this.pickaxeInfo = new PickaxeInfo(this, data[0]);
        this.pickaxeBooster = data[1].equals("none") ? null : new PickaxeBooster(this, data[1]);
        this.pickaxeEnchants = new PickaxeEnchants(this, enchantStorage, data[2]);
    }

    public ItemStack toItem() {

        List<String> lore = new ArrayList<>();
        lore.add("&aInformation:");
        lore.addAll(this.pickaxeInfo.toLore());
        lore.add("&aEnchants:");
        lore.addAll(this.pickaxeEnchants.toLore());
        lore.add("&aBooster:");
        if (this.pickaxeBooster == null)
            lore.add("&fNone");
        else
            lore.addAll(this.pickaxeBooster.toLore());

        return ItemStackBuilder.of(Material.DIAMOND_PICKAXE)
                .name("&2&l" + toBukkit().getName() + "'s Pickaxe")
                .lore(
                    lore
                )
                .enchant(Enchantment.DIG_SPEED, 100)
                .hideAttributes()
                .build();
    }

    public Player toBukkit() {
        return Bukkit.getPlayer(this.uuid);
    }

    public String serialise() {
        return String.format("%s,%s,%s", this.pickaxeInfo.serialise(), this.pickaxeBooster == null ? "none" : this.pickaxeBooster.serialise(), this.pickaxeEnchants.serialise());
    }

}
