package dev.foggies.prisoncore.cell;

import dev.foggies.prisoncore.api.Database;
import dev.foggies.prisoncore.cell.data.Cell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CellDatabase extends Database<Cell> {

    public CellDatabase() {
        this.createTable();
    }

    @Override
    public void createTable() {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS cells(" +
                                "uuid VARCHAR(37)," +
                                "data TEXT," +
                                "PRIMARY KEY(uuid)" +
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
                        "CREATE TABLE IF NOT EXISTS lastcelllocation(" +
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
        }

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT location FROM lastcelllocation"
                )
        ) {
            return ps.executeQuery().getInt("location");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean containsLastLocation() {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT location FROM lastcelllocation"
                )
        ) {
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertLastLocation(int x) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO lastcelllocation(location) VALUES(?)"
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
                        "UPDATE lastcelllocation SET location = ?"
                )
        ) {
            ps.setInt(1, x);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Cell> loadAllCells() {
        Map<UUID, Cell> cells = new ConcurrentHashMap<>();
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM cells"
                )
        ) {

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                Cell cell = new Cell(uuid, resultSet);
                cells.put(uuid, cell);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cells;
    }

    @Override
    public Optional<Cell> get(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public void insert(Cell cell) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO cells(uuid, data) VALUES(?, ?)"
                )
        ) {

            ps.setString(1, cell.getCellOwner().toString());
            ps.setString(2, cell.serialise());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cell cell) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE cells SET data = ? WHERE uuid = ?"
                )
        ) {

            ps.setString(1, cell.serialise());
            ps.setString(2, cell.getCellOwner().toString());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {

    }
}
