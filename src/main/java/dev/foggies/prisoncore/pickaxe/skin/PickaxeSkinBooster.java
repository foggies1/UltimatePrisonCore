package dev.foggies.prisoncore.pickaxe.skin;

import lombok.Getter;

import java.util.List;

@Getter
public class PickaxeSkinBooster {

    private final PickaxeSkin pickaxeSkin;
    private final PickaxeSkinBoosterType type;
    private final float percentage;

    public PickaxeSkinBooster(PickaxeSkin pickaxeSkin, PickaxeSkinBoosterType type, float percentage) {
        this.pickaxeSkin = pickaxeSkin;
        this.type = type;
        this.percentage = percentage;
    }

    public PickaxeSkinBooster(PickaxeSkin pickaxeSkin, String serialisedData) {
        this.pickaxeSkin = pickaxeSkin;
        String[] data = serialisedData.split(";");
        this.type = PickaxeSkinBoosterType.valueOf(data[0]);
        this.percentage = Float.parseFloat(data[1]);
    }

    public List<String> toLore() {
        return List.of(
                "§aBooster Type: §f" + this.type.getDisplayName(),
                "§aMultiplier: §f" + this.percentage + "%"
        );
    }

    public String serialise() {
        return String.format("%s;%s", type.name(), percentage);
    }

}
