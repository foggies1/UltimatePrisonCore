package dev.foggies.prisoncore.pickaxe.enchants.ui;

import dev.foggies.prisoncore.api.ClickRunnableMap;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.data.PickaxeEnchants;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.player.data.TPlayer;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantUI extends Gui {

    private Pickaxe pickaxe;
    private TPlayer tPlayer;

    public EnchantUI(Player player, Pickaxe pickaxe, TPlayer tPlayer) {
        super(player, 6, "&2Enchantments");
        this.pickaxe = pickaxe;
        this.tPlayer = tPlayer;
    }

    private final MenuScheme outline = new MenuScheme()
            .mask("111101111")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("111111111");

    private final MenuScheme enchants = new MenuScheme()
            .mask("000000000")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("000000000");

    @Override
    public void redraw() {
        MenuPopulator outlinePopulator = outline.newPopulator(this);
        MenuPopulator enchantPopulator = enchants.newPopulator(this);

        outlinePopulator.getSlots().forEach(slot -> {
            outlinePopulator.accept(ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("").buildItem().build());
        });

        PickaxeEnchants pickaxeEnchants = pickaxe.getPickaxeEnchants();

        pickaxeEnchants
                .getEnchants()
                .values()
                .forEach(enchant -> {

                    AbstractEnchant aEnchant = enchant.getEnchant();
                    long affordableLevels = enchant.getMaximumLevelPlayerCanAfford(tPlayer);

                    List<String> lore = new ArrayList<>(aEnchant.getDescription());
                    lore.add("");
                    lore.add("&7Current Level: &a" + enchant.getLevel());
                    lore.add("&7Upgrade x1 (Left): &a" + enchant.calculateValue(1));
                    lore.add("&7Upgrade x10 (Right): &a" + enchant.calculateValue(10));
                    lore.add("&7Upgrade x100 (Middle): &a" + enchant.calculateValue(100));
                    lore.add("&7Upgrade Max: (Q) &a" + enchant.calculateValue(affordableLevels));

                    Map<ClickType, Runnable> clickTypeRunnableMap = new ClickRunnableMap()
                            .add(ClickType.LEFT, () -> {
                                enchant.upgradeEnchant(tPlayer, 1);
                                redraw();
                            })
                            .add(ClickType.RIGHT, () -> {
                                enchant.upgradeEnchant(tPlayer, 1);
                                redraw();
                            })
                            .add(ClickType.MIDDLE, () -> {
                                enchant.upgradeEnchant(tPlayer, 100);
                                redraw();
                            }).add(ClickType.DROP, () -> {
                                enchant.upgradeEnchant(tPlayer, affordableLevels);
                                redraw();
                            }).build();


                    enchantPopulator
                            .accept(
                                    ItemStackBuilder.of(Material.ENCHANTED_BOOK)
                                            .name(aEnchant.getDisplayName())
                                            .lore(lore)
                                            .hideAttributes()
                                            .buildFromMap(clickTypeRunnableMap)
                            );

                });

    }


}
