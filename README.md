# Projet de Moteur de Jeu en Java avec libGDX

## Description

Ce projet est un moteur de jeu extensible développé en Java à l’aide de la bibliothèque libGDX. Il est conçu pour fournir une base modulaire et claire permettant de développer facilement des jeux multiplateformes. L’architecture sépare la logique principale du moteur des plateformes d’exécution, ce qui facilite la maintenance et l’extension du projet. Un jeu exemple est inclus afin de démontrer le fonctionnement du moteur et son utilisation.

## Platforms

Le projet est organisé en plusieurs modules :

- **core** : module principal contenant la logique partagée du moteur de jeu.
- **lwjgl3** : module desktop utilisant LWJGL3 pour l’exécution sur ordinateur.

## Prérequis

Avant de compiler et d’exécuter le projet, assurez-vous d’avoir :
- Java Development Kit (JDK) version 17 ou supérieure
- Gradle ou le wrapper Gradle inclus (`gradlew`)

## Structure du Projet

├── core                # Code source principal du moteur de jeu
├── lwjgl3              # Code source pour la plateforme desktop
├── concreate_game      # Exemple de jeu utilisant le moteur
├── build.gradle        # Configuration Gradle
├── settings.gradle     # Configuration Gradle
├── gradlew             # Gradle Wrapper (Linux / Mac)
├── gradlew.bat         # Gradle Wrapper (Windows)
└── README.md           # Documentation

## Instructions d’Exécution

### Étape 1 : Récupération du projet

Option 1 : Cloner le dépôt

git clone https://github.com/MehdiBenzada/game-engine-pcoo  
cd game-engine-pcoo

Option 2 : Téléchargement direct

Le projet peut également être téléchargé sous forme d’archive ZIP depuis GitHub. Après extraction, placez-vous dans le dossier du projet.

## Étape 2 : Configuration du répertoire de travail

Après avoir récupéré le projet, le répertoire de travail doit pointer vers le dossier `concreate_game`.

- IntelliJ IDEA : modifier le Working Directory vers `game-engine-pcoo/concreate_game`
- Eclipse : modifier le Working Directory dans l’onglet Arguments
- Ligne de commande :
cd concreate_game

## Étape 3 : Exécution du jeu exemple

Linux / Mac :
./run.sh

Windows :
run.bat

Vous pouvez créer votre propre jeu en ajoutant vos fichiers dans le dossier `concreate_game`.

## Gradle

Le projet utilise Gradle pour la gestion des dépendances et l’automatisation des tâches.

Tâches utiles :
- build
- clean
- lwjgl3:run
- lwjgl3:jar
- test
- --refresh-dependencies

## Licence

Ce projet est distribué sous licence MIT. Consultez le fichier LICENSE pour plus de détails.
