package dev.foggies.prisoncore.pickaxe.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.pickaxe.provider.PickaxeSkinProvider;
import dev.foggies.prisoncore.pickaxe.skin.PickaxeSkins;
import me.lucko.helper.utils.Players;
import net.ultragrav.command.Parameter;
import net.ultragrav.command.platform.SpigotCommand;
import net.ultragrav.command.provider.impl.spigot.PlayerProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PickaxeSkinCommand extends SpigotCommand {

    private final PrisonCore plugin;

    public PickaxeSkinCommand(PrisonCore plugin) {
        this.plugin = plugin;
        addAlias("skin");
        addParameter(Parameter.builder(PickaxeSkinProvider.getInstance()).name("skin").build());
        addParameter(Parameter.builder(PlayerProvider.getInstance()).name("player").build());
    }

    @Override
    protected void perform() {
        PickaxeSkins skin = getArgument(0);
        Player player = getArgument(1);

        ItemStack skinItem = skin.getPickaxeSkin().toItem(plugin);
        player.getInventory().addItem(skinItem);
        Players.msg(player, "&2&lPICKAXE &8» &7You have received a skin for your pickaxe!");
        tell("&2&lPICKAXE &8» &7You have given " + player.getName() + " a skin for their pickaxe!");

    }
}
