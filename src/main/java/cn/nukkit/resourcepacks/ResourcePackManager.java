package cn.nukkit.resourcepacks;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import com.google.common.io.Files;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.*;

@Log4j2
public class ResourcePackManager {

    private int maxChunkSize = 102400;

    private final Map<UUID, ResourcePack> resourcePacksById = new HashMap<>();
    private final ResourcePack[] resourcePacks;
    private final ResourcePack[] behaviorPacks;

    public ResourcePackManager(@NonNull File resourcePacksPath, @NonNull File behaviorPacksPath) {
        if (!resourcePacksPath.exists()) {
            resourcePacksPath.mkdirs();
        } else if (!resourcePacksPath.isDirectory()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage()
                    .translateString("nukkit.resources.invalid-path", resourcePacksPath.getName()));
        }

        if (!behaviorPacksPath.exists()) {
            behaviorPacksPath.mkdirs();
        } else if (!behaviorPacksPath.isDirectory()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage()
                    .translateString("nukkit.resources.invalid-path", behaviorPacksPath.getName()));
        }

        List<ResourcePack> loadedResourcePacks = new ArrayList<>();
        List<ResourcePack> loadedBehaviorPacks = new ArrayList<>();
        for (File pack : resourcePacksPath.listFiles()) {
            try {
                ResourcePack resourcePack = null;

                if (!pack.isDirectory()) { //directory resource packs temporarily unsupported
                    switch (Files.getFileExtension(pack.getName())) {
                        case "zip":
                        case "mcpack":
                            resourcePack = new ZippedResourcePack(pack);
                            break;
                        default:
                            log.warn(Server.getInstance().getLanguage()
                                    .translateString("nukkit.resources.unknown-format", pack.getName()));
                            break;
                    }
                }

                if (resourcePack != null) {
                    if (!resourcePack.getType().equals(ResourcePack.Type.RESOURCE_PACK)) {
                        log.warn(Server.getInstance().getLanguage()
                                .translateString("nukkit.resources.invalid-type-resource", pack.getName(), resourcePack.getPackName()));

                        continue;
                    }

                    loadedResourcePacks.add(resourcePack);
                    this.resourcePacksById.put(resourcePack.getPackId(), resourcePack);

                    log.info(Server.getInstance().getLanguage()
                            .translateString("nukkit.resources.loaded-resource-pack", resourcePack.getPackName()));
                }
            } catch (IllegalArgumentException e) {
                log.warn(Server.getInstance().getLanguage().translateString("nukkit.resources.fail", pack.getName(), e.getMessage()), e);
            }
        }

        for (File pack : behaviorPacksPath.listFiles()) {
            try {
                ResourcePack behaviorPack = null;

                if (!pack.isDirectory()) { //directory resource packs temporarily unsupported
                    switch (Files.getFileExtension(pack.getName())) {
                        case "zip":
                        case "mcpack":
                            behaviorPack = new ZippedResourcePack(pack);
                            break;
                        default:
                            Server.getInstance().getLogger().warning(Server.getInstance().getLanguage()
                                    .translateString("nukkit.resources.unknown-format", pack.getName()));
                            break;
                    }
                }

                if (behaviorPack != null) {
                    if (!behaviorPack.getType().equals(ResourcePack.Type.BEHAVIOR_PACK)) {
                        log.warn(Server.getInstance().getLanguage()
                                .translateString("nukkit.resources.invalid-type-behavior", pack.getName(), behaviorPack.getPackName()));

                        continue;
                    }

                    loadedBehaviorPacks.add(behaviorPack);
                    this.resourcePacksById.put(behaviorPack.getPackId(), behaviorPack);

                    log.info(Server.getInstance().getLanguage()
                            .translateString("nukkit.resources.loaded-behavior-pack", behaviorPack.getPackName()));
                }
            } catch (IllegalArgumentException e) {
                log.warn(Server.getInstance().getLanguage().translateString("nukkit.resources.fail", pack.getName(), e.getMessage()), e);
            }
        }

        this.resourcePacks = loadedResourcePacks.toArray(ResourcePack.EMPTY_ARRAY);
        this.behaviorPacks = loadedBehaviorPacks.toArray(ResourcePack.EMPTY_ARRAY);
        log.info(Server.getInstance().getLanguage()
                .translateString("nukkit.resources.resource.success", String.valueOf(this.resourcePacks.length)));
        log.info(Server.getInstance().getLanguage()
                .translateString("nukkit.resources.behavior.success", String.valueOf(this.behaviorPacks.length)));
    }

    public ResourcePack[] getResourcePacks() {
        return this.resourcePacks;
    }

    public ResourcePack[] getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public ResourcePack getPackById(UUID id) {
        return this.resourcePacksById.get(id);
    }

    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public int getMaxChunkSize() {
        return this.maxChunkSize;
    }

    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public void setMaxChunkSize(int size) {
        this.maxChunkSize = size;
    }
}
