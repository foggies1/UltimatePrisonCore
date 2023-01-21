package dev.foggies.prisoncore.mine.data;

import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MineBlock {

    STONE("Stone", 10, 0, BlockTypes.STONE);

    private final String displayName;
    private final long worth;
    private final long requiredLevel;
    private final BlockType type;

}
