package com.game.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe de base abstraite pour toutes les entités du jeu.
 * 
 * Une entité représente tout objet du jeu qui a une position, une taille et
 * une texture (joueur, ennemi, projectile, objet à ramasser).
 * 
 * Cette classe fournit les propriétés et comportements de base que toutes
 * les entités partagent.
 * 
 * Architecture MVC : Cette classe fait partie du MODEL.
 * 
 * @author RPG Game Engine
 * @version 3.0 (Simplifié - Suppression interfaces et HitBox)
 */
public abstract class Entity {
    
    /** Position de l'entité dans le monde (coordonnées x, y en pixels) */
    protected Vector2 position;
    
    /** Vecteur de vitesse pour les déplacements (pixels par seconde) */
    protected Vector2 velocity;
    
    /** Largeur de l'entité en pixels */
    protected float width;
    
    /** Hauteur de l'entité en pixels */
    protected float height;
    
    /** Texture (image) pour afficher l'entité */
    protected Texture texture;
    
    /** Indique si l'entité est active (visible et en jeu) */
    protected boolean active;

    /**
     * Crée une nouvelle entité.
     * 
     * @param x Position initiale X
     * @param y Position initiale Y
     * @param width Largeur en pixels
     * @param height Hauteur en pixels
     */
    public Entity(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.width = width;
        this.height = height;
        this.active = true;
    }

    /**
     * Met à jour l'entité chaque frame.
     * Cette méthode doit être implémentée par les sous-classes.
     * 
     * @param delta Temps écoulé depuis la dernière frame (en secondes)
     */
    public abstract void update(float delta);

    /**
     * Dessine l'entité à l'écran.
     * 
     * @param batch Le SpriteBatch utilisé pour dessiner
     */
    public void render(SpriteBatch batch) {
        // Dessiner uniquement si actif et texture chargee
        if (texture != null && active) {
            batch.draw(texture, position.x, position.y, width, height);
        }
    }

    /**
     * Retourne le rectangle de collision de l'entité.
     * Utilisé pour détecter les collisions avec d'autres entités.
     * 
     
     * 
     * @return Rectangle représentant les limites de l'entité
     */
    public Rectangle getBounds() {
        // Rectangle base sur la position et la taille actuelles
        return new Rectangle(position.x, position.y, width, height);
    }

    // ========== GETTERS ==========

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isActive() {
        return active;
    }

    // ========== SETTERS ==========

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Libère les ressources (texture) utilisées par l'entité.
     */
    public void dispose() {
        // Liberer la texture si elle existe
        if (texture != null) {
            texture.dispose();
        }
    }
}
