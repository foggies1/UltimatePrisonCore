package dev.foggies.prisoncore.mine.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MineInfo {

    private final Mine mine;
    private long blocksBroken;

    public MineInfo(Mine mine) {
        this.mine = mine;
        this.blocksBroken = 0L;
    }

    public MineInfo(Mine mine, String serialisedData){
        this.mine = mine;
        this.blocksBroken = Long.parseLong(serialisedData);
    }


    public String serialise() {
        return String.valueOf(this.blocksBroken);
    }

}
