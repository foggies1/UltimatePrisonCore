package dev.foggies.prisoncore.cell.defense;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum DefenseBlockTier {

    TIER_1("Tier 1", Material.IRON_BLOCK, "&7", 1),
    TIER_2("Tier 2", Material.GOLD_BLOCK, "&f", 2),
    TIER_3("Tier 3", Material.DIAMOND_BLOCK, "&e", 3),
    TIER_4("Tier 4", Material.EMERALD_BLOCK, "&6", 4),
    TIER_5("Tier 5", Material.NETHERITE_BLOCK, "&c", 5),
    TIER_6("Tier 6", Material.OBSIDIAN, "&b", 6),
    TIER_7("Tier 7", Material.REDSTONE_BLOCK, "&5", 7);

    private final String displayName;
    private final Material material;
    private final String color;
    private final int maxHealth;

}
