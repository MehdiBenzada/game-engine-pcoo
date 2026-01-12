# Game Engine (libGDX) - Zombie Survival demo

## Description
Ce projet est un jeu libGDX (desktop) avec une structure type "game engine" : menu, boucle de jeu, gestion d''entites, vagues d''ennemis, et ecran Game Over. Le module `core` contient la logique principale et le module `lwjgl3` sert de lanceur desktop.

## Platforms
- `core` : logique du jeu et rendu partage.
- `lwjgl3` : application desktop (LWJGL3).

## Prerequis
- JDK 17 ou plus.
- Gradle (ou le wrapper `gradlew`).

## Structure du projet
```
Game_Engine-main/
|-- assets/              # Assets du jeu (images, maps, data)
|-- core/                # Code principal du jeu
|-- lwjgl3/              # Lanceur desktop
|-- gradle/              # Fichiers Gradle
|-- build.gradle         # Config Gradle racine
|-- settings.gradle      # Modules charges
|-- gradlew              # Wrapper Gradle (Linux/Mac)
|-- gradlew.bat          # Wrapper Gradle (Windows)
|-- run.sh               # Script de lancement (Linux/Mac)
|-- run.bat              # Script de lancement (Windows)
|-- README.md            # Ce fichier
```

## Execution
Option 1 : Gradle directement

Linux/Mac :
```bash
./gradlew lwjgl3:run
```

Windows :
```bat
gradlew.bat lwjgl3:run
```

Option 2 : Scripts fournis

Linux/Mac :
```bash
./run.sh
```

Windows :
```bat
run.bat
```

## Commandes en jeu
- WASD / Fleches : deplacer
- Clic gauche : tirer
- P : pause
- ESC : retour menu / quitter
- ENTER / SPACE : demarrer / rejouer

## Gradle
Taches utiles :
- `build` : compile les modules.
- `clean` : supprime les sorties de build.
- `lwjgl3:run` : lance le jeu desktop.
- `lwjgl3:jar` : genere un jar executable.
- `generateAssetList` : regenere `assets/assets.txt`.

## Licence
Ce projet est sous licence MIT. Voir `LICENSE`.
