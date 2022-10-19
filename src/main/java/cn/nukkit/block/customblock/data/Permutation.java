package cn.nukkit.block.customblock.data;

import cn.nukkit.math.Vector3f;
import lombok.Builder;

/**
 * The type Permutation builder.
 */
@Builder
public class Permutation {
    Boolean collision_box_enabled;
    Vector3f collision_box_origin;
    Vector3f collision_box_size;
    Boolean selection_box_enabled;
    Vector3f selection_box_origin;
    Vector3f selection_box_size;
    /**
     * The molang condition.
     */
    String condition;
}