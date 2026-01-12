package com.game.model.managers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.game.model.entities.Collectible;
import com.game.model.entities.Enemy;
import com.game.model.entities.Player;
import com.game.model.entities.Projectile;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire des collisions
 *
 * Trouve AUTOMATIQUEMENT les calques d'obstacles dans Tiled :
 * - Cherche "walls", "obstacles", "collision"
 * - Cherche TOUS les calques avec "Object Layer" dans le nom
 * - Extrait TOUS les rectangles trouvés
 * - Si aucun trouvé → Crée obstacles de test
 */
public class CollisionManager {

    private List<Rectangle> obstacles;
    private boolean obstaclesLoaded;
    private static final int PLAYER_HIT_DAMAGE = 10;

    public CollisionManager() {
        this.obstacles = new ArrayList<>();
        this.obstaclesLoaded = false;
    }

    /**
     *   Charge les obstacles depuis N'IMPORTE QUEL calque d'objets
     */
    public void loadObstaclesFromMap(TiledMap map) {
        // Reset de la liste avant chargement
        obstacles.clear();
        obstaclesLoaded = false;

        if (map == null) {
            System.err.println(" Carte null !");
            createTestObstacles();
            return;
        }

        System.out.println("========================================");
        System.out.println("  ANALYSE DE LA MAP TILED");
        System.out.println("========================================");

        // Lister TOUS les calques disponibles
        System.out.println(" Calques disponibles :");
        for (MapLayer layer : map.getLayers()) {
            int objectCount = layer.getObjects().getCount();
            String type = layer.getClass().getSimpleName();
            System.out.println("   • " + layer.getName() + " (" + type + ", " + objectCount + " objets)");
        }

        //   Chercher TOUS les calques avec des objets
        List<MapLayer> candidateLayers = new ArrayList<>();

        // Priorité 1 : Calques nommés spécifiquement
        String[] priorityNames = {"walls", "Walls", "WALLS", "obstacles", "Obstacles", "collision", "Collision"};
        for (String name : priorityNames) {
            MapLayer layer = map.getLayers().get(name);
            if (layer != null && layer.getObjects().getCount() > 0) {
                candidateLayers.add(layer);
                System.out.println("✓ Calque prioritaire trouvé : '" + name + "'");
            }
        }

        // Priorité 2 : TOUS les calques avec "Object" dans le nom
        if (candidateLayers.isEmpty()) {
            System.out.println("  Aucun calque prioritaire, recherche dans TOUS les calques d'objets...");

            for (MapLayer layer : map.getLayers()) {
                String layerName = layer.getName();
                int objectCount = layer.getObjects().getCount();

                // Accepter TOUS les calques avec des objets
                if (objectCount > 0 &&
                    (layerName.toLowerCase().contains("object") ||
                        layerName.toLowerCase().contains("layer"))) {
                    candidateLayers.add(layer);
                    System.out.println("   → Calque candidat : '" + layerName + "' (" + objectCount + " objets)");
                }
            }
        }

        // Si toujours rien, prendre le PREMIER calque avec des objets
        if (candidateLayers.isEmpty()) {
            System.out.println("  Toujours aucun calque, recherche du PREMIER calque avec objets...");

            for (MapLayer layer : map.getLayers()) {
                if (layer.getObjects().getCount() > 0) {
                    candidateLayers.add(layer);
                    System.out.println("   → Utilisation de : '" + layer.getName() + "'");
                    break;
                }
            }
        }

        // Si AUCUN calque trouvé → Obstacles de test
        if (candidateLayers.isEmpty()) {
            System.err.println(" AUCUN calque d'objets trouvé dans la map !");
            System.err.println("   → Création d'obstacles de test");
            createTestObstacles();
            return;
        }

        // EXTRAIRE LES RECTANGLES DE TOUS LES CALQUES CANDIDATS
        System.out.println("========================================");
        System.out.println(" EXTRACTION DES OBSTACLES");
        System.out.println("========================================");

        int totalRectangles = 0;

        for (MapLayer layer : candidateLayers) {
            int rectanglesInLayer = 0;

            System.out.println("Calque : '" + layer.getName() + "'");

            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    obstacles.add(rect);
                    rectanglesInLayer++;
                    totalRectangles++;

                    // Debug : Afficher les 3 premiers obstacles de chaque calque
                    if (rectanglesInLayer <= 3) {
                        System.out.println("   ✓ Obstacle " + rectanglesInLayer + ": " +
                            "x=" + (int)rect.x + " y=" + (int)rect.y +
                            " w=" + (int)rect.width + " h=" + (int)rect.height);
                    }
                }
            }

            if (rectanglesInLayer > 3) {
                System.out.println("   ... et " + (rectanglesInLayer - 3) + " autres");
            }

            System.out.println("   → Total : " + rectanglesInLayer + " rectangles");
        }

        if (totalRectangles == 0) {
            System.err.println(" Aucun rectangle trouvé dans les calques !");
            System.err.println("   (Les objets doivent être de type RECTANGLE, pas polygone)");
            System.err.println("   → Création d'obstacles de test");
            createTestObstacles();
            return;
        }

        obstaclesLoaded = true;
        System.out.println("========================================");
        System.out.println( totalRectangles + " obstacles chargés depuis Tiled");
        System.out.println("========================================");
    }

    /**
     * Crée des obstacles de test si Tiled échoue
     */
    private void createTestObstacles() {
        obstacles.clear();

        System.out.println("========================================");
        System.out.println(" CRÉATION D'OBSTACLES DE TEST");
        System.out.println("========================================");

        // Bordures de la map (720x720)
        obstacles.add(new Rectangle(0, 0, 720, 20));           // Bas
        obstacles.add(new Rectangle(0, 700, 720, 20));         // Haut
        obstacles.add(new Rectangle(0, 0, 20, 720));           // Gauche
        obstacles.add(new Rectangle(700, 0, 20, 720));         // Droite

        // Murs centraux en croix
        obstacles.add(new Rectangle(340, 200, 40, 320));       // Vertical
        obstacles.add(new Rectangle(200, 340, 320, 40));       // Horizontal

        obstaclesLoaded = true;
        System.out.println(obstacles.size() + " obstacles de test créés");
        System.out.println("   (4 bordures + 2 murs centraux en croix)");
        System.out.println("========================================");
    }

    /**
     * Vérifie si un rectangle entre en collision avec un obstacle.
     */
    public boolean isCollidingWithObstacles(Rectangle bounds) {
        // Aucun test si pas d'obstacles
        if (!obstaclesLoaded || obstacles.isEmpty()) {
            return false;
        }

        for (Rectangle obstacle : obstacles) {
            if (bounds.overlaps(obstacle)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollision(Rectangle a, Rectangle b) {
        return a.overlaps(b);
    }

    public void handlePlayerEnemyCollisions(Player player, List<Enemy> enemies) {
        if (!player.isActive()) {
            return;
        }

        // Rectangle de collision du joueur
        Rectangle playerBounds = player.getBounds();

        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) {
                continue;
            }

            if (checkCollision(playerBounds, enemy.getBounds())) {
                player.takeDamage(PLAYER_HIT_DAMAGE);
            }
        }
    }

    public void handleProjectileEnemyCollisions(List<Projectile> projectiles, List<Enemy> enemies) {
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive()) {
                continue;
            }

            // Rectangle de collision du projectile
            Rectangle projectileBounds = projectile.getBounds();

            for (Enemy enemy : enemies) {
                if (!enemy.isActive()) {
                    continue;
                }

                if (checkCollision(projectileBounds, enemy.getBounds())) {
                    enemy.takeDamage(projectile.getDamage());
                    projectile.setActive(false);
                    break;
                }
            }
        }
    }

    public void handlePlayerCollectibleCollisions(Player player, List<Collectible> collectibles) {
        if (!player.isActive()) {
            return;
        }

        // Rectangle de collision du joueur
        Rectangle playerBounds = player.getBounds();

        for (Collectible collectible : collectibles) {
            if (!collectible.isActive()) {
                continue;
            }

            if (checkCollision(playerBounds, collectible.getBounds())) {
                collectible.collect(player);
            }
        }
    }

    public void handleProjectileObstacleCollisions(List<Projectile> projectiles) {
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive()) {
                continue;
            }

            // Desactiver le projectile si il touche un obstacle
            if (isCollidingWithObstacles(projectile.getBounds())) {
                projectile.setActive(false);
            }
        }
    }

    public void handleAllCollisions(GameState state) {
        Player player = state.getPlayer();

        if (player == null || !player.isActive()) {
            return;
        }

        handlePlayerEnemyCollisions(player, state.getEnemies());
        handleProjectileEnemyCollisions(state.getProjectiles(), state.getEnemies());
        handlePlayerCollectibleCollisions(player, state.getCollectibles());
        handleProjectileObstacleCollisions(state.getProjectiles());
    }

    public List<Rectangle> getObstacles() {
        return obstacles;
    }

    public boolean areObstaclesLoaded() {
        return obstaclesLoaded;
    }
}
