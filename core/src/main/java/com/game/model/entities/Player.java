package com.game.model.entities;

/**
 * Classe représentant le joueur.
 */
public class Player extends Entity {

    private float speed;
    private int health;
    private int maxHealth;
    private int level;
    private int experience;
    private int experienceToNext;
    private int coins;

    // Attributs améliorables
    private int damage;
    private int damageLevel;
    private int speedLevel;

    //  SYSTÈME D'INVINCIBILITÉ TEMPORAIRE
    private boolean invincible;
    private float invincibilityTimer;
    private static final float INVINCIBILITY_DURATION = 1.0f; // 1 seconde

    // RÉDUCTION DES DÉGÂTS
    private static final float DAMAGE_REDUCTION = 1.0f; // Degats complets

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        // Statistiques de base
        this.speed = 120f;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.level = 1;
        this.experience = 0;
        this.experienceToNext = 100;
        this.coins = 0;

        // Améliorations
        this.damage = 30;
        this.damageLevel = 0;
        this.speedLevel = 0;

        // Invincibilité désactivée au départ
        this.invincible = false;
        this.invincibilityTimer = 0;

        System.out.println("===== JOUEUR CRÉÉ =====");
        System.out.println("Vie: " + health + "/" + maxHealth);
        System.out.println("Vitesse: " + speed);
        System.out.println("Dégâts: " + damage);
        System.out.println("Multiplicateur degats: " + (int)(DAMAGE_REDUCTION * 100) + "%");
        System.out.println("=======================");
    }

    @Override
    public void update(float delta) {
        //  Mettre à jour le timer d'invincibilité
        // Decompte du temps d'invincibilite temporaire
        if (invincibilityTimer > 0) {
            invincibilityTimer -= delta;
            if (invincibilityTimer <= 0) {
                invincibilityTimer = 0;
                invincible = false;
                // System.out.println("✓ Invincibilité terminée");
            }
        }
    }

    /**
     * Le joueur subit des dégâts.
     
     * - Pas de reduction des degats
     * - Invincibilité temporaire de 1 seconde après chaque hit
     * - Empêche les multi-hits instantanés
     */
    public void takeDamage(int damage) {
        // Si invincible temporairement, bloquer les dégâts
        if (invincible) {
            return; // Pas de message pour éviter spam console
        }

        // Réduire les dégâts de 50%
        // Degats apres reduction
        int reducedDamage = (int)(damage * DAMAGE_REDUCTION);

        health -= reducedDamage;
        System.out.println(" Joueur touché ! -" + reducedDamage + " PV (Vie: " + health + "/" + maxHealth + ")");

        //  Activer l'invincibilité temporaire
        invincible = true;
        invincibilityTimer = INVINCIBILITY_DURATION;

        if (health <= 0) {
            health = 0;
            active = false;
            System.out.println("========== GAME OVER ==========");
        }
    }

    public void addExperience(int exp) {
        experience += exp;
        System.out.println(exp + " XP (Total: " + experience + "/" + experienceToNext + ")");

        // Monte de niveau tant qu'il reste assez d'XP
        while (experience >= experienceToNext) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= experienceToNext;
        experienceToNext = (int)(experienceToNext * 1.5f);

        // Augmente la vie max et soigne complet
        maxHealth += 10;
        health = maxHealth;

        System.out.println("=============================");
        System.out.println(" LEVEL UP! Niveau " + level );
        System.out.println("Vie max: " + maxHealth);
        System.out.println("Prochain niveau: " + experienceToNext + " XP");
        System.out.println("=============================");
    }

    public void addCoins(int amount) {
        // Ajoute les pieces ramassees
        coins += amount;
        System.out.println(  amount + " pièces (Total: " + coins + ")");
    }

    public void heal(int amount) {
        // Limite les soins au max
        health = Math.min(health + amount, maxHealth);
        System.out.println(amount + " PV (Vie: " + health + "/" + maxHealth + ")");
    }

    /**
     * Active ou désactive le mode invincible PERMANENT
     * (différent de l'invincibilité temporaire après hit)
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        this.invincibilityTimer = 0;
        System.out.println("Mode invincible permanent: " + (invincible ? "ACTIVÉ " : "DÉSACTIVÉ"));
    }

    // ========== MÉTHODES D'AMÉLIORATION ==========

    public boolean upgradeDamage() {
        // Cout d'amelioration des degats
        int cost = getDamageUpgradeCost();

        if (coins >= cost) {
            coins -= cost;
            damageLevel++;
            damage += 10;

            System.out.println("========================================");
            System.out.println("AMÉLIORATION DÉGÂTS!");
            System.out.println("Niveau: " + damageLevel);
            System.out.println("Dégâts: " + damage);
            System.out.println("Coût: " + cost + " coins");
            System.out.println("Coins restants: " + coins);
            System.out.println("========================================");
            return true;
        } else {
            System.out.println("Pas assez de coins pour améliorer les dégâts!");
            System.out.println("   Requis: " + cost + " | Vous avez: " + coins);
            return false;
        }
    }

    public boolean upgradeSpeed() {
        // Cout d'amelioration de la vitesse
        int cost = getSpeedUpgradeCost();

        if (coins >= cost) {
            coins -= cost;
            speedLevel++;
            speed += 20f;

            System.out.println("========================================");
            System.out.println("AMÉLIORATION VITESSE!");
            System.out.println("Niveau: " + speedLevel);
            System.out.println("Vitesse: " + speed);
            System.out.println("Coût: " + cost + " coins");
            System.out.println("Coins restants: " + coins);
            System.out.println("========================================");
            return true;
        } else {
            System.out.println("Pas assez de coins pour améliorer la vitesse!");
            System.out.println("   Requis: " + cost + " | Vous avez: " + coins);
            return false;
        }
    }

    public boolean buyHealth() {
        // Cout d'achat de soins
        int cost = getHealthCost();

        if (health >= maxHealth) {
            System.out.println("Vie déjà au maximum!");
            return false;
        }

        if (coins >= cost) {
            coins -= cost;
            int healAmount = 30;
            health = Math.min(health + healAmount, maxHealth);

            System.out.println("========================================");
            System.out.println("VIE RESTAURÉE!");
            System.out.println("+" + healAmount + " PV");
            System.out.println("Vie: " + health + "/" + maxHealth);
            System.out.println("Coût: " + cost + " coins");
            System.out.println("Coins restants: " + coins);
            System.out.println("========================================");
            return true;
        } else {
            System.out.println("Pas assez de coins pour acheter de la vie!");
            System.out.println("   Requis: " + cost + " | Vous avez: " + coins);
            return false;
        }
    }

    // ========== CALCUL DES COÛTS ==========

    public int getDamageUpgradeCost() {
        return 50 + (damageLevel * 25);
    }

    public int getSpeedUpgradeCost() {
        return 60 + (speedLevel * 30);
    }

    public int getHealthCost() {
        return 40;
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

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNext() {
        return experienceToNext;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public int getDamage() {
        return damage;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    /**
     * Retourne le temps d'invincibilité restant
     */
    public float getInvincibilityTimer() {
        return invincibilityTimer;
    }

    // ========== SETTERS ==========

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
