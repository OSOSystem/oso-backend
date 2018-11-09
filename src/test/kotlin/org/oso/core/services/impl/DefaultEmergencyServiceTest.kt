//package org.oso.core.services.impl
//
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.ArgumentMatchers
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.oso.any
//import org.oso.core.dtos.PushNotification
//import org.oso.core.entities.Emergency
//import org.oso.core.entities.HelpProvider
//import org.oso.core.entities.HelpRequester
//import org.oso.core.repositories.EmergencyRepository
//import org.oso.core.services.external.NotificationService
//import org.springframework.test.context.junit.jupiter.SpringExtension
//
//@ExtendWith(SpringExtension::class)
//class DefaultEmergencyServiceTest {
//
//    @InjectMocks
//    lateinit var defaultEmergencyService: DefaultEmergencyService
//
//    @Mock
//    lateinit var emergencyRepository: EmergencyRepository
//
//    @Mock
//    lateinit var notificationService: NotificationService
//
//    @Test
//    fun testEmit() {
//        val helpRequester = HelpRequester(
//            name = "helpRequester"
//        )
//        val helpProvider = HelpProvider(
//            name = "HelpProvider",
//            password = "HelpProvider"
//        )
//        val emergency = Emergency(
//            helpRequester = helpRequester
//        )
//        val notification = PushNotification(
//                to = "To",
//                body = "body",
//                data = "data",
//                title = "title"
//        )
//
//        helpProvider.expoPushToken = "ExpoPushToken"
//        helpProvider.helpRequesters.add(helpRequester)
//        helpRequester.helpProviders.add(helpProvider)
//
//        Mockito.`when`(emergencyRepository.save(any<Emergency>())).thenThrow(IllegalArgumentException())
//        Mockito.doReturn(emergency).`when`(emergencyRepository).save(emergency)
//        Mockito.`when`(notificationService.createEmergencyPushNotification(ArgumentMatchers.anyString(), any())).thenThrow(IllegalArgumentException())
//        Mockito.doReturn(notification).`when`(notificationService).createEmergencyPushNotification(helpProvider.expoPushToken!!, emergency)
//        Mockito.`when`(notificationService.sendPushNotification(any())).thenThrow(IllegalArgumentException())
//        Mockito.doNothing().`when`(notificationService).sendPushNotification(listOf(notification))
//
//        defaultEmergencyService.emit(emergency)
//
//        Mockito.verify(emergencyRepository, Mockito.times(1)).save(emergency)
//        Mockito.verify(notificationService, Mockito.times(1)).sendPushNotification(listOf(notification))
//    }
//
//    @Test
//    fun `testEmit without notifications`() {
//        val helpRequester = HelpRequester(
//                name = "helpRequester"
//        )
//        val helpProvider = HelpProvider(
//                name = "HelpProvider",
//                password = "HelpProvider"
//        )
//        val emergency = Emergency(
//                helpRequester = helpRequester
//        )
//
//        helpProvider.helpRequesters.add(helpRequester)
//        helpRequester.helpProviders.add(helpProvider)
//
//        Mockito.`when`(emergencyRepository.save(any<Emergency>())).thenThrow(IllegalArgumentException())
//        Mockito.doReturn(emergency).`when`(emergencyRepository).save(emergency)
//
//        defaultEmergencyService.emit(emergency)
//
//        Mockito.verify(emergencyRepository, Mockito.times(1)).save(emergency)
//        Mockito.verify(notificationService, Mockito.times(0)).sendPushNotification(listOf(any()))
//    }
//}