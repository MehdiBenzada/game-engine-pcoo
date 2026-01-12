package com.game.model.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.game.model.entities.Enemy;
import com.game.model.entities.Player;
import com.game.model.factories.EntityFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gestionnaire des vagues d'ennemis.
 *
 *
 * - Les ennemis NE SPAWNENT JAMAIS dans les obstacles
 * - Vérifie 100 positions avant de placer l'ennemi
 * - Si aucune position sûre trouvée, ne spawn pas l'ennemi
 */
public class WaveManager {

    private int currentWave;
    private float spawnTimer;
    private float spawnInterval;
    private int enemiesSpawned;
    private int totalEnemies;
    private List<EnemyToSpawn> enemiesToSpawn;
    private Random random;
    private CollisionManager collisionManager;

    private static final float MAP_WIDTH = 720f;
    private static final float MAP_HEIGHT = 720f;

    private static class EnemyToSpawn {
        String type;
        int count;
        int spawned;

        EnemyToSpawn(String type, int count) {
            this.type = type;
            this.count = count;
            this.spawned = 0;
        }

        boolean hasMore() {
            return spawned < count;
        }

        void spawn() {
            spawned++;
        }
    }

    public WaveManager() {
        this.currentWave = 0;
        this.spawnTimer = 0;
        this.spawnInterval = 3.0f;
        this.enemiesToSpawn = new ArrayList<>();
        this.random = new Random();
    }

    /**
     *  Définit le CollisionManager pour vérifier les spawns
     */
    public void setCollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        System.out.println("✓ CollisionManager lié au WaveManager");
    }

    public void startWave(int waveNumber) {
        // Reset des compteurs et chargement de la vague
        currentWave = waveNumber;
        loadWaveFromJSON(waveNumber);
        spawnTimer = 0;
        enemiesSpawned = 0;

        System.out.println(" VAGUE " + waveNumber + " - " + totalEnemies + " ennemis");
    }

    private void loadWaveFromJSON(int waveNumber) {
        // Recharge la liste des ennemis a spawn
        enemiesToSpawn.clear();
        totalEnemies = 0;

        try {
            // Fichier de configuration de la vague
            String path = "data/waves/wave_0" + waveNumber + ".json";
            JsonReader reader = new JsonReader();
            JsonValue json = reader.parse(Gdx.files.internal(path));

            // Intervalle entre spawns (valeur par defaut 3s)
            spawnInterval = json.getFloat("spawnInterval", 3.0f);

            JsonValue enemies = json.get("enemies");
            for (JsonValue enemy : enemies) {
                String type = enemy.getString("type");
                int count = enemy.getInt("count");

                enemiesToSpawn.add(new EnemyToSpawn(type, count));
                totalEnemies += count;
            }

        } catch (Exception e) {
            System.err.println(" Erreur chargement vague " + waveNumber);
            e.printStackTrace();

            enemiesToSpawn.add(new EnemyToSpawn("Normal", 3));
            totalEnemies = 3;
        }
    }

    public Enemy update(float delta, Player player) {
        // Rien a faire si la vague est terminee
        if (isWaveComplete()) {
            return null;
        }

        // Decompte du timer de spawn
        spawnTimer -= delta;

        if (spawnTimer <= 0) {
            spawnTimer = spawnInterval;

            // Choisit le prochain type d'ennemi
            EnemyToSpawn toSpawn = getNextEnemyToSpawn();
            if (toSpawn != null) {
                toSpawn.spawn();
                enemiesSpawned++;

                // CRÉER ENNEMI DANS POSITION SÛRE (HORS OBSTACLES)
                return createEnemyInSafeSpot(toSpawn.type, player);
            }
        }

        return null;
    }

    private EnemyToSpawn getNextEnemyToSpawn() {
        List<EnemyToSpawn> available = new ArrayList<>();

        for (EnemyToSpawn spawn : enemiesToSpawn) {
            if (spawn.hasMore()) {
                available.add(spawn);
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }

    /**
     * Crée un ennemi dans une position SÛRE
     *
     * Stratégie :
     * 1. Essaye de spawn aux BORDS de la carte (100 tentatives)
     * 2. Vérifie CHAQUE position avec le CollisionManager
     * 3. Si aucune position trouvée, NE SPAWN PAS (plutôt que spawner dans un mur)
     */
    private Enemy createEnemyInSafeSpot(String type, Player player) {
        float x = 0, y = 0;
        boolean foundSafeSpot = false;
        int attempts = 0;
        int maxAttempts = 100;  // Augmenté à 100 tentatives

        // Fallback si le collision manager n'est pas disponible
        if (collisionManager == null) {
            System.err.println(" CollisionManager null ! Spawn aléatoire (risqué)");
            // Fallback : spawn au centre
            x = MAP_WIDTH / 2;
            y = MAP_HEIGHT / 2;
            return EntityFactory.createEnemy(type, x, y, player);
        }

        // Boucle d'essais pour trouver une position libre
        while (!foundSafeSpot && attempts < maxAttempts) {
            // Spawn aux BORDS de la carte (évite le centre souvent rempli d'obstacles)
            int edge = random.nextInt(4);

            switch (edge) {
                case 0: // Haut
                    x = 50 + random.nextFloat() * (MAP_WIDTH - 100);
                    y = MAP_HEIGHT - 50;
                    break;
                case 1: // Bas
                    x = 50 + random.nextFloat() * (MAP_WIDTH - 100);
                    y = 50;
                    break;
                case 2: // Gauche
                    x = 50;
                    y = 50 + random.nextFloat() * (MAP_HEIGHT - 100);
                    break;
                case 3: // Droite
                    x = MAP_WIDTH - 50;
                    y = 50 + random.nextFloat() * (MAP_HEIGHT - 100);
                    break;
            }

            //   La position est-elle SÛRE ?
            Rectangle testRect = new Rectangle(x, y, 32, 32);

            if (!collisionManager.isCollidingWithObstacles(testRect)) {
                //  POSITION SÛRE TROUVÉE !
                foundSafeSpot = true;
                System.out.println("Spawn sûr trouvé en (" + (int)x + ", " + (int)y + ") après " + attempts + " tentatives");
            }

            attempts++;
        }

        // Si AUCUNE position sûre trouvée après 100 tentatives
        if (!foundSafeSpot) {
            System.err.println("ERREUR : Aucune position sûre trouvée après " + maxAttempts + " tentatives !");
            System.err.println("Ennemi NON spawné (évite spawn dans obstacle)");
            return null;  //  NE PAS SPAWNER plutôt que spawner dans un mur
        }

        // Créer l'ennemi à la position sûre
        return EntityFactory.createEnemy(type, x, y, player);
    }

    public boolean isWaveComplete() {
        return enemiesSpawned >= totalEnemies;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }
}
