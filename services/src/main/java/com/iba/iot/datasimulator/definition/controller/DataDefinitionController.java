package com.iba.iot.datasimulator.definition.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;
import com.iba.iot.datasimulator.definition.model.DataDefinitionViews;
import com.iba.iot.datasimulator.definition.service.DataDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/definitions")
public class DataDefinitionController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionController.class);

    @Autowired
    private DataDefinitionManager dataDefinitionManager;

    @RequestMapping(method = RequestMethod.POST)
    public DataDefinition create(@RequestBody @Valid @NotNull DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest) {

        return dataDefinitionManager.create(dataDefinitionCreateUpdateRequest);
    }

    @JsonView(DataDefinitionViews.Short.class)
    @RequestMapping(method = RequestMethod.GET)
    public Collection<DataDefinition> get() {

        return dataDefinitionManager.get();
    }

    @RequestMapping(value = "/{dataDefinitionId}", method = RequestMethod.GET)
    public DataDefinition get(@PathVariable("dataDefinitionId") @NotNull String dataDefinitionId) {

        return dataDefinitionManager.get(dataDefinitionId);
    }

    @RequestMapping(value = "/{dataDefinitionId}/schema/properties", method = RequestMethod.GET)
    public Collection<SchemaPropertyDescription> getSchemaPropertyPaths(@PathVariable("dataDefinitionId") @NotNull String dataDefinitionId) {

        return dataDefinitionManager.getSchemaPropertiesDescription(dataDefinitionId);
    }

    @RequestMapping(value = "/{dataDefinitionId}/schema/rules", method = RequestMethod.GET)
    public Schema populateSchemaDefaultRules(@PathVariable("dataDefinitionId") @NotNull String dataDefinitionId) throws IOException {

        return dataDefinitionManager.populateSchemaDefaultProcessingRules(dataDefinitionId);
    }

    @RequestMapping(value = "/{dataDefinitionId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("dataDefinitionId") @NotNull String dataDefinitionId) {

        dataDefinitionManager.remove(dataDefinitionId);
    }

    @RequestMapping(value = "/{dataDefinitionId}", method = RequestMethod.PUT)
    public DataDefinition update(@PathVariable("dataDefinitionId") @NotNull String dataDefinitionId,
                                 @RequestBody @Valid @NotNull DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest) throws IOException {

        return dataDefinitionManager.update(dataDefinitionId, dataDefinitionCreateUpdateRequest);
    }

    @ExceptionHandler({BeanCreationException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {
        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }
}
