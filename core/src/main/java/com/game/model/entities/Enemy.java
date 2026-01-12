package com.game.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un ennemi dans le jeu.
 * 
 * VERSION CORRIGÉE :
 * - Comportement de poursuite simplifié
 * - Système d'évitement d'obstacles intégré (8 directions)
 * - Détection de blocage rapide
 */
public class Enemy extends Entity {
    
    private Player target;
    private float speed;
    private int health;
    private int maxHealth;
    private int damage;
    private int coinValue;
    private int expValue;
    private String behaviorType;
    
    //  SYSTÈME D'ÉVITEMENT D'OBSTACLES
    private Vector2 desiredDirection;
    private float stuckTimer;
    private float lastDistance;
    private int directionAttempt;
    
    private static final float STUCK_CHECK_TIME = 0.2f;      // Vérifie toutes les 0.2 sec
    private static final float DISTANCE_THRESHOLD = 1.5f;    // Si bouge moins de 1.5px = bloqué
    private static final int MAX_DIRECTIONS = 8;             // 8 directions d'évitement

    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height);
        // Valeurs par defaut de l'ennemi
        this.speed = 50f;
        this.maxHealth = 50;
        this.health = maxHealth;
        this.damage = 10;
        this.coinValue = 5;
        this.expValue = 10;
        this.behaviorType = "chase";
        
        // Initialiser le système d'évitement
        // Direction cible pour l'evitement d'obstacles
        this.desiredDirection = new Vector2();
        this.stuckTimer = 0;
        this.lastDistance = 9999f;
        this.directionAttempt = 0;
    }

    @Override
    public void update(float delta) {
        // Ne rien faire si inactif ou sans cible
        if (!active || target == null) {
            return;
        }

        // Le comportement "idle" reste immobile
        if ("idle".equalsIgnoreCase(behaviorType)) {
            return;
        }
        
        // Timer pour verifier le blocage
        stuckTimer += delta;
        
        // Distance actuelle au joueur
        float currentDistance = Vector2.dst(
            position.x, position.y,
            target.getPosition().x, target.getPosition().y
        );
        
        // DÉTECTION DE BLOCAGE (toutes les 0.2 sec)
        if (stuckTimer >= STUCK_CHECK_TIME) {
            // Si l'ennemi n'a presque pas bougé = bloqué !
            if (currentDistance >= lastDistance - DISTANCE_THRESHOLD) {
                directionAttempt++;
                if (directionAttempt >= MAX_DIRECTIONS) {
                    directionAttempt = 0;  // Recommencer le cycle
                }
            } else {
                // L'ennemi bouge bien, revenir à la direction directe
                directionAttempt = 0;
            }
            
            lastDistance = currentDistance;
            stuckTimer = 0;
        }
        
        // Calculer la direction avec évitement
        // Calcule la direction finale a utiliser
        calculateMovementDirection();
        
        // Appliquer le mouvement
        velocity.set(desiredDirection).scl(speed);
        position.add(velocity.x * delta, velocity.y * delta);
    }
    
    /**
     * CALCUL DE DIRECTION AVEC 8 POSSIBILITÉS D'ÉVITEMENT
     * 
     * Quand l'ennemi est bloqué, il essaie 8 directions différentes :
     * 0. Direct vers joueur
     * 1. 45° gauche
     * 2. 45° droite
     * 3. 90° gauche (perpendiculaire)
     * 4. 90° droite (perpendiculaire)
     * 5. 135° gauche
     * 6. 135° droite
     * 7. 180° (reculer)
     */
    private void calculateMovementDirection() {
        // Direction de base vers le joueur
        Vector2 toTarget = new Vector2(
            target.getPosition().x - position.x,
            target.getPosition().y - position.y
        );
        
        // Normaliser pour une direction unitaire
        if (toTarget.len() > 0) {
            toTarget.nor();  // Normaliser
        }
        
        //APPLIQUER LA ROTATION SELON LA TENTATIVE
        // Choisir une direction alternative selon l'essai
        switch (directionAttempt) {
            case 0: // Direct vers joueur
                desiredDirection.set(toTarget);
                break;
                
            case 1: // 45° gauche
                desiredDirection.set(toTarget).rotateDeg(45);
                break;
                
            case 2: // 45° droite
                desiredDirection.set(toTarget).rotateDeg(-45);
                break;
                
            case 3: // 90° gauche 
                desiredDirection.set(toTarget).rotateDeg(90);
                break;
                
            case 4: // 90° droite
                desiredDirection.set(toTarget).rotateDeg(-90);
                break;
                
            case 5: // 135° gauche
                desiredDirection.set(toTarget).rotateDeg(135);
                break;
                
            case 6: // 135° droite
                desiredDirection.set(toTarget).rotateDeg(-135);
                break;
                
            case 7: // 180° (reculer)
                desiredDirection.set(toTarget).rotateDeg(180);
                break;
                
            default:
                desiredDirection.set(toTarget);
                directionAttempt = 0;
                break;
        }
    }

    public void takeDamage(int damage) {
        // Retirer les points de vie
        health -= damage;
        
        if (health <= 0) {
            health = 0;
            active = false;
            System.out.println(" Ennemi éliminé ! (Drop: " + coinValue + " pièces, " + expValue + " XP)");
        }
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    // ========== GETTERS ==========

    public float getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public int getExpValue() {
        return expValue;
    }

    public Player getTarget() {
        return target;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    // ========== SETTERS ==========

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setCoinValue(int coinValue) {
        this.coinValue = coinValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public void setBehaviorType(String behaviorType) {
        // Garantit une valeur de comportement valide
        if (behaviorType == null || behaviorType.trim().isEmpty()) {
            this.behaviorType = "chase";
        } else {
            this.behaviorType = behaviorType;
        }
    }
}
