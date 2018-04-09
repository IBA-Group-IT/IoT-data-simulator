/**
 *
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 *
 * @param  {object} ruleState may be used to store information between iterations
 * @param  {object} sessionState may be used to store information between rules
 * @param  {string} deviceName device name
 * @param  {object} entryValue dataset property value
 * @param  {string} datasetRow dataset row value (readonly)
 * @return {string} value that will be populated to specified property
 */
function custom(ruleState, sessionState, deviceName, entryValue, datasetRow) {
    return entryValue;
}