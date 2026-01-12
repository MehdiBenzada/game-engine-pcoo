package com.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.model.entities.Player;
import com.game.model.managers.GameState;

/**
 * Interface utilisateur (HUD)
 * Layout :
 * - GAUCHE : Barre de vie, Niveau, XP, Coins, Vague
 * - DROITE : Améliorations disponibles (si achetable uniquement)
 */
public class HUD {

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont upgradeFont;
    private ShapeRenderer shapeRenderer;

    public HUD() {
        batch = new SpriteBatch();

        // Police principale (gauche)
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        // Police améliorations (droite, plus petite)
        upgradeFont = new BitmapFont();
        upgradeFont.setColor(Color.WHITE);
        upgradeFont.getData().setScale(1.3f);

        shapeRenderer = new ShapeRenderer();
    }

    public void render(GameState state) {
        Player player = state.getPlayer();
        // Aucun HUD si pas de joueur
        if (player == null) return;

        batch.begin();

        // ===== GAUCHE : INFOS PRINCIPALES =====
        drawLeftPanel(player, state);

        // ===== DROITE : AMÉLIORATIONS DISPONIBLES =====
        drawRightPanel(player);

        batch.end();

        // Barre de vie (après batch pour éviter conflit)
        drawHealthBar(player);
    }

    /**
     * Panneau GAUCHE : Vie, Niveau, XP, Coins, Vague
     */
    private void drawLeftPanel(Player player, GameState state) {
        float x = 10;
        float baseY = Gdx.graphics.getHeight() - 50;
        float lineHeight = 30;

        // Niveau et XP
        font.setColor(Color.WHITE);
        font.draw(batch,
            "Niveau " + player.getLevel() + " | XP: " + player.getExperience() + "/" + player.getExperienceToNext(),
            x, baseY);

        // Coins
        font.setColor(Color.GOLD);
        font.draw(batch,
            "Pieces: " + player.getCoins(),
            x, baseY - lineHeight);

        // Vague
        font.setColor(Color.WHITE);
        font.draw(batch,
            "Vague: " + state.getCurrentWave(),
            x, baseY - lineHeight * 2);

    }

    /**
     * Panneau DROITE : Améliorations (si achetable uniquement)
     */
    private void drawRightPanel(Player player) {
        float screenWidth = Gdx.graphics.getWidth();
        float x = screenWidth - 320; // Marge de 320px depuis la droite
        float baseY = Gdx.graphics.getHeight() - 20;
        float lineHeight = 30;
        int lineCount = 0;

        // Calculer les coûts et disponibilités
        // Calculer les couts et la disponibilite
        int damageCost = player.getDamageUpgradeCost();
        int speedCost = player.getSpeedUpgradeCost();
        int healthCost = player.getHealthCost();

        boolean canUpgradeDamage = player.getCoins() >= damageCost;
        boolean canUpgradeSpeed = player.getCoins() >= speedCost;
        boolean canBuyHealth = player.getCoins() >= healthCost && player.getHealth() < player.getMaxHealth();

        // ===== AFFICHER UNIQUEMENT LES AMÉLIORATIONS DISPONIBLES =====

        // [U] Dégâts (si achetable)
        if (canUpgradeDamage) {
            upgradeFont.setColor(Color.GREEN);

            // Calculer le bonus en pourcentage (chaque niveau = +10 dégâts sur base 30)
            int currentDamage = player.getDamage();
            int baseDamage = 30;
            int bonusPercent = ((currentDamage - baseDamage) * 100) / baseDamage;
            int nextBonusPercent = bonusPercent + 33; // +10 dégâts = +33%

            upgradeFont.draw(batch,
                "[U]: +" + nextBonusPercent + "% degats - " + damageCost + " coins",
                x, baseY - (lineCount * lineHeight));
            lineCount++;
        }

        // [I] Vitesse (si achetable)
        if (canUpgradeSpeed) {
            upgradeFont.setColor(Color.CYAN);

            // Calculer le bonus en pourcentage (chaque niveau = +20 vitesse sur base 120)
            float currentSpeed = player.getSpeed();
            float baseSpeed = 120f;
            int bonusPercent = (int)(((currentSpeed - baseSpeed) * 100) / baseSpeed);
            int nextBonusPercent = bonusPercent + 17; // +20 vitesse = +17%

            upgradeFont.draw(batch,
                "[I]: +" + nextBonusPercent + "% vitesse - " + speedCost + " coins",
                x, baseY - (lineCount * lineHeight));
            lineCount++;
        }

        // [H] Vie (si achetable ET pas pleine)
        if (canBuyHealth) {
            upgradeFont.setColor(Color.RED);

            upgradeFont.draw(batch,
                "[H]: +30 PV - " + healthCost + " coins",
                x, baseY - (lineCount * lineHeight));
            lineCount++;
        }

        // Message si rien n'est disponible
        if (!canUpgradeDamage && !canUpgradeSpeed && !canBuyHealth) {
            upgradeFont.setColor(Color.GRAY);
            upgradeFont.draw(batch,
                "Farmez des coins !",
                x + 40, baseY);
        }
    }

    /**
     * Barre de vie en haut à gauche
     */
    private void drawHealthBar(Player player) {
        float barWidth = 200;
        float barHeight = 20;
        float x = 10;
        float y = Gdx.graphics.getHeight() - 30;
        // Pourcentage de vie pour la largeur de barre
        float healthPercent = (float) player.getHealth() / player.getMaxHealth();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fond noir
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Barre de vie colorée
        if (healthPercent > 0.6f) {
            shapeRenderer.setColor(Color.GREEN);
        } else if (healthPercent > 0.3f) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.RED);
        }
        shapeRenderer.rect(x + 2, y + 2, (barWidth - 4) * healthPercent, barHeight - 4);

        shapeRenderer.end();

        // Texte de la vie
        batch.begin();
        font.draw(batch, player.getHealth() + "/" + player.getMaxHealth(), x + barWidth + 10, y + 15);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        upgradeFont.dispose();
        shapeRenderer.dispose();
    }
}
