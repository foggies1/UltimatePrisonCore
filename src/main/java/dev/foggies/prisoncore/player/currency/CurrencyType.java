package dev.foggies.prisoncore.player.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrencyType {

    ORBS("Orbs"),
    COINS("Coins"),
    SHARDS("Shards");

    private final String displayName;

}
