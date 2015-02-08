# qualia
exercise for rss parser

Samedi 3h : 
Etudes des solutions avec Jackson http://demeranville.com/deserialising-json-or-xml-to-a-map-using-java/
Ajout des librairies via Maven (voir post http://stackoverflow.com/questions/27144781/why-do-i-get-unable-to-invoke-factory-method-in-class-layout-xmllayout)
Toujours un bug au niveau de la compilation (indépendant du code)
Changement de solution => parser simple via org.w3c.dom

Samedi 1h : premier commit pour afficher quelques élements dans une listview (titres, lien)
Samedi 1h : ajout d'une nouvelle activité pour visualiser le contenu de chaque flux (stockage dans les préférences)
Samedi 3h : création de la base de données (2 tables), création des daos associées (pas de génération automatique), requête permettant de lier les deux tables par un id commun, essais dans le code


Dimanche : 4/5h

- gestion des erreurs
- alignement des variables

- Créer une activité (nouveau main) qui présente les liens rss déjà entrés dans la bdd
- Mise en place des requêtes et entrées bdd pour chaque activité
- Pour les nouvelles entrées : parser le xml et stocker les données
- Gérer la perte/reprise de connexion internet


