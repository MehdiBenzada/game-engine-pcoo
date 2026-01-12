# Projet de Moteur de Jeu en Java avec LibGDX

## Description
Ce projet est un moteur de jeu extensible développé en Java à l'aide de la bibliothèque LibGDX. Il est conçu pour fournir une base modulaire et claire permettant de développer facilement des jeux multiplateformes. L'architecture sépare la logique principale du moteur des plateformes d'exécution, ce qui facilite la maintenance et l'extension du projet. Un jeu exemple est inclus afin de démontrer le fonctionnement du moteur et son utilisation.

## Plateformes
Le projet est organisé en plusieurs modules :
- **core** : module principal contenant la logique partagée du moteur de jeu.
- **lwjgl3** : module desktop utilisant LWJGL3 pour l'exécution sur ordinateur.

## Prérequis
Avant de compiler et d'exécuter le projet, assurez-vous d'avoir :
- Java Development Kit (JDK) version 17 ou supérieure
- Gradle ou le wrapper Gradle inclus (`gradlew`)

## Structure du Projet
```
├── .gradle/            # Fichiers de cache Gradle
├── .idea/              # Configuration IntelliJ IDEA
├── assets/             # Ressources du jeu (images, sons, cartes Tiled, etc.)
├── core/               # Code source principal du moteur de jeu
├── gradle/             # Wrapper Gradle
├── lwjgl3/             # Code source pour la plateforme desktop
├── build.gradle        # Configuration Gradle principale
├── nativeimage.gradle  # Configuration pour GraalVM Native Image
├── .editorconfig       # Configuration de l'éditeur
├── .gitattributes      # Attributs Git
├── .gitignore          # Fichiers ignorés par Git
├── build.gradle        # Configuration Gradle du projet racine
├── gradle.properties   # Propriétés Gradle
├── gradlew             # Gradle Wrapper (Linux / Mac)
├── gradlew.bat         # Gradle Wrapper (Windows)
├── LICENSE             # Licence du projet
└── README.md           # Documentation
```

## Instructions d'Exécution

### Étape 1 : Récupération du projet

#### Option 1 : Cloner le dépôt
```bash
git clone https://github.com/MehdiBenzada/game-engine-pcoo  
cd game-engine-pcoo
```

#### Option 2 : Téléchargement direct
Le projet peut également être téléchargé sous forme d'archive ZIP depuis GitHub. Après extraction, placez-vous dans le dossier du projet.

### Étape 2 : Compilation du projet

#### Linux / Mac :
```bash
./gradlew build
```

#### Windows :
```bash
gradlew.bat build
```

### Étape 3 : Exécution du jeu

#### Méthode 1 : Via Gradle

**Linux / Mac :**
```bash
./gradlew lwjgl3:run
```

**Windows :**
```bash
gradlew.bat lwjgl3:run
```

#### Méthode 2 : Via les scripts fournis (si disponibles)

**Linux / Mac :**
```bash
./run.sh
```

**Windows :**
```bash
run.bat
```

### Étape 4 : Personnalisation

Pour créer votre propre jeu, vous pouvez :
- Modifier les ressources dans le dossier `assets/`
- Ajouter vos cartes Tiled dans `assets/`
- Étendre les classes du module `core/` pour implémenter votre logique de jeu

## Gradle
Le projet utilise Gradle pour la gestion des dépendances et l'automatisation des tâches.

### Tâches utiles :
- `./gradlew build` : Compile le projet
- `./gradlew clean` : Nettoie les fichiers compilés
- `./gradlew lwjgl3:run` : Lance le jeu sur desktop
- `./gradlew lwjgl3:jar` : Crée un JAR exécutable
- `./gradlew test` : Exécute les tests
- `./gradlew --refresh-dependencies` : Rafraîchit les dépendances

## Configuration de l'IDE

### IntelliJ IDEA
Le projet contient déjà la configuration IntelliJ IDEA (dossier `.idea/`). Ouvrez simplement le projet avec IntelliJ IDEA, qui détectera automatiquement la configuration Gradle.

### Eclipse
1. Importez le projet en tant que projet Gradle
2. Eclipse configurera automatiquement les dépendances

## Licence
Ce projet est distribué sous licence MIT. Consultez le fichier LICENSE pour plus de détails.
