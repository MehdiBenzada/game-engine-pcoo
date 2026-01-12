package com.game.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un objet à ramasser (pièces ou XP)
 * FUSION de Coin.java + Experience.java
 */
public class Collectible extends Entity {
    
    public enum Type {
        COIN,
        EXPERIENCE
    }
    
    private Type type;
    private int value;
    private Player target;
    private float attractionRadius;
    private float attractionSpeed;

    public Collectible(float x, float y, float width, float height, Type type, int value) {
        super(x, y, width, height);
        // Type et valeur associee a la collecte
        this.type = type;
        this.value = value;
        // Parametres d'attraction vers le joueur
        this.attractionRadius = 100f;
        this.attractionSpeed = 150f;
    }

    @Override
    public void update(float delta) {
        // Aucun mouvement si inactif ou sans cible
        if (!active || target == null) return;
        
        // Distance actuelle au joueur
        float distanceToPlayer = Vector2.dst(
            position.x, position.y,
            target.getPosition().x, target.getPosition().y
        );
        
        // Attire l'objet si le joueur est proche
        if (distanceToPlayer < attractionRadius) {
            Vector2 toPlayer = new Vector2(
                target.getPosition().x - position.x,
                target.getPosition().y - position.y
            );
            
            if (toPlayer.len() > 0) {
                // Vecteur normalise pour une vitesse constante
                toPlayer.nor().scl(attractionSpeed);
                position.add(toPlayer.x * delta, toPlayer.y * delta);
            }
        }
    }

    public void collect(Player player) {
        // Ignorer si deja collecte
        if (!active) return;
        
        // Appliquer la recompense selon le type
        switch (type) {
            case COIN:
                player.addCoins(value);
                break;
            case EXPERIENCE:
                player.addExperience(value);
                break;
        }
        
        active = false;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
