package dev.foggies.prisoncore.cell.defense;

import dev.foggies.prisoncore.cell.data.Cell;
import dev.foggies.prisoncore.cell.region.CellRaidRegion;
import lombok.Getter;
import me.lucko.helper.serialize.Position;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class DefenseBlocks {

    private final Cell cell;
    private final CellRaidRegion cellRaidRegion;
    private final Map<Position, DefenseBlock> defenseBlocks = new ConcurrentHashMap<>();

    public DefenseBlocks(Cell cell, CellRaidRegion cellRaidRegion) {
        this.cell = cell;
        this.cellRaidRegion = cellRaidRegion;
    }

    public DefenseBlocks(Cell cell, CellRaidRegion cellRaidRegion, String serialisedData) {
        this.cell = cell;
        this.cellRaidRegion = cellRaidRegion;
        String[] data = serialisedData.split(";");
        for (String defenseBlockData : data) {
            DefenseBlock defenseBlock = new DefenseBlock(cell, defenseBlockData);
            this.defenseBlocks.put(defenseBlock.getPosition(), defenseBlock);
        }
    }

    public void addDefenseBlock(DefenseBlock defenseBlock) {
        this.defenseBlocks.put(defenseBlock.getPosition(), defenseBlock);
    }

    public void removeDefenseBlock(DefenseBlock defenseBlock) {
        this.defenseBlocks.remove(defenseBlock.getPosition());
    }

    public Optional<DefenseBlock> getDefenseBlock(Position position) {
        return Optional.ofNullable(this.defenseBlocks.get(position));
    }

    public boolean isDefenseBlock(Position position) {
        return this.defenseBlocks.containsKey(position);
    }

    public long getTotalDefense() {
        return this.defenseBlocks.values()
                .stream()
                .mapToLong(DefenseBlock::getHealth)
                .sum();
    }

    public String serialise(){
        return this.defenseBlocks.values()
                .stream()
                .map(DefenseBlock::serialise)
                .collect(Collectors.joining(";"));
    }

}
