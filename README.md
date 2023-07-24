# Job Scraper
The Job Scraper is a web application that scrapes job listings from the Techstars job board. 
It provides an API endpoint to scrape and save job listings based on job functions. 
The application uses Selenium and Jsoup libraries for web scraping and Spring Boot for the backend.

## Features

* Scrapes job listings from the Techstars job board based on specified job functions.
* Saves the scraped job listings to a MySQL database.

## Prerequisites

* Java JDK 11 or higher
* MySQL Database

## API Endpoint

```http
GET /scrape
```
Scrapes job listings based on the specified job function and saves them to the database.

## Parameters

* `jobFunction`: (Optional) The name of the job function to filter the job listings.
* `size`: (Optional) The number of job listings to scrape. (Default value: 10)

## Technologies Used

* Java
* Spring Boot
* Selenium
* Jsoup
* MySQL