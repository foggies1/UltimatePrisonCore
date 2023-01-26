package dev.foggies.prisoncore.cell.data;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class CellInformation {

    private final Cell cell;
    private long cellValue;
    private long cellCredits;
    private int cellMaxMembers;

    public CellInformation(Cell cell) {
        this.cell = cell;
        this.cellValue = ThreadLocalRandom.current().nextLong(10000000);
        this.cellMaxMembers = 5;
    }

    public CellInformation(Cell cell, String serialisedData){
        this.cell = cell;
        String[] data = serialisedData.split(":");
        this.cellValue = Long.parseLong(data[0]);
        this.cellCredits = Long.parseLong(data[1]);
        this.cellMaxMembers = Integer.parseInt(data[2]);
    }

    public void addCellMaxMembers(int amount) {
        this.cellMaxMembers += amount;
    }

    public void removeCellMaxMembers(int amount) {
        this.cellMaxMembers -= amount;
    }

    public void addCellCredits(long amount) {
        this.cellCredits += amount;
    }

    public void removeCellCredits(long amount) {
        this.cellCredits -= amount;
    }

    public void addCellValue(long amount) {
        this.cellValue += amount;
    }

    public void removeCellValue(long amount) {
        this.cellValue -= amount;
    }

    public String serialise(){
        return String.format("%s:%s:%s", cellValue, cellCredits, cellMaxMembers);
    }



}
