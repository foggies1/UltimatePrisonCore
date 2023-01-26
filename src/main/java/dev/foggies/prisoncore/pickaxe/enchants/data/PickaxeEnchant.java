package dev.foggies.prisoncore.pickaxe.enchants.data;

import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.utils.Players;

import java.text.NumberFormat;

@Getter
@Setter
public class PickaxeEnchant {

    private Pickaxe pickaxe;
    private final AbstractEnchant enchant;
    private long level;

    public PickaxeEnchant(Pickaxe pickaxe, AbstractEnchant enchant, long level) {
        this.pickaxe = pickaxe;
        this.enchant = enchant;
        this.level = level;
    }

    public void upgradeMax(TPlayer tPlayer) {
        long levels = getLevelPlayerCanAfford(tPlayer);
        upgradeEnchant(tPlayer, levels);
    }

    public void upgradeEnchant(TPlayer tPlayer, long levels) {

        NumberFormat nf = NumberFormat.getInstance();
        if (this.level == this.enchant.getMaxLevel()) {
            Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &cYou cannot upgrade this enchant past level " + this.enchant.getMaxLevel() + "!");
            return;
        }

        if (this.level + levels > this.enchant.getMaxLevel()) {
            levels = this.enchant.getMaxLevel() - this.level;
        }

        CurrencyHolder currencyHolder = tPlayer.getPlayerInfo().getCurrencyHolder();
        long orbs = currencyHolder.getCurrency(CurrencyType.ORBS);
        long cost = calculateValue(levels);

        if (orbs < cost) {
            Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &7You need &c" + nf.format(cost - orbs) + " &7more orbs to upgrade this enchant!");
            return;
        }

        currencyHolder.removeCurrency(CurrencyType.ORBS, cost);
        addLevel(levels);
        Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &7You have upgraded your " + this.enchant.getDisplayName() + " &7enchant to level " + this.level + "&7!");
        Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &7You have spent &c" + nf.format(cost) + " &7Orbs to upgrade this enchant!");

    }

    public long getLevelPlayerCanAfford(TPlayer tPlayer) {
        long orbs = tPlayer.getPlayerInfo().getCurrencyHolder().getCurrency(CurrencyType.ORBS);
        long cost = 0;
        long levels = 0;
        while (this.level + levels < this.enchant.getMaxLevel() || orbs <= cost) {
            levels++;
            cost += calculateValue(levels);
        }
        return levels;
    }

    public long calculateValue(long levels) {
        long baseCost = this.enchant.getCost();
        double costMultiplier = this.enchant.getCostMultiplier();
        long level = this.level;

        long firstTerm = baseCost * level;
        long commonDiff = baseCost;
        long n = levels;
        return (firstTerm * n + commonDiff * n * (n - 1) / 2) * (long)costMultiplier;
    }

    public void addLevel(long level) {
        this.level += level;
    }

    public void removeLevel(long level) {
        if (this.level - level < 0) {
            this.level = 0;
        } else {
            this.level -= level;
        }
    }

}
