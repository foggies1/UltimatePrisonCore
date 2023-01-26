package dev.foggies.prisoncore.cell.punishment;

import dev.foggies.prisoncore.cell.data.Cell;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PunishedCellPlayer {

    private final Cell cell;
    private final UUID uuid;
    private final String reason;
    private final long punishDate;
    private final PunishType punishType;

    public PunishedCellPlayer(Cell cell, UUID uuid, String reason, long punishDate, PunishType punishType) {
        this.cell = cell;
        this.uuid = uuid;
        this.reason = reason;
        this.punishDate = punishDate;
        this.punishType = punishType;
    }

    public PunishedCellPlayer(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(":");
        this.uuid = UUID.fromString(data[0]);
        this.reason = data[1];
        this.punishDate = Long.parseLong(data[2]);
        this.punishType = PunishType.valueOf(data[3]);
    }

    public String serialise() {
        return String.format("%s:%s:%s:%s", this.uuid.toString(), this.reason, this.punishDate, this.punishType.name());
    }

}
