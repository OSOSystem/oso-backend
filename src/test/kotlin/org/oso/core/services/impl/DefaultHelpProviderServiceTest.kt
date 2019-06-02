package org.oso.core.services.impl

import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.oso.any
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyStatusType
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.HelpProviderRepository
import org.oso.core.services.EmergencyStatusService
import org.oso.core.services.EmergencyService
import org.oso.core.services.external.NotificationService
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class DefaultHelpProviderServiceTest {

    @Mock
    lateinit var notificationService: NotificationService


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


}