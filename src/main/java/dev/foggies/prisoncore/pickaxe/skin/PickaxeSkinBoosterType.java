package dev.foggies.prisoncore.pickaxe.skin;

import lombok.Getter;

@Getter
public enum PickaxeSkinBoosterType {

    ORBS("Orbs"),
    COINS("Coins"),
    SHARDS("Shards"),
    XP("Experience"),
    BLOCKS("Blocks"),
    STAGE("Stage"),
    ALL("All");

    private final String displayName;

    PickaxeSkinBoosterType(String displayName) {
        this.displayName = displayName;
    }

}
