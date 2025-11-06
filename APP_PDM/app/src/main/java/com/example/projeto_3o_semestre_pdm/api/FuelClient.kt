package com.example.projeto_3o_semestre_pdm.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result
import com.google.gson.Gson

object FuelClient {

    // TODO: Replace with your actual API base URL
    private const val BASE_URL = "https://your-api-url.com/api/"

    private val gson = Gson()

    // GET request
    inline fun <reified T : Any> get(
        endpoint: String,
        parameters: List<Pair<String, Any?>>? = null,
        headers: Map<String, String>? = null,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (FuelError) -> Unit
    ) {
        var request = Fuel.get("$BASE_URL$endpoint", parameters)

        headers?.forEach { (key, value) ->
            request = request.header(key, value)
        }

        request.responseObject<T> { _, _, result ->
            when (result) {
                is Result.Success -> onSuccess(result.value)
                is Result.Failure -> onFailure(result.error)
            }
        }
    }

    // POST request
    inline fun <reified T : Any> post(
        endpoint: String,
        body: Any? = null,
        headers: Map<String, String>? = null,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (FuelError) -> Unit
    ) {
        var request = Fuel.post("$BASE_URL$endpoint")

        // Add headers
        request = request.header("Content-Type" to "application/json")
        headers?.forEach { (key, value) ->
            request = request.header(key, value)
        }

        // Add body
        if (body != null) {
            val jsonBody = gson.toJson(body)
            request = request.body(jsonBody)
        }

        request.responseObject<T> { _, _, result ->
            when (result) {
                is Result.Success -> onSuccess(result.value)
                is Result.Failure -> onFailure(result.error)
            }
        }
    }

    // PUT request
    inline fun <reified T : Any> put(
        endpoint: String,
        body: Any? = null,
        headers: Map<String, String>? = null,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (FuelError) -> Unit
    ) {
        var request = Fuel.put("$BASE_URL$endpoint")

        request = request.header("Content-Type" to "application/json")
        headers?.forEach { (key, value) ->
            request = request.header(key, value)
        }

        if (body != null) {
            val jsonBody = gson.toJson(body)
            request = request.body(jsonBody)
        }

        request.responseObject<T> { _, _, result ->
            when (result) {
                is Result.Success -> onSuccess(result.value)
                is Result.Failure -> onFailure(result.error)
            }
        }
    }

    // DELETE request
    fun delete(
        endpoint: String,
        headers: Map<String, String>? = null,
        onSuccess: () -> Unit,
        onFailure: (FuelError) -> Unit
    ) {
        var request = Fuel.delete("$BASE_URL$endpoint")

        headers?.forEach { (key, value) ->
            request = request.header(key, value)
        }

        request.response { _, _, result ->
            when (result) {
                is Result.Success -> onSuccess()
                is Result.Failure -> onFailure(result.error)
            }
        }
    }
}