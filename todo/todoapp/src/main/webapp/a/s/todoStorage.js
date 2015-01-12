/*global angular */

/**
 * Services that persists and retrieves todos from localStorage or a backend API
 * if available.
 *
 * They both follow the same API, returning promises for all changes to the
 * model.
 */
console.log('todoStorage.js');
angular.module('todomvc')
	.factory('todoStorage', function ($http, $injector) {
		'use strict';

		// Detect if an API backend is present. If so, return the API module, else
		// hand off the localStorage adapter
		return $injector.get('localStorage');
//        $http.get('/api')
//			.then(function () {
//				return $injector.get('api');
//			}, function () {
//				return $injector.get('localStorage');
//			});
	})

	.factory('api', function ($http) {
		'use strict';

		var store = {
			todos: [],

			clearCompleted: function () {
				var originalTodos = store.todos.slice(0);

				var completeTodos = [], incompleteTodos = [];
				store.todos.forEach(function (todo) {
					if (todo.completed) {
						completeTodos.push(todo);
					} else {
						incompleteTodos.push(todo);
					}
				});

				angular.copy(incompleteTodos, store.todos);

				return $http.delete('/api/todos/completed')
					.then(function success() {
						return store.todos;
					}, function error() {
						angular.copy(originalTodos, store.todos);
						return originalTodos;
					});
			},

			delete: function (todo) {
				var originalTodos = store.todos.slice(0);

				store.todos.splice(store.todos.indexOf(todo), 1);

				return $http.delete('/api/todos/' + todo.id)
					.then(function success() {
						return store.todos;
					}, function error() {
						angular.copy(originalTodos, store.todos);
						return originalTodos;
					});
			},

			get: function () {
				return $http.get('/api/todos')
					.then(function (resp) {
						angular.copy(resp.data, store.todos);
						return store.todos;
					});
			},

			insert: function (todo) {
				var originalTodos = store.todos.slice(0);

				return $http.post('/api/todos', todo)
					.then(function success(resp) {
						todo.id = resp.data.id;
						store.todos.push(todo);
						return store.todos;
					}, function error() {
						angular.copy(originalTodos, store.todos);
						return store.todos;
					});
			},

			put: function (todo) {
				var originalTodos = store.todos.slice(0);

				return $http.put('/api/todos/' + todo.id, todo)
					.then(function success() {
						return store.todos;
					}, function error() {
						angular.copy(originalTodos, store.todos);
						return originalTodos;
					});
			}
		};

		return store;
	})

	.factory('localStorage', function ($q, $http) {
		'use strict';

		var store = {

            fetch: function() {
                return $http({
                    method: 'GET',
                    url: 'api/todos'
                });
            },

			clearCompleted: function () {
                var originalTodos = store.todos.slice(0);

                var completeTodos = [], incompleteTodos = [];
                store.todos.forEach(function (todo) {
                    if (todo.completed) {
                        completeTodos.push(todo);
                    } else {
                        incompleteTodos.push(todo);
                    }
                });

                angular.copy(incompleteTodos, store.todos);

                return $http.delete('api/todos/completed')
                    .then(function success() {
                        return store.todos;
                    }, function error() {
                        angular.copy(originalTodos, store.todos);
                        return originalTodos;
                    });
			},

			delete: function (todo) {

                return $http({
                    method: 'DELETE',
                    url:    'api/todos',
                    data:   todo,
                    headers:    {
                        "Content-Type": "application/json"
                    }
                });
			},

			get: function () {
                return $http({
                    method: 'GET',
                    url:    'api/todos'
                });
			},

			insert: function (todo) {

                return $http({
                    method: 'POST',
                    url:    'api/todos',
                    data:   todo
                });
			},

			put: function (todo, index) {
                return $http({
                    method: 'PUT',
                    url:    'api/todos',
                    data:   todo
                });
			}
		};
		return store;
	});
