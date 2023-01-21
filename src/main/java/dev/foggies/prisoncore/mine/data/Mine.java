package dev.foggies.prisoncore.mine.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.serialize.Position;
import org.bukkit.World;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Mine {

    private static final int BEGINNING_MINE_SIZE = 50;
    private final UUID mineOwner;
    private MineRegion mineRegion;
    private OuterMineRegion outerRegion;
    private MineBlock mineBlock;
    private MineInfo mineInfo;

    public Mine(UUID mineOwner, World mineWorld, double lastLocation) {
        this.mineOwner = mineOwner;

        this.mineRegion = new MineRegion(
                this,
                Position.of(lastLocation, 50, lastLocation, mineWorld),
                Position.of(lastLocation + BEGINNING_MINE_SIZE, 100, lastLocation + BEGINNING_MINE_SIZE, mineWorld)
        );

        this.outerRegion = new OuterMineRegion(
                this,
                this.mineRegion.getPos1().subtract(1, 0, 1),
                this.mineRegion.getPos2().add(1, 0, 1)
        );

        this.mineBlock = MineBlock.STONE;
        this.mineInfo = new MineInfo(this);
    }

    public Mine(UUID mineOwner, String serialisedData) {
        this.mineOwner = mineOwner;
        String[] data = serialisedData.split(",");
        this.mineRegion = new MineRegion(this, data[0]);
        this.outerRegion = new OuterMineRegion(this, data[1]);
        this.mineBlock = MineBlock.valueOf(data[2]);
        this.mineInfo = new MineInfo(this, data[3]);
    }


    public String serialise(){
        return String.format("%s,%s,%s,%s", mineRegion.serialise(), outerRegion.serialise(), mineBlock.name(), mineInfo.serialise());
    }


}
