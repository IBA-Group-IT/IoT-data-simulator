const HAS_DATASET_TEMPLATE = (type, returnValue) => `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} ruleState may be used to store information between iterations
 * @param  {object} sessionState may be used to store information between rules
 * @param  {string} deviceName device name
 * @param  {any} entryValue dataset property value (readonly)
 * @param  {object} datasetRow dataset row value (readonly)
 * @return {${type}} value that will be populated to specified property
 */
function custom(ruleState, sessionState, deviceName, entryValue, datasetRow) { 
    return ${returnValue};
}
`;

const NO_DATASET_TEMPLATE = (type, returnValue) => `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} ruleState may be used to store information between iterations
 * @param  {object} sessionState may be used to store information between rules
 * @param  {string} deviceName device name
 * @return {${type}} value that will be populated to specified property
 */
function custom(ruleState, sessionState, deviceName) { 
    return ${returnValue};
}
`;

let defaultValues = {
    string: () => '""',
    timestamp: property => {
        if (property.getParameter("format") === "seconds") {
            return "moment().unix()";
        }
        return "moment().valueOf()";
    },
    date: (property) => {
        let format = property.getParameter('format') || 'YYYY-MM-DD HH:mm:ss';
        return `moment().format('${format}')`;
    },
    integer: () => 0,
    double: () => 0,
    long: () => 0,
    boolean: () => true,
    object: () => '{}',
    array: () => '[]'
};


export default {
    build: function build(session, property) {
        if (session.hasDefinition && session.dataDefinition.hasDataset) {
            return HAS_DATASET_TEMPLATE(
                property.type,
                defaultValues[property.type](property)
            );
        }
        return NO_DATASET_TEMPLATE(
            property.type,
            defaultValues[property.type](property)
        );
    }
};
