var app = angular.module('crudApp', [ 'ui.router', 'ngStorage' ]);

app.constant('urls', {
	BASE : 'http://localhost:8080/SpringBootCRUDApp',
	USER_SERVICE_API : 'http://localhost:8080/SpringBootCRUDApp/api/user/',
	CUSTOMER_SERVICE_API : 'http://localhost:8080/SpringBootCRUDApp/customer/'
});

app.config([
		'$stateProvider',
		'$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {
			console.log('inside state.......');
			$stateProvider.state(
					'home',
					{
						url : '/',
						templateUrl : 'partials/list',
						controller : 'UserController',
						controllerAs : 'ctrl',
						resolve : {
							users : function($q, UserService) {
								console.log('Load all users');
								var deferred = $q.defer();
								UserService.loadAllUsers().then(
										deferred.resolve, deferred.resolve);
								return deferred.promise;
							}
						}
					}).state(
					'cust',
					{
						url : '/',
						templateUrl : 'customers/customerlist',
						controller : 'CustomerController',
						controllerAs : 'ctrl',
						resolve : {
							customers : function($q, CustomerService) {
								console.log('Load all customers');
								var deferred = $q.defer();
								CustomerService.loadAllCustomers().then(
										deferred.resolve, deferred.resolve);
								return deferred.promise;
							}
						}
					});
			$urlRouterProvider.otherwise('/');
		} ]);
