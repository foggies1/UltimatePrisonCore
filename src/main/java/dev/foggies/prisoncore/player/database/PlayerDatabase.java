package dev.foggies.prisoncore.player.database;

import dev.foggies.prisoncore.api.Database;
import dev.foggies.prisoncore.player.data.TPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

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
                                "data TEXT," +
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
                return Optional.of(new TPlayer(uuid, rs.getString("data")));
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
                        "INSERT INTO players(uuid, data) VALUES(?, ?)"
                )
        ) {
            ps.setString(1, player.getUuid().toString());
            ps.setString(2, player.serialise());
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
                        "UPDATE players SET data = ? WHERE uuid = ?"
                )
        ) {
            ps.setString(1, player.serialise());
            ps.setString(2, player.getUuid().toString());
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


}
