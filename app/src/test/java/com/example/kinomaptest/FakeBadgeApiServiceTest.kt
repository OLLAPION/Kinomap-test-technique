@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.kinomaptest

import com.example.kinomaptest.data.remote.api.FakeBadgeApiService
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeBadgeApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var service: FakeBadgeApiService

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        service = Retrofit.Builder()
            .baseUrl(server.url("/")) // important
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(FakeBadgeApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getBadges parses response and hits correct path`() = runTest {
        val json = """
        {
          "data": [
            {
              "name": "Travel",
              "badges": [
                {
                  "id": 15,
                  "name": "Trained in Oceania",
                  "description": "Trained on a Oceania based video",
                  "action": "activity_trained_OC",
                  "category": "Travel",
                  "unlocked_date": 1736780630,
                  "unlocked_percent": null,
                  "images_url": {
                    "unlocked": "https://static.kinomap.com/badges/activity_trained_OC-win.png",
                    "locked": "https://static.kinomap.com/badges/activity_trained_OC.png"
                  }
                }
              ]
            }
          ]
        }
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = service.getBadges()

        // parsing
        assertEquals(1, response.data.size)
        assertEquals("Travel", response.data.first().name)
        assertEquals(1, response.data.first().badges.size)
        assertEquals(15, response.data.first().badges.first().id)

        // request path
        val request = server.takeRequest()
        assertTrue(request.path!!.startsWith("/v4/badges/mobile-tech-test?appToken="))
    }
}
