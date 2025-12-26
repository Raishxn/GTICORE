package com.raishxn.gticore.mixin.ae2.gui;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import net.minecraft.network.chat.Component;
import com.raishxn.gticore.client.gui.ModifyIcon;
import com.raishxn.gticore.client.gui.ModifyIconButton;
import com.raishxn.gticore.client.gui.PatterEncodingTermMenuModify;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author EasterFG on 2024/9/12
 */

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {

    @Unique
    private ModifyIconButton gTICore$multipleTow;
    @Unique
    private ModifyIconButton gTICore$multipleThree;
    @Unique
    private ModifyIconButton gTICore$multipleFive;
    @Unique
    private ModifyIconButton gTICore$dividingTow;
    @Unique
    private ModifyIconButton gTICore$dividingThree;
    @Unique
    private ModifyIconButton gTICore$dividingFive;

    public ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        gTICore$multipleTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(2), ModifyIcon.MULTIPLY_2,
                Component.translatable("gui.gtlcore.pattern_recipe_multiply_2"),
                Component.translatable("tooltip.gtlcore.pattern_materials_multiply_2"));

        gTICore$multipleThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(3), ModifyIcon.MULTIPLY_3,
                Component.translatable("gui.gtlcore.pattern_recipe_multiply_3"),
                Component.translatable("tooltip.gtlcore.pattern_materials_multiply_3"));

        gTICore$multipleFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(5), ModifyIcon.MULTIPLY_5,
                Component.translatable("gui.gtlcore.pattern_recipe_multiply_5"),
                Component.translatable("tooltip.gtlcore.pattern_materials_multiply_5"));

        gTICore$dividingTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-2), ModifyIcon.DIVISION_2,
                Component.translatable("gui.gtlcore.pattern_recipe_divide_2"),
                Component.translatable("tooltip.gtlcore.pattern_materials_divide_2"));

        gTICore$dividingThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-3), ModifyIcon.DIVISION_3,
                Component.translatable("gui.gtlcore.pattern_recipe_divide_3"),
                Component.translatable("tooltip.gtlcore.pattern_materials_divide_3"));

        gTICore$dividingFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-5), ModifyIcon.DIVISION_5,
                Component.translatable("gui.gtlcore.pattern_recipe_divide_5"),
                Component.translatable("tooltip.gtlcore.pattern_materials_divide_5"));

        widgets.add("modify1", gTICore$multipleTow);
        widgets.add("modify2", gTICore$multipleThree);
        widgets.add("modify3", gTICore$multipleFive);
        widgets.add("modify4", gTICore$dividingTow);
        widgets.add("modify5", gTICore$dividingThree);
        widgets.add("modify6", gTICore$dividingFive);
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    public void setVisibleHooks(boolean visible, CallbackInfo ci) {
        this.gTICore$multipleTow.setVisibility(visible);
        this.gTICore$multipleThree.setVisibility(visible);
        this.gTICore$multipleFive.setVisibility(visible);
        this.gTICore$dividingTow.setVisibility(visible);
        this.gTICore$dividingThree.setVisibility(visible);
        this.gTICore$dividingFive.setVisibility(visible);
    }
}
