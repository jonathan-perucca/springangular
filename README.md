## Qu'est-ce que c'est ?

- Application Todo list avec archi backend/frontend moderne (frontend light) basé sur une Api rest (json)

## À quoi ça sert ?

- CRUD sur todo list (ajout de todo, suppression, update(checked/unchecked), list)

## Comment travailler dessus ?

### De quoi j'ai besoin ?

- java/jdk d'installé (fonctionne à partir de jdk 6 (compliant 7/8))
- maven
- node (pour la partie front)
- IDE indépendant (intellij/eclipse ou autre ...)
- bower (npm install)

### Comment je m'y prends ?

#### Pour lancer l'application avec backend et frontend

- Dans un terminal, aller à la racine du projet (où se trouve le pom.xml)
- Lancer la commande : bower install (pour installer les lib front)
- Lancer la commande : mvn spring-boot:run
- spring boot va monter automatiquement un tomcat embarqué, avec le déploiement automatique de l'application dessus, ainsi qu'une base de donnée en mémoire (hsqldb)

#### Pour lancer les tests cotés back

- Dans un terminal, aller à la racine du projet (où se trouve le pom.xml)
- 1a) Lancer la commande : mvn test
- 1b) Vous pouvez aussi lancer les tests à la main en allant dans les sources et en faisant "Run <NomDuTest>" dans le menu contextuel de votre IDE

#### Pour lancer les tests cotés front

- Dans un terminal, aller à la racine du projet (où se trouve le pom.xml)
- Lancer d'abord l'application avec mvn spring-boot:run
- Lancer ensuite dans un autre terminal la commande (toujours à la racine du projet) : protractor protractor-conf.js


### Comment est-ce organisé ?

- Deux parties du projet distingues le front du back
- src/main/java (et src/test/java) correspondent à l'appli/tests backend
- src/main/resources/static (et src/test/js) correspond à l'appli/tests frontend

#### Backend

- com.jperucca.springangular.config est le package de bean de configuration spring pour le back
- com.jperucca.springangular.domain est le package d'objet du domain (Todo)
- com.jperucca.springangular.repository est le package "ORM" pour les opérations CRUD en DB
- com.jperucca.springangular.web est le package de la partie "Web"(ou controller) (point d'entré des API)
- com.jperucca.springangular.web.dto est le package des objets de transfer (DTO)
- com.jperucca.springangular.web.exception est le package des exceptions fonctionnelles
- com.jperucca.springangular.web.helper est un package utilitaire sur la partie web (controller)


#### Frontend

- src/main/resources/static/app correspond à l'application "todo" avec controller/services angular (archi light)


## Quels choix ont été faits ?

### Backend

- Full stack spring pour avoir une application cohérente

#### Couche web

- Spring MVC pour l'exposition d'api rest(json)
- Gestion des exceptions (ExceptionHandler) avec spring mvc
- Dozer pour le mapping de DTO <-> POJO (séparation des objets provenant de l'exterieur du backend / des objets du domain)
- Dependency Injection (DI) avec @Autowired de Spring core (IOC)

#### Couche repository (mix persistence/service)

- Un cas plus réel aurait été de mettre en place une couche service entre la couche web et repository,
pour cette application "light", nous pouvons nous en passer.
- Spring Data JPA utilisé par extension de l'interface CrudRepository (donne accès aux méthodes CRUD sur la DB pour les Todo)

#### Couche domain

- Annotation Hibernate/JPA sur l'entity pour l'ORM
- Builder pattern pour construction "fluent" d'un objet Todo dans l'appli et dans les tests

#### Couche infrastructure

- maven (pour gestion des dépendences, plugin de build, possibilité de modulariser l'application)
- Spring boot (autoconfiguration permet de ne pas avoir à rajouter de configuration de tomcat, de la DB ... dans un premier temps,
il va s'appuyer sur les classes présentes dans le classloader (monté grâce aux dépendances présentes dans le pom.xml), et va charger 
une configuration par défaut si aucune n'est trouvée)
- protractor

## Comment l'améliorer ?

- Ajouter un peu plus de fonctionnel pour avoir la pertinence d'ajouter une couche service (avec aspect transactionnel de spring)
- Améliorer la partie angularjs (design applicatif pas forcément standard, à retravailler)
- Utiliser les "partial views" pour angularjs
- Utiliser ngRoute pour la définition des routes angular
- Ajouter des tests unitaires sur les controllers/services/factories d'angular
- Ajouter un profil "INT"/"PROD" sur le backend avec deux configurations pour le tomcat et DB pour switcher en fonction de l'environnement où se trouve l'application 
(simple argument complémentaire à ajouter à mvn spring-boot:run --spring.profiles.active=dev par exemple).
- L'implémentation du RestController peut être refactoré avec une annotation @RepositoryRestResource(collectionResourceRel = "todos", path = "todo")

## Liste des frameworks utilisés

- spring core, spring boot, spring starter module, spring mvc, spring data, spring profiles
- junit, restassured, spring test, hamcrest
- dozer, java config, guava
- angularjs, jasmine, protractor, bower
- maven
- hsqldb, embedded tomcat


## Bibliographie

- [Spring Boot Github]
- [Spring Boot Documentation]
- [RestAssured Github]
- [AngularJs internal patterns Github]
- [Guava Github]
- [Junit Github]
- [Maven Github]
- [Hamcrest Github]
- [Dozer Github]
- [Jasmine Github]
- [Protractor Github]
- [Bower Github]

[Spring Boot Github]: https://github.com/spring-projects/spring-boot/
[Spring Boot Documentation]: http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/
[RestAssured Github]: https://github.com/jayway/rest-assured/
[AngularJs internal patterns Github]: https://github.com/mgechev/angularjs-in-patterns/
[Guava Github]: https://github.com/google/guava
[Junit Github]: https://github.com/junit-team/junit
[Maven Github]: https://github.com/apache/maven
[Hamcrest Github]: https://github.com/hamcrest/JavaHamcrest
[Dozer Github]: https://github.com/DozerMapper/dozer
[Jasmine Github]: https://github.com/jasmine/jasmine
[Protractor Github]: https://github.com/angular/protractor
[Bower Github]: https://github.com/bower/bower