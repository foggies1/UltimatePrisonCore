package dev.foggies.prisoncore.cell.members;

import dev.foggies.prisoncore.cell.data.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CellMember {

    private final Cell cell;
    private final UUID uuid;
    private CellMemberRank cellMemberRank;

    public CellMember(Cell cell, UUID uuid) {
        this.cell = cell;
        this.uuid = uuid;
        this.cellMemberRank = CellMemberRank.MEMBER;
    }

    public CellMember(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(":");
        this.uuid = UUID.fromString(data[0]);
        this.cellMemberRank = CellMemberRank.valueOf(data[1]);
    }

    public String serialise() {
        return String.format("%s:%s", this.uuid.toString(), this.cellMemberRank.name());
    }


}
