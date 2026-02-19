# Émergence

Thomas Schelling, économiste américain, a découvert un modèle simple qui construit une structure à partir d'un état désordonné composé d'unités élémentaires qui suivent une règle très simple, sans la moindre coordination entre elles ou avec un système centralisé.

Le plus étonnant, c'est que la règle ne contient pas, en substance, la propriété de la structure finale, au contraire même !  
Ce modèle a permis de montrer que des regroupements communautaires dans une cité peuvent se produire alors même que les individus n'ont pas cette volonté : c'est à dire qu'ils supportent facilement des voisins de l'autre communauté, à condition d'avoir une proportion minimum de voisins de leur communauté ; et cette proportion peut être très minoritaire, jusqu'à 1/3 environ.
À partir de cette valeur, les simulations montrent que des groupes importants et homogènes de membres d'une même communauté se forment. Si cela paraît évident pour un seuil supérieur à 0.5 traduisant une certaine "xénophobie" et en substance une envie au regroupement, ça l'est beaucoup moins pour les valeurs inférieures (jusqu'à 1/3) qui expriment l'inverse.  

Cette ségrégation non voulue est une propriété émergente, car elle ne se trouve exprimée nulle part dans les règles qui animent le modèle.


Pour plus de précision, un article de "Pour la science" : https://www.cristal.univ-lille.fr/profil/jdelahay/pls:2006:139.pdf  
Pour tester, un simulateur en ligne : https://courses.cs.duke.edu/spring21/compsci308/assign/02_simulation/nifty/mccown-schelling-model-segregation/

À partir du code généré par l'IA (voir le prompt) et corrigé manuellement "à l'arrache" plusieurs exercices sont possibles (sur le modèle uniquement).  
Dans IntelliJ, IL faut importer le projet à partir de sources existantes et bien mettre dans les paramètres, pour le build Grdale, le jdk 17 (il l'installera si nécessaire).

## Échauffement
- implémenter le décompte des groupes afin de permettre de lancer plusieurs simulations pour avoir des statistiques précises (histogrammes) du phénomène de regroupement et à partir de quels seuils le comportement change et si ce changement est brusque (discontinuité).
- faire une relecture de code : refactoring possibles et bugs assurés !

## Étude
- étudier la convergence et sa vitesse vers un état stable. On pourrait convenir d'un état quasi-stable s'il ne reste que quelque insatisfaits ou si leur nombre diminue très lentement. (asymptotiquement vers 0)

## Conception
Modifier le code pour :
- prendre en compte n communautés
- chaque communauté peut avoir son propre seuil de satisfaction
- les règles de satisfaction deviennent :
- des fonctions toujours binaires (satisfait/pas satisfait) mais plus complexes, par ex : au moins 1 étranger, mais pas plus de 80%
- des fonctions non-binaires => un taux de satisfaction. Chaque entité ne bouge que si elle trouve un meilleur taux, mais ne cherche pas le 100%

## Codage
C'est un simulateur donc on devrait optimiser (parallélisation) et faire un système de batch pour lancer des simulations en faisant varier 1 ou plusieurs paramètres sur des milliers de valeurs.

Questions :
- qu'est-ce qu'un test d'intégration dans le contexte d'un code de simulation dont le résultat peut être inattendu ?
- comment s'assurer que le résultat n'est pas un artefact du calcul informatique ?
- le générateur aléatoire peut-il avoir un biais si l'on s'en sert pour des coordonnées ?
