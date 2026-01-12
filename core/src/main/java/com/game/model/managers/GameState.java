package com.game.model.managers;

import com.game.model.entities.Collectible;
import com.game.model.entities.Enemy;
import com.game.model.entities.Player;
import com.game.model.entities.Projectile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe contenant tout l'état du jeu.
 * 
 * GameState centralise toutes les données du jeu :
 * - Le joueur
 * - Les listes d'ennemis, projectiles et collectibles
 * - Les informations de progression (vague, score)
 * 
 * Cette classe fait partie du MODEL dans l'architecture MVC.
 * Elle ne contient QUE des données et des méthodes pour les gérer,
 * pas de logique de jeu complexe.
 * 
 * @author RPG Game Engine
 * @version 3.0 (Nouveau - Centralise l'état)
 */
public class GameState {
    
    // ===== ENTITÉS =====
    
    /** Le joueur */
    private Player player;
    
    /** Liste des ennemis actifs */
    private List<Enemy> enemies;
    
    /** Liste des projectiles actifs */
    private List<Projectile> projectiles;
    
    /** Liste des objets à ramasser actifs */
    private List<Collectible> collectibles;
    
    // ===== PROGRESSION =====
    
    /** Numéro de la vague actuelle */
    private int currentWave;
    
    /** Score du joueur (basé sur les pièces) */
    private int score;
    
    // ===== DIMENSIONS DE LA CARTE =====
    
    /** Largeur de la carte en pixels */
    private float mapWidth;
    
    /** Hauteur de la carte en pixels */
    private float mapHeight;

    /**
     * Crée un nouveau GameState.
     * 
     * @param mapWidth Largeur de la carte
     * @param mapHeight Hauteur de la carte
     */
    public GameState(float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        
        // Initialiser les listes
        // Listes d'entites actives
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.collectibles = new ArrayList<>();
        
        // Initialiser les valeurs
        // Compteurs de progression
        this.currentWave = 0;
        this.score = 0;
        
        System.out.println(" GameState créé (Map: " + mapWidth + "x" + mapHeight + ")");
    }

    /**
     * Définit le joueur.
     * 
     * @param player Le joueur
     */
    public void setPlayer(Player player) {
        this.player = player;
        System.out.println(" Joueur ajouté au GameState");
    }

    /**
     * Ajoute un ennemi au jeu.
     * 
     * @param enemy L'ennemi à ajouter
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Ajoute un projectile au jeu.
     * 
     * @param projectile Le projectile à ajouter
     */
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    /**
     * Ajoute un collectible au jeu.
     * 
     * @param collectible L'objet à ramasser à ajouter
     */
    public void addCollectible(Collectible collectible) {
        collectibles.add(collectible);
    }

    /**
     * Supprime toutes les entités inactives (mortes).
     * 
     * Cette méthode nettoie les listes en enlevant :
     * - Les ennemis éliminés
     * - Les projectiles qui ont touché quelque chose ou expiré
     * - Les collectibles ramassés
     */
    public void removeDeadEntities() {
        // Supprimer les ennemis morts
        Iterator<Enemy> enemyIt = enemies.iterator();
        while (enemyIt.hasNext()) {
            Enemy enemy = enemyIt.next();
            if (!enemy.isActive()) {
                enemyIt.remove();
            }
        }
        
        // Supprimer les projectiles inactifs
        // Retirer les projectiles inactifs
        projectiles.removeIf(p -> !p.isActive());
        
        // Supprimer les collectibles ramassés
        // Retirer les collectibles inactifs
        collectibles.removeIf(c -> !c.isActive());
    }

    /**
     * Réinitialise le GameState (nouvelle partie).
     * 
     * Vide toutes les listes et remet les compteurs à zéro.
     */
    public void reset() {
        // Vide les collections et remet les compteurs a zero
        enemies.clear();
        projectiles.clear();
        collectibles.clear();
        currentWave = 0;
        score = 0;
        
        System.out.println("GameState réinitialisé");
    }

    /**
     * Incrémente le numéro de vague.
     */
    public void nextWave() {
        currentWave++;
        System.out.println("Passage à la vague " + currentWave);
    }

    /**
     * Ajoute au score.
     * 
     * @param points Points à ajouter
     */
    public void addScore(int points) {
        score += points;
    }

    // ========== GETTERS ==========

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getScore() {
        return score;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    // ========== SETTERS ==========

    public void setCurrentWave(int wave) {
        this.currentWave = wave;
    }
}
