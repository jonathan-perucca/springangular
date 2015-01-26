describe('Todo app crud features', function () {
    var page = require('./todoPage.js');
    
    beforeEach(function () {
        page.get();
    });

    it('should insert new todo', function () {
        var todoDescription = 'addedTodo';

        page.addTodo(todoDescription);

        expect(page.list.count()).toBeGreaterThan(0);
        expect(page.list.last().getText()).toEqual('addedTodo');
    });

    it('should delete a todo', function() {
        expect(page.list.count()).toEqual(1);

        page.deleteOneTodo();

        expect(page.list.count()).toEqual(0);
    });

});