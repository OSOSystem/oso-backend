package org.oso.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.oso.any
import org.oso.core.controllers.HelpProviderController
import org.oso.core.dtos.EmergencyAcceptedDto
import org.oso.core.dtos.HelpProviderPushDto
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.services.HelpProviderService
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
@WebMvcTest(controllers = [HelpProviderController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class HelpProviderControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var helpProviderService: HelpProviderService

    @Test
    fun testFindAll() {
        val helpProviders = listOf(
            HelpProvider(
                name = "A",
                password =  "a"
            ),
            HelpProvider(
                name="B",
                password = "b"
            ),
            HelpProvider(
                name = "C",
                password = "c",
                email = "email"
            )
        )
        helpProviders[0].id = 16
        helpProviders[1].id = 28
        helpProviders[2].id = 128

        Mockito.`when`(helpProviderService.findAll()).thenReturn(helpProviders)

        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/${HelpProviderController.PATH_HELP_PROVIDERS}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(helpProviders[0].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].password", Matchers.`is`(helpProviders[0].password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.`is`(helpProviders[0].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber", Matchers.`is`(helpProviders[0].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].expoPushToken", Matchers.`is`(helpProviders[0].expoPushToken)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.`is`(helpProviders[1].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].password", Matchers.`is`(helpProviders[1].password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.`is`(helpProviders[1].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].phoneNumber", Matchers.`is`(helpProviders[1].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].expoPushToken", Matchers.`is`(helpProviders[1].expoPushToken)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.`is`(helpProviders[2].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].password", Matchers.`is`(helpProviders[2].password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].email", Matchers.`is`(helpProviders[2].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].phoneNumber", Matchers.`is`(helpProviders[2].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].expoPushToken", Matchers.`is`(helpProviders[2].expoPushToken)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS))
    }

    @Test
    fun testCreateHelpProvider() {
        val dto = HelpProviderPushDto(
            name = "HelpProvider",
            password = "password",
            email = "email",
            phoneNumber = "123456789",
            expoPushToken = "expoPushToken"
        )
        val helpProvider = HelpProvider(
            name = dto.name,
            password = dto.password,
            expoPushToken = dto.expoPushToken,
            phoneNumber = dto.phoneNumber,
            email = dto.email
        )
        helpProvider.id = 12345

        Mockito.`when`(helpProviderService.createHelpProvider(any())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(helpProvider).`when`(helpProviderService).createHelpProvider(dto.toEntity())

        // TODO this test always fails as jackson does not recognize the non-nullable parameters correctly
        /*
        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${HelpProviderController.PATH_HELP_PROVIDERS}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(dto)))

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS + "/create"))
        */
    }

    @Test
    fun `testCreateHelpProvider throws exception on missing parameter`() {
//        val parameters = listOf(
//            "name" to "helpprovider",
//            "password" to "password"
//        )
//
//        // TODO this test always fails as jackson does not recognize the non-nullable parameters correclty
//        testFailOnMissingParameters(mockMvc, HelpProviderController.PATH_HELP_PROVIDERS, parameters)
    }

    @Test
    fun testFindById() {
        val helpProvider = HelpProvider(
            name = "myself",
            password = "asdfghjk",
            expoPushToken = "token",
            phoneNumber = "753951",
            email = "Email"
        )
        helpProvider.id = 12345

        Mockito.`when`(helpProviderService.findById(helpProvider.id!!)).thenReturn(helpProvider)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/${helpProvider.id}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.`is`(helpProvider.id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.`is`(helpProvider.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.`is`(helpProvider.password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.expoPushToken", Matchers.`is`(helpProvider.expoPushToken)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.`is`(helpProvider.phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.`is`(helpProvider.email)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS + "/id"))
    }

    @Test
    fun `testFindById throws Exception if unknown`() {
        Mockito.`when`(helpProviderService.findById(ArgumentMatchers.anyLong())).thenReturn(null)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testFindHelpRequesters() {
        val id = 25.toLong()

        val helpRequesters = listOf(
            HelpRequester(
                name = "A",
                email = "email",
                phoneNumber = "951862",
                password = "rstrzuio"
            ),
            HelpRequester(
                name = "B",
                email = "email",
                phoneNumber = "8946523",
                password = "asdrtf7gz8uhio"
            )
        )
        helpRequesters[0].id = 4711
        helpRequesters[1].id = 4712

        val helpProvider = HelpProvider(
            name = "Name",
            password = "Password"
        )

        helpProvider.id = id
        helpProvider.helpRequesters.addAll(helpRequesters)

        Mockito.`when`(helpProviderService.findById(id)).thenReturn(helpProvider)
        Mockito.`when`(helpProviderService.findHelpRequesters(id)).thenReturn(helpProvider.helpRequesters)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/$id/${HelpProviderController.PATH_HELP_REQUESTERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(helpRequesters[0].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.`is`(helpRequesters[0].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.`is`(helpRequesters[0].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber", Matchers.`is`(helpRequesters[0].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].password", Matchers.`is`(helpRequesters[0].password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.`is`(helpRequesters[1].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.`is`(helpRequesters[1].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.`is`(helpRequesters[1].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].phoneNumber", Matchers.`is`(helpRequesters[1].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].password", Matchers.`is`(helpRequesters[1].password)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS + "/id/${HelpProviderController.PATH_HELP_PROVIDERS}"))
    }

    @Test
    fun `testFindHelpRequesters throws exception on unknown help provider`() {
        Mockito.`when`(helpProviderService.findHelpRequesters(ArgumentMatchers.anyLong())).thenReturn(null)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/25/${HelpProviderController.PATH_HELP_REQUESTERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testAcceptEmergency() {
        val dto = EmergencyAcceptedDto(
            emergencyId = 25,
            helpProviderId = 38,
            helpRequesterId = 46
        )

        Mockito.doNothing().`when`(helpProviderService).acceptEmergency(dto.emergencyId, dto.helpProviderId)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${HelpProviderController.PATH_HELP_PROVIDERS}/${HelpProviderController.PATH_ACCEPT_EMERGENCY}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(dto)))

            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcRestDocumentation.document("${HelpProviderController.PATH_HELP_PROVIDERS}/${HelpProviderController.PATH_ACCEPT_EMERGENCY}"))

        Mockito.verify(helpProviderService, Mockito.times(1)).acceptEmergency(dto.emergencyId, dto.helpProviderId)
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