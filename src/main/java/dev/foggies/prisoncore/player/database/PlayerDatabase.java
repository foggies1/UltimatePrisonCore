package dev.foggies.prisoncore.player.database;

import dev.foggies.prisoncore.api.Database;
import dev.foggies.prisoncore.player.currency.CurrencyHolder;
import dev.foggies.prisoncore.player.currency.CurrencyType;
import dev.foggies.prisoncore.player.data.TPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDatabase extends Database<TPlayer> {

    public PlayerDatabase() {
        this.createTable();
    }

    @Override
    public void createTable() {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS players(" +
                                "uuid VARCHAR(37)," +
                                "level BIGINT," +
                                "blocks BIGINT," +
                                "joined BIGINT," +
                                "last_login BIGINT," +
                                "coins BIGINT," +
                                "orbs BIGINT," +
                                "shards BIGINT," +
                                "PRIMARY KEY (uuid)" +
                                ")"
                )
        ) {

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<TPlayer> get(UUID uuid) {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM players WHERE uuid = ?"
                )
        ) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new TPlayer(uuid, rs)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void insert(TPlayer player) {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO players(uuid, level, blocks, joined, last_login, coins, orbs, shards) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
                )
        ) {

            CurrencyHolder currencyHolder = player.getPlayerInfo().getCurrencyHolder();

            ps.setString(1, player.getUuid().toString());
            ps.setLong(2, player.getPlayerInfo().getLevel());
            ps.setLong(3, player.getPlayerInfo().getBlocksBroken());
            ps.setLong(4, player.getPlayerInfo().getJoinDate());
            ps.setLong(5, player.getPlayerInfo().getLastLogin());
            ps.setLong(6, currencyHolder.getCurrency(CurrencyType.COINS));
            ps.setLong(7, currencyHolder.getCurrency(CurrencyType.ORBS));
            ps.setLong(8, currencyHolder.getCurrency(CurrencyType.SHARDS));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(TPlayer player) {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE players SET level = ?, blocks = ?, joined = ?, last_login = ?, coins = ?, orbs = ?, shards = ? WHERE uuid = ?"
                )
        ) {

            CurrencyHolder currencyHolder = player.getPlayerInfo().getCurrencyHolder();

            ps.setLong(1, player.getPlayerInfo().getLevel());
            ps.setLong(2, player.getPlayerInfo().getBlocksBroken());
            ps.setLong(3, player.getPlayerInfo().getJoinDate());
            ps.setLong(4, player.getPlayerInfo().getLastLogin());
            ps.setLong(5, currencyHolder.getCurrency(CurrencyType.COINS));
            ps.setLong(6, currencyHolder.getCurrency(CurrencyType.ORBS));
            ps.setLong(7, currencyHolder.getCurrency(CurrencyType.SHARDS));
            ps.setString(8, player.getUuid().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(UUID uuid) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM players WHERE uuid = ?"
                )
        ) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Long> getBlockTop(int limit) {

        Map<UUID, Long> top = new ConcurrentHashMap<>();

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT uuid, blocks FROM players ORDER BY blocks DESC LIMIT 10"
                )
        ) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                long blocks = rs.getLong("blocks");
                top.put(uuid, blocks);
            }

            return top;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ConcurrentHashMap<>();
    }


}
