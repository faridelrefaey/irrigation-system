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
All the API's documentation could be found in the Swagger documentation "/swagger-ui.html".<br>
All endpoints other than the authentication are secured and should take the token created in login as an Authorization bearer token.
#### Land: <br> 
- POST "/land": <br>

Request Body:  
```JSON
{
  "seedType": "Corn",
  "landName": "Corn Field",
  "area": 12.5
}
```
Response:
```JSON
{
  "id": 1,
  "seedType": "Corn",
  "landName": "Corn Field",
  "area": 12.5,
  "irrigationConfigurationList": [
    {
      "id": 1,
      "startDate": "2023-07-11T15:16:11.268",
      "endDate": "2023-07-11T15:16:11.268",
      "timesToWaterDuringInterval": 3,
      "waterAmount": 10.75,
      "sensor": {
        "id": 1,
        "sensorName": "Sensor1"
      },
      "irrigationPeriodList": [
        {
          "id": 1,
          "startTime": "2023-07-11T15:16:11.268",
          "endTime": "2023-07-11T15:16:11.268",
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
  "id": 1,
  "seedType": "Corn",
  "landName": "Corn Field",
  "area": 12.5,
  "irrigationConfigurationList": [
    {
      "id": 1,
      "startDate": "2023-07-11T15:16:11.268",
      "endDate": "2023-07-11T15:16:11.268",
      "timesToWaterDuringInterval": 3,
      "waterAmount": 10.75,
      "sensor": {
        "id": 1,
        "sensorName": "Sensor1"
      },
      "irrigationPeriodList": [
        {
          "id": 1,
          "startTime": "2023-07-11T15:16:11.268",
          "endTime": "2023-07-11T15:16:11.268",
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
  "seedType": "Wheat",
  "landName": "Wheat Field",
  "area": 25.5
}
```

Response:
```JSON
{
  "id": 1,
  "seedType": "Wheat",
  "landName": "Wheat Field",
  "area": 25.5,
  "irrigationConfigurationList": [
    {
      "id": 1,
      "startDate": "2023-07-11T15:30:56.330",
      "endDate": "2023-07-11T15:30:56.330",
      "timesToWaterDuringInterval": 4,
      "waterAmount": 15,
      "sensor": {
        "id": 1,
        "sensorName": "Sensor1"
      },
      "irrigationPeriodList": [
        {
          "id": 1,
          "startTime": "2023-07-11T15:30:56.3301",
          "endTime": "2023-07-11T15:30:56.3301",
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
    "id": 1,
    "seedType": "Corn",
    "landName": "Corn Field",
    "area": 15.0,
    "irrigationConfigurationList": [
      {
        "id": 1,
        "startDate": "2023-07-11T15:34:41.455",
        "endDate": "2023-07-11T15:34:41.455",
        "timesToWaterDuringInterval": 5,
        "waterAmount": 13.0,
        "sensor": {
          "id": 1,
          "sensorName": "Sensor1"
        },
        "irrigationPeriodList": [
          {
            "id": 1,
            "startTime": "2023-07-11T15:34:41.455",
            "endTime": "2023-07-11T15:34:41.455",
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
  "id": 1,
  "sensorName": "Sensor1"
}
```

- PUT "/sensor/{id}"

Request Body:
```JSON
{
  "sensorName": "Sensor2"
}
```

Response:
```JSON
{
  "id": 1,
  "sensorName": "Sensor2"
}
```

- GET "/sensor"

Response:
```JSON
[
  {
    "id": 1,
    "sensorName": "Sensor1"
  }
]
```

- POST "/sensor"

Request Body:
```JSON
{
  "sensorName": "Sensor1"
}
```

Response:
```JSON
{
  "id": 1,
  "sensorName": "Sensor1"
}
```

#### Irrigation Configuration: <br>

- GET "/irr"

Response:
```JSON
[
  {
    "id": 1,
    "startDate": "2023-07-11T15:41:51.085",
    "endDate": "2023-07-11T15:41:51.085",
    "timesToWaterDuringInterval": 4,
    "waterAmount": 12.5,
    "sensor": {
      "id": 1,
      "sensorName": "Sensor1"
    },
    "irrigationPeriodList": [
      {
        "id": 1,
        "startTime": "2023-07-11T15:41:51.085",
        "endTime": "2023-07-11T15:41:51.085",
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
  "startDate": "2023-07-11T15:42:41.092",
  "endDate": "2023-07-11T15:42:41.092",
  "timesToWaterDuringInterval": 3,
  "waterAmount": 12.4,
  "landId": 1,
  "sensorId": 1
}
```

Response:
```JSON
{
  "id": 1,
  "startDate": "2023-07-11T15:42:41.093",
  "endDate": "2023-07-11T15:42:41.093",
  "timesToWaterDuringInterval": 3,
  "waterAmount": 12.4,
  "sensor": {
    "id": 1,
    "sensorName": "Sensor1"
  },
  "irrigationPeriodList": [
    {
      "id": 1,
      "startTime": "2023-07-11T15:42:41.093",
      "endTime": "2023-07-11T15:42:41.093",
      "isSuccessful": true
    }
  ]
}
```

- GET "/irr/{id}"

Response:
```JSON
{
  "id": 1,
  "startDate": "2023-07-11T15:43:30.089",
  "endDate": "2023-07-11T15:43:30.089",
  "timesToWaterDuringInterval": 4,
  "waterAmount": 12.4,
  "sensor": {
    "id": 1,
    "sensorName": "Sensor1"
  },
  "irrigationPeriodList": [
    {
      "id": 1,
      "startTime": "2023-07-11T15:43:30.089",
      "endTime": "2023-07-11T15:43:30.089",
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

### Authentication: <br>
We have 2 roles in the application, ENGINEER and FARMER. On registration, if the username includes engineer, it will be registered as an ENGINEER, else as a FARMER.
- POST "/auth/register": <br>

Request Body:
```JSON
{
  "username": "farid.farmer",
  "password": "12345"
}
```
Response:
```
string
```

- POST "/auth/login": <br>

Request Body:
```JSON
{
  "username": "farid.farmer",
  "password": "12345"
}
```
Response:
```JSON
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJGYXJpZCBFbHJlZmFleSIsImlhdCI6MTcyMDAwMzU5OCwiZXhwIjoxNzIwMDA3MTk4LCJ1c2VybmFtZSI6ImZhcmlkLmVuZ2luZWVyIiwicm9sZSI6IkVOR0lORUVSIn0.lVkjlO5lc4cQICIsQndten6t3tIOs055LV563tOMRj75x32eAPXTcGBGZvsBbLjfMtrskcsBmZJdLjFRgoU8FQ"
}
```