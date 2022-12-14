# Currency Converter APP

### Build With 🏗️

- Kotlin - Programming language for Android
- Hilt-Dagger - Standard library to incorporate Dagger dependency injection into an Android application.
- Retrofit -  A HTTP client.
- Coroutines - For asynchronous
- WorkManager - WorkManager is the recommended library for persistent work.
- LiveData - Data objects that notify views.
- ViewModel - Stores UI-related data that isn't destroyed on UI changes.
- ViewBinding - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.

### Data flow

- Use repository because repository combine different data sources and solve any potential conflicts between the data sources to update the single source of truth regularly.
- In order to provide offline-first support, a local data source—such as a database—is the recommended source of truth.
- Used mutable and immutable variable in viewModel that benefits following:


1. It centralizes all the changes to a particular type of data in one place.
2. It protects the data so that other types cannot tamper with it.
3. It makes changes to the data more traceable. Thus, bugs are easier to spot.

### Project Architecture 🗼

This app uses [MVVM (Model View View-Model)] architecture.

