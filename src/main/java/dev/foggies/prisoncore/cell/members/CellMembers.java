package dev.foggies.prisoncore.cell.members;

import dev.foggies.prisoncore.cell.data.Cell;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CellMembers {

    private final Cell cell;
    private final Map<UUID, CellMember> cellMembers = new ConcurrentHashMap<>();

    public CellMembers(Cell cell) {
        this.cell = cell;
    }

    public CellMembers(Cell cell, String serialisedData) {
        this.cell = cell;
        String[] data = serialisedData.split(";");
        for (String cellMemberData : data) {
            CellMember cellMember = new CellMember(cell, cellMemberData);
            this.cellMembers.put(cellMember.getUuid(), cellMember);
        }
    }

    public boolean isCellMember(UUID uuid) {
        return this.cellMembers.containsKey(uuid) || this.cell.getCellOwner().equals(uuid);
    }

    public String serialise() {
        return this.cellMembers.values()
                .stream()
                .map(CellMember::serialise)
                .collect(Collectors.joining(";"));
    }


}
