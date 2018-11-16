package org.oso.core.services.impl

import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.oso.any
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Action
import org.oso.core.entities.Emergency
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
        // new action added for the emergency
        // pushnotification to provider sent

        val helpRequester = HelpRequester(
            name = "Requester",
            password = "Requester"
        )
        val helpProvider = HelpProvider(
            name = "HelpProvider",
            password = "HelpProvider"
        )
        val helpProvider2 = HelpProvider(
            name = "HelpProvider2",
            password = "HelpProvider2"
        )
        val emergency = Emergency(
            helpRequester = helpRequester
        )
        val notification = PushNotification(
            to = "To",
            body = "body",
            data = "data",
            title = "title"
        )

        helpProvider.id = 123
        helpProvider.helpRequesters.add(helpRequester)
        helpProvider2.id = 321
        helpProvider2.expoPushToken = "ExpoPushToken"
        helpProvider2.helpRequesters.add(helpRequester)
        helpRequester.id = 456
        helpRequester.helpProviders.add(helpProvider)
        helpRequester.helpProviders.add(helpProvider2)
        emergency.id = 789

        Mockito.`when`(helpProviderRepository.findById(ArgumentMatchers.anyLong())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(Optional.of(helpProvider)).`when`(helpProviderRepository).findById(helpProvider.id!!)
        Mockito.`when`(emergencyService.findEmergency(ArgumentMatchers.anyLong())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyService).findEmergency(emergency.id!!)
        Mockito.`when`(emergencyActionService.addAction(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyActionService).addAction(emergency, helpProvider, Action.ACCEPT)
        Mockito.`when`(notificationService.createEmergencyAcceptedPushNotification(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(notification).`when`(notificationService).createEmergencyAcceptedPushNotification(helpProvider2.expoPushToken!!, emergency.id!!, helpRequester.id!!, helpProvider.id!!)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(notificationService).sendPushNotification(listOf(notification))

        helpProviderService.acceptEmergency(
            emergencyId = emergency.id!!,
            helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyActionService, Mockito.times(1)).addAction(emergency, helpProvider, Action.ACCEPT)
        Mockito.verify(notificationService, Mockito.times(1)).sendPushNotification(listOf(notification))
    }

    @Test
    fun `testAcceptEmergency without notification`() {
        // new action added for the emergency
        // pushnotification to provider sent

        val helpRequester = HelpRequester(
                name = "Requester",
                password = "Requester"
        )
        val helpProvider = HelpProvider(
                name = "HelpProvider",
                password = "HelpProvider"
        )
        val emergency = Emergency(
                helpRequester = helpRequester
        )

        helpProvider.id = 123
        helpProvider.helpRequesters.add(helpRequester)
        helpRequester.id = 456
        helpRequester.helpProviders.add(helpProvider)
        emergency.id = 789

        Mockito.`when`(helpProviderRepository.findById(ArgumentMatchers.anyLong())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(Optional.of(helpProvider)).`when`(helpProviderRepository).findById(helpProvider.id!!)
        Mockito.`when`(emergencyService.findEmergency(ArgumentMatchers.anyLong())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyService).findEmergency(emergency.id!!)
        Mockito.`when`(emergencyActionService.addAction(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyActionService).addAction(emergency, helpProvider, Action.ACCEPT)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())

        helpProviderService.acceptEmergency(
                emergencyId = emergency.id!!,
                helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyActionService, Mockito.times(1)).addAction(emergency, helpProvider, Action.ACCEPT)
    }
}