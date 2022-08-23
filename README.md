# Book Store

The aim of this app is to learn Kotlin using the [Google Books API](https://developers.google.com/books/docs/overview). 
It connects to the API getting all the books with the current query being "android", we can select a book, go to the detail and save it as Favorite.
This app consists of 2 pieces of UI right now:

1. Home with all the books and Favorites
2. Book Detail

This app is under development. 


## Android Development and Architecture

* The entire codebase is in [Kotlin](https://kotlinlang.org/)
* Uses MVVM Architecture by [Architecture Components](https://developer.android.com/topic/libraries/architecture/). Room, ViewModel
* Uses [Dagger](https://dagger.dev/) for dependency injection
* Uses [Retrofit](https://square.github.io/retrofit/) for API connection
* Uses [Glide](https://bumptech.github.io/glide/) for Image Loading
