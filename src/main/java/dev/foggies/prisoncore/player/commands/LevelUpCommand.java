package dev.foggies.prisoncore.player.commands;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import dev.foggies.prisoncore.player.data.TPlayer;
import net.ultragrav.command.platform.SpigotCommand;

public class LevelUpCommand  extends SpigotCommand {

    private final PlayerManager playerManager;
    private final MineManager mineManager;

    public LevelUpCommand(PrisonCore plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.mineManager = plugin.getMineManager();

        addChildren(new LevelUpMaxCommand(playerManager, mineManager));
        addAlias("levelup");
        addAlias("rankup");
        addAlias("ru");
        addAlias("lu");
    }

    @Override
    public void perform() {
        TPlayer tPlayer = this.playerManager.getPlayer(getPlayer().getUniqueId());
        Mine mine = this.mineManager.getMine(getPlayer().getUniqueId());
        tPlayer.getPlayerInfo().levelUp(mine);
    }

    private static class LevelUpMaxCommand extends SpigotCommand {

        private PlayerManager playerManager;
        private MineManager mineManager;

        public LevelUpMaxCommand(PlayerManager playerManager, MineManager mineManager) {
            this.playerManager = playerManager;
            this.mineManager = mineManager;
            addAlias("max");
        }

        @Override
        public void perform() {
            TPlayer tPlayer = playerManager.getPlayer(getPlayer().getUniqueId());
            Mine mine = mineManager.getMine(getPlayer().getUniqueId());
            tPlayer.getPlayerInfo().levelUpMax(mine);
        }
    }


}
