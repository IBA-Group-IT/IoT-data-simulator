package com.iba.iot.datasimulator.target.controller;

import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.model.ErrorsResponse;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import com.iba.iot.datasimulator.target.service.TargetSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/systems")
@Validated
public class TargetSystemController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TargetSystemController.class);

    @Autowired
    private TargetSystemManager targetSystemManager;

    @RequestMapping(method = RequestMethod.POST)
    public TargetSystem create(@RequestBody @Valid @NotNull TargetSystem targetSystem) {

        return targetSystemManager.create(targetSystem);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<TargetSystem> get() {

        return targetSystemManager.get();
    }

    @RequestMapping(value = "/{targetSystemId}", method = RequestMethod.GET)
    public TargetSystem get(@PathVariable("targetSystemId") @NotNull String targetSystemId) {

        return targetSystemManager.get(targetSystemId);
    }

    @RequestMapping(value = "/{targetSystemId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("targetSystemId") @NotNull String targetSystemId) {

        targetSystemManager.remove(targetSystemId);
    }

    @RequestMapping(value = "/{targetSystemId}", method = RequestMethod.PUT)
    public TargetSystem update(@PathVariable("targetSystemId") @NotNull String targetSystemId,
                         @RequestBody @Valid @NotNull TargetSystem targetSystem) {

        return targetSystemManager.update(targetSystemId, targetSystem);
    }

    @ExceptionHandler({BeanCreationException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {

        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleConstraintViolationException(ConstraintViolationException exception) {

        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorsResponse(errors);
    }
}
