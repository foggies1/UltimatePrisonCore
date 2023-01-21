package dev.foggies.prisoncore.api;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import dev.foggies.prisoncore.mine.data.Mine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.serialize.Position;

@AllArgsConstructor
@Getter
@Setter
public abstract class PrisonRegion {

    private final Mine mine;
    private Position pos1;
    private Position pos2;

    public PrisonRegion(Mine mine, String data) {
        this.mine = mine;
        String[] split = data.split(":");
        this.pos1 = Position.of(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), split[0]);
        this.pos2 = Position.of(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]), split[0]);
    }

    public abstract void reset();

    public void expand(int amount) {
        this.pos1 = this.pos1.subtract(amount, 0, amount);
        this.pos2 = this.pos2.add(amount, 0, amount);
        reset();
    }

    public CuboidRegion toCuboidRegion() {
        return new CuboidRegion(
                toFaweWorld(),
                BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ()),
                BlockVector3.at(pos2.getX(), pos2.getY(), pos2.getZ())
        );
    }

    public World toFaweWorld() {
        return FaweAPI.getWorld(pos1.getWorld());
    }


    public String serialise() {
        return pos1.getWorld() + ":" + pos1.getX() + ":" + pos1.getY() + ":" + pos1.getZ() + ":" + pos2.getX() + ":" + pos2.getY() + ":" + pos2.getZ();
    }

}
