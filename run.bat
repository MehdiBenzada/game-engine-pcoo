@echo off

:: Compiler si nécessaire
if not exist "build" (
  echo Compilation en cours...
  gradlew.bat build
)
:: Exécuter le jeu
gradlew.bat lwjgl3:run
