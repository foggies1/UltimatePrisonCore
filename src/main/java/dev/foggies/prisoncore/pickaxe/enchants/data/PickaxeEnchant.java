package dev.foggies.prisoncore.pickaxe.enchants.data;

import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.utils.Players;

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

    public void upgradeEnchantMax(TPlayer tPlayer) {
        long upgradableLevels = getMaximumLevelPlayerCanAfford(tPlayer);
        if (upgradableLevels > 0) {
            upgradeEnchant(tPlayer, upgradableLevels);
        } else {
            long cost = calculateValue(1);
            long orbs = tPlayer.getPlayerInfo().getCurrencyHolder().getCurrency(CurrencyType.ORBS);
            Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &7You need &c" + (cost - orbs) + " &7more Orbs to upgrade this enchant.");
        }
    }

    public void upgradeEnchant(TPlayer tPlayer, long levels) {

        CurrencyHolder currencyHolder = tPlayer.getPlayerInfo().getCurrencyHolder();
        long orbs = currencyHolder.getCurrency(CurrencyType.ORBS);
        long cost = calculateValue(levels);

        if (orbs < cost) {
            Players.msg(tPlayer.toBukkit(), "&2&lENCHANT &8» &7You need &c" + (cost - orbs) + " &7more Orbs to upgrade this enchant!");
            return;
        }

        currencyHolder.removeCurrency(CurrencyType.ORBS, cost);
        addLevel(level);

    }

    public long getMaximumLevelPlayerCanAfford(TPlayer tPlayer) {
        long orbs = tPlayer.getPlayerInfo().getCurrencyHolder().getCurrency(CurrencyType.ORBS);
        long maxLevel = 0;
        long cost = 0;
        while (cost < orbs) {
            maxLevel++;
            cost = calculateValue(maxLevel);
        }
        return maxLevel;
    }

    public long calculateValue(long levels) {
        return (long) (this.enchant.getCost() * Math.pow(this.enchant.getCostMultiplier(), this.level + levels));
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
