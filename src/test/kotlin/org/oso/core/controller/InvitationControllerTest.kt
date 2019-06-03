package org.oso.core.controller

import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.oso.any
import org.oso.config.Paths
import org.oso.core.controllers.InvitationController
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.entities.VerificationToken
import org.oso.core.services.HelpProviderService
import org.oso.core.services.HelpRequesterService
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [InvitationController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class InvitationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var securityService: SecurityService

    @MockBean
    private lateinit var helpRequesterService: HelpRequesterService

    @MockBean
    private lateinit var  helpProviderService: HelpProviderService

    @Test
    fun testRequestInvitation() {
        val hr = HelpRequester(id = "1", name = "Peter", keycloakName = "test@test.de")
        val hp = HelpProvider(id = "2", name = "Max", keycloakName = "test@test.de")

        Mockito.`when`(helpRequesterService.findById("1")).thenReturn(hr)
        Mockito.`when`(helpProviderService.findById("2")).thenReturn(hp)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/${Paths.Invitation.INVITATION}/${Paths.Invitation.REQUEST}?hrId=${hr.id}&hpId=${hp.id}")
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun testAcceptInvitationSuccess() {
        val hr = HelpRequester(id = "1", name = "Peter", keycloakName = "test@test.de")
        val hp = HelpProvider(id = "2", name = "Max", keycloakName = "test@test.de")

        Mockito.`when`(securityService.getVerificationToken(any())).thenReturn(VerificationToken("test123", hr, hp))
        Mockito.`when`(securityService.verificationTokenExpired(any())).thenReturn(false)
        Mockito.`when`(helpRequesterService.addHelpProviderToHelpRequester(hr, hp)).thenAnswer {
            hr.helpProviders.add(hp)
            hp.helpRequesters.add(hr)
        }

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/${Paths.Invitation.INVITATION}/${Paths.Invitation.ACCEPTED}?token=test123"))
            .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.anyOf(
                                Matchers.containsString("successfull"),
                                Matchers.containsString("erfolgreich angenommen"))
                )
                )

        Assert.assertThat(hr.helpProviders, Matchers.iterableWithSize<HelpProvider>(1))
        Assert.assertThat(hr.helpProviders, Matchers.hasItem(hp))
        Assert.assertThat(hp.helpRequesters, Matchers.iterableWithSize<HelpRequester>(1))
        Assert.assertThat(hp.helpRequesters, Matchers.hasItem(hr))
    }

    @Test
    fun testAcceptInvitationExpired() {
        val hr = HelpRequester(id = "1", name = "Peter", keycloakName = "test@test.de")
        val hp = HelpProvider(id = "2", name = "Max", keycloakName = "test@test.de")

        Mockito.`when`(securityService.getVerificationToken(any())).thenReturn(VerificationToken("test123", hr, hp))
        Mockito.`when`(securityService.verificationTokenExpired(any())).thenReturn(true)

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/${Paths.Invitation.INVITATION}/${Paths.Invitation.ACCEPTED}?token=test123"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.anyOf(
                                Matchers.containsString("expired"),
                                Matchers.containsString("nicht mehr gültig"))
                )
                )

        Assert.assertThat(hr.helpProviders, Matchers.iterableWithSize<HelpProvider>(0))
        Assert.assertThat(hp.helpRequesters, Matchers.iterableWithSize<HelpRequester>(0))
    }
}