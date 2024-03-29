
# DES2: GT Bus Discrete Event Simulation
Report: https://docs.google.com/document/d/1fHYXjvcPzBmfSv0OkZdOE4SzlAvXD3rKUOb3p4F1ecw/edit?usp=sharing

## My contributions to the project
This was is the course project for CS 4230/CX 6730 Modeling & Simulation. The objective of this project was to create a design and implement a simulation based on a conceptual model, gather and analyze data, and extract meaningful results. I led the team on this project. I designed and implemented the GT Bus discrete event simulation, allocated tasks to the team, and finalized the report. See 'GroupPlan.txt' to see some sprint planning and task allocation in the initial stages of the project.

## Introduction
Welcome to DES2, a comprehensive Java-based discrete event simulation framework designed to model and analyze traffic patterns and public transport systems. Our project leverages an event-driven approach to simulate real-world scenarios involving road traffic, bus stops, passengers, and traffic lights.

## Key Components
- **Simulation Executive**: `SimulationExecutive.java` orchestrates the entire simulation process.
- **Event System**: Classes like `DriverBreakEvent.java`, `ArriveAtBusStopEvent.java`, etc., represent various events in the traffic system.
- **Simulation Entities**: `Bus.java`, `Passenger.java`, `TrafficLight.java`, etc., define the primary entities in the simulation.
- **Simulation Driverss**: Java files beginning with `DES` represent the drivers where the simulation environment is created with specified inputs and ran on
- **Express Bus**: `Utility.java` contains express route functionality
- **Visualization**: `RouteVisualizer.java` contains functionality on visualizing the route
- **Data Collection**: `DataExport.java` is used to generate CSV files (`BusStops.csv`, `Passengers.csv`), which store important simulation data.
- **Data Analysis**: Python-based Jupyter notebooks in the top directory should be ran on the .csv files in /DES2 and /Data to produce results/analysis

## Getting Started
1. **Prerequisites**: Ensure you have Java JDK 8 or later installed.
2. **Installation**: Clone this repository and import it into your preferred Java IDE or compile the code using a Java compiler.
3. **Running the Simulation**: Compile all `*.java` files and then run the DES*.java Driver programs. This should produce output to the terminal and generate data CSV files
4. **Performing data analysis**: Run the jupyter notebooks in the topdir after CSV files from the driver have been generated
5. **Customization**: Set up your own simulation environment with bus routes and buses in a new driver, following the convention in the previous Drivers

## How to Contribute
Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request




