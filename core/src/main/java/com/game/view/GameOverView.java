package com.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.game.Main;
import com.game.model.managers.GameState;

/**
 * Vue de l'écran Game Over 
 *
 * Affiche  :
 * - Titre GAME OVER
 * - Niveau atteint
 * - Coins collectés
 * - XP totale (= score)
 * - Boutons REJOUER / MENU
 */
public class GameOverView implements Screen {

    private Main game;
    private SpriteBatch batch;

    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private BitmapFont statsFont;
    private BitmapFont buttonFont;
    private BitmapFont hintFont;

    private Rectangle retryButton;
    private Rectangle menuButton;

    private boolean isRetryHovered;
    private boolean isMenuHovered;

    // Statistiques finales
    private int finalCoins;
    private int finalLevel;
    private int finalExp;

    public GameOverView(Main game, GameState finalState) {
        this.game = game;
        batch = new SpriteBatch();

        // ===== CRÉER LES FONTS =====

        // Titre "GAME OVER"
        titleFont = new BitmapFont();
        titleFont.getData().setScale(4.5f);
        titleFont.setColor(Color.RED);

        // Sous-titre
        subtitleFont = new BitmapFont();
        subtitleFont.getData().setScale(1.8f);
        subtitleFont.setColor(Color.YELLOW);

        // Stats
        statsFont = new BitmapFont();
        statsFont.getData().setScale(1.6f);
        statsFont.setColor(Color.WHITE);

        // Boutons
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2.2f);

        // Hints
        hintFont = new BitmapFont();
        hintFont.getData().setScale(1.1f);
        hintFont.setColor(Color.GRAY);

        // ===== RÉCUPÉRER LES STATISTIQUES =====

        if (finalState != null && finalState.getPlayer() != null) {
            finalCoins = finalState.getPlayer().getCoins();
            finalLevel = finalState.getPlayer().getLevel();
            finalExp = finalState.getPlayer().getExperience();
        } else {
            finalCoins = 0;
            finalLevel = 1;
            finalExp = 0;
        }

        // ===== DÉFINIR LES BOUTONS =====

        float buttonWidth = 280;
        float buttonHeight = 75;
        float centerX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;

        retryButton = new Rectangle(
            centerX,
            Gdx.graphics.getHeight() / 2 - 80,
            buttonWidth, buttonHeight
        );

        menuButton = new Rectangle(
            centerX,
            Gdx.graphics.getHeight() / 2 - 180,
            buttonWidth, buttonHeight
        );

        isRetryHovered = false;
        isMenuHovered = false;

        // ===== LOG CONSOLE =====

        // Relance une partie sans repasser par le menu
        System.out.println("========================================");
        System.out.println(" GAME OVER");
        // Retour a l'ecran de menu
        System.out.println("========================================");
        System.out.println(" STATISTIQUES FINALES :");
        System.out.println("   • Niveau atteint   : " + finalLevel);
        System.out.println("   • Coins collectés  : " + finalCoins);
        System.out.println("   • XP totale        : " + finalExp);
        System.out.println("========================================");
    }

    @Override
    public void render(float delta) {
        // Fond rouge sombre
        Gdx.gl.glClearColor(0.15f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Vérifier survol des boutons
        updateHoverStates();

        // Dessiner l'UI
        batch.begin();

        drawTitle();
        drawSeparator(Gdx.graphics.getHeight() / 2 + 200);
        drawSubtitle();
        drawStats();
        drawSeparator(Gdx.graphics.getHeight() / 2 - 20);
        drawButtons();
        drawHints();

        batch.end();

        // Gérer les entrées
        handleInput();
    }

    /**
     * Met à jour l'état de survol des boutons
     */
    private void updateHoverStates() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        float mouseY = Gdx.graphics.getHeight() - mousePos.y;

        // Teste si la souris est sur un bouton
        isRetryHovered = retryButton.contains(mousePos.x, mouseY);
        isMenuHovered = menuButton.contains(mousePos.x, mouseY);
    }

    /**
     * Dessine le titre "GAME OVER"
     */
    private void drawTitle() {
        titleFont.setColor(Color.RED);
        String title = "GAME OVER";

        float titleX = Gdx.graphics.getWidth() / 2 - 270;
        float titleY = Gdx.graphics.getHeight() / 2 + 270;

        titleFont.draw(batch, title, titleX, titleY);
    }

    /**
     * Dessine une ligne de séparation
     */
    private void drawSeparator(float y) {
        statsFont.setColor(Color.DARK_GRAY);
        String separator = "═══════════════════════════════";

        float sepX = Gdx.graphics.getWidth() / 2 - 280;
        statsFont.draw(batch, separator, sepX, y);
    }

    /**
     * Dessine le sous-titre
     */
    private void drawSubtitle() {
        subtitleFont.setColor(Color.YELLOW);
        String subtitle = "Statistiques Finales";

        float subX = Gdx.graphics.getWidth() / 2 - 150;
        float subY = Gdx.graphics.getHeight() / 2 + 160;

        subtitleFont.draw(batch, subtitle, subX, subY);
    }

    /**
     * Dessine les statistiques (niveau, coins, XP)
     */
    private void drawStats() {
        float baseX = Gdx.graphics.getWidth() / 2 - 130;
        float baseY = Gdx.graphics.getHeight() / 2 + 100;
        float lineHeight = 50;

        // Niveau atteint
        statsFont.setColor(Color.WHITE);
        statsFont.draw(batch,
            "Niveau atteint   : " + finalLevel,
            baseX, baseY);

        // Coins collectés
        statsFont.setColor(Color.GOLD);
        statsFont.draw(batch,
            "Coins collectés  : " + finalCoins,
            baseX, baseY - lineHeight);

        // XP totale (= score)
        statsFont.setColor(Color.CYAN);
        statsFont.draw(batch,
            "Experience : " + finalExp + " XP",
            baseX, baseY - lineHeight * 2);
    }

    /**
     * Dessine les boutons REJOUER et MENU
     */
    private void drawButtons() {
        // Bouton REJOUER
        if (isRetryHovered) {
            buttonFont.setColor(Color.GREEN);
            buttonFont.draw(batch, ">>> REJOUER <<<",
                retryButton.x + 20,
                retryButton.y + 50);
        } else {
            buttonFont.setColor(Color.LIME);
            buttonFont.draw(batch, "[ REJOUER ]",
                retryButton.x + 45,
                retryButton.y + 50);
        }

        // Bouton MENU
        if (isMenuHovered) {
            buttonFont.setColor(Color.YELLOW);
            buttonFont.draw(batch, ">>> MENU <<<",
                menuButton.x + 50,
                menuButton.y + 50);
        } else {
            buttonFont.setColor(Color.WHITE);
            buttonFont.draw(batch, "[ MENU ]",
                menuButton.x + 75,
                menuButton.y + 50);
        }
    }

    /**
     * Dessine les instructions
     */
    private void drawHints() {
        hintFont.setColor(Color.GRAY);

        String hint = "ENTER : Rejouer  |  ESC : Menu";
        float hintX = Gdx.graphics.getWidth() / 2 - 180;
        float hintY = 60;

        hintFont.draw(batch, hint, hintX, hintY);
    }

    /**
     * Gère les entrées utilisateur
     */
    private void handleInput() {
        // Clic souris
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            float touchY = Gdx.graphics.getHeight() - touchPos.y;

            // Selection via clic
            if (retryButton.contains(touchPos.x, touchY)) {
                retry();
            } else if (menuButton.contains(touchPos.x, touchY)) {
                goToMenu();
            }
        }

        // Touches clavier
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            retry();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            goToMenu();
        }
    }

    /**
     * Relance une nouvelle partie
     */
    private void retry() {
        System.out.println("========================================");
        System.out.println("NOUVELLE PARTIE");
        System.out.println("========================================");
        game.setScreen(new GameView(game));
        dispose();
    }

    /**
     * Retourne au menu principal
     */
    private void goToMenu() {
        System.out.println("========================================");
        System.out.println(" RETOUR AU MENU");
        System.out.println("========================================");
        game.setScreen(new MenuView(game));
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        titleFont.dispose();
        subtitleFont.dispose();
        statsFont.dispose();
        buttonFont.dispose();
        hintFont.dispose();
        System.out.println("GameOverView disposed");
    }

    // Méthodes Screen non utilisées
    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
