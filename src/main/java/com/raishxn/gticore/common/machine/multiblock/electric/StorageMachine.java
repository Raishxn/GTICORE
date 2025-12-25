package com.raishxn.gticore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StorageMachine extends WorkableElectricMultiblockMachine implements IMachineModifyDrops {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            StorageMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    public final NotifiableItemStackHandler machineStorage;

    public StorageMachine(IMachineBlockEntity holder, int slot) {
        super(holder);
        this.machineStorage = createMachineStorage(slot);
    }

    protected NotifiableItemStackHandler createMachineStorage(int value) {
        var handler = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH) {
            @Override
            public int getSlotLimit(int slot) {
                return value;
            }
        };

        handler.setFilter(this::filter);
        return handler;
    }

    @Override
    public void onDrops(List<ItemStack> drops) {
        clearInventory(machineStorage.storage);
    }

    protected boolean filter(@NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public @NotNull Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            group.addWidget(
                    new SlotWidget((Container) machineStorage.storage, 0, size.width - 30, size.height - 30, true, true)
                            .setBackground(GuiTextures.SLOT));
        }
        return widget;
    }

    public ItemStack getMachineStorageItem() {
        return machineStorage.getStackInSlot(0);
    }

    public void setMachineStorageItem(ItemStack item) {
        machineStorage.storage.setStackInSlot(0, item);
    }

    public boolean isEmpty() {
        return machineStorage.isEmpty();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
