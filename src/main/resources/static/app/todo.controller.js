(function(){
    var TodoController = function($scope, todoService) {
        
        todoService.query(function(response) {
           $scope.todos = response || [];
        });
        
        $scope.addTodo = function (todoDTO) {
            todoService.save({
                todo : {
                    description: todoDTO.description
                }
            }, function (todoDTO) {
                    $scope.todos.push(todoDTO.todo);
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
                todo: todoDTO
            });
        };
    };

    TodoController.$inject = ['$scope', 'todoService'];
    angular.module("todoApp.controllers").controller("TodoController", TodoController);
    
    /*var AppController = function($scope, Item) {
        Item.query(function(response) {
            $scope.items = response ? response : [];
        });

        $scope.addItem = function(description) {
            new Item({
                description: description,
                checked: false
            }).$save(function(item) {
                    $scope.items.push(item);
                });
            $scope.newItem = "";
        };

        $scope.updateItem = function(item) {
            item.$update();
        };

        $scope.deleteItem = function(item) {
            item.$remove(function() {
                $scope.items.splice($scope.items.indexOf(item), 1);
            });
        };
    };
    AppController.$inject = ['$scope', 'Item'];
    angular.module("myApp.controllers").controller("AppController", AppController);*/
}(angular));