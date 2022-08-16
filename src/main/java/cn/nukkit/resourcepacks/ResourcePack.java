package cn.nukkit.resourcepacks;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import java.util.UUID;

/**
 * Can either be a "Resource Pack" or a "Behavior Pack"
 */
public interface ResourcePack {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    ResourcePack[] EMPTY_ARRAY = new ResourcePack[0];

    String getPackName();

    UUID getPackId();

    String getPackVersion();

    int getPackSize();

    byte[] getSha256();

    byte[] getPackChunk(int off, int len);

    Type getType();

    boolean requiresScripting();

    enum Type {
        // Module types may not overlap.
        RESOURCE_PACK(new String[]{"resources"}),
        BEHAVIOR_PACK(new String[]{"client_data", "data"}); // only client scripts are supported

        private final String[] allowedModuleTypes;

        Type(String[] allowedModuleTypes) {
            this.allowedModuleTypes = allowedModuleTypes;
        }

        public String[] getAllowedModuleTypes() {
            return this.allowedModuleTypes;
        }
    }
}