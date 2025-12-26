package com.raishxn.gticore.mixin.ae2;

import appeng.core.localization.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import com.raishxn.gticore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Tooltips.class)
public final class TooltipsMixin {

    @Inject(method = "ofBytes", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ofBytes(long number, CallbackInfoReturnable<MutableComponent> cir) {
        cir.setReturnValue(NumberUtils.numberText(number).withStyle(ChatFormatting.BLUE));
    }
}
