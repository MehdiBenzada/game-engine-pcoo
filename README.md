# Moteur de Jeu RPG 2D avec LibGDX

## 📋 Description

Moteur de jeu extensible de type **RPG/Survivor** développé en Java avec LibGDX. Le jeu permet d'enrichir le contenu **sans modifier le code Java**, uniquement via des fichiers de configuration JSON et l'éditeur Tiled.

**Type de jeu** : Jeu de survie où le joueur affronte des vagues d'ennemis et doit survivre le plus longtemps possible.

---

## 🎯 Objectifs Pédagogiques

✅ Programmation Orientée Objet (héritage, polymorphisme, interfaces)  
✅ Design Patterns (Factory, Singleton, MVC)  
✅ Lecture et manipulation de fichiers JSON  
✅ Architecture Modèle-Vue-Contrôleur (MVC)  
✅ Code propre et documenté  

---

## 🏗️ Structure du Projet

```
game-engine-pcoo/
│
├── assets/                    # Ressources du jeu
│   ├── bg/                    # Backgrounds et UI
│   ├── sprite/                # Sprites des entités
│   ├── maps/                  # Cartes Tiled (.tmx)
│   └── data/                  # Configuration JSON
│       ├── player/            # Configuration du joueur
│       ├── enemies/           # Types d'ennemis
│       ├── waves/             # Vagues d'ennemis
│       ├── collectible/       # Objets ramassables
│       └── Projectile/        # Configuration des projectiles
│
├── core/                      # Code source du moteur (MVC)
│   └── src/main/java/com/game/
│       ├── controller/        # Contrôleur
│       ├── model/             # Modèle (entities, factories, managers)
│       ├── view/              # Vue (rendu, UI)
│       └── Main.java          # Point d'entrée
│
└── lwjgl3/                    # Module desktop
```

---

## 🚀 Installation et Exécution

### Prérequis
- Java JDK 17 ou supérieur
- Gradle (ou wrapper `gradlew` inclus)

### Cloner le Projet
```bash
git clone https://github.com/MehdiBenzada/game-engine-pcoo
cd game-engine-pcoo
```

### Compiler
```bash
./gradlew build
```

### Lancer le Jeu
```bash
./gradlew lwjgl3:run
```

---

## 🎮 Contrôles

- **ZQSD** ou **Flèches** : Déplacement
- **Clic gauche** : Tir
- **Échap** :Pause/Quittes

---

## 🛠️ Extensibilité (Sans Modifier le Code)

Le moteur permet d'ajouter du contenu uniquement via des fichiers :

✅ **Ennemis** → `assets/data/enemies/*.json`  
✅ **Vagues** → `assets/data/waves/*.json`  
✅ **Joueur** → `assets/data/player/player.json`  
✅ **Collectibles** → `assets/data/collectible/*.json`  
✅ **Cartes Tiled** → `assets/maps/`  
✅ **Sprites** → `assets/sprite/`  

> 📖 **Pour les détails**, consultez le rapport du projet (PDF).

---

## 🧰 Commandes Gradle

| Commande | Description |
|----------|-------------|
| `./gradlew build` | Compiler le projet |
| `./gradlew clean` | Nettoyer les fichiers compilés |
| `./gradlew lwjgl3:run` | Lancer le jeu |
| `./gradlew lwjgl3:jar` | Créer un JAR exécutable |

---

## 🎯 Design Patterns

- **MVC** : Architecture Modèle-Vue-Contrôleur
- **Factory** : Création d'entités depuis JSON
- **Singleton** : État global du jeu

---

## 👨‍💻 Auteurs

**Mehdi Benzada** - [GitHub](https://github.com/MehdiBenzada)  
**Bilal Meziani** - [GitHub](https://github.com/mezianibilaldev)

---

## 🔗 Liens Utiles

- [Dépôt GitHub](https://github.com/MehdiBenzada/game-engine-pcoo)
- [LibGDX Documentation](https://libgdx.com/wiki/)
- [Tiled Map Editor](https://www.mapeditor.org/)

---

## 📝 Licence

Projet distribué sous licence MIT.
