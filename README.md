# PCD - Coding Week

## Introduction

Ce dépôt contient les sources du projet réalisé dans le cadre de la coding week 2022-2023.

Ce projet utilise l'outil Gradle.
Dans un terminal, `./gradlew run` permet de télécharger les dépendances nécessaires (JavaFX, Gson...), lancer la compilation et exécuter le programme. Le script `./gradlew` soit être exécutable. S'il ne l'est pas, la commande `chmod a+x ./gradlew` peut régler le souci.

Il est également possible de lancer l'application à l'aide de l'[exécutable](codingweek-19.jar) par un double clique depuis l'interface graphique, ou bien en tapant la commande `java -jar codingweek-19.jar`

## Membres du groupe

* BOISSEAU Thibault (Thibault.Boisseau@telecomnancy.eu)
* BOURGEON Guillaume (Guillaume.Bourgeon@telecomnancy.eu)
* SANCHEZ Paul (Paul.Sanchez@telecomnancy.eu)
* WANG Caroline (Caroline.Wang@telecomnancy.eu)

## Structure du dépôt

Ce dépôt contient :

* les fichiers et dossiers liés à l'utilisation de Gradle
* les documents de [gestion de projet](GDP/) :
    * le [sujet du projet](GDP/CodingWeek 2022-2023 - Sujet[4122].pdf)
    * le [code PlantUML](GDP/) permettant de générer le [diagramme de classes du modèle](GDP/out/Diag_classes_model/Diagramme de classes model.png) et le [diagramme de classes complet](GDP/out/Diag_classes_complet/Diagramme de classes FlashCards complet.png)
    * les [bilans d'activité par journée](GDP/)
* le [code source](app/src/) de l'application, contenant les [classes java](app/src/main/java/codingweek/), les [vues au format fxml](app/src/main/resources/codingweek/) et les [images](app/src/main/resources/codingweek/images/) utilisées dans l'application (tirées de ce [site](https://www.sadnuggie.com/))
* des [jeux de piles](json_example/) à importer dans l'application pour la tester
* les exécutables rendus après chaque journée
* le [rendu final exécutable](codingweek-19.jar)

## Présentation de l'application

L'objectif de cette application de flashcards est d'aider à l'apprentissage d'un sujet grâce à la mémorisation par répétition.

### Fonctionnalités de l'application
* le mode création permet de créer une pile, une carte et de l'éditer et de la supprimer
* il est possible d'importer ou d'exporter des piles de cartes
* le mode apprentissage permet de travailler une carte de piles, de choisir les paramètres de proposition des cartes
* une page permet d'afficher les statistiques du joueur
* il est possible d'inverser l'affichage des questions et des réponses
* un mode multijoueurs à 3 est également accessible depuis le menu de la page d'affichage d'une pile

### Utilisation de l'application

* l'importation des piles de cartes se fait depuis le menu
* la création des piles et des cartes se fait respectivement depuis la page d'accueil, page de pile et depuis la page de pile, page de carte. Il n'est pas possible d'ajouter deux piles ou cartes du même nom, ni de laisser des champs de textes vides. 
* le mode apprentissage est accessible depuis la page d'une pile. Il propose différents algorithmes de proposition des cartes, que l'on peut sélectionner depuis le menu d'une pile.
* la page des statistiques propose des informations globales, et par pile

## Vidéo de présentation du projet

Une [vidéo de présentation](https://www.youtube.com/watch?v=0NRWWhM-4AY) du projet a été mise en ligne.
