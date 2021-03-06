/*global angular */

/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */
console.log('todoCtrl.js');
angular.module('todomvc')
	.controller('TodoCtrl', function TodoCtrl($scope, $routeParams, $filter, todoStorage) {
		'use strict';

        var store = todoStorage;
        var todos = $scope.todos = store.todos = [];

        todoStorage.fetch().then(function onResponse(resp) {
            console.log(resp, resp.data);
            angular.copy(resp.data, $scope.todos);
            var todos = $scope.todos;
        });


		$scope.newTodo = '';
		$scope.editedTodo = null;

		$scope.$watch('todos', function () {
			$scope.remainingCount = $filter('filter')(todos, { completed: false }).length;
			$scope.completedCount = todos.length - $scope.remainingCount;
			$scope.allChecked = !$scope.remainingCount;
		}, true);

		// Monitor the current route for changes and adjust the filter accordingly.
		$scope.$on('$routeChangeSuccess', function () {
			var status = $scope.status = $routeParams.status || '';

			$scope.statusFilter = (status === 'active') ?
				{ completed: false } : (status === 'completed') ?
				{ completed: true } : null;
		});

		$scope.addTodo = function () {
			var newTodo = {
				title: $scope.newTodo.trim(),
				completed: false
			};

			if (!newTodo.title) {
				return;
			}

			$scope.saving = true;
            console.log('insert', newTodo);
            store.insert(newTodo).then(function onResponse(resp) {
                console.log(resp, resp.data);
                angular.copy(resp.data, $scope.todos);
                var todos = $scope.todos;
                $scope.newTodo = '';
            }).finally(function () {
				$scope.saving = false;
			});
		};

		$scope.editTodo = function (todo) {
			$scope.editedTodo = todo;
			// Clone the original todo to restore it on demand.
			$scope.originalTodo = angular.extend({}, todo);
		};

		$scope.saveEdits = function (todo, event) {
			// Blur events are automatically triggered after the form submit event.
			// This does some unfortunate logic handling to prevent saving twice.
			if (event === 'blur' && $scope.saveEvent === 'submit') {
				$scope.saveEvent = null;
				return;
			}

			$scope.saveEvent = event;

			if ($scope.reverted) {
				// Todo edits were reverted-- don't save.
				$scope.reverted = null;
				return;
			}

			todo.title = todo.title.trim();

			if (todo.title === $scope.originalTodo.title) {
				return;
			}

            console.log(todo.title ? 'put' : 'delete', todo);
			store[todo.title ? 'put' : 'delete'](todo)
                .then(function success(resp) {
                    console.log(resp, resp.data);
                    angular.copy(resp.data, $scope.todos);
                    var todos = $scope.todos;
                    $scope.newTodo = '';
                }, function error() {
                    todo.title = $scope.originalTodo.title;
                })
				.finally(function () {
					$scope.editedTodo = null;
				});
		};

		$scope.revertEdits = function (todo) {
			todos[todos.indexOf(todo)] = $scope.originalTodo;
			$scope.editedTodo = null;
			$scope.originalTodo = null;
			$scope.reverted = true;
		};

		$scope.removeTodo = function (todo) {
            console.log('delete', todo);
			store.delete(todo).then(function onResponse(resp) {
				console.log(resp, resp.data);
				angular.copy(resp.data, $scope.todos);
				var todos = $scope.todos;
				$scope.newTodo = '';
			});
		};

		$scope.saveTodo = function (todo) {
            console.log('put', todo);
			store.put(todo);
		};

		$scope.toggleCompleted = function (todo, completed) {
			if (angular.isDefined(completed)) {
				todo.completed = completed;
			}
            console.log('put', todo, todos.indexOf(todo));
			store.put(todo, todos.indexOf(todo))
				.then(function success() {}, function error() {
					todo.completed = !todo.completed;
				});
		};

		$scope.clearCompletedTodos = function () {
            console.log('clear');
			store.clearCompleted();
		};

		$scope.markAll = function (completed) {
			todos.forEach(function (todo) {
				if (todo.completed !== completed) {
					$scope.toggleCompleted(todo, completed);
				}
			});
		};
	});
