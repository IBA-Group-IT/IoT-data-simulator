
const isNumeric = val => {
    let num = parseFloat(val);
    return !Number.isNaN(num) && Number.isFinite(num);
};

export const throwsNothing = () => false;

export const throwsRequired = val => {
    return (typeof val === 'undefined' || val === null || val === "") && "Value required"
};

export const throwsNumeric = val => {
    return !isNumeric(val) && "Should be numeric";
};

export const throwsInteger = val => {
    return Number.isNaN(parseInt(val, 10)) && "Should be integer";
};

export const throwsLong = val => {
    return Number.isNaN(parseInt(val, 10)) && "Should be integer";
};

export const throwsGreaterThan = threshold => val => {
    if (isNumeric(val)) {
        return !(val > threshold) && `Should be > ${threshold}`;
    }
    return !(val.length > threshold) && `Length should be > ${threshold}`;
};

export const throwsGreaterOrEqual = threshold => val => {
    if (isNumeric(val)) {
        return !(val >= threshold) && `Should be >= ${threshold}`;
    }
    return !(val.length >= threshold) && `Length should be >= ${threshold}`;
}

export const throwsLowerThan = threshold => val => {
    if (isNumeric(val)) {
        return !(val < threshold) && `Should be < ${threshold}`;
    }
    return !(val.length < threshold) && `Length should be < ${threshold}`;
};

export const throwsLowerOrEqual = threshold => val => {
     if (isNumeric(val)) {
        return !(val < threshold) && `Should be <= ${threshold}`;
    }
    return !(val.length < threshold) && `Length should be <= ${threshold}`;
}

export const throwsMinMax = (minKey, maxKey) => ($) => {
    let minStr = $[minKey].$,
        maxStr = $[maxKey].$;

    let min = parseFloat(minStr),
        max = parseFloat(maxStr);

    if(isNumeric(min) && isNumeric(max) && min >= max) {
        return 'Min value should be lower than max';
    }
}

export const throwsNotValidJS = (value) => {
    try {
        eval(`"use strict"; ${value}`);
    } catch (e) {
        return "Function is not valid";
    }
}

export default {
    throwsNothing,
    throwsRequired,
    throwsNumeric,
    throwsInteger,
    throwsLong,
    throwsGreaterThan,
    throwsGreaterOrEqual,
    throwsLowerThan,
    throwsLowerOrEqual,
    throwsMinMax,
    throwsNotValidJS
};
