package dev.foggies.prisoncore.pickaxe.booster;

import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.time.DurationFormatter;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class PickaxeBooster {

    private final Pickaxe pickaxe;
    private final PickaxeBoosterType type;
    private final PickaxeBoosterDurationType durationType;
    private long duration;
    private final float percentage;
    private Task task;

    public PickaxeBooster(Pickaxe pickaxe, PickaxeBoosterType type, long duration, float percentage) {
        this.pickaxe = pickaxe;
        this.type = type;
        this.duration = duration;
        this.percentage = percentage;
        this.durationType = duration == -1 ? PickaxeBoosterDurationType.PERMANENT : PickaxeBoosterDurationType.TEMPORARY;
        if (this.durationType == PickaxeBoosterDurationType.PERMANENT) return;
        this.task = start();
    }

    public PickaxeBooster(Pickaxe pickaxe, String serialisedData) {
        this.pickaxe = pickaxe;
        String[] data = serialisedData.split(":");
        this.type = PickaxeBoosterType.valueOf(data[0]);
        this.duration = Long.parseLong(data[1]);
        this.percentage = Float.parseFloat(data[2]);
        this.durationType = duration == -1 ? PickaxeBoosterDurationType.PERMANENT : PickaxeBoosterDurationType.TEMPORARY;
        if (this.durationType == PickaxeBoosterDurationType.PERMANENT) return;
        this.task = start();
    }

    public List<String> toLore() {
        return List.of(
                "§7Type: §f" + this.type.getDisplayName(),
                "§7Duration: §f" + DurationFormatter.format(Duration.ofSeconds(this.duration), true),
                "§7Percentage: §f" + this.percentage
        );
    }

    public String serialise() {
        return String.format("%s:%s:%s", type.name(), duration, percentage);
    }

    private Task start() {
        return Schedulers.builder()
                .async()
                .every(1, TimeUnit.SECONDS)
                .run(() -> {
                    if (duration <= 0) {
                        pickaxe.setPickaxeBooster(null);
                        task.stop();
                    }
                    duration--;
                });
    }

}
