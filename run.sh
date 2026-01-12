#!/bin/bash

# Compiler si nécessaire
if [ ! -d "build" ]; then
  echo "Compilation en cours..."
  ./gradlew build
fi

# Exécuter le jeu
./gradlew lwjgl3:run
