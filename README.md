# Mars Photos

## Installing
This project is built using gradle. After downloading the files, run 

./gradlew bootJar

to generate the jar. Alternatively, you can download the jar out of the repository

## Running the server

java -jar build/libs/MarsPhotos-1.0-SNAPSHOT.jar

or whatever the path to your jar is. This will start a server running on port 8080

## Executing Requests

the endpoint is /api/photos/. Perform a GET with a request paremeter of requestDate.
The date is in yyyy-MM-dd format. 

The images for that date will be downloaded to MarsPhotos/*date*/

Example:
curl localhost:8080/api/photos/?requestDate=2016-05-03
