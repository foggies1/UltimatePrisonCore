package dev.foggies.prisoncore;

import dev.foggies.prisoncore.cell.CellManager;
import dev.foggies.prisoncore.cell.commands.DefenceBlockCommand;
import dev.foggies.prisoncore.cell.commands.DefenceBombCommand;
import dev.foggies.prisoncore.events.*;
import dev.foggies.prisoncore.item.ClickableItemStorage;
import dev.foggies.prisoncore.item.PlaceableItemStorage;
import dev.foggies.prisoncore.mine.manager.MineManager;
import dev.foggies.prisoncore.pickaxe.commands.PickaxeSkinCommand;
import dev.foggies.prisoncore.pickaxe.manager.PickaxeManager;
import dev.foggies.prisoncore.player.commands.*;
import dev.foggies.prisoncore.player.manager.PlayerManager;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;

@Getter
public final class PrisonCore extends ExtendedJavaPlugin {

    private PlaceableItemStorage placeableItemStorage;
    private ClickableItemStorage clickableItemStorage;
    private MineManager mineManager;
    private PlayerManager playerManager;
    private PickaxeManager pickaxeManager;
    private CellManager cellManager;

    @Override
    public void enable() {
        this.clickableItemStorage = new ClickableItemStorage(this);
        this.placeableItemStorage = new PlaceableItemStorage(this);
        this.mineManager = new MineManager(this);
        this.playerManager = new PlayerManager(this);
        this.pickaxeManager = new PickaxeManager(this);
        this.cellManager = new CellManager(this);

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
        new PickaxeListener(this, pickaxeManager, playerManager, mineManager);
        new ClickableItemListener(this);
        new PlaceableItemListener(this);
    }

    public void registerCommands() {
        new LevelUpCommand(this).register();
        new CurrencyCommand(playerManager).register();
        new BalanceCommand(playerManager).register();
        new PickaxeSkinCommand(this).register();
        new BombCommand(this).register();
        new BlockTopCommand(this).register();
        new DefenceBlockCommand(this).register();
        new DefenceBombCommand(this).register();
    }

}
