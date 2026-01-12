package com.game;

import com.badlogic.gdx.Game;
import com.game.view.MenuView;

/**
 * Classe principale du jeu - Point d'entrée LibGDX
 *  Main est le point d'entrée technique 
 *  Lance la vue initiale (MenuView)
 */
public class Main extends Game {

    @Override
    public void create() {
        // Point d'entree du jeu
        System.out.println("==========================================");
        System.out.println("               GAME ENGINE ");
        System.out.println("==========================================");
        System.out.println("Commandes:");
        System.out.println("  • WASD / Flèches : Déplacer");
        System.out.println("  • CLIC GAUCHE : Tirer");
        System.out.println("  • ESC : Quitter");
        System.out.println("==========================================");
        
        // Afficher la vue du menu au démarrage
        setScreen(new MenuView(this));
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
}
