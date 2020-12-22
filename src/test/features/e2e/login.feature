Feature: Login functionality

  Background:
    Given The app is ready
    And I am on page ''

  Scenario: Open Sign In Page
    When I click the element with id 'account-menu'
    When I click the element with id 'login'
    Then The sign in page is displayed

  Scenario: Log In
    When I click the element with id 'account-menu'
    When I click the element with id 'login'
    When I fill out input with id 'username' with content
      """
      user
      """
    When I fill out input with id 'password' with content
      """
      user
      """
    When I click the element with id 'submitLoginBtn'
    When I wait until element with tagName 'ngb-modal-window' is gone
    When I click the element with id 'account-menu'
    Then The element exists with id 'logout'
