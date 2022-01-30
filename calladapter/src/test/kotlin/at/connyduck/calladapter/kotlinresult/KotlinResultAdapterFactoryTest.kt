package at.connyduck.calladapter.kotlinresult

import com.squareup.moshi.Types
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit

public class KotlinResultAdapterFactoryTest {

    private val retrofit = Retrofit.Builder().baseUrl("http://example.com").build()

    @Test
    public fun `should return a KotlinResultCallAdapter when the type is supported`() {
        val responseType =
            Types.newParameterizedType(Result::class.java, TestResponseClass::class.java)
        val callType = Types.newParameterizedType(Call::class.java, responseType)

        val adapter = KotlinResultCallAdapterFactory().get(callType, arrayOf(), retrofit)

        assertEquals(TestResponseClass::class.java, adapter?.responseType())
    }

    @Test
    public fun `should return null if the type is not supported`() {

        val adapter = KotlinResultCallAdapterFactory().get(TestResponseClass::class.java, arrayOf(), retrofit)

        assertNull(adapter)
    }
}
