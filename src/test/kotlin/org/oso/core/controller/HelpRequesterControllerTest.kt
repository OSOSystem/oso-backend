package org.oso.core.controller

import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.oso.core.controllers.HelpRequesterController
import org.oso.core.entities.HelpProvider
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

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [HelpRequesterController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class HelpRequesterControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var helpRequesterService: HelpRequesterService

    @Test
    fun testFindHelpProviders() {
        val helpProviders = listOf(
            HelpProvider(
                name = "A",
                password = "Password",
                phoneNumber = "phoneNumber",
                email = "email",
                expoPushToken = "expo"
            ),
            HelpProvider(
                name = "B",
                password = "secret",
                phoneNumber = null,
                email = null,
                expoPushToken = null
            )
        )

        helpProviders[0].id = 88
        helpProviders[1].id = 1268

        val id = 25.toLong()

        Mockito.doReturn(helpProviders.toSet()).`when`(helpRequesterService).findHelpProviders(id)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpRequesterController.PATH_HELP_REQUESTERS}/$id/${HelpRequesterController.PATH_HELP_PROVIDERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(helpProviders[0].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.`is`(helpProviders[0].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.`is`(helpProviders[0].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber", Matchers.`is`(helpProviders[0].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].password", Matchers.`is`(helpProviders[0].password)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.`is`(helpProviders[1].id?.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.`is`(helpProviders[1].name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.`is`(helpProviders[1].email)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].phoneNumber", Matchers.`is`(helpProviders[1].phoneNumber)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].password", Matchers.`is`(helpProviders[1].password)))
            .andDo(MockMvcRestDocumentation.document("${HelpRequesterController.PATH_HELP_REQUESTERS}/id/${HelpRequesterController.PATH_HELP_PROVIDERS}"))
    }

    @Test
    fun `testFindHelpProviders throws exception on unknown HelpRequester`() {
        Mockito.`when`(helpRequesterService.findHelpProviders(ArgumentMatchers.anyLong())).thenReturn(null)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${HelpRequesterController.PATH_HELP_REQUESTERS}/1/${HelpRequesterController.PATH_HELP_PROVIDERS}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}