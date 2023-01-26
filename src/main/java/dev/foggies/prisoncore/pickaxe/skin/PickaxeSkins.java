package dev.foggies.prisoncore.pickaxe.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PickaxeSkins {

    STAGE_1(new PickaxeSkin("Ruby", PickaxeSkinBoosterType.COINS, 3445, 0.5f)),
    STAGE_10(new PickaxeSkin("Sapphire", PickaxeSkinBoosterType.COINS, 3445, 5f)),
    STAGE_20(new PickaxeSkin("Amethyst", PickaxeSkinBoosterType.COINS, 3445, 7f)),
    STAGE_30(new PickaxeSkin("Bloody Corpses", PickaxeSkinBoosterType.COINS, 3445, 8f)),
    STAGE_40(new PickaxeSkin("Undeniable", PickaxeSkinBoosterType.COINS, 3445, 12f)),
    STAGE_50(new PickaxeSkin("Godly", PickaxeSkinBoosterType.COINS, 3445, 55f)),
    GODLY(new PickaxeSkin("Almighty", PickaxeSkinBoosterType.ALL, 6367, 14560f));

    private final PickaxeSkin pickaxeSkin;

}
