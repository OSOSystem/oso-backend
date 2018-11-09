package org.oso.core.controller

import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.oso.core.controllers.DeviceController
import org.oso.core.entities.Device
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
        val devices = listOf(
            Device(null, "Flic-Button", "World smallest button"),
            Device(null, "ReachFar Tracker RF-V16", "Cheap allrounder tracker"),
            Device(null, "Nano Tracker", "World smallest tracker")
        )

        `when`(deviceService.findAll()).thenReturn(devices)

        this.mockMvc
            .perform(get("/${DeviceController.PATH_DEVICES}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(3)))
            .andExpect(jsonPath("$[0].name", `is`(devices[0].name)))
            .andExpect(jsonPath("$[0].description", `is`(devices[0].description)))
            .andExpect(jsonPath("$[1].name", `is`(devices[1].name)))
            .andExpect(jsonPath("$[1].description", `is`(devices[1].description)))
            .andExpect(jsonPath("$[2].name", `is`(devices[2].name)))
            .andExpect(jsonPath("$[2].description", `is`(devices[2].description)))
            .andDo(document(DeviceController.PATH_DEVICES))
    }
}