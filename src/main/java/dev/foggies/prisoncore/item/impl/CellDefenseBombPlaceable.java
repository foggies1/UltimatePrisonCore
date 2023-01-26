package dev.foggies.prisoncore.item.impl;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.cell.defense.DefenseBlock;
import dev.foggies.prisoncore.cell.defense.DefenseBlocks;
import dev.foggies.prisoncore.item.PlaceableItem;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.utils.SmallCaps;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.text3.Text;
import me.lucko.helper.text3.TextComponent;
import me.lucko.helper.text3.event.HoverEvent;
import me.lucko.helper.utils.Players;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CellDefenseBombPlaceable extends PlaceableItem {


    public CellDefenseBombPlaceable() {
        super(
                "defense_bomb"
        );
    }

    @Override
    public void onClick(PrisonCore plugin, BlockPlaceEvent e, ItemStack itemStack, Cell cell, TPlayer tPlayer, Mine mine, Pickaxe pickaxe) {

        DefenseBlocks defenseBlocks = cell.getCellRaidRegion().getDefenseBlocks();
        Position position = Position.of(e.getBlockAgainst());

        if (!isDefenseBlockInPerimeter(position, 1, cell)) {
            Players.msg(tPlayer.toBukkit(), "&c" + SmallCaps.convert("You can only place Defense Bombs next to a Defense Block!"));
            e.setCancelled(true);
            return;
        }

        List<Position> blocks = getBlocksInPerimeter(position.toLocation(), 1);
        List<Position> blocksDestroyed = new ArrayList<>();
        List<Position> blocksDamaged = new ArrayList<>();

        for (Position block : blocks) {

            Optional<DefenseBlock> defenseBlock = defenseBlocks.getDefenseBlock(block);
            if (defenseBlock.isEmpty()) continue;
            boolean destroyed = defenseBlock.get().damage(1);

            if (destroyed) {
                defenseBlocks.removeDefenseBlock(defenseBlock.get());
                block.toLocation().getBlock().setType(Material.AIR);
                blocksDestroyed.add(block);
            } else {
                blocksDamaged.add(block);
            }

            e.getBlockPlaced().setType(Material.AIR);
            position.toLocation().getWorld().spawnParticle(Particle.CLOUD, position.toLocation(), 100, 0.5, 0.5, 0.5, 0.5);
        }


        TextComponent.Builder hoverText = TextComponent.builder();

        hoverText.append(
                TextComponent.of(Text.colorize("&a" + SmallCaps.convert("Blocks Destroyed: ") + "&7" + blocksDestroyed.size() + "\n"))
        );
        for (Position block : blocksDestroyed) {
            hoverText.append(
                    TextComponent.of(Text.colorize("&7 - " + block.toLocation().getBlockX() + ", " + block.toLocation().getBlockY() + ", " + block.toLocation().getBlockZ() + "\n"))
            );
        }
        hoverText.append(
                TextComponent.of(Text.colorize("&a" + SmallCaps.convert("Blocks Damaged: ") + "&7" + blocksDamaged.size() + "\n"))
        );

        for (Position block : blocksDamaged) {
            hoverText.append(
                    TextComponent.of(Text.colorize("&7 - " + block.toLocation().getBlockX() + ", " + block.toLocation().getBlockY() + ", " + block.toLocation().getBlockZ() + "\n"))
            );
        }

        TextComponent hover = TextComponent.builder()
                .append(TextComponent.of(Text.colorize(" &7(&2" + SmallCaps.convert("Hover for details") + "&7)")))
                .hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, hoverText.build()))
                .build();

        TextComponent textComponent = TextComponent.builder()
                .append(TextComponent.of(Text.colorize("&a" + SmallCaps.convert("Defense Bomb Exploded!"))))
                .append(hover)
                .build();

        System.out.println(textComponent);
        Text.sendMessage(tPlayer.toBukkit(), textComponent);

    }

    public boolean isDefenseBlockInPerimeter(Position position, int radius, Cell cell) {
        List<Position> blocks = getBlocksInPerimeter(position.toLocation(), radius);
        for (Position pos : blocks) {
            if (cell.getCellRaidRegion().getDefenseBlocks().getDefenseBlock(pos).isPresent()) return true;
        }
        return false;
    }

    public List<Position> getBlocksInPerimeter(Location loc, int radius) {
        List<Position> blocks = new ArrayList<>();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                blocks.add(Position.of(new Location(loc.getWorld(), x, loc.getBlockY(), z)));
            }
        }
        return blocks;
    }


}
