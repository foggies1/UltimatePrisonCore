package dev.foggies.prisoncore.mine.ui;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.utils.Players;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class MineUI extends Gui {

    private final PrisonCore plugin;
    private Mine mine;

    public MineUI(PrisonCore plugin, Mine mine, Player player) {
        super(player, 3, "&aMine");
        this.plugin = plugin;
        this.mine = mine;
    }

    private final MenuScheme outline = new MenuScheme()
            .mask("111111111")
            .mask("100000001")
            .mask("111111111");

    @Override
    public void redraw() {

        MenuPopulator outlinePopulator = new MenuPopulator(this, outline);
        outlinePopulator.getSlots().forEach(slot -> {
            outlinePopulator.accept(
                    ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("").buildItem().build()
            );
        });

        setItem(
                16,
                ItemStackBuilder.of(Material.DIAMOND_PICKAXE)
                        .name("&aReset Mine")
                        .lore(
                                "&7Click here to reset your mine."
                        )
                        .enchant(Enchantment.DIG_SPEED)
                        .build(() -> {
                            mine.getMineRegion().reset();
                            Players.msg(getPlayer(), "&aYou have reset your mine.");
                            redraw();
                        })
        );

        setItem(
                14,
                ItemStackBuilder.of(Material.IRON_DOOR)
                        .name("&aVisit Mine")
                        .lore(
                                "&7Click here to visit your Mine."
                        )
                        .enchant(Enchantment.DIG_SPEED)
                        .hideAttributes()
                        .build(() -> {
                            Player player = getPlayer();
                            player.teleport(mine.getMineRegion().getSpawn());
                            Players.msg(player, "&7Teleported to Mine successfully.");
                        })
        );

        setItem(
                12,
                ItemStackBuilder.of(Material.STONE)
                        .name("&aMine Blocks")
                        .lore(
                                "&7Click here to change mine composition."
                        )
                        .enchant(Enchantment.DIG_SPEED)
                        .hideAttributes()
                        .build(() -> {
                            Player player = getPlayer();
                            new MineBlockUI(plugin, mine, player).open();
                        })
        );

    }


}
