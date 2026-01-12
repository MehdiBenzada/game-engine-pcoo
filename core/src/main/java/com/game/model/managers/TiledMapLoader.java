package com.game.model.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;



import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de chargement et rendu de cartes Tiled.
 *
 * Charge les fichiers .tmx créés avec Tiled Map Editor et permet de les afficher.
 * Peut aussi extraire les obstacles pour les collisions.
 *
 * SpawnPoint et MapTransition ne sont plus utilisés.
 */
public class TiledMapLoader {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    /**
     * Charge une carte Tiled depuis un fichier .tmx.
     */
    public void loadMap(String mapPath) {
        try {
            // Chargement du fichier TMX
            map = new TmxMapLoader().load(mapPath);
            renderer = new OrthogonalTiledMapRenderer(map);

            System.out.println("✓ Carte chargée : " + mapPath);
        } catch (Exception e) {
            System.err.println(" Erreur chargement carte : " + mapPath);
            e.printStackTrace();
        }
    }

    /**
     * Configure la vue de la caméra pour le rendu.
     */
    public void setView(OrthographicCamera camera) {
        if (renderer != null) {
            // Appliquer la camera a la vue de la map
            renderer.setView(camera);
        }
    }

    /**
     * Affiche la carte à l'écran.
     */
    public void render() {
        if (renderer != null) {
            // Rendu de la map
            renderer.render();
        }
    }

    /**
     * Retourne la carte chargée.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Libère les ressources de la carte.
     */
    public void dispose() {
        if (map != null) {
            map.dispose();
        }
    }
}
