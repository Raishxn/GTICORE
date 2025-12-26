package com.raishxn.gticore.mixin.ae2.gui;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.items.SetProcessingPatternAmountScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SetProcessingPatternAmountScreen.class)
public class SetProcessingPatternAmountScreenMixin {

    @Shadow(remap = false)
    @Final
    private GenericStack currentStack;

    /**
     * @author .
     * @reason 样板终端中键设置数量上限
     */
    @Overwrite(remap = false)
    private long getMaxAmount() {
        return (long) Integer.MAX_VALUE * this.currentStack.what().getAmountPerUnit();
    }
}
