package org.oso.core.controller

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets

fun testFailOnMissingParameters(mockMvc: MockMvc, urlTemplate: String, parameters: List<Pair<String, String>>) {
    for(size in 1..(parameters.size - 1)) {
        val temp = mutableListOf<Pair<String, String>>()
        testFailOnMissingParameters(mockMvc, urlTemplate, parameters, size, temp)
    }
}

private fun testFailOnMissingParameters(mockMvc: MockMvc, urlTemplate: String, parameters: List<Pair<String, String>>, size: Int, temp: MutableList<Pair<String, String>>) {
    when {
        size == 0 -> {
            val json = "{" + temp.map { """ "${it.first}":"${it.second}" """ }.joinToString(",") + "}"

            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        parameters.size < size -> return

        else -> {
            for (i in parameters.indices) {
                val tempNew = temp.toMutableList()
                tempNew.add(parameters[i])

                testFailOnMissingParameters(mockMvc, urlTemplate, parameters.subList(i + 1, parameters.size), size - 1, tempNew)
            }
        }
    }
}