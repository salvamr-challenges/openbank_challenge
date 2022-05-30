
## Modules distribution

<img src="https://i.imgur.com/M59L94S.png" alt="Modules" width="600"/>  

I usually divide Android applications following the above diagram. This approach allows us to separate the responsibilities clear.

Modules:

* **Presentation**: this module is completely written in Kotlin with Android references, and contains all the UI elements. It's responsible for:
  * *Activities/Fragments*.
  * *ViewModels*.
* **Domain**: this module is completely written in Kotlin, without Android references, and contains the business logic. It's responsible for:
  * *Domain models*.
  * *Repositories*.
  * *Use cases*.
* **Data**: this module is completely written in Kotlin with Android references. Contains the implementation of our data fetchers. It's responsible for:
  * *Data models*
  * *Data sources*
  * *Repository implementations*
* **App**: this module is completely written in Kotlin with Android references. It's responsible for manage:
  * *Dependency injection*.
  * *The Android application itself*.


## Architecture

<img src="https://i.imgur.com/18ojN9W.png" alt="Clean Architecture" width="800"/>  

**Business layer**:

* **Data Source**: each data source is responsible to fetch data from its clients. It could be an API client, an in memory cache, a database... Data sources must convert data models to domain models, this is necessary to  combine all of them into a repository.
  * **Remote Data Source**.
  * **Cache Data Source**
* **Database Data Source** (for this test I consider to not use a database client)
* **Repository**: the repository combines from 1 to N data sources to fetch data from the most suitable data source. In this case, our repository tries to fetch data from the cache and, if it's empty, tries to fetch data from the API data source.
*  **Use case**: it combines from 1 to N repositories to perform our business logic.

**Presentation layer** (*MVVM*):

* **ViewModel**: a *ViewModel* injects from 1 to N use cases and subscribes to them to perform our business logic. Once the use case has completed its operations, it updates a *StateFlow* object to update the view.
* **Activities/Fragments/Custom views**: they only handle what the viewmodel tells them to print and all the posible UI related logic.

### Api service configuration
Drop these lines to your local.properties file

api.key=YOUR_PUBLIC_KEY
api.key.private=YOUR_PRIVATE_KEY
api.url=https://gateway.marvel.com:443/v1/