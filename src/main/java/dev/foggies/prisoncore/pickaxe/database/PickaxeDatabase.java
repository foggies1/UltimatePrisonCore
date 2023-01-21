package dev.foggies.prisoncore.pickaxe.database;

import dev.foggies.prisoncore.api.Database;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.pickaxe.enchants.storage.EnchantStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PickaxeDatabase extends Database<Pickaxe> {

    private final EnchantStorage enchantStorage;

    public PickaxeDatabase(EnchantStorage errorStorage) {
        this.enchantStorage = errorStorage;
        this.createTable();
    }

    @Override
    public void createTable() {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS pickaxes(" +
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
    public Optional<Pickaxe> get(UUID uuid) {

        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM pickaxes WHERE uuid = ?"
                )
        ) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Pickaxe(
                        uuid,
                        enchantStorage,
                        rs.getString("data")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void insert(Pickaxe pickaxe) {

            try (
                    Connection connection = getConnection();
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO pickaxes(uuid, data) VALUES(?, ?)"
                    )
            ) {

                ps.setString(1, pickaxe.getUuid().toString());
                ps.setString(2, pickaxe.serialise());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void update(Pickaxe pickaxe) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE pickaxes SET data = ? WHERE uuid = ?"
                )
        ) {

            ps.setString(1, pickaxe.serialise());
            ps.setString(2, pickaxe.getUuid().toString());
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
                        "DELETE FROM pickaxes WHERE uuid = ?"
                )
        ) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
