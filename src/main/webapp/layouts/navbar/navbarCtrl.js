/**
 * Created by dakamadafaka on 7/7/17.
 */

(function() {
    'use strict';

    angular
        .module('SBZApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state'];

    function NavbarController ($state) {

        var vm = this;

        vm.login = login;
        vm.registration = registration;
        vm.account = account;
        vm.articles = articles;
        vm.articles_new = articles_new;
        vm.$state = $state;

        function login() {
            $state.go('login');
        }

        function registration() {
            $state.go('registration');
        }

        function account() {
            $state.go('account');
        }

        function articles() {
            $state.go('articles');
        }

        function articles_new() {
            $state.go('articleNew');
        }

    }
})();