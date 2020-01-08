# Rapport: Traitement d'image sous android


Foobar is a Python library for dealing with word pluralization.

## Introduction

L'application contient le travail du premier semestre. Le projet est disponible sur mon
[Gitbub](https://github.com/Sebastian-Pages/Traitement-d-image-Android).
Les tests ont été effectué sur un google pixel avec la version 9 (Pie) d’android (Pixel API 28 sur
Android Studio).
Les tests sur appareil physique ne fonctionne pas encore. Il semblerais que l'architecture du Pixel 
ne soit pas compatible. <br />
`
01/08 21:01:20: Launching 'MainActivity' on Google Pixel.
Installation did not succeed.
The application could not be installed: INSTALL_FAILED_NO_MATCHING_ABIS
Installation failed due to: 'null'
`
#### Interface
Chaque bouton "TP" ouvre un menu déroulant avec les fonctions du TP. Le bouton "Reset" annule les
modifications réalisé. Il est possible de saisir du texte pour choisir la taille du filtre de
convolution par exemple.<br />

Le projet contient 3 image numéroté pour tester les différent effet. Pour changer d'image, il suffit
de changer le numéro de l'image dans le fichier **activity_main.xml** à la ligne : `android:src="@drawable/test3"`

## 1 Algorithmes de Grisement
Dans le premier TP, deux version d'un algorithme de grisement ont été réalisé. Une première version
toGray() utilisant la méthode getPixel() qui récupère les données d'un pixel. Puis une deuxième
version toGray2() utilisant la méthode getPixels(). Pour mettre en évidence l’utilité de la deuxième
version, nous avons mesurer leurs performance à l’aide de l’outil de profilage.

|                   | ToGray()      |ToGray2() | 
| :-----------:     |:-------------:| :-----:  | 
| Temps (µs)        | 84,425        | 2,700    | 

## 2 Espaces de couleur
Dans le deuxième TP, nous avons différente manière de représenter les couleur. RBG et HSV. 
Pour mettre cela en pratique, nous avons écrit les méthode suivantes.<br />

`colorize()` : change la teinte d'une image. En convertissant les couleurs RGB en
HSV, il suffit de changer la teinte qui varie entre 0 et 360.

`keepRed()` : grise les pixel dont la teinte et n'est pas autour du rouge.

## 3 Contraste
Dans le troisième TP nous avons implémenté différentes façon de changer le contraste. 
La première manière consiste à resserrer l’histogramme. Les méthode implémenté: <br />

`contrasteExtensionLineaire()`: étire chaque canal de couleur de 0 à 255 . 
Pour réduire le nombre de calcul, on calcule pour chaque valeur, sa nouvelle valeur. 
L’information est stockée dans une LUT. Le calcul est fait 256 fois au lieu de le faire pour chaque pixel.
Cette première version peux augmenter le contraste différemment pour chaque canal et donc le résultat n’est plus réaliste.
 
`hsvContraste()`: étire les valeurs des pixels en couleur HSV. Ainsi, la proportion des couleurs ne
change pas.

`displayHistogramme()`: affiche l'histogramme de l'image.

`egalisationHistogramme()`: L'extension linéaire no fonctionne pas si l'image contient en pixel blanc 
et un pixel noir car déjà étiré au maximum. L'égalisation vise à équilibre la répartition des 
couleurs.

## 4 Renderscript
RenderScript permet de faire des calculs en parrallèle. Nous avons optimiser certaines fonctions:<br />
`toGrayRS`: grise les pixels.

|                   | ToGray2()      |ToGrayRS() | 
| :-----------:     |:-------------:| :-----:  | 
| Temps (µs)        | 2,700       | 0 | 
Je n'ai pas réussi à mesurer le temps d'éxécution de la version Renderscript avec l'outil de profilage. 
La recherche ne permet pas de trouver la fonction.
`invertRS`: inverse les couleurs.




## 5 Convolution
Dans le dernier TP, nous avons implémenté des fonctions de convolution pour réaliser des calculs sur
un pixel en prenant en compte ses voisins. Les filtres mis en place sont:<br />
`filtreMoyeneur3x3()` : chaque pixel vaut la moyenne du pixel et de ses voisins alentours.
`filtrePrewitt3x3()` : permet de faire ressortir les contours. Cette version est horizontal.
`filtreSobel3x3()` : Comme prexitt avec un pondération différente.
`filtreMoyenneur()` : Cette version prend en paramètre la taille du filtre. (metre un entier impair
dans la zone text) 

## Limites & Bug

L'application ne contient pas les versions renderscript de toutes les fonctions. 
Le language est difficile et j'ai passé plus de temps à faire fonctionner renderscript sur
mon ordinateur.

egalisationHistogramme(): le résultat n'est pas celui souhaité.
filtreMoyenneur(): fait planter l'application.

## Exemples
 