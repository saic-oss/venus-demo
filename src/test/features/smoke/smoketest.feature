Feature: Smoke Test

  Background:
    Given The app is ready
    And I am signed in with username 'user' and password 'user'

  Scenario: User Settings Page is ready
    Given I am on page '/account/settings'
    Then The element exists with id 'email'
