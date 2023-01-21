package dev.foggies.prisoncore.player.commands;

import dev.foggies.prisoncore.player.currency.CurrencyEditAction;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import dev.foggies.prisoncore.player.provider.CurrencyEditActionProvider;
import dev.foggies.prisoncore.player.provider.CurrencyProvider;
import me.lucko.helper.utils.Players;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.LongProvider;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class CurrencyCommand extends SpigotCommand {

    private final PlayerManager playerManager;

    public CurrencyCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
        addAlias("eco");
        addAlias("economy");

        addParameter(Parameter.builder(CurrencyProvider.getInstance()).name("currency").build());
        addParameter(Parameter.builder(CurrencyEditActionProvider.getInstance()).name("action").build());
        addParameter(Parameter.builder(LongProvider.getInstance()).name("amount").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    protected void perform() {
        CurrencyType currencyType = getArgument(0);
        CurrencyEditAction action = getArgument(1);
        long amount = getArgument(2);
        Player player = getArgument(3);

        NumberFormat nf = NumberFormat.getInstance();
        TPlayer tPlayer = this.playerManager.getPlayer(player.getUniqueId());
        CurrencyHolder currencyHolder = tPlayer.getPlayerInfo().getCurrencyHolder();
        String amountFormatted = nf.format(amount);
        
        switch (action) {
            case ADD, GIVE -> {
                currencyHolder.addCurrency(currencyType, amount);
                Players.msg(player, "&2&lECONOMY &8» &7You have been given &a" + amountFormatted + " " + currencyType.getDisplayName() + "&7.");
                tell("&2&lECONOMY &8» &7You have given &a" + amountFormatted + " " + currencyType.getDisplayName() + "&7 to &a" + player.getName() + "&7.");
            }
            case REMOVE, TAKE -> {
                currencyHolder.removeCurrency(currencyType, amount);
                Players.msg(player, "&2&lECONOMY &8» &c" + amountFormatted + " " + currencyType.getDisplayName() + "&7 has been taken from you.");
                tell("&2&lECONOMY &8» &7You have taken &c" + amountFormatted + " " + currencyType.getDisplayName() + "&7 from &a" + player.getName() + "&7.");
            }
            case SET -> {
                currencyHolder.setCurrency(currencyType, amount);
                Players.msg(player, "&2&lECONOMY &8» &7Your " + currencyType.getDisplayName() + " balance has been set to &a" + amountFormatted + "&7.");
                tell("&2&lECONOMY &8» &7You've set &a" + player.getName() + "'s &7" + currencyType.getDisplayName() + " balance to &a" + amountFormatted + "&7.");
            }
        }


    }
}
