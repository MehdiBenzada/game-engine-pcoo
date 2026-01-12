package com.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.model.entities.Collectible;
import com.game.model.entities.Enemy;
import com.game.model.entities.Player;
import com.game.model.entities.Projectile;
import com.game.model.factories.EntityFactory;
import com.game.model.managers.CollisionManager;
import com.game.model.managers.GameState;
import com.game.model.managers.WaveManager;
import com.game.view.GameRenderer;

import java.util.Random;

/**
 * Contrôleur principal du jeu (CONTROLLER)
 *  Les vagues se débloquent UNIQUEMENT avec le niveau du joueur
 *  Les coins servent UNIQUEMENT pour les améliorations (U, I, H)
 */
public class GameController {

    // Etat global du jeu (joueur, ennemis, projectiles, etc.)
    private GameState gameState;
    // Gestionnaire des collisions et obstacles
    private CollisionManager collisionManager;
    // Gestion des vagues d'ennemis
    private WaveManager waveManager;
    // Rendu graphique du jeu
    private GameRenderer renderer;

    // Dimensions de la map en pixels
    private static final float MAP_WIDTH = 720f;
    private static final float MAP_HEIGHT = 720f;

    // Cooldown entre deux tirs
    private float shootCooldown;
    private static final float SHOOT_COOLDOWN_MAX = 0.5f;

    // Textures partagees pour les entites
    private Texture projectileTexture;
    private Texture coinTexture;
    private Texture expTexture;

    // Generateur aleatoire pour placements
    private Random random;

    public GameController() {
        // Logs de demarrage
        System.out.println("========================================");
        System.out.println("INITIALISATION DU CONTRÔLEUR");
        System.out.println("========================================");

        // Initialisations de base
        random = new Random();

        // Creation des composants principaux
        gameState = new GameState(MAP_WIDTH, MAP_HEIGHT);
        collisionManager = new CollisionManager();
        waveManager = new WaveManager();
        renderer = new GameRenderer();

        // Chargements et setup initial
        loadTextures();
        loadMapCollisions();
        createPlayerInSafePosition();

        // Liaisons et data des ennemis
        waveManager.setCollisionManager(collisionManager);
        EntityFactory.loadEnemyTypes();

        // Demarrer la premiere vague
        waveManager.startWave(1);
        gameState.setCurrentWave(1);

        // Cooldown de tir initial
        shootCooldown = 0;

        System.out.println(" Contrôleur prêt !");
        System.out.println("========================================");
    }

    private void loadTextures() {
        try {
            // Texture du projectile
            projectileTexture = new Texture("sprite/cannon.png");
        } catch (Exception e) {
            System.err.println(" Erreur chargement texture projectile");
            projectileTexture = createColorTexture(255, 255, 0);
        }

        try {
            // Texture des pieces
            coinTexture = new Texture("sprite/coin.png");
        } catch (Exception e) {
            System.err.println(" Erreur chargement texture coin");
            coinTexture = createColorTexture(255, 215, 0);
        }

        try {
            // Texture de l'XP (etoile)
            expTexture = new Texture("sprite/star.png");
        } catch (Exception e) {
            System.err.println(" Erreur chargement texture XP");
            expTexture = createColorTexture(0, 255, 255);
        }
    }

    private void loadMapCollisions() {
        // Charger les obstacles depuis la map Tiled si disponible
        if (renderer.getMapLoader().getMap() != null) {
            collisionManager.loadObstaclesFromMap(renderer.getMapLoader().getMap());
        } else {
            System.err.println(" Map non chargée, pas d'obstacles");
        }
    }

    private void createPlayerInSafePosition() {
        // Placement du joueur dans une zone sans collision
        System.out.println("========================================");
        System.out.println(" CRÉATION DU JOUEUR");
        System.out.println("========================================");

        // Variables de recherche d'une position libre
        float x = MAP_WIDTH / 2;
        float y = MAP_HEIGHT / 2;
        boolean foundSafeSpot = false;
        int attempts = 0;
        int maxAttempts = 100;

        // Essayer plusieurs positions aleatoires
        while (!foundSafeSpot && attempts < maxAttempts) {
            x = 100 + random.nextFloat() * (MAP_WIDTH - 200);
            y = 100 + random.nextFloat() * (MAP_HEIGHT - 200);

            // Rectangle de test pour collision
            Rectangle testRect = new Rectangle(x, y, 32, 32);

            if (!collisionManager.isCollidingWithObstacles(testRect)) {
                foundSafeSpot = true;
                System.out.println(" Position sûre trouvée : (" + (int)x + ", " + (int)y + ") après " + attempts + " tentatives");
            }

            attempts++;
        }

        // Si aucune position libre, garder la position actuelle
        if (!foundSafeSpot) {
            System.err.println(" Aucune position sûre trouvée après " + maxAttempts + " tentatives");
            System.err.println("   Utilisation du centre de la map : (" + (int)x + ", " + (int)y + ")");
        }

        // Creation de l'entite joueur
        Player player = new Player(x, y, 32, 32);
        player.setInvincible(false);

        try {
            // Texture du joueur
            Texture playerTexture = new Texture("sprite/player.png");
            player.setTexture(playerTexture);
        } catch (Exception e) {
            System.err.println(" Texture joueur non trouvée");
            player.setTexture(createColorTexture(0, 255, 0));
        }

        // Enregistrer le joueur dans l'etat
        gameState.setPlayer(player);

        System.out.println("========================================");
    }

    public void update(float delta) {
        Player player = gameState.getPlayer();
        // Si le joueur est absent ou mort, on stoppe l'update
        if (player == null || !player.isActive()) return;

        // Gestion des inputs joueur
        handlePlayerMovement(delta);
        handleShooting(delta);
        handleUpgrades();

        // Mise a jour des entites du jeu
        updatePlayer(delta);
        updateEnemies(delta);
        updateProjectiles(delta);
        updateCollectibles(delta);

        // Collisions globales (joueur, ennemis, projectiles, loot)
        collisionManager.handleAllCollisions(gameState);

        // Spawn eventuel d'un nouvel ennemi
        Enemy newEnemy = waveManager.update(delta, player);
        if (newEnemy != null) {
            gameState.addEnemy(newEnemy);
        }

        // Nettoyage et progression de vague
        cleanupDeadEntities();
        checkWaveProgression();
    }

    private void handlePlayerMovement(float delta) {
        Player player = gameState.getPlayer();
        float speed = player.getSpeed();

        float prevX = player.getPosition().x;
        float prevY = player.getPosition().y;

        Vector2 movement = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x += speed;
        }

        // Normaliser pour une vitesse constante
        if (movement.len() > 0) {
            movement.nor().scl(speed);
        }

        // Appliquer la vitesse et le mouvement
        player.setVelocity(movement.x, movement.y);
        player.getPosition().add(movement.x * delta, movement.y * delta);

        // Revenir en arriere si collision
        if (collisionManager.isCollidingWithObstacles(player.getBounds())) {
            player.setPosition(prevX, prevY);
        }

        // S'assurer que le joueur reste dans la map
        keepPlayerInBounds();
    }

    private void handleShooting(float delta) {
        // Met a jour le cooldown de tir
        shootCooldown -= delta;

        // Tir au clic gauche quand le cooldown est termine
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootCooldown <= 0) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            renderer.getCamera().unproject(mousePos);

            Player player = gameState.getPlayer();
            Vector2 direction = new Vector2(
                mousePos.x - player.getPosition().x,
                mousePos.y - player.getPosition().y
            );

            shootProjectile(direction);
            shootCooldown = SHOOT_COOLDOWN_MAX;
        }
    }

    private void handleUpgrades() {
        Player player = gameState.getPlayer();

        // Ameliorations selon les touches
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            player.upgradeDamage();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            player.upgradeSpeed();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.buyHealth();
        }
    }

    private void shootProjectile(Vector2 direction) {
        Player player = gameState.getPlayer();

        // Creer un projectile au centre du joueur
        Projectile projectile = new Projectile(
            player.getPosition().x + 16,
            player.getPosition().y + 16,
            8, 8
        );
        projectile.setTexture(projectileTexture);
        projectile.setDamage(player.getDamage());
        projectile.shoot(direction);

        // Ajouter le projectile a l'etat du jeu
        gameState.addProjectile(projectile);
    }

    private void updatePlayer(float delta) {
        gameState.getPlayer().update(delta);
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : gameState.getEnemies()) {
            if (!enemy.isActive()) continue;

            float prevX = enemy.getPosition().x;
            float prevY = enemy.getPosition().y;

            // Mise a jour IA / mouvement
            enemy.update(delta);

            if (collisionManager.isCollidingWithObstacles(enemy.getBounds())) {
                // Annule le mouvement en cas de collision
                enemy.setPosition(prevX, prevY);
            }

            // Empeche l'ennemi de sortir de la map
            keepEnemyInBounds(enemy);
        }
    }

    private void updateProjectiles(float delta) {
        for (Projectile projectile : gameState.getProjectiles()) {
            if (!projectile.isActive()) continue;

            // Deplacement du projectile
            projectile.update(delta);

            if (collisionManager.isCollidingWithObstacles(projectile.getBounds())) {
                // Detruit le projectile si obstacle
                projectile.setActive(false);
            }
        }
    }

    private void updateCollectibles(float delta) {
        for (Collectible collectible : gameState.getCollectibles()) {
            // Attraction vers le joueur si actif
            if (collectible.isActive()) collectible.update(delta);
        }
    }

    private void cleanupDeadEntities() {
        for (Enemy enemy : gameState.getEnemies()) {
            if (!enemy.isActive()) {
                // Drop loot a la position de l'ennemi
                dropLoot(enemy.getPosition().x, enemy.getPosition().y,
                    enemy.getCoinValue(), enemy.getExpValue());
            }
        }

        // Supprimer les entites inactives des listes
        gameState.removeDeadEntities();
    }

    private void dropLoot(float x, float y, int coins, int exp) {
        // Generer les objets ramassables a la mort d'un ennemi
        if (coins > 0) {
            Collectible coin = new Collectible(x, y, 16, 16, Collectible.Type.COIN, coins);
            coin.setTexture(coinTexture);
            coin.setTarget(gameState.getPlayer());
            gameState.addCollectible(coin);
        }

        if (exp > 0) {
            Collectible xp = new Collectible(x + 20, y, 16, 16, Collectible.Type.EXPERIENCE, exp);
            xp.setTexture(expTexture);
            xp.setTarget(gameState.getPlayer());
            gameState.addCollectible(xp);
        }
    }

    /**
     * Les vagues se débloquent UNIQUEMENT avec le niveau
     *
     * Condition pour passer à la vague suivante :
     * 1. Niveau joueur > Vague actuelle
     * 2. Vague terminée (plus d'ennemis)
     *
     * Les coins ne sont PAS nécessaires pour débloquer les vagues.
     */
    private void checkWaveProgression() {
        Player player = gameState.getPlayer();
        int playerLevel = player.getLevel();
        int currentWave = waveManager.getCurrentWave();

        // Vérifier si on peut passer à la vague suivante
        if (playerLevel > currentWave &&
            waveManager.isWaveComplete() &&
            gameState.getEnemies().isEmpty()) {

            System.out.println("========================================");
            System.out.println("VAGUE " + (currentWave + 1) + " DÉBLOQUÉE !");
            System.out.println("Niveau requis atteint : " + playerLevel);
            System.out.println("========================================");

            // Lancer la vague suivante
            waveManager.startWave(currentWave + 1);
            gameState.setCurrentWave(currentWave + 1);
        }
    }

    private void keepPlayerInBounds() {
        Player player = gameState.getPlayer();
        // Limiter la position aux bords de la map
        float x = Math.max(0, Math.min(player.getPosition().x, MAP_WIDTH - 32));
        float y = Math.max(0, Math.min(player.getPosition().y, MAP_HEIGHT - 32));
        player.setPosition(x, y);
    }

    private void keepEnemyInBounds(Enemy enemy) {
        // Limiter la position aux bords de la map
        float x = Math.max(0, Math.min(enemy.getPosition().x, MAP_WIDTH - 32));
        float y = Math.max(0, Math.min(enemy.getPosition().y, MAP_HEIGHT - 32));
        enemy.setPosition(x, y);
    }

    private Texture createColorTexture(int r, int g, int b) {
        // Cree une texture 1x1 pour les fallbacks
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r/255f, g/255f, b/255f, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void render() {
        renderer.render(gameState);
    }

    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    public void dispose() {
        renderer.dispose();
        EntityFactory.dispose();

        if (projectileTexture != null) projectileTexture.dispose();
        if (coinTexture != null) coinTexture.dispose();
        if (expTexture != null) expTexture.dispose();
    }

    public GameState getGameState() {
        return gameState;
    }
}

