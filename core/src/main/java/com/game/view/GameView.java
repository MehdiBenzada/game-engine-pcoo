package com.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.game.Main;
import com.game.controller.GameController;

/**
 * Vue de l'écran de jeu principal.
 * Responsabilité : Affichage du jeu + délégation au contrôleur
 * Gestion des inputs système : P (pause) et ESC (retour menu)
 */
public class GameView implements Screen {

    private Main game;
    private GameController gameController;

    private boolean isPaused;

    public GameView(Main game) {
        this.game = game;
        // Controleur du jeu (logique et rendu)
        this.gameController = new GameController();
        this.isPaused = false;

        System.out.println("========================================");
        System.out.println(" DÉBUT DE LA PARTIE");
        System.out.println("========================================");
    }

    @Override
    public void render(float delta) {
        // Fond noir
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gérer les inputs système
        // Gestion des touches systeme
        handleSystemInputs();

        // Mettre à jour et afficher le jeu (si pas en pause)
        if (!isPaused) {
            // Mise a jour logique + rendu
            gameController.update(delta);
            gameController.render();

            // Vérifier si le joueur est mort
            checkGameOver();
        } else {
            // En pause : continuer à afficher mais ne pas update
            gameController.render();
        }
    }

    /** Gère les inputs système (pause et quitter)
     */
    private void handleSystemInputs() {
        // Touche P : Pause / Reprendre
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            // Inverse l'etat de pause
            isPaused = !isPaused;
            if (isPaused) {
                System.out.println("JEU EN PAUSE");
            } else {
                System.out.println(" JEU REPRIS");
            }
        }

        // Retour au menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("========================================");
            System.out.println("RETOUR AU MENU");
            System.out.println("========================================");

            // Retourner au menu principal
            game.setScreen(new MenuView(game));
            dispose();
        }
    }

    private void checkGameOver() {
        // Vérifier si le joueur est mort
        // Verifie l'etat du joueur
        if (gameController.getGameState() != null &&
            gameController.getGameState().getPlayer() != null) {

            if (!gameController.getGameState().getPlayer().isActive()) {
                System.out.println("========================================");
                System.out.println("💀 GAME OVER");
                System.out.println("========================================");

                // Passer à la vue Game Over
                game.setScreen(new GameOverView(game, gameController.getGameState()));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (gameController != null) {
            gameController.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (gameController != null) {
            gameController.dispose();
        }
        System.out.println("✓ GameView disposed");
    }

    // Méthodes Screen non utilisées
    @Override
    public void show() {}

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {}
}
