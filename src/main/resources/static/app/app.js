(function(){
    angular.module("todoApp.controllers", []);
    angular.module("todoApp.services", []);
    angular.module("todoApp", ["ngResource", "todoApp.controllers", "todoApp.services"]);
}(angular));