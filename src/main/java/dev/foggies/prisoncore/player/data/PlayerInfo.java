package dev.foggies.prisoncore.player.data;

import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.utils.Players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter

public class PlayerInfo {

    private static final int MAX_LEVEL = 1000;
    private TPlayer player;
    private CurrencyHolder currencyHolder;
    private long level;
    private long blocksBroken;
    private long joinDate;
    private long lastLogin;

    public PlayerInfo(TPlayer player) {
        this.player = player;
        this.currencyHolder = new CurrencyHolder(player);
        this.level = 1L;
        this.blocksBroken = ThreadLocalRandom.current().nextInt(1000000000);
        this.joinDate = System.currentTimeMillis();
        this.lastLogin = System.currentTimeMillis();
    }

    public PlayerInfo(TPlayer tPlayer, ResultSet rs) throws SQLException {
        this.player = tPlayer;
        this.currencyHolder = new CurrencyHolder(tPlayer, rs);
        this.level = rs.getLong("level");
        this.blocksBroken = rs.getLong("blocks");
        this.joinDate = rs.getLong("joined");
        this.lastLogin = rs.getLong("last_login");
    }

    public void levelUpMax(Mine mine) {

        NumberFormat nf = NumberFormat.getInstance();
        long totalCost = 0;
        long totalLevels = 0;
        long coins = currencyHolder.getCurrency(CurrencyType.COINS);
        long cost = 0;
        int mineExpansions = 0;

        while (coins >= cost) {
            if (this.level >= MAX_LEVEL) break;

            cost = getLevelUpCost();
            if (coins < cost) break;

            this.level++;
            totalCost += cost;
            totalLevels++;
            coins -= cost;

            if (this.level % 50 == 0) {
                mineExpansions++;
            }

        }

        if (totalLevels <= 0) {
            Players.msg(player.toBukkit(), "&2&lLEVEL &8» &cYou could not level up!");
            return;
        }

        Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You've levelled up to the maximum level you could.");
        Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You've spent &a" + nf.format(totalCost) + " &7to level up &a" + nf.format(totalLevels) + " &7times.");
        this.currencyHolder.removeCurrency(CurrencyType.COINS, totalCost);

        Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You've expanded your mine &a" + mineExpansions + " &7times.");
        mine.getMineRegion().expand(mineExpansions);
        mine.getOuterRegion().expand(mineExpansions);

    }

    public long levelUp(Mine mine) {

        if (this.level == MAX_LEVEL) {
            Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You're at maximum level.");
            return -1;
        }

        long coins = currencyHolder.getCurrency(CurrencyType.COINS);
        long cost = getLevelUpCost();

        if (coins < cost) {
            Players.msg(player.toBukkit(), "&2&lLEVEL &8» &cYou need &4" + NumberFormat.getInstance().format((cost - coins)) + " &cmore coins to level up!");
            return -1;
        }

        this.level++;
        this.currencyHolder.removeCurrency(CurrencyType.COINS, cost);
        Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You've leveled up successfully.");

        if (this.level % 50 == 0) {
            Players.msg(player.toBukkit(), "&2&lLEVEL &8» &7You've reached 50 levels! Your mine size has increased.");
            mine.getMineRegion().expand(1);
            mine.getOuterRegion().expand(1);
        }

        return cost;
    }

    public long getLevelUpCost() {
        return (long) (Math.pow(level, 2) * 100);
    }


}
