package com.game.model.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.game.model.entities.Enemy;
import com.game.model.entities.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Fabrique (Factory) pour créer des entités depuis des fichiers JSON.
 * FUSION de EnemyFactory + JsonLoader
 */
public class EntityFactory {

    private static Map<String, EnemyConfig> enemyConfigs = new HashMap<>();

    /**
     * Classe interne pour stocker la config d'un ennemi
     */
    private static class EnemyConfig {
        String texturePath;
        String aiType;
        float width, height;
        int health, damage;
        float speed;
        int coinValue, expValue;
    }

    /**
     * Charge tous les types d'ennemis depuis les fichiers JSON.
     */
    public static void loadEnemyTypes() {
        // Vide les configs precedentes
        enemyConfigs.clear();

        int loadedFromDir = 0;
        FileHandle enemyDir = Gdx.files.internal("data/enemies");
        if (enemyDir.exists() && enemyDir.isDirectory()) {
            // Charge tous les json du dossier interne
            for (FileHandle file : enemyDir.list()) {
                if (file.extension().equalsIgnoreCase("json")) {
                    loadEnemyType(file);
                    loadedFromDir++;
                }
            }
        }

        if (loadedFromDir == 0) {
            FileHandle localEnemyDir = Gdx.files.local("resources/data/enemies");
            if (localEnemyDir.exists() && localEnemyDir.isDirectory()) {
                // Fallback sur le dossier local
                for (FileHandle file : localEnemyDir.list()) {
                    if (file.extension().equalsIgnoreCase("json")) {
                        loadEnemyType(file);
                        loadedFromDir++;
                    }
                }
            }
        }

        if (loadedFromDir == 0) {
            // Dernier fallback: types par defaut
            loadEnemyType("Normal");
            loadEnemyType("Bat");
        }

        System.out.println("Loaded " + enemyConfigs.size() + " enemy types");
    }

    /**
     * Charge un type d'ennemi depuis JSON.
     */
    private static void loadEnemyType(String type) {
        try {
            // Chargement via un id de type
            String path = "data/enemies/" + type + ".json";
            JsonReader reader = new JsonReader();
            JsonValue json = reader.parse(Gdx.files.internal(path));

            EnemyConfig config = new EnemyConfig();
            config.texturePath = json.getString("idlePath", "");
            config.aiType = json.getString("aiType", "chase");
            config.width = json.getFloat("width", 32);
            config.height = json.getFloat("height", 32);
            config.health = json.getInt("maxHp", 50);
            config.damage = json.getInt("damage", 10);
            config.speed = json.getFloat("speed", 60);

            // Recuperer les valeurs de drop
            JsonValue drops = json.get("drops");
            if (drops != null) {
                config.coinValue = drops.getInt("coin", 5);
                config.expValue = drops.getInt("exp", 10);
            } else {
                config.coinValue = 5;
                config.expValue = 10;
            }

            enemyConfigs.put(type, config);

        } catch (Exception e) {
            System.err.println(" Erreur chargement ennemi " + type);
            e.printStackTrace();
        }
    }

    /**
     * Charge un type d'ennemi depuis un fichier JSON.
     */
    private static void loadEnemyType(FileHandle file) {
        try {
            JsonReader reader = new JsonReader();
            JsonValue json = reader.parse(file);
            String type = json.getString("id", file.nameWithoutExtension());

            EnemyConfig config = new EnemyConfig();
            config.texturePath = json.getString("idlePath", "");
            config.aiType = json.getString("aiType", "chase");
            config.width = json.getFloat("width", 32);
            config.height = json.getFloat("height", 32);
            config.health = json.getInt("maxHp", 50);
            config.damage = json.getInt("damage", 10);
            config.speed = json.getFloat("speed", 60);

            JsonValue drops = json.get("drops");
            if (drops != null) {
                config.coinValue = drops.getInt("coin", 5);
                config.expValue = drops.getInt("exp", 10);
            } else {
                config.coinValue = 5;
                config.expValue = 10;
            }

            enemyConfigs.put(type, config);
        } catch (Exception e) {
            System.err.println("Failed to load enemy: " + file.path());
            e.printStackTrace();
        }
    }

    /**
     * Crée un ennemi d'un type donné.
     */
    public static Enemy createEnemy(String type, float x, float y, Player target) {
        EnemyConfig config = enemyConfigs.get(type);

        // Fallback si le type est inconnu
        if (config == null) {
            System.err.println(" Type d'ennemi inconnu : " + type);
            config = new EnemyConfig();
            config.width = 32;
            config.height = 32;
            config.health = 50;
            config.damage = 10;
            config.speed = 60;
            config.coinValue = 5;
            config.expValue = 10;
            config.texturePath = "";
            config.aiType = "chase";
        }

        // Créer l'ennemi
        // Instancie l'ennemi avec les stats chargees
        Enemy enemy = new Enemy(x, y, config.width, config.height);
        enemy.setMaxHealth(config.health);
        enemy.setDamage(config.damage);
        enemy.setSpeed(config.speed);
        enemy.setCoinValue(config.coinValue);
        enemy.setExpValue(config.expValue);
        enemy.setTarget(target);
        enemy.setBehaviorType(config.aiType);

        // Charger texture
        try {
            if (!config.texturePath.isEmpty()) {
                String texturePath = config.texturePath;
                // Retire le prefixe pour l'acces interne
                if (texturePath.startsWith("assets/")) {
                    texturePath = texturePath.substring("assets/".length());
                }
                Texture texture = new Texture(Gdx.files.internal(texturePath));
                enemy.setTexture(texture);
            } else {
                enemy.setTexture(createColorTexture(255, 0, 0));
            }
        } catch (Exception e) {
            System.err.println(" Texture introuvable pour " + type);
            enemy.setTexture(createColorTexture(255, 0, 0));
        }

        return enemy;
    }

    /**
     * Crée une texture d'une couleur unie (fallback).
     */
    private static Texture createColorTexture(int r, int g, int b) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r / 255f, g / 255f, b / 255f, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Libère toutes les ressources.
     */
    public static void dispose() {
        enemyConfigs.clear();
    }
}
