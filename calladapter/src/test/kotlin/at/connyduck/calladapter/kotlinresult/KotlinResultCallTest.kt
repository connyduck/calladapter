package at.connyduck.calladapter.kotlinresult

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

public class KotlinResultCallTest {

    private val backingCall = TestCall<String>()
    private val kotlinResultCall = KotlinResultCall(backingCall)

    @Test
    public fun `should throw an error when invoking 'execute'`() {
        assertThrows<UnsupportedOperationException> {
            kotlinResultCall.execute()
        }
    }

    @Test
    public fun `should delegate properties to backing call`() {
        with(kotlinResultCall) {
            assertEquals(isExecuted, backingCall.isExecuted)
            assertEquals(isCanceled, backingCall.isCanceled)
            assertEquals(request(), backingCall.request())
        }
    }

    @Test
    public fun `should return new instance when cloned`() {
        val clonedCall = kotlinResultCall.clone()
        assert(clonedCall !== kotlinResultCall)
    }

    @Test
    public fun `should cancel backing call as well when cancelled`() {
        kotlinResultCall.cancel()
        assert(backingCall.isCanceled)
    }

    @Test
    public fun `should parse successful call as Result-success`() {
        val body = "Test body"
        kotlinResultCall.enqueue(
            object : Callback<Result<String>> {
                override fun onResponse(
                    call: Call<Result<String>>,
                    response: Response<Result<String>>
                ) {
                    assertTrue(response.isSuccessful)
                    assertEquals(
                        response.body(),
                        Result.success(body)
                    )
                }

                override fun onFailure(call: Call<Result<String>>, t: Throwable) {
                    throw IllegalStateException()
                }
            }
        )
        backingCall.complete(body)
    }

    @Test
    public fun `should parse call with 404 error code as Result-failure`() {
        val errorCode = 404
        val errorBody = "not found"
        kotlinResultCall.enqueue(
            object : Callback<Result<String>> {
                override fun onResponse(
                    call: Call<Result<String>>,
                    response: Response<Result<String>>
                ) {
                    assertEquals(
                        Result.failure<TestResponseClass>(
                            object : HttpException(Response.error<String>(errorCode, errorBody.toResponseBody())) {
                                override fun equals(other: Any?): Boolean {
                                    return (other is HttpException) && other.code() == code() && other.message() == message()
                                }
                            }
                        ),
                        response.body(),
                    )
                }

                override fun onFailure(call: Call<Result<String>>, t: Throwable) {
                    throw IllegalStateException()
                }
            }
        )

        backingCall.complete(Response.error(errorCode, errorBody.toResponseBody()))
    }

    @Test
    public fun `should parse call with IOException as Result-failure`() {
        val exception = IOException()
        kotlinResultCall.enqueue(
            object : Callback<Result<String>> {
                override fun onResponse(
                    call: Call<Result<String>>,
                    response: Response<Result<String>>
                ) {
                    assertEquals(
                        response.body(),
                        Result.failure<String>(exception)
                    )
                }

                override fun onFailure(call: Call<Result<String>>, t: Throwable) {
                    throw IllegalStateException()
                }
            }
        )

        backingCall.completeWithException(exception)
    }
}
