(function(angular) {
    var TodoFactory = function ($resource) {
        return $resource('/todo/:id', {
            id: '@id'
        }, {
            save: {
                method: 'PUT'
            },
            update: {
                method: 'PUT'
            },
            remove: {
                method: 'DELETE'
            }
        });
    };
    
    TodoFactory.$inject = ['$resource'];
    angular.module('todoApp.services').factory('todoService', TodoFactory);
}(angular));