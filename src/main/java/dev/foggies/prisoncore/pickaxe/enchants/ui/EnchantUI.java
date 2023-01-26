package dev.foggies.prisoncore.pickaxe.enchants.ui;

import dev.foggies.prisoncore.api.ClickRunnableMap;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.data.PickaxeEnchants;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.utils.Hex;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantUI extends Gui {

    private Pickaxe pickaxe;
    private TPlayer tPlayer;

    public EnchantUI(Player player, Pickaxe pickaxe, TPlayer tPlayer) {
        super(player, 6, "&f\uf000\uE009");
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
        CurrencyHolder currencyHolder = tPlayer.getPlayerInfo().getCurrencyHolder();
        NumberFormat nf = NumberFormat.getInstance();

        pickaxeEnchants
                .getEnchants()
                .values()
                .forEach(enchant -> {

                    AbstractEnchant aEnchant = enchant.getEnchant();
                    long affordableLevels = enchant.getLevelPlayerCanAfford(tPlayer);
                    long orbs = currencyHolder.getCurrency(CurrencyType.ORBS);

                    List<String> lore = new ArrayList<>(aEnchant.getDescription());
                    lore.add("");
                    lore.add("#417D4DPickaxe Information");
                    lore.add("");
                    lore.add("#73F18ECurrent Level &7❘ &f" + nf.format(enchant.getLevel()));
                    lore.add("#73F18EMaximum Level &7❘ &f" + nf.format(aEnchant.getMaxLevel()));
                    lore.add("");
                    lore.add("#417D4DPrice Information");
                    lore.add("");
                    lore.add("#417D4D[!] #73F18EYour Orbs: #2BFF577" + nf.format(orbs));
                    lore.add("");
                    lore.add("#417D4D[+1] #73F18ELeft Click &7❘ #2BFF57" + nf.format(enchant.calculateValue(1)));
                    lore.add("#417D4D[+10] #73F18ERight Click &7❘ #2BFF57" + nf.format(enchant.calculateValue(10)));
                    lore.add("#417D4D[+100] #73F18EMiddle Click &7❘ #2BFF57" + nf.format(enchant.calculateValue(100)));
                    lore.add("#417D4D[Max] #73F18EDrop Click &7❘ #2BFF57" + nf.format(enchant.calculateValue(affordableLevels)));

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
                                enchant.upgradeMax(tPlayer);
                                redraw();
                            }).build();


                    enchantPopulator
                            .accept(
                                    ItemStackBuilder.of(Material.ENCHANTED_BOOK)
                                            .name(Hex.translate("#417D4D&l" + aEnchant.getDisplayName() + " Enchantment"))
                                            .lore(Hex.translate(lore))
                                            .hideAttributes()
                                            .buildFromMap(clickTypeRunnableMap)
                            );

                });

    }


}
