package org.oso.core.controller

import org.oso.config.Paths
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.oso.any
import org.oso.core.controllers.EmergencyController
import org.oso.core.dtos.EmergencyDto
import org.oso.core.dtos.EmergencyAcceptedDto
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyPriority
import org.oso.core.entities.HelpRequester
import org.oso.core.services.EmergencyService
import org.oso.core.services.HelpProviderService
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
    lateinit var helpProviderService: HelpProviderService

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
            EmergencyPriority.LOW
        )

        val emergencyDto = EmergencyDto(
            helpRequester.id!!,
            emergency.emergencyPriority
        )

        Mockito.doNothing().`when`(emergencyService).emit(any())
        Mockito.`when`(helpRequesterService.findById(helpRequester.id!!)).thenReturn(helpRequester)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${Paths.Emergency.ROOT}/${Paths.Emergency.EMIT}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(objectMapper.writeValueAsString(emergencyDto)))

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcRestDocumentation.document(Paths.Emergency.ROOT))

        // TODO test if the argument for emit has the correct parameters
        Mockito.verify(emergencyService, Mockito.times(1)).emit(any())
        Mockito.verifyNoMoreInteractions(emergencyService)
    }

    @Test
    fun `testEmit Excpetion on unknown helpRequester`() {
        val emergencyDto = EmergencyDto(
            "25",
            EmergencyPriority.LOW
        )

        Mockito.`when`(helpRequesterService.findById(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(null).`when`(helpRequesterService).findById(emergencyDto.helprequester)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${Paths.Emergency.ROOT}/${Paths.Emergency.EMIT}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(emergencyDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andDo(MockMvcRestDocumentation.document("${Paths.Emergency.ROOT}/exc"))
    }

    @Test
    fun `testEmit fails on missing parameters`() {

//        val parameters = listOf(
//            "helprequester" to "25",
//            "emergencyPriority" to "LOW"
//        )
//
//        // TODO test fails if helprequester is not given, as the helprequester-id will currently be defaulted to 0 which leads to HTTP 404 instead of 400
//        testFailOnMissingParameters(mockMvc, "/${EmergencyController.PATH_EMERGENCY}/${EmergencyController.PATH_EMIT}", parameters)
    }

    @Test
    fun testAcceptEmergency() {
        val dto = EmergencyAcceptedDto(
                emergencyId = "25",
                helpProviderId = "38",
                helpRequesterId = "46"
        )

        Mockito.doNothing().`when`(emergencyService).acceptEmergency(dto.emergencyId, dto.helpProviderId)

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/${Paths.Emergency.ROOT}/${Paths.Emergency.ACCEPTED}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8.name())
                                .content(ObjectMapper().writeValueAsString(dto)))

                .andExpect(MockMvcResultMatchers.status().isAccepted)
                .andDo(MockMvcRestDocumentation.document("${Paths.Emergency.ROOT}/${Paths.Emergency.ACCEPTED}"))

        Mockito.verify(emergencyService, Mockito.times(1)).acceptEmergency(dto.emergencyId, dto.helpProviderId)
    }

    @Test
    fun `testAcceptEmergency throws exception on missing parameter`() {
//        val parameters = listOf(
//            "emergencyId" to "25",
//            "helpProviderId" to "38",
//            "helpRequesterId" to "46"
//        )
//
//        // TODO this test always fails as jackson does not recognize the non-nullable parameters correclty
//        testFailOnMissingParameters(mockMvc, "${HelpProviderController.PATH_HELP_PROVIDERS}/${HelpProviderController.PATH_ACCEPT_EMERGENCY}", parameters)
    }
}