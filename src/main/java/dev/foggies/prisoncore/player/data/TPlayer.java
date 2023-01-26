package dev.foggies.prisoncore.player.data;

import lombok.Getter;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
public class TPlayer {

    private final UUID uuid;
    private PlayerInfo playerInfo;

    public TPlayer(UUID uuid) {
        this.uuid = uuid;
        this.playerInfo = new PlayerInfo(this);
    }

    public TPlayer(UUID uuid, ResultSet rs) throws SQLException {
        this.uuid = uuid;
        this.playerInfo = new PlayerInfo(this, rs);
    }

    public Player toBukkit(){
        return Players.getNullable(this.uuid);
    }



}
