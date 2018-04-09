export default {
    getFileExtension(file) {
        let temp = file.name.split(".");
        let extension = temp[temp.length - 1];
        return extension;
    },

    getBase64(file, cb) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function() {
            let { result } = reader;
            let formatted = result.split(",")[1];
            cb(formatted);
        };
        reader.onerror = function(error) {
            console.log("Error: ", error);
        };
    }
};
