package com.raishxn.gticore.api.machine.trait.MEStock;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import static com.lowdragmc.lowdraglib.LDLib.isRemote;

public class ExportOnlyAEConfigureFluidSlot extends ExportOnlyAEFluidSlot implements IMESlot {

    @Getter
    @Setter
    private Runnable onConfigChanged;

    public ExportOnlyAEConfigureFluidSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        super(config, stock);
    }

    public ExportOnlyAEConfigureFluidSlot() {
        super();
    }

    @Override
    public void setConfig(@Nullable GenericStack config) {
        super.setConfig(config);
        if (!isRemote()) onConfigChanged.run();
    }

    @Override
    public void setOnConfigChanged(Runnable onConfigChanged) {

    }

    @Override
    public Runnable getOnConfigChanged() {
        return null;
    }

    @Override
    public void setConfigWithoutNotify(@Nullable GenericStack config) {
        this.config = config;
    }
}
