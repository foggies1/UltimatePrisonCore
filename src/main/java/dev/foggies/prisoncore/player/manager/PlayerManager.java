package dev.foggies.prisoncore.player.manager;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.player.data.TPlayer;
import dev.foggies.prisoncore.player.database.PlayerDatabase;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerManager {

    private final PrisonCore plugin;
    private final PlayerDatabase playerDatabase;
    private final Map<UUID, TPlayer> players = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Long> topBlocks = new ConcurrentHashMap<>();

    public PlayerManager(PrisonCore plugin) {
        this.plugin = plugin;
        this.playerDatabase = new PlayerDatabase();

        loadAllOnline();

        for (int i = 0; i < 10; i++) {
            loadPlayer(UUID.randomUUID());
        }

        beginSavePlayerTask();
        refreshTopBlocks();

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

    public void printTopBlocks(Player player) {
        AtomicInteger i = new AtomicInteger(1);

        Players.msg(player, "&7&m                   &r &6&l" + SmallCaps.convert("Blocks top") + " &7&m                   ");

        this.topBlocks.entrySet().stream().sorted(Map.Entry.<UUID, Long>comparingByValue().reversed()).forEach(entry -> {
            Optional<Player> p = Players.get(entry.getKey());

            String name = SmallCaps.convert(p.map(Player::getName).orElse("Test Player #" + i.get()));
            long blocks = entry.getValue();
            String formatted = NumberFormat.getInstance().format(blocks);

            Players.msg(player, "&6" + i.get() + ". &e" + name + " &f- &l" + SmallCaps.convert(formatted));
            i.getAndIncrement();
        });
    }


    public void refreshTopBlocks() {
        Schedulers.builder()
                .async()
                .every(1, TimeUnit.MINUTES)
                .run(() -> {
                    this.topBlocks.clear();
                    Map<UUID, Long> map = this.playerDatabase.getBlockTop(10);
                    System.out.println(map.size());
                    this.topBlocks.putAll(map);
                });
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
