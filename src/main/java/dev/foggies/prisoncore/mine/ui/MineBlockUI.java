package dev.foggies.prisoncore.mine.ui;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.data.MineBlock;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.utils.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;

public class MineBlockUI extends Gui {

    private final PrisonCore plugin;
    private Mine mine;

    public MineBlockUI(PrisonCore plugin, Mine mine, Player player) {
        super(player, 6, "&aMine Blocks");
        this.plugin = plugin;
        this.mine = mine;
        setFallbackGui(p -> new MineUI(plugin, mine, p));
    }

    private final MenuScheme outline = new MenuScheme()
            .mask("111111111")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("111111111");

    private final MenuScheme blocks = new MenuScheme()
            .mask("000000000")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("000000000");


    @Override
    public void redraw() {
        MenuPopulator outlinePopulator = new MenuPopulator(this, outline);
        outlinePopulator.getSlots().forEach(slot -> {
            outlinePopulator.accept(
                    ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("").buildItem().build()
            );
        });

        MenuPopulator blockPopulator = new MenuPopulator(this, blocks);
        Arrays.stream(MineBlock.values())
                .forEach(block -> {
                    blockPopulator.accept(
                            ItemStackBuilder.of(Material.valueOf(block.name()))
                                    .name("&a" + block.getDisplayName())
                                    .lore(
                                            "&7Click here to change mine composition.",
                                            "",
                                            "&aWorth: &7" + NumberFormat.getInstance().format(block.getWorth())
                                    )
                                    .build(() -> {
                                        mine.setMineBlock(block);
                                        Players.msg(getPlayer(), "&7Mine block changed to " + block.getDisplayName() + ".");
                                        mine.getMineRegion().reset();
                                    })
                    );
                });


    }
}
