# Lucene_Retrival_MicroService
Spring Boot Microservice Application for Indexing and Searching using Apache Lucene

<b>Objective:</b>
Modify the existing project to run on server and deploy as microservice. By converting to microservice, the project can be improvised 
to provide necessary UI for user to browse for the path of document corpus that needs to be indexed and searched from. 
In subsequent versions, search micro service will be implemented. Also UI will be designed using react js.

<b>Overview:</b>
Currently the system includes the following services:
1. Indexing Service - Completed. Hardcoded document path will be modified to get values form UI after completion
2. Searching service - Will be added soon

<b>Pre-requisites:</b>
1. Java
2. Maven
3. IDE(Preferably Eclipse)

<b>Steps to run:</b>
1. Clone and Import as Maven Project into local workspace.
2. Build the pom.xml by Maven command (mvn clean install) and up the server
3. Server is currently configured to default port: 8080(Change the port in application.properties if required)
4. Run the indexing service using the url below:
   http://localhost:8080/index
5. Indexed documents will be created in the local workspace inside index folder.

