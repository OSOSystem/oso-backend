package org.oso.core.controller

import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.oso.core.controllers.DeviceController
import org.oso.core.entities.Device
import org.oso.core.entities.DeviceType
import org.oso.core.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [DeviceController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class DeviceControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var deviceService: DeviceService

    @Test
    fun testFindAll() {
        val typeFlic = DeviceType(
            "Flic-Button"
        )
        val typeReachfar = DeviceType(
            "Rechfar Tracker RV-V18"
        )
        val typeNano = DeviceType(
            "Nano Tracker"
        )
        val devices = listOf(
            Device("Flic1", null, typeFlic),
            Device("Reachfar1234567890", null, typeReachfar),
            Device("Nano4711", null, typeNano)
        )

        `when`(deviceService.findAll()).thenReturn(devices)

        this.mockMvc
            .perform(get("/${DeviceController.PATH_DEVICES}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(3)))
            .andExpect(jsonPath("$[0].id", `is`(devices[0].id)))
            .andExpect(jsonPath("$[0].deviceType.name", `is`(devices[0].deviceType!!.name)))
            .andExpect(jsonPath("$[1].id", `is`(devices[1].id)))
            .andExpect(jsonPath("$[1].deviceType.name", `is`(devices[1].deviceType!!.name)))
            .andExpect(jsonPath("$[2].id", `is`(devices[2].id)))
            .andExpect(jsonPath("$[2].deviceType.name", `is`(devices[2].deviceType!!.name)))
            .andDo(document(DeviceController.PATH_DEVICES))
    }
}