package org.oso.core.services.impl

import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.*
import org.oso.any
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyStatusType
import org.oso.core.services.EmergencyStatusService
import org.oso.core.services.EmergencyService
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.EmergencyRepository
import org.oso.core.repositories.HelpProviderRepository
import org.oso.core.services.external.NotificationService
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class DefaultEmergencyServiceTest {

    @InjectMocks
    lateinit var defaultEmergencyService: DefaultEmergencyService

    @Mock
    lateinit var emergencyRepository: EmergencyRepository

    @Mock
    lateinit var emergencyService: EmergencyService

    @Mock
    lateinit var emergencyStatusService: EmergencyStatusService

    @Mock
    lateinit var helpProviderRepository: HelpProviderRepository

    @InjectMocks
    lateinit var helpProviderService: DefaultHelpProviderService

    @Mock
    lateinit var notificationService: NotificationService

    @Test
    fun testEmit() {
        val helpRequester = HelpRequester(
            name = "helpRequester",
            keycloakName = "HelpRequester"
        )
        val helpProvider = HelpProvider(
            name = "HelpProvider",
            keycloakName = "HelpProvider"
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

        helpProvider.expoPushToken = "ExpoPushToken"
        helpProvider.helpRequesters.add(helpRequester)
        helpRequester.helpProviders.add(helpProvider)

        Mockito.`when`(emergencyRepository.save(any<Emergency>())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyRepository).save(emergency)
        Mockito.`when`(notificationService.createEmergencyPushNotification(ArgumentMatchers.anyString(), any())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(notification).`when`(notificationService).createEmergencyPushNotification(helpProvider.expoPushToken!!, emergency)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(notificationService).sendPushNotification(listOf(notification))

        defaultEmergencyService.emit(emergency)

        Mockito.verify(emergencyRepository, Mockito.times(1)).save(emergency)
        Mockito.verify(notificationService, Mockito.times(1)).sendPushNotification(listOf(notification))
    }

    @Test
    fun `testEmit without notifications`() {
        val helpRequester = HelpRequester(
                name = "helpRequester",
                keycloakName = "HelpRequester"
        )
        val helpProvider = HelpProvider(
                name = "HelpProvider",
                keycloakName = "HelpProvider"
        )
        val emergency = Emergency(
                helpRequester = helpRequester
        )

        helpProvider.helpRequesters.add(helpRequester)
        helpRequester.helpProviders.add(helpProvider)

        Mockito.`when`(emergencyRepository.save(any<Emergency>())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(emergency).`when`(emergencyRepository).save(emergency)

        defaultEmergencyService.emit(emergency)

        Mockito.verify(emergencyRepository, Mockito.times(1)).save(emergency)
        Mockito.verify(notificationService, Mockito.times(0)).sendPushNotification(listOf(any()))
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
        Mockito.`when`(emergencyStatusService.addStatus(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyStatusService).addStatus(emergency, helpProvider, EmergencyStatusType.TYPE_ACCEPTED)
        Mockito.`when`(notificationService.createEmergencyAcceptedPushNotification(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(notification).`when`(notificationService).createEmergencyAcceptedPushNotification(helpProvider2.expoPushToken!!, emergency.id!!, helpRequester.id!!, helpProvider.id!!)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(notificationService).sendPushNotification(listOf(notification))

        emergencyService.acceptEmergency(
                emergencyId = emergency.id!!,
                helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyStatusService, Mockito.times(1)).addStatus(emergency, helpProvider, EmergencyStatusType.TYPE_ACCEPTED)
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
        Mockito.`when`(emergencyStatusService.addStatus(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(emergencyStatusService).addStatus(emergency, helpProvider, EmergencyStatusType.TYPE_ACCEPTED)
        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())

        emergencyService.acceptEmergency(
                emergencyId = emergency.id!!,
                helpProviderId = helpProvider.id!!
        )

        Mockito.verify(emergencyStatusService, Mockito.times(1)).addStatus(emergency, helpProvider, EmergencyStatusType.TYPE_ACCEPTED)
    }


}