# The Challenge:

The challenge is to create a simple Android app that exercises a REST-ful API. The API endpoint `https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB&offset=0` returns a JSON object which is a list of different books published by the New York Times. 

Using this endpoint, show a list of these items, with each row displaying at least the following data:

- Image
- Title
- Description 

### Technical Instructions:
- MVP architecture
- Demonstrate use of Dagger, Retrofit and Glide (for the images)
- Feel free to make any assumptions you want along the way or use any third party libraries as needed and document why you used them.

### General Instructions:
- This isn't a visual design exercise. For example, if you set random background colors to clearly differentiate the views when debugging, pick Comic Sans or Papyrus, we won't hold that against you. Well, maybe a little bit if you use Comic Sans :)
- This is also most of the code you'll be showing us – don't understimate the difficulty of the task, or the importance of this exercise in our process, and rush your PR. Put up your best professional game.
- This isn't just about handling the happy path. Think slow network (or no network at all), supporting different device sizes, ease of build and run of the project. If we can't check out and click the run button in Android Studio, you're off to a bad start (we've had PRs without a graddle for instance).
- Explanations on any choice you've made are welcome.
- We appreciate there's a lot that is asked in this exercise. If you need more time, feel free to ask. If you need to de-prioritize something, apply the same judgement you would on a professional project, argument your decision. 

Bonus Points:
  - Unit tests

---

### Project description

This is an approach to the requested requirement of an application that shows the list of elements (books).
The general idea is to have an Android application with three screens, a splash screen, a screen with the list of elements and a last screen with the details of each element.
The app uses a clean architecture with MVP (Model-View-Presenter) design pattern and demonstrates various technologies including Retrofit, Dagger, Room, RxJava, and Navigation Architecture Component.

#### Architecture
##### Clean Architecture Layers
1. Presentation Layer
  - **View (Fragment):** Displays data and handles user interactions.
  - **Presenter:** Contains the presentation logic and communicates with the Use Cases to fetch data.

2. Domain Layer
  - **Use Cases:** Encapsulate the application's business logic. Example: GetBooksUseCase, GetBookDetailsUseCase.
  - **Models:** Business models used within the application logic.

3. Data Layer
  - **Repositories**: Responsible for fetching data from data sources (API, database) and converting it to domain models.
  - **Data Sources**: Include API service interfaces and DAOs for database access.
  - **Mappers**: Convert data models to domain models and vice versa.

##### MVP Design Pattern

1. **Model**: Represents the data and business logic.
2. **View**: Displays data and routes user commands to the Presenter.
3. **Presenter**: Retrieves data from the Model, applies presentation logic, and updates the View.

#### Technologies Used

- **Kotlin**: Programming language for Android development.
- **Retrofit**: For making network requests to the New York Times Books API.
- **Dagger**: For dependency injection.
- **Room**: For local database storage.
- **RxJava**: For handling asynchronous operations.
- **Glide**: For image loading and caching.
- **JUnit & Mockito**: For unit testing.
- **Navigation Architecture Component**: For managing navigation and fragment transitions.

#### Project Structure

├───data  
│   ├───api  
│   ├───database  
│   ├───di  
│   ├───mapper  
│   ├───model  
│   └───repository  
├───di  
├───domain  
│   ├───executor  
│   ├───mapper  
│   ├───model  
│   ├───repository  
│   └───usecase  
├───presentation  
│   ├───model  
│   ├───presenter  
│   └───view  
└───utils  
 
#### Known Issues
There is an unresolved bug where the loadBookDetails method in BookDetailPresenter does not always reach the subscribe block. This might be related to threading issues or improper disposal of RxJava subscriptions. This bug prevents the detail information from being painted in the book detail fragment.
Further debugging is required to pinpoint the exact cause and resolve the issue.


