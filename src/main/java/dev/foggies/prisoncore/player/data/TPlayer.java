package dev.foggies.prisoncore.player.data;

import lombok.Getter;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class TPlayer {

    private final UUID uuid;
    private PlayerInfo playerInfo;

    public TPlayer(UUID uuid) {
        this.uuid = uuid;
        this.playerInfo = new PlayerInfo(this);
    }

    public TPlayer(UUID uuid, String serialisedData) {
        this.uuid = uuid;
        this.playerInfo = new PlayerInfo(this, serialisedData);
    }

    public Player toBukkit(){
        return Players.getNullable(this.uuid);
    }

    public String serialise() {
        return String.format("%s", playerInfo.serialise());
    }


}
