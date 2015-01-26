var TodoPage = function () {
    this.list = element.all(by.repeater('todo in todos'));
    this.descriptionField = element(by.model('newTodo.description'));
    this.submitButton = element(by.css('[name="submitTodo"]'));

    this.get = function () {
        browser.get('index.html');
    };
    
    this.addTodo = function(description) {
        this.descriptionField.sendKeys(description);
        this.submitButton.click();
    };
    
    this.deleteOneTodo = function() {
        this.list.first().element(by.css('[title="Delete"]')).click();
    }
};

module.exports = new TodoPage();