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

/**
 * Vue de l'écran de menu principal.
 * Responsabilité : Affichage du menu uniquement
 * Pas de logique métier
 */
public class MenuView implements Screen {
    
    private Main game;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private BitmapFont infoFont;
    
    private Rectangle playButton;
    private boolean isPlayHovered;
    
    public MenuView(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        // Créer les fonts avec différentes tailles
        titleFont = new BitmapFont();
        titleFont.getData().setScale(3.5f);
        titleFont.setColor(Color.CYAN);
        
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2.5f);
        
        infoFont = new BitmapFont();
        infoFont.getData().setScale(1.5f);
        infoFont.setColor(Color.LIGHT_GRAY);
        
        // Définir la zone du bouton Play (centre de l'écran)
        float buttonWidth = 250;
        float buttonHeight = 80;
        playButton = new Rectangle(
            Gdx.graphics.getWidth() / 2 - buttonWidth / 2,
            Gdx.graphics.getHeight() / 2 - buttonHeight / 2,
            buttonWidth, buttonHeight
        );
        
        isPlayHovered = false;
        
        System.out.println("Vue de menu initialisée");
    }
    
    @Override
    public void render(float delta) {
        // Fond noir
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Vérifier survol du bouton
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        float mouseY = Gdx.graphics.getHeight() - mousePos.y;
        // Detecter le survol du bouton
        isPlayHovered = playButton.contains(mousePos.x, mouseY);
        
        batch.begin();
        
        // Titre du jeu
        titleFont.setColor(Color.CYAN);
        String title = "  GAME ENGINE";
        titleFont.draw(batch, title, 
            Gdx.graphics.getWidth() / 2 - 280, 
            Gdx.graphics.getHeight() / 2 + 200);
        
        // Sous-titre
        infoFont.setColor(Color.YELLOW);
        infoFont.getData().setScale(1.2f);
        String subtitle = "Zombie  Survival";
        infoFont.draw(batch, subtitle,
            Gdx.graphics.getWidth() / 2 - 100,
            Gdx.graphics.getHeight() / 2 + 150);
        
        // Bouton Play
        if (isPlayHovered) {
            buttonFont.setColor(Color.GREEN);
            String highlight = ">>> PLAY <<<";
            buttonFont.draw(batch, highlight,
                playButton.x + 20,
                playButton.y + 55);
        } else {
            buttonFont.setColor(Color.WHITE);
            String playText = "[ PLAY ]";
            buttonFont.draw(batch, playText,
                playButton.x + 50,
                playButton.y + 55);
        }
        
        // Instructions
        infoFont.setColor(Color.LIGHT_GRAY);
        infoFont.getData().setScale(1f);
        int instructionY = (int)(playButton.y - 80);
        
        infoFont.draw(batch, "Commandes :",
            Gdx.graphics.getWidth() / 2 - 60,
            instructionY);
        
        infoFont.getData().setScale(0.9f);
        infoFont.draw(batch, "WASD / Fleches : Deplacer",
            Gdx.graphics.getWidth() / 2 - 120,
            instructionY - 30);
        
        infoFont.draw(batch, "CLIC GAUCHE : Tirer",
            Gdx.graphics.getWidth() / 2 - 70,
            instructionY - 55);
        
        infoFont.draw(batch, "ESC : Quitter",
            Gdx.graphics.getWidth() / 2 - 60,
            instructionY - 80);
        
        // Message de clic
        if (isPlayHovered) {
            infoFont.setColor(Color.GREEN);
            infoFont.getData().setScale(1.1f);
            infoFont.draw(batch, "Cliquez pour commencer !",
                Gdx.graphics.getWidth() / 2 - 110,
                instructionY - 120);
        }
        
        batch.end();
        
        // Gérer les clics
        handleInput();
    }
    
    private void handleInput() {
        // Clic souris
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            float touchY = Gdx.graphics.getHeight() - touchPos.y;
            
            // Lancer le jeu si clic sur PLAY
            if (playButton.contains(touchPos.x, touchY)) {
                startGame();
            }
        }
        
        // Touche ENTER ou SPACE pour démarrer
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || 
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            startGame();
        }
        
        // ESC pour quitter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
    
    private void startGame() {
        // Basculer vers l'ecran de jeu
        System.out.println("🎮 Lancement du jeu...");
        game.setScreen(new GameView(game));
        dispose();
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        infoFont.dispose();
        System.out.println("✓ MenuView disposed");
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
