package org.oso.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Matchers
import org.mockito.Mockito
import org.oso.any
import org.oso.core.controllers.EmergencyController
import org.oso.core.dtos.EmergencyDto
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyType
import org.oso.core.entities.HelpRequester
import org.oso.core.services.EmergencyService
import org.oso.core.services.HelpRequesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [EmergencyController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class EmergencyControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var emergencyService: EmergencyService

    @MockBean
    lateinit var helpRequesterService: HelpRequesterService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun testEmit() {
        val helpRequester = HelpRequester(
            name = "HelpRequester",
            keycloakName = "Keycloak4711",
            id = "UUID-4711-0815"
        )

        val emergency = Emergency(
            "4711",
            helpRequester,
            EmergencyType.LOW
        )

        val emergencyDto = EmergencyDto(
            helpRequester.id!!,
            emergency.emergencyType
        )

        Mockito.doNothing().`when`(emergencyService).emit(any())
        Mockito.`when`(helpRequesterService.findById(helpRequester.id!!)).thenReturn(helpRequester)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${EmergencyController.PATH_EMERGENCY}/${EmergencyController.PATH_EMIT}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(objectMapper.writeValueAsString(emergencyDto)))

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcRestDocumentation.document(EmergencyController.PATH_EMERGENCY))

        // TODO test if the argument for emit has the correct parameters
        Mockito.verify(emergencyService, Mockito.times(1)).emit(any())
        Mockito.verifyNoMoreInteractions(emergencyService)
    }

    @Test
    fun `testEmit Excpetion on unknown helpRequester`() {
        val emergencyDto = EmergencyDto(
            "25",
            EmergencyType.LOW
        )

        Mockito.`when`(helpRequesterService.findById(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(null).`when`(helpRequesterService).findById(emergencyDto.helprequester)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${EmergencyController.PATH_EMERGENCY}/${EmergencyController.PATH_EMIT}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(emergencyDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andDo(MockMvcRestDocumentation.document("${EmergencyController.PATH_EMERGENCY}/exc"))
    }

    @Test
    fun `testEmit fails on missing parameters`() {

//        val parameters = listOf(
//            "helprequester" to "25",
//            "emergencyType" to "LOW"
//        )
//
//        // TODO test fails if helprequester is not given, as the helprequester-id will currently be defaulted to 0 which leads to HTTP 404 instead of 400
//        testFailOnMissingParameters(mockMvc, "/${EmergencyController.PATH_EMERGENCY}/${EmergencyController.PATH_EMIT}", parameters)
    }
}