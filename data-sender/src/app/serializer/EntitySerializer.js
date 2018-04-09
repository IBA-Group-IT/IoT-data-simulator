let logger = require('../common/log');
let Constants = require('../common/Constants');
let fsExtra = require("fs-extra");
let protobuf = require('protobufjs');
let Base64 = require("js-base64").Base64;

class EntitySerializer {

    /**
     *
     */
    constructor() {
        this.serializerTypesCache = new Map();
        this.serializerFunctionsCache = new Map();
    }

    /**
     *
     * @param target
     * @param serializerProperty
     * @param entity
     * @returns {Promise.<void>}
     */
    async serialize(target, serializerProperty, entity) {

        logger.debug(">>> Serializing entity.");

        let serializer = target[serializerProperty];
        switch (serializer.type) {

            case Constants.SERIALIZER_TYPE_PROTOBUF:
                return await this.processProtobufSerialization(target,  serializer, entity);
        }

        logger.error(`>>> Unsupported ${serializer.type} serializer type`);
        throw Error("Unsupported serialization type error.");
    }

    /**
     *
     * @param target
     * @param serializerProperty
     * @param entity
     * @returns {Promise<void|*>}
     */
    async processProtobufSerialization(target, serializer, entity) {

        let protoDescriptor = Base64.decode(serializer.protoDescriptor);
        let protoTypeName = serializer.protoType;
        let protoFunction = serializer.jsBuilder;

        let protobufType = await this.getProtobufType(target, protoTypeName, protoDescriptor);
        let protoBuilderFunction = this.getProtobufBuilderFunction(protoFunction);

        let typeContent = protoBuilderFunction(entity);
        let errMsg = protobufType.verify(typeContent);
        if (errMsg) throw Error("Protobuf serialization error: " + errMsg);

        let protobufMessage = protobufType.create(typeContent);
        let result = protobufType.encode(protobufMessage).finish();

        if (result.length === 0) {
            logger.error(`>>> Wrong data format detected during data serialization`);
            throw Error("Wrong serialization data format error.");
        }

        return result;
    }

    /**
     *
     * @param target
     * @param protoTypeName
     * @param protoDescriptor
     * @returns {Promise<V>}
     */
    async getProtobufType(target, protoTypeName, protoDescriptor) {

        let protobufTypeResolver = this.serializerTypesCache.get(protoTypeName);
        if (!protobufTypeResolver) {
            protobufTypeResolver = this.buildProtobufType(target, protoDescriptor, protoTypeName);
            this.serializerTypesCache.set(protoTypeName, protobufTypeResolver)
        }

        return await protobufTypeResolver;
    }

    /**
     *
     */
    getProtobufBuilderFunction(protoFunction) {

        let protoBuilderFunction = this.serializerFunctionsCache.get(protoFunction);
        if (!protoBuilderFunction) {
            protoBuilderFunction = eval('(' + protoFunction + ')');
            this.serializerFunctionsCache.set(protoFunction, protoBuilderFunction);
        }

        return protoBuilderFunction;
    }

    /**
     *
     * @param target
     * @param protoDescriptor
     * @param protoType
     * @returns {Promise<void>}
     */
    async buildProtobufType(target, protoDescriptor, protoType) {

        logger.debug(`>>> Building protobuf class ${protoType}`);

        let filePath = Constants.DATASETS_FOLDER + protoType;
        await fsExtra.writeFile(filePath, protoDescriptor);
        let root = await protobuf.load(filePath);
        await fsExtra.unlink(filePath);
        return root.lookupType(protoType);
    }


}

module.exports = new EntitySerializer();