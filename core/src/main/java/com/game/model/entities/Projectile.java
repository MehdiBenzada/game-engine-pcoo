package com.game.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un projectile tiré par le joueur.
 * 
 * Un projectile se déplace dans une direction donnée à vitesse constante
 * et inflige des dégâts aux ennemis touchés.
 * 
 * Architecture MVC : Partie MODEL - Entité projectile.
 * 
 * @author RPG Game Engine
 * @version 3.0
 */
public class Projectile extends Entity {
    
    // ===== PROPRIÉTÉS =====
    
    /** Direction normalisée du projectile */
    private Vector2 direction;
    
    /** Vitesse du projectile en pixels par seconde */
    private float speed;
    
    /** Dégâts infligés à l'ennemi touché */
    private int damage;
    
    /** Durée de vie maximale du projectile (secondes) */
    private float lifetime;
    
    /** Temps écoulé depuis la création */
    private float timeAlive;

    /**
     * Crée un nouveau projectile.
     * 
     * @param x Position initiale X
     * @param y Position initiale Y
     * @param width Largeur du projectile (8 pixels)
     * @param height Hauteur du projectile (8 pixels)
     */
    public Projectile(float x, float y, float width, float height) {
        super(x, y, width, height);
        // Parametres de base du projectile
        this.direction = new Vector2();
        this.speed = 400f;        // Vitesse rapide
        this.damage = 30;         // Dégâts par défaut
        this.lifetime = 3.0f;     // Disparaît après 3 secondes
        this.timeAlive = 0;
    }

    /**
     * Tire le projectile dans une direction.
     * 
     * @param targetDirection Direction vers laquelle tirer (sera normalisée)
     */
    public void shoot(Vector2 targetDirection) {
        // Normaliser la direction
        // Normaliser la direction de tir
        this.direction.set(targetDirection).nor();
        
        // Calculer la vélocité
        // Appliquer la vitesse dans cette direction
        this.velocity.set(direction).scl(speed);
    }

    /**
     * Met à jour le projectile chaque frame.
     * 
     * Le projectile se déplace dans sa direction et est désactivé
     * après sa durée de vie maximale.
     * 
     * @param delta Temps écoulé depuis la dernière frame
     */
    @Override
    public void update(float delta) {
        // Ne rien faire si inactif
        if (!active) {
            return;
        }
        
        // Déplacer le projectile
        // Mise a jour de la position
        position.add(velocity.x * delta, velocity.y * delta);
        
        // Vérifier la durée de vie
        // Duree de vie du projectile
        timeAlive += delta;
        if (timeAlive >= lifetime) {
            active = false;  // Disparaît après expiration
        }
    }

    // ========== GETTERS ==========

    public int getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public Vector2 getDirection() {
        return direction;
    }

    // ========== SETTERS ==========

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }
}
