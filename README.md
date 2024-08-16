# Foreign Exchange API Documentation

## Overview

This API provides endpoints for currency conversion, retrieving conversion history, and fetching current exchange rates.

## Endpoints

## Exchange Rate API

### Get Exchange Rates

**Endpoint:** `POST /exchange-rate`

**Summary:**
Retrieves the current exchange rates between specified currencies based on the provided request details. The endpoint
fetches the latest exchange rates without performing any conversions.

**Description:**
This endpoint provides the latest exchange rates for the specified currencies. It does not perform any currency
conversions but retrieves up-to-date exchange rate data.

**Request Body:**

   ```json
    {
     "from": "USD",
     "to": "EUR"
    }
```

**Response Body:**

   ```json
    {
      "currency": "USD",
      "rate": 1.097381
    }
```

## Currency Conversion API

### Convert Currency

- **Endpoint:** `POST /convert`
- **Summary:** Converts a specified amount from one currency to another.
- **Description:** Converts a given amount from one currency to another based on the request details. The conversion
  uses the latest exchange rates available.

**Request Body:**

```json
{
  "exchangeRateRequest": {
    "from": "USD",
    "to": "EUR"
  },
  "amount": 100
}
```

**Response Body:**

```json
{
  "success": true,
  "transactionDateTime": "2024-08-16T16:03:54.222Z",
  "source": "USD",
  "rate": 1.098847,
  "result": 6834.83
}
```

## Conversion History API

### Get Conversion History

- **Endpoint:** `GET /history`
- **Summary:** Retrieve a paginated list of conversion histories based on the specified criteria.
- **Description:** Fetches a paginated list of conversion histories based on the provided request criteria. This
  endpoint allows users to retrieve past conversion records with optional pagination.

**Request Parameters:**

- **Query Parameters:**
    - `startDate` (string, optional): The start date for the conversion history records in `YYYY-MM-DD-HH-MM` format. If
      provided, filters records starting from this date.
    - `endDate` (string, optional): The end date for the conversion history records in `YYYY-MM-DD-HH-MM` format. If
      provided, filters records up to this date.
    - `page` (integer, optional): The page number for pagination. Default is `0`.
    - `size` (integer, optional): The number of records per page. Default is `20`.

**Request Body:**

```http
GET /history?startDate=2024-08-15T11:57:04&endDate=2024-08-15T11:57:04&page=1&size=100
```

**Response Body:**

```json
{
  "totalElements": 3,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10,
  "isFirst": true,
  "isLast": true,
  "data": [
    {
      "id": 1,
      "fromCurrency": "EUR",
      "toCurrency": "TRY",
      "amount": 1000.00,
      "rate": 37.0511160000,
      "convertedAmount": 37051.12,
      "transactionDateTime": "2024-08-01T14:54:57.346879"
    },
    {
      "id": 2,
      "fromCurrency": "TRY",
      "toCurrency": "GBP",
      "amount": 350.00,
      "rate": 0.0229890000,
      "convertedAmount": 8.05,
      "transactionDateTime": "2024-08-13T15:13:04.248811"
    },
    {
      "id": 3,
      "fromCurrency": "GBP",
      "toCurrency": "TRY",
      "amount": 350.00,
      "rate": 43.4980250000,
      "convertedAmount": 15224.31,
      "transactionDateTime": "2024-08-15T15:17:27.804413"
    }
  ]
}
```

## Exchange Rates Service Provider

The application uses [CurrencyLayer](https://currencylayer.com/) as the service provider for fetching exchange rates.
The free plan allows up to **100 requests** per month.

### API Key Configuration

To configure the API key, add the following entry to your `application.properties` file:

```properties
currencylayer.api.key=NsC2yLlisny8FbRvj1nNBL3SHqmVyrzw
```

Note: Replace example key with your actual API key.

For more information on the CurrencyLayer API and its usage limits, visit
the [CurrencyLayer](https://currencylayer.com/) website.

# In-Memory Database: H2

### Start the Application

- Ensure your application is running:
    - **Docker:** Start the container.
    - **Local:** Ensure the Spring Boot application is active.

### Open the Console

- Go to [http://localhost:8080/h2-console](http://localhost:8080/h2-console) in your browser.

### Log In

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `in properties file`

# How to run Dockerized App

## Overview

This guide provides instructions on how to build and run the Dockerized application. The application is containerized
using Docker, which allows for consistent development and deployment environments.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed on your machine.

## Building the Docker Image

To build the Docker image for the application, follow these steps:

1. **Navigate to the Project Directory:**

   Open your terminal and change to the directory containing the `Dockerfile`:

   ```bash
   cd path/to/foreign-exchange-app
   ```
2. **Build the Project:**
   Run the following Maven command to compile and package your application:
   ```bash
   mvn clean package
   ```
   This command will compile your code, run tests, and package the application into a JAR file.


3. **Build the Docker Image**:

   Run the following command to build the Docker image:
   ```bash
   docker build -t foreign-exhange-app
   ```

4. **Running the Docker Container**:

   Run the following command to build the Docker image:
   ```bash
   docker run -d -p 8080:8080 foreign-exhange-app
   ```

## Swagger Documentation

For more detailed information about the API endpoints, including request and response formats, please refer to the
Swagger documentation available at:

- Go to http://localhost:8080/swagger-ui/index.html#/ in your browser.
   