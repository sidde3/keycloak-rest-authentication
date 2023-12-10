# Sample authentication of Keycloak over a Rest API
This project contains three module 
- Authenticator
- User Storage Provider 
- Rest API

## Authenticator
A simple Keycloak Authenticator which is developed extending ``UsernamePasswordForm``. Authenticator SPI validates the user over a REST API and upon successful validation it checks the user in local repo and if the user does not exists at local it will create a new user.
This authenticator can be added in browser authentication flow. 

## Storage Provider
A simple user storage provider which validates the user over a REST API.

## Service
A sample REST WebServer developed using Quarkus. This has a hardcoded map which holds user credential information.

*Note:* The Authenticator is unnecessary if the user is just required to be authenticated over REST Api.