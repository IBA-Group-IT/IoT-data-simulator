package com.iba.iot.datasimulator.definition.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.service.stream.ContentSizeAwareInputStreamWrapper;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetViews;
import com.iba.iot.datasimulator.definition.service.DatasetManager;
import io.minio.errors.*;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/datasets")
public class DatasetController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetController.class);

    /** **/
    private static final String FILE_PARAMETER_NAME = "file";

    /** **/
    private static final String FILE_SIZE_HEADER = "File-Size";

    @Autowired
    private DatasetManager datasetManager;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public Dataset upload(HttpServletRequest request) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException,
            InsufficientDataException, ErrorResponseException, RegionConflictException, InvalidPortException, InvalidEndpointException, InvalidObjectPrefixException, FileUploadException {

        if (ServletFileUpload.isMultipartContent(request)) {

            ServletFileUpload uploadingHandler = new ServletFileUpload();
            FileItemIterator iterator = uploadingHandler.getItemIterator(request);
            while (iterator.hasNext()) {

                FileItemStream item = iterator.next();
                String name = item.getFieldName();
                if (FILE_PARAMETER_NAME.equalsIgnoreCase(name) && !item.isFormField()) {

                    String fileName = item.getName();
                    long contentSize = Long.parseLong(request.getHeader(FILE_SIZE_HEADER));

                    logger.debug(">>> Starting file {} uploading process...", fileName);
                    try (InputStream inputStream = new ContentSizeAwareInputStreamWrapper(item.openStream(), contentSize)) {
                        return datasetManager.upload(fileName, inputStream, contentSize);
                    }
                }
            }
        }

        logger.error(">>> Dataset uploading request doesn't contain file.");
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Dataset create(@RequestBody @Valid @NotNull Dataset dataset) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            XmlPullParserException, InternalException, NoResponseException, InvalidBucketNameException,
            InsufficientDataException, ErrorResponseException, InvalidPortException, InvalidEndpointException {

        return datasetManager.create(dataset);
    }

    @JsonView(DatasetViews.Short.class)
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Dataset> get(@RequestParam(value="name", required=false) String name,
                                   @RequestParam(value="type", required=false) String type) {
        return datasetManager.get(name, type);
    }

    @RequestMapping(value = "/{datasetId}", method=RequestMethod.GET)
    public Dataset get(@PathVariable("datasetId") @NotNull String datasetId) {

        return datasetManager.get(datasetId);
    }

    @RequestMapping(value = "/{datasetId}/schema", method=RequestMethod.GET)
    public Schema deriveSchema(@PathVariable("datasetId") @NotNull String datasetId) throws IOException {

        return datasetManager.deriveSchema(datasetId);
    }

    @RequestMapping(value = "/{datasetId}", method=RequestMethod.PUT)
    public Dataset update(@PathVariable("datasetId") @NotNull String datasetId,
                          @RequestBody @Valid @NotNull Dataset dataset) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            XmlPullParserException, InvalidPortException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException,
            InvalidEndpointException, ErrorResponseException {

        return datasetManager.update(datasetId, dataset);
    }

    @RequestMapping(value = "/{datasetId}", method=RequestMethod.DELETE)
    public void remove(@PathVariable("datasetId") @NotNull String datasetId) {

        datasetManager.remove(datasetId);
    }

    @ExceptionHandler(java.lang.IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {

        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }
}
