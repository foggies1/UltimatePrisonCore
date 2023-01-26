package dev.foggies.prisoncore.player.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import net.ultragrav.command.platform.SpigotCommand;

public class BlockTopCommand extends SpigotCommand {

    private final PlayerManager playerManager;

    public BlockTopCommand(PrisonCore plugin) {
        this.playerManager = plugin.getPlayerManager();

        addAlias("blocktop");
        addAlias("bt");
        addAlias("btop");
        addAlias("topblocks");

    }

    @Override
    protected void perform() {
        playerManager.printTopBlocks(getSpigotPlayer());
    }


}
