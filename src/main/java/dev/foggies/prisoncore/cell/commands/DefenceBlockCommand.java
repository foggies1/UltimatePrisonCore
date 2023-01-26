package dev.foggies.prisoncore.cell.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.defense.DefenseBlock;
import dev.foggies.prisoncore.cell.defense.DefenseBlockTier;
import dev.foggies.prisoncore.cell.provider.DefenseTierProvider;
import dev.foggies.prisoncore.utils.SmallCaps;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.IntegerProvider;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefenceBlockCommand extends SpigotCommand {

    private final PrisonCore plugin;

    public DefenceBlockCommand(PrisonCore plugin) {
        this.plugin = plugin;

        addAlias("defenceblock");
        addAlias("defence");
        addAlias("db");
        addParameter(Parameter.builder(DefenseTierProvider.getInstance()).name("tier").build());
        addParameter(Parameter.builder(IntegerProvider.getInstance()).name("amount").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    protected void perform() {
        DefenseBlockTier tier = getArgument(0);
        int amount = getArgument(1);
        Player target = getArgument(2);

        DefenseBlock defenseBlock = new DefenseBlock(tier);
        ItemStack itemStack = defenseBlock.toItem(plugin);
        itemStack.setAmount(amount);

        target.getInventory().addItem(itemStack);

        tell("&a" + SmallCaps.convert("You've given " + target.getName() + " " + amount + " " + tier.getDisplayName() + " Defense Blocks!"));

    }
}
