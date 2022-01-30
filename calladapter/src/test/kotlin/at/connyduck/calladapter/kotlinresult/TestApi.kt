package at.connyduck.calladapter.kotlinresult

import retrofit2.http.GET

public interface TestApi {

    @GET("testpath")
    public suspend fun testEndpoint(): Result<TestResponseClass>
}
