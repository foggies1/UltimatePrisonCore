package dev.foggies.prisoncore.pickaxe.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PickaxeInfo {

    private final Pickaxe pickaxe;

    private long stage;
    private long blocksBroken;
    private long orbsEarned;
    private long coinsEarned;
    private long shardsEarned;

    public PickaxeInfo(Pickaxe pickaxe) {
        this.pickaxe = pickaxe;
        this.stage = 1L;
        this.blocksBroken = 0;
        this.orbsEarned = 0;
        this.coinsEarned = 0;
        this.shardsEarned = 0;
    }

    public PickaxeInfo(Pickaxe pickaxe, String serialisedData){
        this.pickaxe = pickaxe;
        String[] data = serialisedData.split(":");
        this.stage = Long.parseLong(data[0]);
        this.blocksBroken = Long.parseLong(data[1]);
        this.orbsEarned = Long.parseLong(data[2]);
        this.coinsEarned = Long.parseLong(data[3]);
        this.shardsEarned = Long.parseLong(data[4]);
    }

    public List<String> toLore(){
        return List.of(
                "§7Stage: §f" + this.stage,
                "§7Blocks Broken: §f" + this.blocksBroken,
                "§7Orbs Earned: §f" + this.orbsEarned,
                "§7Coins Earned: §f" + this.coinsEarned,
                "§7Shards Earned: §f" + this.shardsEarned
        );
    }

    public String serialise(){
        return String.format("%s:%s:%s:%s:%s", this.stage, this.blocksBroken, this.orbsEarned, this.coinsEarned, this.shardsEarned);
    }


}
