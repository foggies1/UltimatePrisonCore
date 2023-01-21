package dev.foggies.prisoncore.mine.database;

import dev.foggies.prisoncore.api.Database;
import dev.foggies.prisoncore.mine.data.Mine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class MineDatabase extends Database<Mine> {

    public MineDatabase() {
        this.createTable();
    }

    @Override
    public void createTable() {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS mines(" +
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

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS lastlocation(" +
                                "location INT" +
                                ")"
                )
        ) {

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getLastLocation() {

        if (!containsLastLocation()) {
            insertLastLocation(0);
            return 0;
        }

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM lastlocation"
                )
        ) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("location");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertLastLocation(int x) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO lastlocation (location) VALUES (?)"
                )
        ) {
            ps.setInt(1, x);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLastLocation(int x) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE lastlocation SET location = ?"
                )
        ) {
            ps.setInt(1, x);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean containsLastLocation() {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM lastlocation"
                )
        ) {
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Mine> get(UUID uuid) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM mines WHERE uuid = ?")
        ) {
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Mine(uuid, resultSet.getString("data")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void insert(Mine mine) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO mines (uuid, data) VALUES (?, ?)")
        ) {
            ps.setString(1, mine.getMineOwner().toString());
            ps.setString(2, mine.serialise());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Mine object) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement("UPDATE mines SET data = ? WHERE uuid = ?")
        ) {
            ps.setString(1, object.serialise());
            ps.setString(2, object.getMineOwner().toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM mines WHERE uuid = ?")
        ) {
            ps.setString(1, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
