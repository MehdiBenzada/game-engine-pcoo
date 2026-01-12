package com.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.model.entities.Collectible;
import com.game.model.entities.Enemy;
import com.game.model.entities.Projectile;
import com.game.model.managers.GameState;
import com.game.model.managers.TiledMapLoader;

/**
 * Gestionnaire de rendu graphique (VIEW)
 */
public class GameRenderer {
    
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TiledMapLoader mapLoader;
    private HUD hud;
    
    private static final float MAP_WIDTH = 720f;
    private static final float MAP_HEIGHT = 720f;
    
    public GameRenderer() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();
        
        batch = new SpriteBatch();
        mapLoader = new TiledMapLoader();
        mapLoader.loadMap("maps/map1/map.tmx");
        hud = new HUD();
        
        System.out.println(" GameRenderer initialisé");
    }
    
    public void render(GameState state) {
        // 1. Map
        mapLoader.setView(camera);
        mapLoader.render();
        
        // 2. Entités
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Objets ramassables
        for (Collectible collectible : state.getCollectibles()) {
            if (collectible.isActive()) collectible.render(batch);
        }
        
        // Ennemis
        for (Enemy enemy : state.getEnemies()) {
            if (enemy.isActive()) enemy.render(batch);
        }
        
        // Projectiles
        for (Projectile projectile : state.getProjectiles()) {
            if (projectile.isActive()) projectile.render(batch);
        }
        
        // Joueur
        if (state.getPlayer() != null && state.getPlayer().isActive()) {
            state.getPlayer().render(batch);
        }
        
        batch.end();
        
        // 3. HUD
        hud.render(state);
    }
    
    public void resize(int width, int height) {
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();
    }
    
    public void dispose() {
        batch.dispose();
        mapLoader.dispose();
        hud.dispose();
    }
    
    public TiledMapLoader getMapLoader() {
        return mapLoader;
    }
    
    public OrthographicCamera getCamera() {
        return camera;
    }
}

