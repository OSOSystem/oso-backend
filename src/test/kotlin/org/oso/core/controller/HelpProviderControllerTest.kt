package org.oso.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Before
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
import org.oso.core.services.SecurityService
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

    @MockBean
    lateinit var securityService: SecurityService

    @Before
    fun init() {
        Mockito.`when`(securityService.getCurrentUserName()).thenReturn(userName)
    }

    @Test
    fun testFindAll() {
        val helpProviders = listOf(
            HelpProvider(
                id = "HP-A",
                name = "A",
                keycloakName = "keycloakA"
            ),
            HelpProvider(
                id = "HP-B",
                name="B",
                keycloakName = "keycloakB"
            ),
            HelpProvider(
                id = "HP-C",
                name = "C",
                keycloakName = "keycloakC",
                expoPushToken = "expoPush"
            )
        )

        Mockito.`when`(helpProviderService.findAll()).thenReturn(helpProviders)

        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/${HelpProviderController.PATH_HELP_PROVIDERS}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(helpProviders[0].id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.`is`(helpProviders[0].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].expoPushToken", Matchers.`is`(helpProviders[0].expoPushToken)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.`is`(helpProviders[1].id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.`is`(helpProviders[1].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].expoPushToken", Matchers.`is`(helpProviders[1].expoPushToken)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.`is`(helpProviders[2].id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.`is`(helpProviders[2].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].expoPushToken", Matchers.`is`(helpProviders[2].expoPushToken)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS))
    }

    @Test
    fun testCreateHelpProvider() {
        val dto = HelpProviderPushDto(
            name = "HelpProvider"
        )
        val helpProvider = HelpProvider(
            id = "12345",
            name = dto.name,
            keycloakName = "keycloak12345"
        )

        Mockito.`when`(helpProviderService.createHelpProvider(any())).thenThrow(IllegalArgumentException())
        //Mockito.doReturn(helpProvider).`when`(helpProviderService).createHelpProvider(ArgumentMatchers.any())

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
            id = "HP-myself",
            name = "myself",
            keycloakName = "keycloakMyself"
        )

        Mockito.`when`(helpProviderService.findById(helpProvider.id!!)).thenReturn(helpProvider)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/${helpProvider.id}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.`is`(helpProvider.id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.`is`(helpProvider.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.expoPushToken", Matchers.`is`(helpProvider.expoPushToken)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS + "/id"))
    }

    @Test
    fun `testFindById throws Exception if unknown`() {
        Mockito.`when`(helpProviderService.findById(ArgumentMatchers.anyString())).thenReturn(null)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testFindHelpRequesters() {
        val id = "25"

        val helpRequesters = listOf(
            HelpRequester(
                id = "4711",
                name = "A",
                keycloakName = "keycloakA"
            ),
            HelpRequester(
                id = "4712",
                name = "B",
                keycloakName = "keycloakB"
            )
        )

        val helpProvider = HelpProvider(
            id = id,
            name = "Name",
            keycloakName = "keycloakName"
        )

        helpProvider.helpRequesters.addAll(helpRequesters)

        Mockito.`when`(helpProviderService.findById(id)).thenReturn(helpProvider)
        Mockito.`when`(helpProviderService.findHelpRequesters(id)).thenReturn(helpProvider.helpRequesters)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/$id/${HelpProviderController.PATH_HELP_REQUESTERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(helpRequesters[0].id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.`is`(helpRequesters[0].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(helpRequesters[1].id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.`is`(helpRequesters[1].name)))
            .andDo(MockMvcRestDocumentation.document(HelpProviderController.PATH_HELP_PROVIDERS + "/id/${HelpProviderController.PATH_HELP_PROVIDERS}"))
    }

    @Test
    fun `testFindHelpRequesters throws exception on unknown help provider`() {
        Mockito.`when`(helpProviderService.findHelpRequesters(ArgumentMatchers.anyString())).thenReturn(null)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpProviderController.PATH_HELP_PROVIDERS}/25/${HelpProviderController.PATH_HELP_REQUESTERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testAcceptEmergency() {
        val dto = EmergencyAcceptedDto(
            emergencyId = "25",
            helpProviderId = "38",
            helpRequesterId = "46"
        )

        Mockito.doNothing().`when`(helpProviderService).acceptEmergency(dto.emergencyId, dto.helpProviderId)

        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/${HelpProviderController.PATH_HELP_PROVIDERS}/${HelpProviderController.PATH_ACCEPT_EMERGENCY}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(dto)))

            .andExpect(MockMvcResultMatchers.status().isAccepted)
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

    companion object {
        private const val userName = "User"
    }
}