package com.iba.iot.datasimulator.target.validator;

import com.iba.iot.datasimulator.common.model.security.Security;
import com.iba.iot.datasimulator.target.model.S3TargetSystem;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TargetSystemValidator implements ConstraintValidator<TargetSystemValid, TargetSystem> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TargetSystemValidator.class);

    @Override
    public void initialize(TargetSystemValid constraintAnnotation) {}

    @Override
    public boolean isValid(TargetSystem targetSystem, ConstraintValidatorContext context) {

        if (targetSystem instanceof S3TargetSystem) {
            return validateS3TargetSystem((S3TargetSystem) targetSystem, context);
        }

        return true;
    }

    /**
     *
     * @param s3targetSystem
     * @param context
     * @return
     */
    private boolean validateS3TargetSystem(S3TargetSystem s3targetSystem, ConstraintValidatorContext context) {

        String url = s3targetSystem.getUrl();
        String bucket = s3targetSystem.getBucket();
        Security security = s3targetSystem.getSecurity();

        if ((StringUtils.isEmpty(url) && StringUtils.isEmpty(bucket)) ||
            (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(bucket) && security != null)) {

            return true;
        }

        logger.error(">>> Wrong s3 target system create update request provided: {}", s3targetSystem);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{target.system.s3.wrong}")
                .addConstraintViolation();

        return false;
    }
}
