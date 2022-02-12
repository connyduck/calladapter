# Kotlin Result CallAdapter for Retrofit

A `CallAdapter` for [Retrofit](https://github.com/square/retrofit) that allows to handle network calls with suspending functions that return Kotlin's [`Result`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/).

## Usage

Kotlin Result CallAdapter is published on Maven Central, so make sure you have it in your repository list:

```
repositories {
    mavenCentral()
}
```

Add Kotlin Result CallAdapter to your dependencies:

```
implementation "at.connyduck:kotlin-result-calladapter:1.0.0"
```

Install the CallAdapterFactory:

```Kotlin
Retrofit.Builder()
    .baseUrl("https://example.org")
    .addCallAdapterFactory(KotlinResultCallAdapterFactory.create())
    // additional configuration...
    .build()
```

Define the call like this in your Retrofit interface:

```Kotlin
@GET("api/v1/example/{id}")
suspend fun getData(id:String): Result<Example>
```

And then make the call like this

```Kotlin
api.getData("1").fold(
    { example ->
        // Do something with the response object
    },
    { throwable ->
        if (throwable is HttpException) {
            // server returned a non-success code
        } else {
            // another problem occurred, probably a network error
        }
    }
)
```

## License

```
Copyright 2022 Conny Duck

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
