package com.iba.iot.datasimulator.device.controller;

import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.util.CollectionUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.service.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/devices")
public class DeviceController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceManager deviceManager;

    @RequestMapping(method = RequestMethod.POST)
    public Device create(@RequestBody @Valid @NotNull Device device) {

        return deviceManager.create(device);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Device> get(@RequestParam(value="name", required=false) String name,
                                  @RequestParam(value="targetSystem", required=false) String targetSystem) {

        return deviceManager.get(name, targetSystem);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public Device get(@PathVariable("deviceId") @NotNull String deviceId) {

        return deviceManager.get(deviceId);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("deviceId") @NotNull String deviceId) {

        deviceManager.remove(deviceId);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.PUT)
    public Device update(@PathVariable("deviceId") @NotNull String deviceId,
                         @RequestBody @Valid @NotNull Device device) {

        return deviceManager.update(deviceId, device);
    }

    @ExceptionHandler({BeanCreationException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {
        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }
}
