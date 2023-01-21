package dev.foggies.prisoncore.mine.commands;

import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.mine.ui.MineUI;
import net.ultragrav.command.platform.SpigotCommand;

public class MineCommand extends SpigotCommand {

    private final MineManager mineManager;

    public MineCommand(MineManager mineManager) {
        addAlias("mine");
        this.mineManager = mineManager;
    }

    @Override
    protected void perform() {
        Mine mine = this.mineManager.getMine(getSpigotPlayer().getUniqueId());

        if(mine == null){
            tell("&cMine was null, please report this to management.");
            return;
        }

        new MineUI(mineManager.getPlugin(), mine, getSpigotPlayer()).open();


    }


}
