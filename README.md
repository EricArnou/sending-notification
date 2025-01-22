<h1 align = center>Sending Notification Microservice</h1>

# About

This project consists of a microservice that receives notification registrations through a REST API, saves them in a PostgreSQL database and, at the scheduled time, calls the appropriate channel's sending services through Spring Scheduling. 

The services considered are WhatsApp, SMS, Email and Push. The call to these services is made through AWS SQS queues and the notification status is also updated with an SQS queue. 

In addition to registering for notifications, it is also possible to cancel a notification and check its current status.

## Table of Contents
    - Technologies
    - Usage
    - API EndPoint
    - SQS Consumer Queue
    - SQS Producer Queue
    - License
## Technologies
    - Java 21
    - SpringBoot
    - PostgreSQL
    - SQS (AWS)
    - Docker
## Usage

Build PostgreSQL container with docker-compose.yml with:
```
docker compose up
```
You must have [LocalStack](https://www.localstack.cloud/) installed to simulate the AWS environment

## API EndPoint

```
POST /api/v1/notifications: register notification
GET /api/v1/notifications/{id}: get notification status
PUT /api/v1/notifications/{id}/cancel: cancel notification
```

## SQS Consumer Queue Template
```
{
    "notificationId": "8b54228b-28dc-4a78-9e03-97217b933db4",
    "status": "SUCCESS"
}
```

## SQS Producer Queue Template
```
{
    "notificationId" : "8b54228b-28dc-4a78-9e03-97217b933db4",
    "recipient" : "email@email.com",
    "message" : "message 1"
}
```
## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/EricArnou/sending-notification/blob/main/LICENSE) file for details.
