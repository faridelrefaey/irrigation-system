# **Automatic Land Irrigation Application**

### This project contains API's for handling some basic operations on a piece of land.

To run this project on your local machine:
1. Download the project and open it in any IDE.
2. Download MySQL database as this project makes use of it.
3. Create a database using the command "CREATE database irr", 'irr' is the name of the database used in the project.
4. Change the values of the "spring.datasource.username" and "spring.datasource.password" in the application.properties file to suit the username and password used from your side.
5. Run the application

### Business Logic ###
This application mainly consists of handling irrigation configurations for multiple pieces of land. These configurations control a sensor assigned to a land. Firstly, the user will create a land and a sensor. 
After that, the user will create an irrigation configuration where they will add the start date, end date, times to water during interval. These 3 fields control the creation of irrigation periods.
Irrigation periods are the main driver for operating the sensors. These periods will be created automatically once the configuration is created. 
Irrigation periods are created based on the 3 fields mentioned above with each period having a start date and an end date(once the sensor is done).

### API Endpoints ###
All the API's documentation could be found in the Swagger documentation "/swagger-ui.html"
#### Land: <br> 
- POST "/land": <br>

Request Body:  
```JSON
{
  "seedType": "string",
  "landName": "string",
  "area": 0
}
```
Response:
```JSON
{
  "id": 0,
  "seedType": "string",
  "landName": "string",
  "area": 0,
  "irrigationConfigurationList": [
    {
      "id": 0,
      "startDate": "2023-07-11T15:16:11.268Z",
      "endDate": "2023-07-11T15:16:11.268Z",
      "timesToWaterDuringInterval": 0,
      "waterAmount": 0,
      "sensor": {
        "id": 0,
        "sensorName": "string"
      },
      "irrigationPeriodList": [
        {
          "id": 0,
          "startTime": "2023-07-11T15:16:11.268Z",
          "endTime": "2023-07-11T15:16:11.268Z",
          "isSuccessful": true
        }
      ]
    }
  ]
}
```

- GET "/land/{id}" <br>

Response:
```JSON
{
  "id": 0,
  "seedType": "string",
  "landName": "string",
  "area": 0,
  "irrigationConfigurationList": [
    {
      "id": 0,
      "startDate": "2023-07-11T15:29:35.583Z",
      "endDate": "2023-07-11T15:29:35.583Z",
      "timesToWaterDuringInterval": 0,
      "waterAmount": 0,
      "sensor": {
        "id": 0,
        "sensorName": "string"
      },
      "irrigationPeriodList": [
        {
          "id": 0,
          "startTime": "2023-07-11T15:29:35.583Z",
          "endTime": "2023-07-11T15:29:35.583Z",
          "isSuccessful": true
        }
      ]
    }
  ]
}
```

- PUT "/land/{id}"

Request Body:
```JSON
{
  "seedType": "string",
  "landName": "string",
  "area": 0
}
```

Response:
```JSON
{
  "id": 0,
  "seedType": "string",
  "landName": "string",
  "area": 0,
  "irrigationConfigurationList": [
    {
      "id": 0,
      "startDate": "2023-07-11T15:30:56.330Z",
      "endDate": "2023-07-11T15:30:56.330Z",
      "timesToWaterDuringInterval": 0,
      "waterAmount": 0,
      "sensor": {
        "id": 0,
        "sensorName": "string"
      },
      "irrigationPeriodList": [
        {
          "id": 0,
          "startTime": "2023-07-11T15:30:56.330Z",
          "endTime": "2023-07-11T15:30:56.330Z",
          "isSuccessful": true
        }
      ]
    }
  ]
}
```

- DELETE "/land/{id}"

Response:
```
string
```

- GET "/land"

Response:
```JSON
[
  {
    "id": 0,
    "seedType": "string",
    "landName": "string",
    "area": 0,
    "irrigationConfigurationList": [
      {
        "id": 0,
        "startDate": "2023-07-11T15:34:41.455Z",
        "endDate": "2023-07-11T15:34:41.455Z",
        "timesToWaterDuringInterval": 0,
        "waterAmount": 0,
        "sensor": {
          "id": 0,
          "sensorName": "string"
        },
        "irrigationPeriodList": [
          {
            "id": 0,
            "startTime": "2023-07-11T15:34:41.455Z",
            "endTime": "2023-07-11T15:34:41.455Z",
            "isSuccessful": true
          }
        ]
      }
    ]
  }
]
```

#### Sensor: <br>

- GET "/sensor/{id}"

Response:
```JSON
{
  "id": 0,
  "sensorName": "string"
}
```

- PUT "/sensor/{id}"

Request Body:
```JSON
{
  "sensorName": "string"
}
```

Response:
```JSON
{
  "id": 0,
  "sensorName": "string"
}
```

- GET "/sensor"

Response:
```JSON
[
  {
    "id": 0,
    "sensorName": "string"
  }
]
```

- POST "/sensor"

Request Body:
```JSON
{
  "sensorName": "string"
}
```

Response:
```JSON
{
  "id": 0,
  "sensorName": "string"
}
```

#### Irrigation Configuration: <br>

- GET "/irr"

Response:
```JSON
[
  {
    "id": 0,
    "startDate": "2023-07-11T15:41:51.085Z",
    "endDate": "2023-07-11T15:41:51.085Z",
    "timesToWaterDuringInterval": 0,
    "waterAmount": 0,
    "sensor": {
      "id": 0,
      "sensorName": "string"
    },
    "irrigationPeriodList": [
      {
        "id": 0,
        "startTime": "2023-07-11T15:41:51.085Z",
        "endTime": "2023-07-11T15:41:51.085Z",
        "isSuccessful": true
      }
    ]
  }
]
```

- POST "/irr"

Request Body:
```JSON
{
  "startDate": "2023-07-11T15:42:41.092Z",
  "endDate": "2023-07-11T15:42:41.092Z",
  "timesToWaterDuringInterval": 0,
  "waterAmount": 0,
  "landId": 0,
  "sensorId": 0
}
```

Response:
```JSON
{
  "id": 0,
  "startDate": "2023-07-11T15:42:41.093Z",
  "endDate": "2023-07-11T15:42:41.093Z",
  "timesToWaterDuringInterval": 0,
  "waterAmount": 0,
  "sensor": {
    "id": 0,
    "sensorName": "string"
  },
  "irrigationPeriodList": [
    {
      "id": 0,
      "startTime": "2023-07-11T15:42:41.093Z",
      "endTime": "2023-07-11T15:42:41.093Z",
      "isSuccessful": true
    }
  ]
}
```

- GET "/irr/{id}"

Response:
```JSON
{
  "id": 0,
  "startDate": "2023-07-11T15:43:30.089Z",
  "endDate": "2023-07-11T15:43:30.089Z",
  "timesToWaterDuringInterval": 0,
  "waterAmount": 0,
  "sensor": {
    "id": 0,
    "sensorName": "string"
  },
  "irrigationPeriodList": [
    {
      "id": 0,
      "startTime": "2023-07-11T15:43:30.089Z",
      "endTime": "2023-07-11T15:43:30.089Z",
      "isSuccessful": true
    }
  ]
}
```

- DELETE "/irr/{id}"

Response:
```
string
```