package dev.vertcode.vertwhitelist.data.object;

import java.util.Arrays;
import java.util.Optional;

public enum SaveMethod {

    MONGODB, MYSQL;

    public static Optional<SaveMethod> getByString(String str) {
        return Arrays.stream(values()).filter(saveMethod -> saveMethod.name().equalsIgnoreCase(str)).findFirst();
    }

}
