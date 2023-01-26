package dev.foggies.prisoncore.pickaxe.stage;

import dev.foggies.prisoncore.pickaxe.skin.PickaxeSkins;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PickaxeStage {

    STAGE_1(PickaxeSkins.STAGE_1),
    STAGE_10(PickaxeSkins.STAGE_10),
    STAGE_20(PickaxeSkins.STAGE_20),
    STAGE_30(PickaxeSkins.STAGE_30),
    STAGE_40(PickaxeSkins.STAGE_40),
    STAGE_50(PickaxeSkins.STAGE_50);

    private final PickaxeSkins pickaxeSkin;

}
