# Job Scraper - Installation and Configuration Guide

This guide provides step-by-step instructions on how to configure and run the Job Scraper application.

## Prerequisites

Before proceeding with the installation, make sure you have the following prerequisites installed:

* Java JDK 11 or higher
* MySQL Database

## Step 1: Clone the Repository

Clone the Job Scraper repository from GitHub to your local machine.

```http
git clone https://github.com/Yar-slav/TaskDataOx.git
```

## Step 2: Database Configuration

Create a new MySQL database for the Job Scraper application. You can use the following commands in the MySQL shell:

```sql
CREATE DATABASE DataOx;
```

# Step 3: Configure Database Credentials

Open the `application.properties` file located at `src/main/resources/application.properties`.

Replace the following properties with your MySQL database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/DataOx
spring.datasource.username=root
spring.datasource.password=your-database-password
```

# Step 4: Build the Application

Navigate to the project's root directory and build the application using Maven.

```bash
cd job-scraper
./mvnw clean package
```

## Step 5: Run the Application

Run the Job Scraper application using the following command:

```bash
./mvnw spring-boot:run
```

The application will start, and you can access it at http://localhost:8080.

## Usage

You can now use the API endpoint to scrape job listings based on job functions.

## API Endpoint

```bash
GET /scrape
```

Parameters:

* `jobFunction`: (Optional body) The name of the job function to filter the job listings.
* `size`: (Optional) The number of job listings to scrape. (Default value: 10)
* Example:

```bash
GET http://localhost:8080/scrape?size=20
```

```
{
    "jobFunction": "Software Engineering"
}
```

## Conclusion

Congratulations! You have successfully installed and configured the Job Scraper application. You can now use the API endpoint to scrape job listings based on job functions and store them in the MySQL database. Happy scraping!