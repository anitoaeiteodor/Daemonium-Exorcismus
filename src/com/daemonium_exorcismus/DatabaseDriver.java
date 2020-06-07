package com.daemonium_exorcismus;

import com.daemonium_exorcismus.ecs.components.shooting.ShooterType;
import com.daemonium_exorcismus.engine.utils.Vec2D;
import com.daemonium_exorcismus.spawn.LevelInfo;

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
            System.out.println("Fetching data from Constants table...");
            Statement stmt = c.createStatement();

            String[] cts = {"TextureSize", "WindowWidth", "WindowHeight", "EnemyProjSpeed", "PlayerProjSpeed",
                            "SpawnA", "SpawnB", "SpawnC", "SpawnD", "PlayerProjDamage", "EnemyProjDamage"};

            for(String entry : cts) {
                ResultSet rs = stmt.executeQuery(
                        String.format("SELECT * FROM Constants WHERE Name='%s'", entry));
                if (rs.isClosed()) {
                    continue;
                }
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
                    case "PlayerProjSpeed":
                        Constants.PLAYER_PROJ_SPEED = Integer.parseInt(rs.getString("Value"));
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
                    case "PlayerProjDamage":
                        Constants.PLAYER_PROJ_DAMAGE = Integer.parseInt(rs.getString("Value"));
                        break;
                    case "EnemyProjDamage":
                        Constants.ENEMY_PROJ_DAMAGE = Integer.parseInt(rs.getString("Value"));
                        break;
                }
            }

            System.out.println("Done fetching data from Constants table.");
            System.out.println("Fetching data from Constants table...");

            String[] entities = {"Regular", "Medium", "Heavy", "Player", "Crate", "Column", "Projectile", "Skull"};

            for (String entry : entities) {
                ResultSet rs = stmt.executeQuery(
                        String.format("SELECT * FROM Entities WHERE Type='%s'", entry));

                String[] values;
                Vec2D colliderOffsetFirst = null;
                Vec2D colliderOffsetSecond = null;
                Vec2D size = null;

                if (rs.getString("ColliderOffsetFirst") != null) {
                    values = rs.getString("ColliderOffsetFirst").split(" ");
                    colliderOffsetFirst = new Vec2D(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
                }

                if (rs.getString("ColliderOffsetSecond") != null) {
                    values = rs.getString("ColliderOffsetSecond").split(" ");
                    colliderOffsetSecond = new Vec2D(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
                }

                if (rs.getString("Size") != null) {
                    values = rs.getString("Size").split(" ");
                    size = new Vec2D(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                }

                int health = rs.getInt("Health");
                int speed = rs.getInt("Speed");
                int reloadTime = rs.getInt("ReloadTime");

                String shootingType = rs.getString("ShootingType");
                ShooterType shooterType = null;

                if (shootingType != null) {
                    switch (shootingType) {
                        case "Basic":
                            shooterType = ShooterType.BASIC;
                            break;
                        case "Cone":
                            shooterType = ShooterType.CONE;
                            break;
                        case "Radial":
                            shooterType = ShooterType.RADIAL;
                            break;
                    }
                }

                switch (entry) {
                    case "Regular":
                        EntityProperties.RegularEnemy.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.RegularEnemy.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.RegularEnemy.SIZE = size;
                        EntityProperties.RegularEnemy.HEALTH = health;
                        EntityProperties.RegularEnemy.SHOOTER_TYPE = shooterType;
                        EntityProperties.RegularEnemy.SPEED = speed;
                        EntityProperties.RegularEnemy.RELOAD_TIME = reloadTime;
                        break;
                    case "Medium":
                        EntityProperties.MediumEnemy.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.MediumEnemy.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.MediumEnemy.SIZE = size;
                        EntityProperties.MediumEnemy.HEALTH = health;
                        EntityProperties.MediumEnemy.SHOOTER_TYPE = shooterType;
                        EntityProperties.MediumEnemy.SPEED = speed;
                        EntityProperties.MediumEnemy.RELOAD_TIME = reloadTime;
                        break;
                    case "Heavy":
                        EntityProperties.HeavyEnemy.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.HeavyEnemy.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.HeavyEnemy.SIZE = size;
                        EntityProperties.HeavyEnemy.HEALTH = health;
                        EntityProperties.HeavyEnemy.SHOOTER_TYPE = shooterType;
                        EntityProperties.HeavyEnemy.SPEED = speed;
                        EntityProperties.HeavyEnemy.RELOAD_TIME = reloadTime;
                        break;
                    case "Player":
                        EntityProperties.Player.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.Player.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.Player.SIZE = size;
                        EntityProperties.Player.HEALTH = health;
                        EntityProperties.Player.SPEED = speed;
                        EntityProperties.Player.RELOAD_TIME = reloadTime;
                        break;
                    case "Crate":
                        EntityProperties.Crate.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.Crate.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.Crate.SIZE = size;
                        break;
                    case "Column":
                        EntityProperties.Column.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.Column.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.Column.SIZE = size;
                        break;
                    case "Projectile":
                        EntityProperties.Projectile.COLLIDER_OFFSET_FIRST = colliderOffsetFirst;
                        EntityProperties.Projectile.COLLIDER_OFFSET_SECOND = colliderOffsetSecond;
                        EntityProperties.Projectile.SIZE = size;
                        break;
                    case "Skull":
                        EntityProperties.Skull.SIZE = size;
                        break;
                }
            }

            System.out.println("Done fetching data from Entities table.");
            System.out.println("Fetching level data...");

            ResultSet rs = stmt.executeQuery("SELECT * FROM Levels ORDER BY Level ASC");
            StringBuilder levelInfo = new StringBuilder();

            while (rs.next()) {
                levelInfo.append(rs.getInt("Level")).append(" ");
                levelInfo.append(String.format("%s %d %d ", rs.getString("SpawnAEn"), rs.getInt("SpawnADel"), rs.getInt("SpawnAOffset")));
                levelInfo.append(String.format("%s %d %d ", rs.getString("SpawnBEn"), rs.getInt("SpawnBDel"), rs.getInt("SpawnBOffset")));
                levelInfo.append(String.format("%s %d %d ", rs.getString("SpawnCEn"), rs.getInt("SpawnCDel"), rs.getInt("SpawnCOffset")));
                levelInfo.append(String.format("%s %d %d ", rs.getString("SpawnDEn"), rs.getInt("SpawnDDel"), rs.getInt("SpawnDOffset")));
                levelInfo.append("\n");
            }

            LevelInfo.levelInfo = levelInfo.toString();

            System.out.println("Done fetching level data.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
