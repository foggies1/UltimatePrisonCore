package dev.foggies.prisoncore.pickaxe.manager;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.database.PickaxeDatabase;
import dev.foggies.prisoncore.pickaxe.enchants.storage.EnchantStorage;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PickaxeManager {

    private final PrisonCore plugin;
    private final EnchantStorage enchantStorage;
    private final PickaxeDatabase pickaxeDatabase;
    private final Map<UUID, Pickaxe> pickaxes = new ConcurrentHashMap<>();

    public PickaxeManager(PrisonCore plugin) {
        this.plugin = plugin;
        this.enchantStorage = new EnchantStorage();
        this.pickaxeDatabase = new PickaxeDatabase(this.enchantStorage);

        loadAllOnline();
        beginSavePickaxeTask();
        beginUpdateTask();
    }

    public void loadAllOnline() {
        Players.forEach(player -> {
            loadPickaxe(player.getUniqueId());
        });
    }

    public void unloadAll() {
        this.pickaxes.keySet().forEach(this::unloadPickaxe);
    }

    public void update(Player player) {
        Pickaxe pickaxe = getPickaxe(player.getUniqueId());
        player.getInventory().setItem(0, pickaxe.toItem());
    }

    public void loadPickaxe(UUID uuid) {
        Optional<Pickaxe> optional = this.pickaxeDatabase.get(uuid);
        if (optional.isPresent()) {
            Pickaxe Pickaxe = optional.get();
            add(Pickaxe);
        } else {
            Pickaxe Pickaxe = createPickaxe(uuid);
            add(Pickaxe);
            this.pickaxeDatabase.insert(Pickaxe);
        }
    }

    public void unloadPickaxe(UUID uuid) {
        savePickaxe(uuid);
        remove(uuid);
    }

    public void savePickaxe(UUID uuid) {
        Pickaxe Pickaxe = getPickaxe(uuid);
        this.pickaxeDatabase.update(Pickaxe);
    }

    public Pickaxe getPickaxe(UUID uuid) {
        return this.pickaxes.get(uuid);
    }

    public Pickaxe createPickaxe(UUID uuid) {
        return new Pickaxe(uuid, enchantStorage);
    }

    public void add(Pickaxe pickaxe) {
        if (contains(pickaxe.getUuid())) return;
        this.pickaxes.put(pickaxe.getUuid(), pickaxe);
    }

    public void remove(UUID uuid) {
        if (!contains(uuid)) return;
        this.pickaxes.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return this.pickaxes.containsKey(uuid);
    }

    public void beginSavePickaxeTask() {
        Schedulers.builder()
                .async()
                .every(5, TimeUnit.MINUTES)
                .run(() -> {
                    this.pickaxes.keySet().forEach(this::savePickaxe);
                });
    }

    public void beginUpdateTask() {
        Schedulers.builder()
                .async()
                .every(1, TimeUnit.SECONDS)
                .run(() -> {
                    Players.forEach(this::update);
                });
    }

}
