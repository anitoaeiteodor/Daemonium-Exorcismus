package com.daemonium_exorcismus;

import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.sql.*;
import java.util.List;

public class DatabaseDriver {

    public static void extractInfo() {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:game-contents.db");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Opened database.");
        try {
            Statement stmt = c.createStatement();

            String[] cts = {"TextureSize", "WindowWidth", "WindowHeight", "EnemyProjSpeed", "PlayerProjSpeed",
                            "PlayerSpeed", "PlayerReloadSpeed", "SpawnA", "SpawnB", "SpawnC", "SpawnD",
                            "SkullSize", "CrateSize", "ColumnSize", "EnemySpeed"};

            for(String entry : cts) {
                ResultSet rs = stmt.executeQuery(
                        String.format("SELECT * FROM Constants WHERE Name='%s'", entry));
                switch (rs.getString("Name")) {
                    case "TextureSize":
                        Constants.TEXTURE_SIZE = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "WindowWidth":
                        Constants.WINDOW_WIDTH = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "WindowHeight":
                        Constants.WINDOW_HEIGHT = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "EnemyProjSpeed":
                        Constants.ENEMY_PROJ_SPEED = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "EnemySpeed":
                        Constants.ENEMY_SPEED = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "PlayerProjSpeed":
                        Constants.PLAYER_PROJ_SPEED = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "PlayerSpeed":
                        Constants.PLAYER_SPEED = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "PlayerReloadSpeed":
                        Constants.PLAYER_RELOAD_SPEED = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "SpawnA":
                        String[] coords = rs.getString("Value").split(" ");
                        Constants.SPAWN_A = new Vec2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        break;
                    case "SpawnB":
                        coords = rs.getString("Value").split(" ");
                        Constants.SPAWN_B = new Vec2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        break;
                    case "SpawnC":
                        coords = rs.getString("Value").split(" ");
                        Constants.SPAWN_C = new Vec2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        break;
                    case "SpawnD":
                        coords = rs.getString("Value").split(" ");
                        Constants.SPAWN_D = new Vec2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        break;
                    case "SkullSize":
                        String[] size = rs.getString("Value").split(" ");
                        Constants.SKULL_SIZE = new Vec2D(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                        break;
                    case "ColumnSize":
                        size = rs.getString("Value").split(" ");
                        Constants.COLUMN_SIZE = new Vec2D(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                        break;
                    case "CrateSize":
                        size = rs.getString("Value").split(" ");
                        Constants.CRATE_SIZE = new Vec2D(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
