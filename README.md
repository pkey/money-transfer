# Money transfer service

Simple money transfer service to handle money transferring from one account to another :moneybag:

## How to run it

Simply do `./gradlew run` and it will start a Spark server on http://localhost:4567 :zap:

## How to run tests

- Unit `./gradlew clean test`
- Integration `./gradlew clean integrationTest`

## API

- POST `/account` -> creates account
- GET `/account/:id` -> retrieves account
- PUT `/account/:id` -> updates account. Should provide new `balance`
- DELETE `/account/:id` -> deletes account
- POST `/transfer` -> transfers money. Should provide: `accountIdFrom`, `accountIdTo` and `amount`
