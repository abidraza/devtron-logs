# Search Logs Service

This project provides a service for searching logs stored in a Google Cloud Storage bucket. It allows users to specify a time range and a keyword to search for in the logs.

## Prerequisites

- Java 8 or higher
- Spring Boot
- Google Cloud Storage SDK

## Installation

1. Clone the repository: `git clone https://github.com/your-username/search-logs-service.git`
2. Build the project: `mvn clean install`
3. Configure the Google Cloud Storage credentials: 
   - Create a service account and download the JSON key file.
   - Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable to the path of the JSON key file.

## Usage

1. Start the application: `java -jar search-logs-service.jar`
2. Make a POST request to the `/search-logs` endpoint with the following parameters:
   - `logsFrom`: The start timestamp of the logs to search.
   - `logsTo`: The end timestamp of the logs to search.
   - `searchKeyword`: The keyword to search for in the logs.
3. The service will return a JSON response containing the searched logs.

## Configuration

The following configuration properties can be modified in the `application.properties` file:

- `spring.datasource.url`: The URL of the database.
- `spring.datasource.username`: The username for the database.
- `spring.datasource.password`: The password for the database.
- `google.cloud.projectId`: The ID of the Google Cloud project.
- `google.cloud.storage.bucketName`: The name of the Google Cloud Storage bucket.

## Contributing

Contributions are welcome! Please follow the [contribution guidelines](CONTRIBUTING.md) when making changes to the project.

## License

This project is not licensed.

## Sample curl
curl --location 'http://localhost:8080/v1/search-logs' \
--header 'Content-Type: application/json' \
--data '{
    "searchKeyword":"hellof",
    "logsFrom": "2024-03-10T00:00:00",
    "logsTo":"2024-03-11T12:30:00"
}'

## Sample Success response
{
    "status": "SUCCESS",
    "statusCode": 20000,
    "data": {
        "searchedLogs": [
            "eqwfwelkfnjwnfjhellofklwnfkmwfmw",
            "eqwfwelkfnjwnfjhellofklwnfkmwfmw",
            "eqwfwelkfnjwnfjhellofklwnfkmwfmw",
            "eqwfwelkfnjwnfjhellofklwnfkmwfmw",
            "eqwfwelkfnjwnfjhellofklwnfkmwfmw"
        ]
    },
    "message": "logs fetched!"
}

## Sample Failure response
{
    "status": "FAILURE",
    "statusCode": 10000,
    "data": null,
    "message": "End date cannot be before start date"
}
