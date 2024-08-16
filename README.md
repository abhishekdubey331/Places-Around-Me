# Nearby-Places-Foursquare-Android

## About
This sample project demonstrates the implementation of Clean Architecture principles in an Android application. It utilizes Kotlin Flows for efficient user location fetching and networking with the Foursquare API. The project emphasizes code quality and reliability through thorough unit testing across view models, repositories, and use cases.

## Clean Architecture

- **Layers**: The architecture is divided into distinct layers, each with its own responsibility:
  - **Presentation Layer**: Manages UI and user interaction. It includes Activities, Fragments, and ViewModels.
  - **Domain Layer**: Contains business logic and use cases. It is independent of other layers and focuses on core functionality.
  - **Data Layer**: Handles data operations and communicates with external sources such as APIs or databases. It includes repositories and data sources.

- **Dependency Rule**: Dependencies flow inward, meaning that higher-level modules (e.g., presentation) depend on lower-level modules (e.g., domain), but not vice versa. This keeps business logic separate from UI and data concerns.

- **Kotlin Flows**: Used for handling asynchronous operations and data streams, providing a seamless and efficient way to manage user location and network responses.

- **Unit Testing**: Thorough unit testing is implemented for view models, repositories, and use cases to ensure code reliability and robustness.

## APIs Used from Foursquare
- **Get Key from Foursquare:** [Foursquare Developer Portal](https://developer.foursquare.com/)
- **Nearby Places API:** [Foursquare Nearby Places](https://developer.foursquare.com/reference/places-nearby)
- **Search Places API:** [Foursquare Place Search](https://developer.foursquare.com/reference/place-search)

## License :oncoming_police_car:
