(function(){
    var TodoController = function($scope, todoService) {
        
        todoService.query(function(response) {
           $scope.todos = response || [];
        });
        
        $scope.addTodo = function (todoDTO) {
            todoService.save({
                description: todoDTO.description
            }, function (todo) {
                    $scope.todos.push(todo);
            });
            $scope.newTodo = "";
        };

        $scope.deleteTodo = function (todoDTO) {
            todoService.remove({ 
                id: todoDTO.id 
            }, function () {
                $scope.todos = _.remove($scope.todos, function (element) {
                    return element.id !== todoDTO.id;
                });
            });
        };
        
        $scope.updateTodo = function (todoDTO) {
            todoService.update({
                id: todoDTO.id
            },{
                todo: todoDTO
            });
        };
    };

    TodoController.$inject = ['$scope', 'todoService'];
    angular.module("todoApp.controllers").controller("TodoController", TodoController);
}(angular));