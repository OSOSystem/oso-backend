package org.oso.core.controllers

import org.oso.core.entities.Device
import org.oso.core.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(DeviceController.PATH_DEVICES)
class DeviceController
    @Autowired constructor(private val deviceService: DeviceService) {

    @GetMapping
    @ResponseBody
    fun findAll(): List<Device> {
        return deviceService.findAll()
    }

    companion object {
        const val PATH_DEVICES = "devices"
    }
}