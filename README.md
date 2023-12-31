# News Application

This is a full-stack application built with Java Spring for the backend and React for the frontend.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
	- [Starting the application](#Starting the application)


## Features

- Browse and read the latest news articles.
- Search for specific topics or keywords.
- Search in a range of dates (the left date - from date, the right date - to date)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 20 
- Maven
- Node.js and npm

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/noam546/News-Application.git
   cd news-application
   npm install
	```
2. Set up the Server:
	```bash
   cd server
   mvn clean install
	```
3. Set up the Client:
	```bash
   cd client
   npm install
	```

### Starting the application 

1. Starting the Server:
	```bash
   cd server
   mvn spring-boot:run
	```
2. Starting the Client:
	```bash
   cd client
   npm run dev
	```