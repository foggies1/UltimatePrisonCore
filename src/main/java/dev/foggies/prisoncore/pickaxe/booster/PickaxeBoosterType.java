package dev.foggies.prisoncore.pickaxe.booster;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PickaxeBoosterType {

    ORBS("Orbs"),
    COINS("Coins"),
    SHARDS("Shards"),
    XP("Experience"),
    BLOCKS("Blocks"),
    STAGE("Stage"),
    ALL("All");

    private final String displayName;

}
