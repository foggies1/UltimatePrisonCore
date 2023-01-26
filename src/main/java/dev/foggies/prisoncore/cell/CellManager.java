package dev.foggies.prisoncore.cell;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.utils.EmptyWorldCreator;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class CellManager {

    private final PrisonCore plugin;
    private final CellDatabase cellDatabase;
    private final Map<UUID, Cell> cells = new ConcurrentHashMap<>();
    private World cellWorld;
    private int lastCellLocation;

    public CellManager(PrisonCore plugin) {
        this.plugin = plugin;
        this.cellDatabase = new CellDatabase();

        if (Bukkit.getWorld("cell_world") == null) {
            this.cellWorld = new EmptyWorldCreator().createEmptyWorld("cell_world");
        } else {
            this.cellWorld = Bukkit.getWorld("cell_world");
        }

        this.lastCellLocation = this.cellDatabase.getLastLocation();
        loadAllCells();
    }

    public void loadAllCells() {
        long ms = System.currentTimeMillis();
        Map<UUID, Cell> loadedCells = this.cellDatabase.loadAllCells();
        this.cells.putAll(loadedCells);
        this.plugin.getLogger().info("Loaded " + loadedCells.size() + " cells in " + (System.currentTimeMillis() - ms) + "ms");
    }

    public void createCell(Player player, String name) {
        final UUID uuid = player.getUniqueId();

        if (hasCell(uuid)) {
            Players.msg(player, "&c" + SmallCaps.convert("Warning ⇄ You already have a cell."));
            return;
        }

        if (doesCellExist(name)) {
            Players.msg(player, "&c" + SmallCaps.convert("Warning ⇄ A cell with that name already exists."));
            return;
        }

        if (!isAlphaName(name)) {
            Players.msg(player, "&c" + SmallCaps.convert("Warning ⇄ Cell names can only contain letters."));
            return;
        }

        if (!isAppropriateLength(name)) {
            Players.msg(player, "&c" + SmallCaps.convert("Warning ⇄ Cell names can only be 12 characters long."));
            return;
        }

        this.lastCellLocation+=500;
        this.cellDatabase.updateLastLocation(this.lastCellLocation);

        Cell cell = new Cell(uuid, name, this.cellWorld, this.lastCellLocation);
        this.cells.put(uuid, cell);
        this.cellDatabase.insert(cell);
        cell.teleport(player);
        Players.msg(player, "&a" + SmallCaps.convert("Success ⇄ You have created a cell called " + name + "."));

    }

    public Optional<Cell> getCell(UUID uuid) {
        return this.cells.values().stream().filter(cell -> cell.getCellMembers().isCellMember(uuid)).findFirst();
    }

    public boolean isAppropriateLength(String name) {
        return name.length() <= 12;
    }

    public boolean isAlphaName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean hasCell(UUID uuid) {
        return this.cells.values()
                .stream()
                .anyMatch(cell -> cell.getCellMembers().isCellMember(uuid));
    }

    public boolean doesCellExist(String name) {
        return this.cells.values().stream().anyMatch(cell -> cell.getCellName().equalsIgnoreCase(name));
    }

}
