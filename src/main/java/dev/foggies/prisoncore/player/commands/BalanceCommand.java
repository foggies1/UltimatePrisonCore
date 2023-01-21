package dev.foggies.prisoncore.player.commands;

import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import dev.foggies.prisoncore.player.provider.CurrencyProvider;
import me.lucko.helper.utils.Players;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;

public class BalanceCommand extends SpigotCommand {

    private final PlayerManager playerManager;

    public BalanceCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
        addAlias("balance");
        addAlias("bal");

        addParameter(Parameter.builder(CurrencyProvider.getInstance()).name("currency").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    public void perform() {
        CurrencyType type = getArgument(0);
        Player player = getArgument(1);

        if (player == null) {
            tell("&4&lERROR &8» &cPlayer not found.");
            return;
        }

        TPlayer tPlayer = this.playerManager.getPlayer(player.getUniqueId());
        CurrencyHolder currencyHolder = tPlayer.getPlayerInfo().getCurrencyHolder();
        long balance = currencyHolder.getCurrency(type);

        Players.msg(player, "&2&lECONOMY &8» " + player.getName() + "'s " + type.getDisplayName() + " balance is &a" + balance + "&7.");
    }


}
