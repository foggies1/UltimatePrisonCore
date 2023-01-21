package dev.foggies.prisoncore.mine.manager;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.mine.commands.MineCommand;
import dev.foggies.prisoncore.mine.database.MineDatabase;
import dev.foggies.prisoncore.utils.EmptyWorldCreator;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Getter
public class MineManager {

    private final PrisonCore plugin;
    private final MineDatabase mineDatabase;
    private final Map<UUID, Mine> mines = new ConcurrentHashMap<>();
    private final World mineWorld;
    private int lastLocation;

    public MineManager(PrisonCore plugin) {
        this.plugin = plugin;
        this.mineDatabase = new MineDatabase();
        this.lastLocation = this.mineDatabase.getLastLocation();

        if (Bukkit.getWorld("mine_world") == null) {
            this.mineWorld = new EmptyWorldCreator().createEmptyWorld("mine_world");
        } else {
            this.mineWorld = Bukkit.getWorld("mine_world");
        }

        loadAllOnline();
        beginSaveMineTask();
        new MineCommand(this).register();
    }

    public void loadAllOnline() {
        Players.forEach(player -> {
            loadMine(player.getUniqueId());
        });
    }

    public void unloadAll() {
        this.mines.keySet().forEach(this::unloadMine);
    }

    public void loadMine(UUID uuid) {
        Optional<Mine> optional = this.mineDatabase.get(uuid);
        if (optional.isPresent()) {
            Mine mine = optional.get();
            this.mines.put(uuid, mine);
        } else {
            Mine mine = createMine(uuid);
            this.mines.put(uuid, mine);
            this.mineDatabase.insert(mine);
        }
    }

    public void unloadMine(UUID uuid) {
        saveMine(uuid);
        remove(uuid);
    }

    public void saveMine(UUID uuid) {
        Mine mine = this.mines.get(uuid);
        this.mineDatabase.update(mine);
    }

    public Mine getMine(UUID uuid) {
        return this.mines.get(uuid);
    }

    public Mine createMine(UUID uuid) {
        this.lastLocation += 500;
        this.mineDatabase.updateLastLocation(this.lastLocation);
        return new Mine(uuid, this.mineWorld, this.lastLocation);
    }

    public void add(Mine mine) {
        if (contains(mine.getMineOwner())) return;
        this.mines.put(mine.getMineOwner(), mine);
    }

    public void remove(UUID uuid) {
        if (!contains(uuid)) return;
        this.mines.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return this.mines.containsKey(uuid);
    }

    public void beginSaveMineTask() {
        Schedulers.builder()
                .async()
                .every(5, TimeUnit.MINUTES)
                .run(() -> {
                    this.mines.keySet().forEach(this::saveMine);
                });
    }


}
