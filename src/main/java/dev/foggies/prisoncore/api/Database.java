package dev.foggies.prisoncore.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public abstract class Database<T> {

    private HikariDataSource dataSource;

    public Database() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/prison");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(hikariConfig);
    }

    // Create a method to add two generic numbers together


    public Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void createTable();
    public abstract Optional<T> get(UUID uuid);
    public abstract void insert(T object);
    public abstract void update(T object);
    public abstract void delete(UUID uuid);

    public void executeQuery(String query) {
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
