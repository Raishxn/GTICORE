package com.raishxn.gticore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import com.raishxn.gticore.api.machine.trait.AECraft.IMECraftParallelCore;

public class MECraftParallelCorePartMachine extends MultiblockPartMachine implements IMECraftParallelCore {

    public final static int PARALLEL = 4194304;

    public MECraftParallelCorePartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public int getParallel() {
        return PARALLEL;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }
}
