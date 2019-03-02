package org.oso.core.services.impl

import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.oso.any
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyActionType
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.HelpProviderRepository
import org.oso.core.services.EmergencyActionService
import org.oso.core.services.EmergencyService
import org.oso.core.services.external.NotificationService
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class DefaultHelpProviderServiceTest {

    @InjectMocks
    lateinit var helpProviderService: DefaultHelpProviderService

    @Mock
    lateinit var helpProviderRepository: HelpProviderRepository

    @Mock
    lateinit var emergencyActionService: EmergencyActionService

    @Mock
    lateinit var notificationService: NotificationService

    @Mock
    lateinit var emergencyService: EmergencyService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testAcceptEmergency() {
        val helpRequester = HelpRequester(
            id = "456",
            name = "Requester",
            keycloakName = "Requester"
        )
        val helpProvider = HelpProvider(
            id = "123",
            name = "HelpProvider",
            keycloakName = "HelpProvider"
        )
        val helpProvider2 = HelpProvider(
            id = "321",
            name = "HelpProvider2",
            keycloakName = "HelpProvider2"
        )
        val emergency = Emergency(
            id = "789",
            helpRequester = helpRequester
        )
        val notification = PushNotification(
            to = "To",
            body = "body",
            data = "data",
            title = "title"
        )

        helpProvider.helpRequesters.add(helpRequester)
        helpProvider2.expoPushToken = "ExpoPushToken"
        helpProvider2.helpRequesters.add(helpRequester)
        helpRequester.helpProviders.add(helpProvider)
        helpRequester.helpProviders.add(helpProvider2)

        Mockito.`when`(helpProviderRepository.findById(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(Optional.of(helpProvider)).`when`(helpProviderRepository).findById(helpProvider.id!!)
        Mockito.`when`(emergencyService.findEmergency(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyService).findEmergency(emergency.id!!)
        Mockito.`when`(emergencyActionService.addAction(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyActionService).addAction(emergency, helpProvider, EmergencyActionType.TYPE_ACCEPT)
        Mockito.`when`(notificationService.createEmergencyAcceptedPushNotification(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(notification).`when`(notificationService).createEmergencyAcceptedPushNotification(helpProvider2.expoPushToken!!, emergency.id!!, helpRequester.id!!, helpProvider.id!!)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(notificationService).sendPushNotification(listOf(notification))

        helpProviderService.acceptEmergency(
            emergencyId = emergency.id!!,
            helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyActionService, Mockito.times(1)).addAction(emergency, helpProvider, EmergencyActionType.TYPE_ACCEPT)
        Mockito.verify(notificationService, Mockito.times(1)).sendPushNotification(listOf(notification))
    }

    @Test
    fun `testAcceptEmergency without notification`() {
        val helpRequester = HelpRequester(
                id = "123",
                name = "Requester",
                keycloakName = "Requester"
        )
        val helpProvider = HelpProvider(
                id = "456",
                name = "HelpProvider",
                keycloakName = "HelpProvider"
        )
        val emergency = Emergency(
                id = "789",
                helpRequester = helpRequester
        )

        helpProvider.helpRequesters.add(helpRequester)
        helpRequester.helpProviders.add(helpProvider)

        Mockito.`when`(helpProviderRepository.findById(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(Optional.of(helpProvider)).`when`(helpProviderRepository).findById(helpProvider.id!!)
        Mockito.`when`(emergencyService.findEmergency(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyService).findEmergency(emergency.id!!)
        Mockito.`when`(emergencyActionService.addAction(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyActionService).addAction(emergency, helpProvider, EmergencyActionType.TYPE_ACCEPT)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())

        helpProviderService.acceptEmergency(
                emergencyId = emergency.id!!,
                helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyActionService, Mockito.times(1)).addAction(emergency, helpProvider, EmergencyActionType.TYPE_ACCEPT)
    }
}