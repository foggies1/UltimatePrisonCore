package dev.foggies.prisoncore;

import dev.foggies.prisoncore.events.BlockBreakListener;
import dev.foggies.prisoncore.events.PickaxeListener;
import dev.foggies.prisoncore.events.PlayerJoinQuitListener;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.commands.BalanceCommand;
import dev.foggies.prisoncore.player.commands.CurrencyCommand;
import dev.foggies.prisoncore.player.commands.LevelUpCommand;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;

@Getter
public final class PrisonCore extends ExtendedJavaPlugin {

    private MineManager mineManager;
    private PlayerManager playerManager;
    private PickaxeManager pickaxeManager;

    @Override
    public void enable() {
        this.mineManager = new MineManager(this);
        this.playerManager = new PlayerManager(this);
        this.pickaxeManager = new PickaxeManager(this);

        registerEvents();
        registerCommands();
    }

    @Override
    public void disable() {
        this.mineManager.unloadAll();
        this.playerManager.unloadAll();
        this.pickaxeManager.unloadAll();
    }

    public void registerEvents() {
        new PlayerJoinQuitListener(this);
        new BlockBreakListener(this);
        new PickaxeListener(pickaxeManager, playerManager);
    }

    public void registerCommands() {
        new LevelUpCommand(this).register();
        new CurrencyCommand(playerManager).register();
        new BalanceCommand(playerManager).register();
    }

}
