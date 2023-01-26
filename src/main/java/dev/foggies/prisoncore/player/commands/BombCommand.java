package dev.foggies.prisoncore.player.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.player.items.Bomb;
import me.lucko.helper.utils.Players;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.LongProvider;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BombCommand extends SpigotCommand {

    private final PrisonCore plugin;

    public BombCommand(PrisonCore plugin) {
        this.plugin = plugin;
        addAlias("bomb");
        addParameter(Parameter.builder(LongProvider.getInstance()).name("size").build());
        addParameter(Parameter.builder(LongProvider.getInstance()).name("amount").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    protected void perform() {
        long size = getArgument(0);
        long amount = getArgument(1);
        Player player = getArgument(2);

        Bomb bomb = new Bomb((int) size);
        ItemStack bombItem = bomb.toItem(plugin);
        bombItem.setAmount((int) amount);

        player.getInventory().addItem(bombItem);
        Players.msg(player, "&2&lPICKAXE &8» &7You've received " + amount + " Mine bombs!");
        tell("&2&lPICKAXE &8» &7You've given " + player.getName() + " " + amount + " Mine bombs!");
    }
}
