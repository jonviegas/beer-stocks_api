# :beer: Beer Stocks API
- - -

## :book: Introduction
This **REST API** provides a system for managing beer stocks, offering the functionality to register a beer by its name, brand, quantity in stock and max limit. in addition, it is also possible to search an individual beer by its name and list all registered beers.
- - -
## :cloud: Cloud

The project is also hosted on *Heroku*, [click here](https://beer-system-api.herokuapp.com/beers) to access it.
- - -
## :green_book: Documentation

[Click here](https://beer-system-api.herokuapp.com/swagger-ui.html) to access the complete documentation made with *swagger*.

- - -
## :computer: Usage

- ### **GET**
    ```/beers```
    Returns all registered beers.
\
    ```/beers/{id}```
    Look for a beer by its id.
\
    ```/beers/search/{name}```
    Look for a beer by its name.

- ### **POST**
    ```/beers```
    Allows you to register a new Beer.

    **Example:**
    ``` JSON
    {
      "name": "Skol Beats",
      "brand": "Ambev",
      "max": 5,
      "quantity": 2,
    }  
    ```

- ### **PATCH**
     ```/beers/{id}/increment```
    Allows you to increase the total amount of this beer in stock as long as it does not exceed the allowed limit.
    
    **Example:**
    ``` JSON
    {
      "quantity": 2,
    }
    ```
    ```/beers/{id}/decrement```
    Allows you to decrease the total amount of this beer in stock as long as stock does not go below 0.
    
    **Example:**
    ``` JSON
    {
      "quantity": 2,
    }
    ```
    
- ### **DELETE**
    ```/beers/{id}```
    Deletes a beer, if exists, by its id.
