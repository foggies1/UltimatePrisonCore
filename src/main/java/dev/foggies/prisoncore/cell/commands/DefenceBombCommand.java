package dev.foggies.prisoncore.cell.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.defense.DefenseBomb;
import dev.foggies.prisoncore.utils.SmallCaps;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.IntegerProvider;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefenceBombCommand extends SpigotCommand {

    private final PrisonCore plugin;

    public DefenceBombCommand(PrisonCore plugin) {
        this.plugin = plugin;
        addAlias("defencebomb");
        addAlias("dbomb");
        addParameter(Parameter.builder(IntegerProvider.getInstance()).name("amount").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    protected void perform() {
        DefenseBomb defenseBomb = new DefenseBomb();
        int amount = getArgument(0);
        Player target = getArgument(1);
        ItemStack itemStack = defenseBomb.toItem(plugin);
        itemStack.setAmount(amount);
        target.getInventory().addItem(itemStack);
        tell("&a" + SmallCaps.convert("You've given " + target.getName() + " " + amount + " Defense Bombs!"));
    }


}
