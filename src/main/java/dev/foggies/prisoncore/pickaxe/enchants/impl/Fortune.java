package dev.foggies.prisoncore.pickaxe.enchants.impl;

import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.pickaxe.enchants.api.EnchantResult;
import dev.foggies.prisoncore.player.data.TPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class Fortune extends AbstractEnchant {

    public Fortune() {
        super("fortune",
                "Fortune",
                "&c",
                "DIAMOND_ORE",
                "1",
                new ArrayList<>(),
                1,
                1000,
                1000,
                5,
                0.5f
        );
    }

    @Override
    public EnchantResult onBlockBreak(Player player, Pickaxe pickaxe, Mine mine, TPlayer tPlayer, BlockBreakEvent e) {
        return new EnchantResult() {
            @Override
            public void apply() {

            }
        };
    }
}
