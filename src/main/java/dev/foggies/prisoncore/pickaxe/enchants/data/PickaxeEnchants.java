package dev.foggies.prisoncore.pickaxe.enchants.data;

import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.pickaxe.enchants.storage.EnchantStorage;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class PickaxeEnchants {

    private final Pickaxe pickaxe;
    private final EnchantStorage enchantStorage;
    private final Map<String, PickaxeEnchant> enchants = new ConcurrentHashMap<>();

    public PickaxeEnchants(Pickaxe pickaxe, EnchantStorage enchantStorage) {
        this.pickaxe = pickaxe;
        this.enchantStorage = enchantStorage;
        this.enchantStorage.getStorage().values().forEach(
                enchant -> {
                    this.enchants.put(enchant.getIdentifier(), new PickaxeEnchant(pickaxe, enchant, enchant.getStartLevel()));
                }
        );
    }

    public PickaxeEnchants(Pickaxe pickaxe, EnchantStorage enchantStorage, String serialisedData) {
        this.pickaxe = pickaxe;
        this.enchantStorage = enchantStorage;
        String[] enchantData = serialisedData.split(";");
        for (String enchant : enchantData) {
            String[] enchantInfo = enchant.split(":");
            AbstractEnchant aEnchant = this.enchantStorage.getStorage().get(enchantInfo[0]);
            long level = Long.parseLong(enchantInfo[1]);
            if (aEnchant == null) {
                continue;
            }
            this.enchants.put(aEnchant.getIdentifier(), new PickaxeEnchant(pickaxe, aEnchant, level));
        }
        checkAndInsert();
    }

    public List<String> toLore() {
        return this.getEnchants().values()
                .stream()
                .map(enchant -> {
                    AbstractEnchant abstractEnchant = enchant.getEnchant();
                    if (enchant.getLevel() <= 0) {
                        return "";
                    }
                    return abstractEnchant.getColor() + "• " + SmallCaps.convert(abstractEnchant.getDisplayName() + " " + enchant.getLevel());
                })
                .collect(Collectors.toList());
    }

    public void checkAndInsert() {
        this.enchantStorage.getStorage().values().forEach(
                enchant -> {
                    if (!this.enchants.containsKey(enchant.getIdentifier())) {
                        this.enchants.put(enchant.getIdentifier(), new PickaxeEnchant(pickaxe, enchant, enchant.getStartLevel()));
                    }
                }
        );
    }

    public String serialise() {
        StringBuilder builder = new StringBuilder();
        this.enchants.values().forEach(
                enchant -> {
                    builder.append(enchant.getEnchant().getIdentifier()).append(":").append(enchant.getLevel()).append(";");
                }
        );
        return builder.toString();
    }


}
