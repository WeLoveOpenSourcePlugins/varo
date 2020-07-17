package org.wlosp.varo.util

import com.google.gson.JsonParser
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

object AccountUtil {
    private val PROFILES_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft".toHttpUrl()
    private val uuidPattern = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex()

    val httpClient = OkHttpClient()
    private val jsonParser = JsonParser()

    fun fetchUUID(name: String): UUID? {
        val request = Request.Builder()
            .url(PROFILES_ENDPOINT.newBuilder().addPathSegment(name).build())
            .get()
            .build()

        val response = httpClient.newCall(request).execute()

        return response.use {
            if (it.code != 200) return null
            val json = it.body?.string()?.let { body ->
                jsonParser.parse(body).asJsonObject
            } ?: return null

            if (json.has("error")) {
                null
            } else {
                UUID.fromString(json.get("id").asString.replace(uuidPattern, "$1-$2-$3-$4-$5"))
            }
        }
    }
}