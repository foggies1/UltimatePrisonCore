package dev.foggies.prisoncore.pickaxe.enchants.api;

import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.player.data.TPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

@RequiredArgsConstructor
@Getter
public abstract class AbstractEnchant {

    private final String identifier;
    private final String displayName;
    private final String color;
    private final String guiItem;
    private final String guiSlot;
    private final List<String> description;

    private final long startLevel;
    private final long maxLevel;
    private final long cost;
    private final long costMultiplier;

    private final float chance;

    public abstract EnchantResult onBlockBreak(Player player, Pickaxe pickaxe, Mine mine, TPlayer tPlayer, BlockBreakEvent e);



}
