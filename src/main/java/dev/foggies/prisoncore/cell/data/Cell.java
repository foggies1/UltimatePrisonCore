package dev.foggies.prisoncore.cell.data;

import dev.foggies.prisoncore.cell.members.CellMembers;
import dev.foggies.prisoncore.cell.punishment.CellPunishHistory;
import dev.foggies.prisoncore.cell.region.CellRaidRegion;
import dev.foggies.prisoncore.cell.region.CellRegion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class Cell {

    private UUID cellOwner;
    private String cellName;
    private CellInformation cellInformation;
    private CellMembers cellMembers;
    private CellPunishHistory cellPunishHistory;
    private CellRegion cellRegion; // Region outside the raid-able region.
    private CellRaidRegion cellRaidRegion; // Region that player's can place Defense Blocks in.

    public Cell(UUID cellOwner, String cellName, World cellWorld, int lastCellLocation) {
        this.cellOwner = cellOwner;
        this.cellName = cellName;
        this.cellInformation = new CellInformation(this);
        this.cellMembers = new CellMembers(this);
        this.cellPunishHistory = new CellPunishHistory(this);
        this.cellRegion = new CellRegion(this, cellWorld, lastCellLocation);
        this.cellRaidRegion = new CellRaidRegion(this, this.cellRegion);
    }

    public Cell(UUID cellOwner, ResultSet serialisedData) throws SQLException {
        this.cellOwner = cellOwner;
        String[] data = serialisedData.getString("data").split(":");
        this.cellName = data[1];
        this.cellInformation = new CellInformation(this, data[2]);
        this.cellMembers = new CellMembers(this, data[3]);
        this.cellPunishHistory = new CellPunishHistory(this, data[4]);
        this.cellRegion = new CellRegion(this, data[5]);
        this.cellRaidRegion = new CellRaidRegion(this, data[6]);
    }

    public void teleport(Player player){
        player.teleport(this.cellRegion.getSpawnLocation().toLocation());
    }

    public String serialise(){
        return String.format("%s,%s,%s,%s,%s,%s", this.cellOwner.toString(), this.cellName, this.cellInformation.serialise(), this.cellMembers.serialise(), this.cellPunishHistory.serialise(), this.cellRegion.serialise());
    }

}
