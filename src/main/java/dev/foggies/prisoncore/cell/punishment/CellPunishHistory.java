package dev.foggies.prisoncore.cell.punishment;

import dev.foggies.prisoncore.cell.data.Cell;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CellPunishHistory {

    private final Cell cell;
    private final Map<UUID, PunishedCellPlayer> punishedPlayers = new ConcurrentHashMap<>();

    public CellPunishHistory(Cell cell) {
        this.cell = cell;
    }

    public CellPunishHistory(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(";");
        for (String punishedPlayerData : data) {
            PunishedCellPlayer punishedCellPlayer = new PunishedCellPlayer(cell, punishedPlayerData);
            this.punishedPlayers.put(punishedCellPlayer.getUuid(), punishedCellPlayer);
        }
    }

    public boolean isBanned(UUID uuid) {
        return this.punishedPlayers.values()
                .stream()
                .anyMatch(punishedCellPlayer -> punishedCellPlayer.getUuid().equals(uuid) && punishedCellPlayer.getPunishType() == PunishType.BANNED);
    }

    public List<PunishedCellPlayer> getPunishedPlayersByPunishment(PunishType punishType) {
        return this.punishedPlayers.values()
                .stream()
                .filter(punishedCellPlayer -> punishedCellPlayer.getPunishType() == punishType)
                .collect(Collectors.toList());
    }

    public String serialise(){
        return this.punishedPlayers.values()
                .stream()
                .map(PunishedCellPlayer::serialise)
                .collect(Collectors.joining(";"));
    }


}
