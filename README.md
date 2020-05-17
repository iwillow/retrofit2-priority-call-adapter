Retrofit2 Priority Call Adapter
========================
A Retrofit 2 `CallAdapter.Factory` for [PriorityBlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/PriorityBlockingQueue.html).

Usage
-----
Add `PriorityBlockCallAdapterFactory` as a `Call` adapter when building your `Retrofit` instance:
```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
	.addConverterFactory(ScalarsConverterFactory.create())
	.addConverterFactory(GsonConverterFactory.create())
     // can only run single one task at the same time order by priority
     .addCallAdapterFactory(PriorityBlockCallAdapterFactory.create())
     // can  run  5 tasks at the same time  order by priority
     //.addCallAdapterFactory(PriorityBlockCallAdapterFactory.create(5))
    .build()
```
Your service methods can now use `PriorityBlockCall` as their return type.

```kotlin
interface MyService {

   @Priority(
        value = TaskPriority.PRIORITY_MIDDLE,//the priority of this call
        description = "getContributors"
    )
    @GET("repos/{owner}/{repo}/contributors")
    fun contributors(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): PriorityBlockCall<String?>


   @GET("repos/{owner}/{repo}/contributors")
    fun contributors2(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): PriorityBlockCall<String?>

}

retrofit.create(MyService::class.java)
        .contributors("square", "retrofit")
        .enqueueBlockCall(
                onResponse = { call: Call<String?>?, response: Response<String?> ->
                  //do something on success callback

                },
                onFailure = { call: Call<String?>?, t: Throwable ->
                  //do something on failure callback
                }
            )

retrofit.create(MyService::class.java)
        .contributors2("square", "retrofit")
        .enqueueBlockCall(
		        TaskPriority.PRIORITY_HIGH,//set the priority at call site
                onResponse = { call: Call<String?>?, response: Response<String?> ->
                  //do something on success callback

                },
                onFailure = { call: Call<String?>?, t: Throwable ->
                  //do something on failure callback
                }
            )

  val response = retrofit.create(SampleApi::class.java)
            .contributors2("square", "retrofit")
            //  Synchronously send the request and return its response.
            .executeBlockCall(TaskPriority.PRIORITY_HIGH)
```

[ ![Download](https://api.bintray.com/packages/iwillow/maven/retrofit2-priority-call-adapter/images/download.svg) ](https://bintray.com/iwillow/maven/retrofit2-priority-call-adapter/_latestVersion)
--------
 Gradle
```groovy
buildscript {
 repositories {
    google()
    jcenter()
    maven { url 'https://dl.bintray.com/iwillow/maven' }
  }
}
allprojects {
    repositories {
    google()
    jcenter()
    maven { url 'https://dl.bintray.com/iwillow/maven' }
  }
}
dependencies {
   implementation 'com.iwillow:retrofit2-priority-call-adapter:0.0.7'
}
```
License
=======

    Copyright 2020 iwillow (dogedoge233@hotmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.