package dev.foggies.prisoncore.player.manager;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.database.PlayerDatabase;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private final PrisonCore plugin;
    private final PlayerDatabase playerDatabase;
    private final Map<UUID, TPlayer> players = new ConcurrentHashMap<>();

    public PlayerManager(PrisonCore plugin) {
        this.plugin = plugin;
        this.playerDatabase = new PlayerDatabase();

        loadAllOnline();
        beginSavePlayerTask();
    }

    public void loadAllOnline() {
        Players.forEach(player -> {
            loadPlayer(player.getUniqueId());
        });
    }

    public void unloadAll() {
        this.players.keySet().forEach(this::unloadPlayer);
    }

    public void loadPlayer(UUID uuid) {
        Optional<TPlayer> optional = this.playerDatabase.get(uuid);
        if (optional.isPresent()) {
            TPlayer player = optional.get();
            add(player);
        } else {
            TPlayer player = createPlayer(uuid);
            add(player);
            this.playerDatabase.insert(player);
        }
    }

    public void unloadPlayer(UUID uuid) {
        savePlayer(uuid);
        remove(uuid);
    }

    public void savePlayer(UUID uuid) {
        TPlayer player = getPlayer(uuid);
        this.playerDatabase.update(player);
    }

    public TPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    public TPlayer createPlayer(UUID uuid) {
        return new TPlayer(uuid);
    }

    public void add(TPlayer player) {
        if (contains(player.getUuid())) return;
        this.players.put(player.getUuid(), player);
    }

    public void remove(UUID uuid) {
        if (!contains(uuid)) return;
        this.players.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return this.players.containsKey(uuid);
    }

    public void beginSavePlayerTask() {
        Schedulers.builder()
                .async()
                .every(5, TimeUnit.MINUTES)
                .run(() -> {
                    this.players.keySet().forEach(this::savePlayer);
                });
    }

}
