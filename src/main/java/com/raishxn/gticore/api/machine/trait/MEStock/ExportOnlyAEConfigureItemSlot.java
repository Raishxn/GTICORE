package com.raishxn.gticore.api.machine.trait.MEStock;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static com.lowdragmc.lowdraglib.LDLib.isRemote;

public abstract class ExportOnlyAEConfigureItemSlot extends ExportOnlyAEItemSlot implements IMESlot {

    @Setter
    @Getter
    private Runnable onConfigChanged;

    public ExportOnlyAEConfigureItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        super(config, stock);
    }

    public ExportOnlyAEConfigureItemSlot() {
        super();
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

    @Override
    public void setConfig(@Nullable GenericStack config) {
        super.setConfig(config);
        if (!isRemote()) onConfigChanged.run();
    }

    public abstract ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges);
}
