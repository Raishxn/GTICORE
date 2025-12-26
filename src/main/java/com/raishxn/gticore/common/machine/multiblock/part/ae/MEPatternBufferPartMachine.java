package com.raishxn.gticore.common.machine.multiblock.part.ae;

import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.misc.FluidTransferList;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.LazyManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import com.raishxn.gticore.api.gui.MEPatternCatalystUIManager;
import com.raishxn.gticore.api.machine.trait.NotifiableCircuitItemStackHandler;
import com.raishxn.gticore.api.machine.trait.MEPart.IMEPatternPartMachine;
import com.raishxn.gticore.api.machine.trait.MEPart.IMEPatternTrait;
import com.raishxn.gticore.api.pattern.AdvancedBlockPattern;
import com.raishxn.gticore.common.data.GTIMachines.GTAEMachines;
import com.raishxn.gticore.integration.ae2.AEUtils;
import com.raishxn.gticore.integration.ae2.handler.MEBufferPatternHelper;
import com.raishxn.gticore.integration.ae2.handler.SlotCacheManager;
import com.raishxn.gticore.integration.ae2.widget.AEPatternViewExtendSlotWidget;
import com.raishxn.gticore.utils.GTIUtil;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

public class MEPatternBufferPartMachine extends MEIOPartMachine implements IInteractedMachine, ICraftingProvider, PatternContainer, IMEPatternPartMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER;
    private final InternalInventory internalPatternInventory = new InternalInventory() {
        public int size() {
            return MEPatternBufferPartMachine.this.maxPatternCount;
        }

        public ItemStack getStackInSlot(int slotIndex) {
            return MEPatternBufferPartMachine.this.patternInventory.getStackInSlot(slotIndex);
        }

        public void setItemDirect(int slotIndex, ItemStack stack) {
            MEPatternBufferPartMachine.this.patternInventory.setStackInSlot(slotIndex, stack);
            MEPatternBufferPartMachine.this.patternInventory.onContentsChanged(slotIndex);
            MEPatternBufferPartMachine.this.onPatternChange(slotIndex);
        }

        public boolean isItemValid(int slot, ItemStack stack) {
            return slot <= MEPatternBufferPartMachine.this.maxPatternCount && (Boolean)AEUtils.PROCESS_FILTER.apply(stack);
        }
    };
    @DescSynced
    @Persisted
    protected String customName = "";
    @Persisted
    protected boolean keepByProduct = false;
    protected final int maxPatternCount;
    private final boolean[] hasPatternArray;
    @DescSynced
    protected final boolean[] cacheRecipe;
    private boolean needPatternSync;
    @Persisted
    private final ItemStackTransfer patternInventory;
    @Persisted(
            key = "shareInventory"
    )
    protected final CatalystItemStackHandler sharedCatalystInventory;
    @Persisted(
            key = "shareTank"
    )
    protected final CatalystFluidStackHandler sharedCatalystTank;
    @Persisted(
            key = "mePatternCircuitInventory"
    )
    protected final NotifiableItemStackHandler sharedCircuitInventory;
    @Persisted
    protected final ItemStackTransfer[] catalystItems;
    @Persisted
    @LazyManaged
    protected final FluidTransferList[] catalystFluids;
    @Persisted
    protected final InternalSlot[] internalInventory;
    protected final Object2LongOpenHashMap<AEKey> buffer;
    protected final MEBufferPatternHelper realPatternHelper;
    protected final MEPatternBufferRecipeHandlerTrait recipeHandler;
    protected final Int2ReferenceMap<ObjectSet<@NotNull GTRecipe>> recipeMultipleCacheMap;
    protected final byte[] cacheRecipeCount;
    private final BiMap<@NotNull IPatternDetails, Integer> patternSlotMap;
    private final Int2ObjectMap<IPatternDetails> slot2PatternMap;
    protected IntConsumer removeSlotFromMap = (i) -> {
    };
    @Persisted
    private final ObjectOpenHashSet<BlockPos> proxies;
    private final Set<MEPatternBufferProxyPartMachine> proxyMachines;
    protected @Nullable TickableSubscription updateSubs;

    public MEPatternBufferPartMachine(IMachineBlockEntity holder, int maxPatternCount, IO io) {
        super(holder, io);
        this.maxPatternCount = maxPatternCount;
        this.hasPatternArray = new boolean[maxPatternCount];
        this.cacheRecipe = new boolean[maxPatternCount];
        this.internalInventory = new InternalSlot[maxPatternCount];
        this.catalystItems = new ItemStackTransfer[maxPatternCount];
        this.catalystFluids = new FluidTransferList[maxPatternCount];
        this.cacheRecipeCount = new byte[maxPatternCount];
        this.patternSlotMap = HashBiMap.create();
        this.slot2PatternMap = new Int2ObjectOpenHashMap();
        this.recipeMultipleCacheMap = new Int2ReferenceOpenHashMap();
        this.proxies = new ObjectOpenHashSet();
        this.proxyMachines = new ReferenceOpenHashSet();
        this.buffer = new Object2LongOpenHashMap();
        this.patternInventory = new ItemStackTransfer(maxPatternCount);
        this.patternInventory.setFilter(AEUtils.PROCESS_FILTER);
        Arrays.setAll(this.internalInventory, (x$0) -> new InternalSlot(x$0));
        Arrays.setAll(this.catalystItems, (i) -> {
            ItemStackTransfer transfer = new ItemStackTransfer(9);
            transfer.setFilter((stack) -> !(stack.getItem() instanceof ProcessingPatternItem));
            return transfer;
        });
        Arrays.setAll(this.catalystFluids, (i) -> new FluidTransferList((IFluidTransfer) Stream.generate(() -> new FluidStorage(16L * FluidHelper.getBucket())).limit(9L).toList()));
        Arrays.fill(this.cacheRecipeCount, (byte)1);
        this.sharedCircuitInventory = new NotifiableCircuitItemStackHandler(this);
        this.realPatternHelper = new MEBufferPatternHelper((NotifiableCircuitItemStackHandler)this.sharedCircuitInventory);
        this.sharedCatalystInventory = new CatalystItemStackHandler(this, 9, IO.IN, IO.NONE);
        this.sharedCatalystTank = new CatalystFluidStackHandler((MetaMachine) this, 9, (int) (16L * FluidHelper.getBucket()), IO.IN, IO.NONE);
        this.recipeHandler = new MEPatternBufferRecipeHandlerTrait(this, io);
        this.getMainNode().addService(ICraftingProvider.class, this);
        if (io == IO.BOTH) {
            this.getMainNode().addService(IGridTickable.class, new Ticker());
        }

    }

    public void onLoad() {
        super.onLoad();
        Level var2 = this.getLevel();
        if (var2 instanceof ServerLevel serverLevel) {
            serverLevel.getServer().execute(() -> {
                for(int i = 0; i < this.patternInventory.getSlots(); ++i) {
                    ItemStack pattern = this.patternInventory.getStackInSlot(i);
                    IPatternDetails realPattern = this.getRealPattern(i, pattern);
                    if (realPattern != null) {
                        this.slot2PatternMap.put(i, realPattern);
                        this.hasPatternArray[i] = true;
                    }
                }

                this.reCalculatePatternSlotMap();
                this.needPatternSync = true;
            });

            for(int i = 0; i < this.maxPatternCount; ++i) {
                int index = i;
                int finalI = i;
                this.catalystItems[i].setOnContentsChanged(() -> this.reCalculateCatalystItemMap(finalI));

                for(IFluidTransfer transfer : this.catalystFluids[i].transfers) {
                    if (transfer instanceof FluidStorage storage) {
                        storage.setOnContentsChanged(() -> this.reCalculateCatalystFluidMap(index));
                    }
                }
            }
        }

    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void setOnline(boolean online) {

    }

    public void onMainNodeStateChanged(IGridNodeListener.@NotNull State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateSubscription();
    }

    protected void updateSubscription() {
        if (this.getMainNode().isOnline()) {
            this.updateSubs = this.subscribeServerTick(this.updateSubs, this::update);
        } else if (this.updateSubs != null) {
            this.updateSubs.unsubscribe();
            this.updateSubs = null;
        }

    }

    protected void update() {
        if (this.needPatternSync) {
            ICraftingProvider.requestUpdate(this.getMainNode());
            this.needPatternSync = false;
        }

    }

    protected void onPatternChange(int index) {
        if (!this.isRemote()) {
            InternalSlot internalInv = this.internalInventory[index];
            ItemStack newPattern = this.patternInventory.getStackInSlot(index);
            IPatternDetails newPatternDetailsWithOutCircuit = this.getRealPattern(index, newPattern);
            IPatternDetails oldPatternDetails = (IPatternDetails)this.slot2PatternMap.get(index);
            if (newPatternDetailsWithOutCircuit != null) {
                this.slot2PatternMap.put(index, newPatternDetailsWithOutCircuit);
                this.hasPatternArray[index] = true;
            } else {
                this.slot2PatternMap.remove(index);
                this.hasPatternArray[index] = false;
            }

            if (oldPatternDetails != null && !oldPatternDetails.equals(newPatternDetailsWithOutCircuit)) {
                internalInv.cacheManager.clearAllCaches();
                this.removeSlotFromGTRecipeCache(index);
                this.refundSlot(internalInv);
                AEUtils.reFunds(this.buffer, this.getMainNode().getGrid(), this.actionSource);
            }

            this.reCalculatePatternSlotMap();
            this.needPatternSync = true;
        }
    }

    public void onMachineRemoved() {
        this.clearInventory((IItemHandlerModifiable) this.patternInventory);
        this.clearInventory(this.sharedCatalystInventory);

        for(ItemStackTransfer catalystItem : this.catalystItems) {
            this.clearInventory((IItemHandlerModifiable) catalystItem);
        }

        for(MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
            proxy.setBuffer((BlockPos)null);
        }

    }

    private void reCalculateCatalystItemMap(int slot) {
        Object2LongMap<AEItemKey> itemCatalystInventory = this.internalInventory[slot].itemCatalystInventory;
        itemCatalystInventory.clear();
        ItemStackTransfer catalystItem = this.catalystItems[slot];

        for(int i = 0; i < catalystItem.getSlots(); ++i) {
            ItemStack stack = catalystItem.getStackInSlot(i);
            if (!stack.isEmpty()) {
                itemCatalystInventory.mergeLong(AEItemKey.of(stack), (long)stack.getCount(), Long::sum);
            }
        }

        this.internalInventory[slot].onContentsChanged.run();
    }

    private void reCalculateCatalystFluidMap(int slot) {
        Object2LongMap<AEFluidKey> fluidCatalystInventory = this.internalInventory[slot].fluidCatalystInventory;
        fluidCatalystInventory.clear();
        FluidTransferList catalystFluid = this.catalystFluids[slot];

        for(int i = 0; i < catalystFluid.getTanks(); ++i) {
            FluidStack stack = catalystFluid.getFluidInTank(i);
            if (!stack.isEmpty()) {
                fluidCatalystInventory.mergeLong(AEFluidKey.of(stack.getFluid()), stack.getAmount(), Long::sum);
            }
        }

        this.internalInventory[slot].onContentsChanged.run();
    }

    protected void reCalculatePatternSlotMap() {
        this.patternSlotMap.clear();
        ObjectIterator var1 = Int2ObjectMaps.fastIterable(this.slot2PatternMap).iterator();

        while(var1.hasNext()) {
            Int2ObjectMap.Entry<IPatternDetails> entry = (Int2ObjectMap.Entry)var1.next();
            int slot = entry.getIntKey();
            IPatternDetails pattern = (IPatternDetails)entry.getValue();
            if (pattern != null) {
                if (this.cacheRecipe[slot]) {
                    this.patternSlotMap.forcePut(pattern, slot);
                } else {
                    this.patternSlotMap.putIfAbsent(pattern, slot);
                }
            }
        }

    }

    protected void removeSlotFromGTRecipeCache(int slot) {
        this.cacheRecipe[slot] = false;
        this.recipeMultipleCacheMap.remove(slot);
        this.removeSlotFromMap.accept(slot);

        for(MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
            proxy.removeSlotFromMap.accept(slot);
        }

    }

    protected void refreshAllByProduct() {
        this.slot2PatternMap.clear();

        for(int i = 0; i < this.patternInventory.getSlots(); ++i) {
            ItemStack pattern = this.patternInventory.getStackInSlot(i);
            IPatternDetails realPattern = this.getRealPattern(i, pattern);
            if (realPattern != null) {
                this.slot2PatternMap.put(i, realPattern);
            }
        }

        this.reCalculatePatternSlotMap();
        this.needPatternSync = true;
    }

    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (!this.recipeMultipleCacheMap.isEmpty()) {
            CompoundTag recipeCacheTag = new CompoundTag();
            ObjectIterator var4 = Int2ReferenceMaps.fastIterable(this.recipeMultipleCacheMap).iterator();

            while(var4.hasNext()) {
                Int2ReferenceMap.Entry<ObjectSet<GTRecipe>> entry = (Int2ReferenceMap.Entry)var4.next();
                ObjectSet<GTRecipe> recipeSet = (ObjectSet)entry.getValue();
                if (!recipeSet.isEmpty()) {
                    ListTag list = new ListTag();
                    ObjectIterator var8 = recipeSet.iterator();

                    while(var8.hasNext()) {
                        GTRecipe recipe = (GTRecipe)var8.next();
                        list.add(GTIUtil.serializeNBT(recipe));
                    }

                    recipeCacheTag.put(Integer.toString(entry.getIntKey()), list);
                }
            }

            tag.put("recipeMultipleCacheIdMap", recipeCacheTag);
        }

        tag.putByteArray("cacheRecipeCount", this.cacheRecipeCount);
        ListTag bufferTag = AEUtils.createListTag(AEKey::toTagGeneric, this.buffer);
        if (!bufferTag.isEmpty()) {
            tag.put("buffer", bufferTag);
        }

    }

    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        byte[] byteArray = tag.getByteArray("cacheRecipeCount");
        System.arraycopy(byteArray, 0, this.cacheRecipeCount, 0, byteArray.length);
        this.recipeMultipleCacheMap.clear();

        RecipeManager recipeManager = this.getLevel() != null ? this.getLevel().getRecipeManager() : null;

        if (tag.contains("recipeMultipleCacheIdMap")) {
            CompoundTag recipeCacheTag = tag.getCompound("recipeMultipleCacheIdMap");

            for(String key : recipeCacheTag.getAllKeys()) {
                int slotIndex = Integer.parseInt(key);

                for(Tag recipeTag : recipeCacheTag.getList(key, 10)) {
                    GTRecipe recipe = GTIUtil.deserializeNBT(recipeTag);
                    if (recipe != null && slotIndex >= 0 && slotIndex < this.maxPatternCount && recipeManager != null) {
                        GTRecipe real = recipeManager.byKey(recipe.id).orElse(null) instanceof GTRecipe gtr ? gtr : null;

                        if (real != null) {
                            ObjectSet<GTRecipe> set = (ObjectSet)this.recipeMultipleCacheMap.computeIfAbsent(slotIndex, (integer) -> new ObjectArraySet());
                            set.add(real);
                            if (set.size() >= this.cacheRecipeCount[slotIndex]) {
                                this.cacheRecipe[slotIndex] = true;
                            }
                        }
                    }
                }
            }
        } else if (tag.contains("gtRecipeCache")) {
            CompoundTag oldRecipeCacheTag = tag.getCompound("gtRecipeCache");

            for(String key : oldRecipeCacheTag.getAllKeys()) {
                int slotIndex = Integer.parseInt(key);
                Tag recipeTag = oldRecipeCacheTag.get(key);
                GTRecipe recipe = GTIUtil.deserializeNBT(recipeTag);
                if (recipe != null && slotIndex >= 0 && slotIndex < this.maxPatternCount && recipeManager != null) {
                    GTRecipe real = recipeManager.byKey(recipe.id).orElse(null) instanceof GTRecipe gtr ? gtr : null;

                    if (real != null) {
                        ObjectSet<GTRecipe> set = (ObjectSet)this.recipeMultipleCacheMap.computeIfAbsent(slotIndex, (integer) -> new ObjectArraySet());
                        set.add(real);
                        if (set.size() >= this.cacheRecipeCount[slotIndex]) {
                            this.cacheRecipe[slotIndex] = true;
                        }
                    }
                }
            }
        }

        ListTag bufferTag = tag.getList("buffer", 10);
        AEUtils.loadInventory(bufferTag, AEKey::fromTagGeneric, this.buffer);
    }

    public void copyFromTag(CompoundTag tag, ServerPlayer serverPlayer) {
        this.setCustomName(tag.getString("name"));
        ListTag list = tag.getList("patterns", 10);
        int listIndex = 0;

        for(int index = 0; index < this.internalPatternInventory.size() && listIndex < list.size(); ++index) {
            if (this.internalPatternInventory.getStackInSlot(index).isEmpty()) {
                List var10001 = List.of(AEItems.BLANK_PATTERN.stack());
                ItemStack var10002 = AEItems.BLANK_PATTERN.stack();
                Objects.requireNonNull(var10002);
                Triplet<ItemStack, IItemHandler, Integer> result = AdvancedBlockPattern.foundItem(serverPlayer, var10001, var10002::is);
                if (result.getA() == null) {
                    break;
                }

                CompoundTag patternData = list.getCompound(listIndex);
                CompoundTag patternTag = patternData.getCompound("pattern");
                byte sourceCacheCount = patternData.getByte("cacheCount");
                if (sourceCacheCount <= 0) {
                    break;
                }

                this.internalPatternInventory.setItemDirect(index, ItemStack.of(patternTag));
                this.cacheRecipeCount[index] = sourceCacheCount;
                IItemHandler handler = (IItemHandler)result.getB();
                if (handler != null) {
                    handler.extractItem((Integer)result.getC(), 1, false);
                }

                ++listIndex;
            }
        }

    }

    public CompoundTag copyToTag(CompoundTag tags) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.customName);
        ListTag listPattern = new ListTag();

        for(int slotIndex : this.patternSlotMap.values()) {
            ItemStack stack = this.internalPatternInventory.getStackInSlot(slotIndex);
            if (!stack.isEmpty()) {
                CompoundTag patternData = new CompoundTag();
                patternData.put("pattern", stack.serializeNBT());
                patternData.putByte("cacheCount", this.cacheRecipeCount[slotIndex]);
                listPattern.add(patternData);
            }
        }

        tag.put("patterns", listPattern);
        tags.put("tag", tag);
        return tags;
    }

    public boolean pasteFromTag(CompoundTag tag) {
        this.setCustomName(tag.getString("name"));
        ListTag list = tag.getList("patterns", 10);
        int usedCount = 0;

        for(ItemStack ignored : this.internalPatternInventory) {
            ++usedCount;
        }

        if (this.internalPatternInventory.size() - usedCount < list.size()) {
            return false;
        } else {
            int listIndex = 0;

            for(int index = 0; index < this.internalPatternInventory.size() && listIndex < list.size(); ++index) {
                if (this.internalPatternInventory.getStackInSlot(index).isEmpty()) {
                    CompoundTag patternData = list.getCompound(listIndex);
                    CompoundTag patternTag = patternData.getCompound("pattern");
                    byte sourceCacheCount = patternData.getByte("cacheCount");
                    CompoundTag catalystItems = patternData.getCompound("catalystItems");
                    CompoundTag catalystFluids = patternData.getCompound("catalystFluids");
                    this.internalPatternInventory.setItemDirect(index, ItemStack.of(patternTag));
                    this.catalystItems[index].deserializeNBT(catalystItems);
                    this.catalystFluids[index].deserializeNBT(catalystFluids);
                    if (sourceCacheCount > 0) {
                        this.cacheRecipeCount[index] = sourceCacheCount;
                    }

                    ++listIndex;
                }
            }

            this.keepByProduct = tag.getBoolean("keepByProduct");
            this.sharedCatalystInventory.storage.deserializeNBT(tag.getCompound("sharedCatalystInventory"));
            this.sharedCircuitInventory.storage.deserializeNBT(tag.getCompound("sharedCircuitInventory"));
            ListTag catalystTanks = tag.getList("sharedCatalystTank", 10);
            CustomFluidTank[] tankStorages = this.sharedCatalystTank.getStorages();

            for(int i = 0; i < Math.min(catalystTanks.size(), tankStorages.length); ++i) {
                tankStorages[i].deserializeNBT(catalystTanks.getCompound(i));
            }

            for(MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
                proxy.setBuffer((BlockPos)null);
            }

            this.proxyMachines.clear();
            this.proxies.clear();

            for(long l : tag.getLongArray("proxies")) {
                BlockPos pos = BlockPos.of(l);
                MetaMachine var14 = MetaMachine.getMachine((BlockGetter)Objects.requireNonNull(this.getLevel()), pos);
                if (var14 instanceof MEPatternBufferProxyPartMachine) {
                    MEPatternBufferProxyPartMachine proxy = (MEPatternBufferProxyPartMachine)var14;
                    proxy.setBuffer(this.getPos());
                }
            }

            this.refreshAllByProduct();
            return true;
        }
    }

    public CompoundTag cutToTag(CompoundTag tags) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.customName);
        ListTag listPattern = new ListTag();

        for(int slotIndex : this.patternSlotMap.values().stream().toList()) {
            ItemStack stack = this.internalPatternInventory.getStackInSlot(slotIndex);
            if (!stack.isEmpty()) {
                CompoundTag patternData = new CompoundTag();
                patternData.put("pattern", stack.serializeNBT());
                patternData.putByte("cacheCount", this.cacheRecipeCount[slotIndex]);
                patternData.put("catalystItems", this.catalystItems[slotIndex].serializeNBT());
                patternData.put("catalystFluids", this.catalystFluids[slotIndex].serializeNBT());
                listPattern.add(patternData);
                this.internalPatternInventory.setItemDirect(slotIndex, ItemStack.EMPTY);

                for(int i = 0; i < this.catalystItems[slotIndex].getSlots(); ++i) {
                    this.catalystItems[slotIndex].setStackInSlot(i, ItemStack.EMPTY);
                }

                for(int i = 0; i < this.catalystFluids[slotIndex].getTanks(); ++i) {
                    this.catalystFluids[slotIndex].setFluidInTank(i, FluidStack.empty());
                }
            }
        }

        tag.put("patterns", listPattern);
        tag.put("sharedCatalystInventory", this.sharedCatalystInventory.storage.serializeNBT());
        tag.put("sharedCircuitInventory", this.sharedCircuitInventory.storage.serializeNBT());
        tag.putBoolean("keepByProduct", this.keepByProduct);
        tag.put("proxies", new LongArrayTag(this.proxies.stream().map(BlockPos::asLong).toList()));
        ListTag tankList = new ListTag();
        Stream<CompoundTag> var10000 = Arrays.stream(this.sharedCatalystTank.getStorages()).map(s -> s.serializeNBT());
        Objects.requireNonNull(tankList);
        var10000.forEach(tankList::add);
        tag.put("sharedCatalystTank", tankList);

        for(int i = 0; i < this.sharedCatalystInventory.getSlots(); ++i) {
            this.sharedCatalystInventory.setStackInSlot(i, ItemStack.EMPTY);
        }

        for(int i = 0; i < this.sharedCatalystTank.getTanks(); ++i) {
            this.sharedCatalystTank.setFluidInTank(i, net.minecraftforge.fluids.FluidStack.EMPTY);
        }

        for(MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
            proxy.setBuffer((BlockPos)null);
        }

        this.proxyMachines.clear();
        this.proxies.clear();
        tags.put("cut", tag);
        return tags;
    }

    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public void addProxy(MEPatternBufferProxyPartMachine proxy) {
        this.proxies.add(proxy.getPos());
        this.proxyMachines.add(proxy);
    }

    public void removeProxy(MEPatternBufferProxyPartMachine proxy) {
        this.proxies.remove(proxy.getPos());
        this.proxyMachines.remove(proxy);
    }

    public Set<MEPatternBufferProxyPartMachine> getProxies() {
        if (this.proxyMachines.size() != this.proxies.size()) {
            this.proxyMachines.clear();
            ObjectIterator<BlockPos> it = this.proxies.iterator();

            while(it.hasNext()) {
                BlockPos pos = (BlockPos)it.next();
                MetaMachine var4 = MetaMachine.getMachine((BlockGetter)Objects.requireNonNull(this.getLevel()), pos);
                if (var4 instanceof MEPatternBufferProxyPartMachine) {
                    MEPatternBufferProxyPartMachine proxy = (MEPatternBufferProxyPartMachine)var4;
                    this.proxyMachines.add(proxy);
                } else {
                    it.remove();
                }
            }
        }

        return Collections.unmodifiableSet(this.proxyMachines);
    }

    private void refundAll(ClickData clickData) {
        if (!clickData.isRemote) {
            Arrays.stream(this.internalInventory).filter(InternalSlot::isActive).forEach(this::refundSlot);
            AEUtils.reFunds(this.buffer, this.getMainNode().getGrid(), this.actionSource);
        }

    }

    public void refundSlot(InternalSlot slot) {
        ObjectIterator<Object2LongMap.Entry<AEItemKey>> it = slot.itemInventory.object2LongEntrySet().fastIterator();
        while(it.hasNext()) {
            Object2LongMap.Entry<AEItemKey> entry = (Object2LongMap.Entry)it.next();
            long amount = entry.getLongValue();
            if (amount > 0L) {
                this.buffer.addTo((AEKey)entry.getKey(), amount);
                it.remove();
            }
        }

        ObjectIterator<Object2LongMap.Entry<AEFluidKey>> itFluid = slot.fluidInventory.object2LongEntrySet().fastIterator();

        while(itFluid.hasNext()) {
            Object2LongMap.Entry<AEFluidKey> entry = (Object2LongMap.Entry)itFluid.next();
            long amount = entry.getLongValue();
            if (amount > 0L) {
                this.buffer.addTo((AEKey)entry.getKey(), amount);
                itFluid.remove();
            }
        }

    }

    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return InteractionResult.PASS;
        } else if (stack.is(GTItems.TOOL_DATA_STICK.asItem())) {
            if (!world.isClientSide) {
                var researchData = ResearchManager.readResearchId(stack);
                if (researchData != null) {
                    return InteractionResult.PASS;
                }

                stack.getOrCreateTag().putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendSystemMessage(Component.translatable("gtceu.machine.me.import_copy_settings"));
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new IFancyConfigurator[]{(new ButtonConfigurator(new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY}), this::refundAll)).setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc")))});
        configuratorPanel.attachConfigurators(new IFancyConfigurator[]{new CircuitFancyConfigurator(this.sharedCircuitInventory.storage)});
        configuratorPanel.attachConfigurators(new IFancyConfigurator[]{(new FancyInvConfigurator(this.sharedCatalystInventory.storage, Component.translatable("gui.gtceu.share_inventory.title"))).setTooltips(List.of(Component.translatable("gui.gtceu.share_inventory.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1")))});
        configuratorPanel.attachConfigurators(new IFancyConfigurator[]{(new FancyTankConfigurator(this.sharedCatalystTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title"))).setTooltips(List.of(Component.translatable("gui.gtceu.share_tank.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1")))});
        configuratorPanel.attachConfigurators(new IFancyConfigurator[]{(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture((double)0.0F, (double)0.0F, (double)1.0F, (double)0.5F), GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture((double)0.0F, (double)0.5F, (double)1.0F, (double)0.5F), () -> !this.keepByProduct, (clickData, pressed) -> {
            this.keepByProduct = !pressed;
            this.refreshAllByProduct();
        })).setTooltipsSupplier((pressed) -> List.of(Component.translatable("tooltip.gtlcore.disable_by_product").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)).append(Component.translatable(pressed ? "gtceu.multiblock.universal.distinct.yes" : "gtceu.multiblock.universal.distinct.no"))))});
    }

    public @NotNull Widget createUIWidget() {
        int rowSize = 9;
        int colSize = this.maxPatternCount / rowSize;
        WidgetGroup group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
        group.addWidget(new LabelWidget(8, 2, () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        group.addWidget((new AETextInputButtonWidget(18 * rowSize + 8 - 70, 2, 70, 10)).setText(this.customName).setOnConfirm(this::setCustomName).setButtonTooltips(new Component[]{Component.translatable("gui.gtceu.rename.desc")}));
        MEPatternCatalystUIManager catalystUIManager = new MEPatternCatalystUIManager(group.getSizeWidth() + 4, this.catalystItems, this.catalystFluids, this.cacheRecipeCount, this::removeSlotFromGTRecipeCache);
        group.waitToAdded(catalystUIManager);
        int index = 0;

        for(int y = 0; y < colSize; ++y) {
            for(int x = 0; x < rowSize; ++x) {
                int finalI = index;
                Widget slot = (new AEPatternViewExtendSlotWidget(this.patternInventory, index++, x * 18 + 8, y * 18 + 14)).setOnMiddleClick(() -> catalystUIManager.toggleFor(finalI)).setOnPatternSlotChanged(() -> this.onPatternChange(finalI)).setOccupiedTexture(new IGuiTexture[]{GuiTextures.SLOT}).setItemHook((stack) -> {
                    if (!stack.isEmpty()) {
                        Item patt0$temp = stack.getItem();
                        if (patt0$temp instanceof EncodedPatternItem) {
                            EncodedPatternItem iep = (EncodedPatternItem)patt0$temp;
                            ItemStack out = iep.getOutput(stack);
                            if (!out.isEmpty()) {
                                return out;
                            }
                        }
                    }

                    return stack;
                }).setOnAddedTooltips((s, l) -> {
                    if (this.cacheRecipe[finalI]) {
                        l.add(Component.translatable("gtceu.machine.pattern.recipe.cache"));
                    }

                }).setBackground(new IGuiTexture[]{GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY});
                group.addWidget(slot);
            }
        }

        return group;
    }

    private IPatternDetails getRealPattern(int slot, ItemStack stack) {
        if (!stack.isEmpty()) {
            InternalSlot internalSlot = this.internalInventory[slot];
            MEBufferPatternHelper var10000 = this.realPatternHelper;
            SlotCacheManager var10002 = internalSlot.cacheManager;
            Objects.requireNonNull(var10002);
            return var10000.processPatternWithCircuit(stack, var10002::setCircuitCache, this.getLevel(), this.keepByProduct);
        } else {
            return null;
        }
    }

    public ItemStack getCircuitForRecipe(int slotIndex) {
        return this.realPatternHelper.getCircuitForRecipe(this.internalInventory[slotIndex].cacheManager.getCircuitStack());
    }

    public List<IPatternDetails> getAvailablePatterns() {
        return this.patternSlotMap.keySet().stream().toList();
    }

    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (this.getMainNode().isActive() && this.patternSlotMap.containsKey(patternDetails)) {
            Integer slotIndex = (Integer)this.patternSlotMap.get(patternDetails);
            if (slotIndex != null && slotIndex >= 0) {
                this.internalInventory[slotIndex].pushPattern(inputHolder);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isBusy() {
        return false;
    }

    public @Nullable IGrid getGrid() {
        return this.getMainNode().getGrid();
    }

    public InternalInventory getTerminalPatternInventory() {
        return this.internalPatternInventory;
    }

    public PatternContainerGroup getTerminalGroup() {
        List<IMultiController> controllers = (List<IMultiController>) this.getControllers();
        if (!controllers.isEmpty()) {
            IMultiController controller = (IMultiController)controllers.get(0);
            MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
            if (!this.customName.isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.literal(this.customName), Collections.emptyList());
            } else {
                ItemStack circuitStack = this.sharedCircuitInventory.storage.getStackInSlot(0);
                int circuitConfiguration = circuitStack.isEmpty() ? -1 : IntCircuitBehaviour.getCircuitConfiguration(circuitStack);
                Component groupName = circuitConfiguration != -1 ? Component.translatable(controllerDefinition.getDescriptionId()).append(" - " + circuitConfiguration) : Component.translatable(controllerDefinition.getDescriptionId());
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
            }
        } else {
            return !this.customName.isEmpty() ? new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_EXTEND_PATTERN_BUFFER.getItem()), Component.literal(this.customName), Collections.emptyList()) : new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_EXTEND_PATTERN_BUFFER.getItem()), GTAEMachines.ME_EXTEND_PATTERN_BUFFER.get().getDefinition().getItem().getDescription(), Collections.emptyList());
        }
    }

    protected @NotNull IMEPatternTrait createMETrait() {
        return new MEPatternTrait(this);
    }

    public @NotNull IMEPatternTrait getMETrait() {
        return (IMEPatternTrait)this.meTrait;
    }

    // CORREÇÃO: Usar FluidStack da LowDragLib (com.lowdragmc.lowdraglib.side.fluid.FluidStack)
    // para casar com a assinatura da interface IMEFilterIOPartMachine
    public Pair getMERecipeHandlerTraits() {

        // Unsafe cast para forçar compatibilidade entre o Pair retornado pelo recipeHandler (que usa
        // net.minecraftforge.fluids.FluidStack) e a assinatura exigida pela interface (que espera LDL FluidStack).
        // Isso resolve o erro de compilação imediato; preferível a médio prazo é harmonizar os tipos (ver comentários abaixo).
        return Pair.of(
                this.recipeHandler.meItemHandler,
                this.recipeHandler.meFluidHandler
        );
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCustomName() {
        return this.customName;
    }

    public ItemStackTransfer getPatternInventory() {
        return this.patternInventory;
    }

    public CatalystItemStackHandler getSharedCatalystInventory() {
        return this.sharedCatalystInventory;
    }

    public CatalystFluidStackHandler getSharedCatalystTank() {
        return this.sharedCatalystTank;
    }

    public NotifiableItemStackHandler getSharedCircuitInventory() {
        return this.sharedCircuitInventory;
    }

    public InternalSlot[] getInternalInventory() {
        return this.internalInventory;
    }

    public Object2LongOpenHashMap<AEKey> getBuffer() {
        return this.buffer;
    }

    public MEBufferPatternHelper getRealPatternHelper() {
        return this.realPatternHelper;
    }

    static {
        MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEPatternBufferPartMachine.class, MEIOPartMachine.MANAGED_FIELD_HOLDER);
    }

    public class InternalSlot implements ITagSerializable<CompoundTag>, IContentChangeAware {
        protected Runnable onContentsChanged = () -> {
            MEPatternBufferPartMachine.this.recipeHandler.getMeFluidHandler().notifyListeners();
            MEPatternBufferPartMachine.this.recipeHandler.getMeItemHandler().notifyListeners();
        };
        private final Object2LongOpenHashMap<AEItemKey> itemInventory = new Object2LongOpenHashMap();
        private final Object2LongOpenHashMap<AEFluidKey> fluidInventory = new Object2LongOpenHashMap();
        private final Object2LongMap<AEItemKey> itemCatalystInventory = new Object2LongArrayMap();
        private final Object2LongMap<AEFluidKey> fluidCatalystInventory = new Object2LongArrayMap();
        @Persisted
        private final SlotCacheManager cacheManager = new SlotCacheManager();
        private final int slotIndex;

        public InternalSlot(int slotIndex) {
            this.slotIndex = slotIndex;
            this.itemInventory.defaultReturnValue(0L);
            this.fluidInventory.defaultReturnValue(0L);
            this.itemCatalystInventory.defaultReturnValue(0L);
            this.fluidCatalystInventory.defaultReturnValue(0L);
        }

        public boolean isActive() {
            return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && (!this.itemInventory.isEmpty() || !this.fluidInventory.isEmpty());
        }

        public boolean isItemActive(boolean simulate) {
            return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && simulate ? !this.itemInventory.isEmpty() || !MEPatternBufferPartMachine.this.sharedCatalystInventory.isEmpty() || !MEPatternBufferPartMachine.this.realPatternHelper.getCircuitForRecipe(this.cacheManager.getCircuitStack()).isEmpty() || !this.itemCatalystInventory.isEmpty() : !this.itemInventory.isEmpty();
        }

        public boolean isFluidActive(boolean simulate) {
            return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && simulate ? !this.fluidInventory.isEmpty() || !MEPatternBufferPartMachine.this.sharedCatalystTank.isEmpty() || !this.fluidCatalystInventory.isEmpty() : !this.fluidInventory.isEmpty();
        }

        private void add(AEKey what, long amount) {
            if (amount > 0L) {
                if (what instanceof AEItemKey) {
                    AEItemKey itemKey = (AEItemKey)what;
                    this.itemInventory.addTo(itemKey, amount);
                } else if (what instanceof AEFluidKey) {
                    AEFluidKey fluidKey = (AEFluidKey)what;
                    this.fluidInventory.addTo(fluidKey, amount);
                }

            }
        }

        public Object2LongMap<ItemStack> getItemStackInputMap() {
            Object2LongOpenCustomHashMap<ItemStack> itemInputMap = new Object2LongOpenCustomHashMap(ItemStackHashStrategy.comparingAllButCount());
            ObjectIterator var2 = Object2LongMaps.fastIterable(this.itemInventory).iterator();

            while(var2.hasNext()) {
                Object2LongMap.Entry<AEItemKey> entry = (Object2LongMap.Entry)var2.next();
                AEItemKey key = (AEItemKey)entry.getKey();
                long amount = entry.getLongValue();
                if (amount > 0L) {
                    ItemStack stack = key.toStack(1);
                    itemInputMap.addTo(stack, amount);
                }
            }

            return itemInputMap;
        }

        public Object2LongMap<FluidStack> getFluidStackInputMap() {
            Object2LongOpenCustomHashMap<FluidStack> fluidInputMap = new Object2LongOpenCustomHashMap(FluidStackHashStrategy.comparingAllButAmount());
            ObjectIterator var2 = Object2LongMaps.fastIterable(this.fluidInventory).iterator();

            while(var2.hasNext()) {
                Object2LongMap.Entry<AEFluidKey> entry = (Object2LongMap.Entry)var2.next();
                AEFluidKey key = (AEFluidKey)entry.getKey();
                long amount = entry.getLongValue();
                if (amount > 0L) {
                    FluidStack stack = FluidStack.create(key.getFluid(), 1L);
                    fluidInputMap.addTo(stack, amount);
                }
            }

            return fluidInputMap;
        }

        public ObjectList<ItemStack> getLimitItemStackInput() {
            ObjectArrayList<ItemStack> limitInput = new ObjectArrayList(this.itemInventory.size());
            ObjectIterator<Object2LongMap.Entry<AEItemKey>> it = Object2LongMaps.fastIterator(this.itemInventory);

            while(it.hasNext()) {
                Object2LongMap.Entry<AEItemKey> entry = (Object2LongMap.Entry)it.next();
                long amount = entry.getLongValue();
                if (amount <= 0L) {
                    it.remove();
                } else {
                    limitInput.add(((AEItemKey)entry.getKey()).toStack(Ints.saturatedCast(amount)));
                }
            }

            it = Object2LongMaps.fastIterable(this.itemCatalystInventory).iterator();

            while(it.hasNext()) {
                Object2LongMap.Entry<AEItemKey> entry = (Object2LongMap.Entry)it.next();
                limitInput.add(((AEItemKey)entry.getKey()).toStack(Ints.saturatedCast(entry.getLongValue())));
            }

            return limitInput;
        }

        public ObjectList<FluidStack> getLimitFluidStackInput() {
            ObjectArrayList<FluidStack> limitInput = new ObjectArrayList(this.fluidInventory.size());
            ObjectIterator<Object2LongMap.Entry<AEFluidKey>> it = Object2LongMaps.fastIterator(this.fluidInventory);

            while(it.hasNext()) {
                Object2LongMap.Entry<AEFluidKey> entry = (Object2LongMap.Entry)it.next();
                long amount = entry.getLongValue();
                if (amount <= 0L) {
                    it.remove();
                } else {
                    limitInput.add(FluidStack.create(((AEFluidKey)entry.getKey()).getFluid(), amount));
                }
            }

            it = Object2LongMaps.fastIterable(this.fluidCatalystInventory).iterator();

            while(it.hasNext()) {
                Object2LongMap.Entry<AEFluidKey> entry = (Object2LongMap.Entry)it.next();
                limitInput.add(FluidStack.create(((AEFluidKey)entry.getKey()).getFluid(), entry.getLongValue()));
            }

            return limitInput;
        }

        public void pushPattern(KeyCounter[] inputHolder) {
            AEUtils.pushInputsToMEPatternBufferInventory(inputHolder, this::add);
            this.onContentsChanged.run();
        }

        public boolean testCatalystItemInternal(GTRecipe recipe) {
            for(Content content : recipe.getInputContents(ItemRecipeCapability.CAP)) {
                if (content.chance > 0) {
                    Ingredient ingredient = (Ingredient)content.getContent();

                    for(ItemStack item : ingredient.getItems()) {
                        AEItemKey key = AEItemKey.of(item);
                        if (this.itemCatalystInventory.containsKey(key)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        public boolean testCatalystFluidInternal(GTRecipe recipe) {
            for(Content content : recipe.getInputContents(FluidRecipeCapability.CAP)) {
                if (content.chance > 0) {
                    FluidIngredient fluidIngredient = (FluidIngredient)content.getContent();

                    for(net.minecraftforge.fluids.FluidStack stack : fluidIngredient.getStacks()) {
                        AEFluidKey key = AEFluidKey.of(stack.getFluid());
                        if (this.fluidCatalystInventory.containsKey(key)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        public boolean handleItemInternal(Object2LongMap<Ingredient> left, int leftCircuit, boolean simulate) {
            if (left.isEmpty() && leftCircuit < 0) {
                return true;
            } else if (simulate && leftCircuit > 0 && leftCircuit != this.cacheManager.getCircuitCache()) {
                return false;
            } else {
                ObjectIterator<Object2LongMap.Entry<Ingredient>> it = Object2LongMaps.fastIterator(left);

                while(it.hasNext()) {
                    Object2LongMap.Entry<Ingredient> entry = (Object2LongMap.Entry)it.next();
                    Ingredient ingredient = (Ingredient)entry.getKey();
                    long needAmount = entry.getLongValue();
                    if (needAmount <= 0L) {
                        it.remove();
                    } else {
                        AEItemKey bestMatch = simulate ? this.cacheManager.getBestItemMatchSimulate(ingredient, this.itemInventory, this.itemCatalystInventory, needAmount) : this.cacheManager.getBestItemMatch(ingredient, this.itemInventory, needAmount);
                        if (bestMatch == null) {
                            return false;
                        }
                    }
                }

                if (!simulate) {
                    it = Object2LongMaps.fastIterator(left);

                    while(it.hasNext()) {
                        Object2LongMap.Entry<Ingredient> entry = (Object2LongMap.Entry)it.next();
                        Ingredient ingredient = (Ingredient)entry.getKey();
                        long needAmount = entry.getLongValue();
                        AEItemKey bestMatch = this.cacheManager.getBestItemMatch(ingredient, this.itemInventory, needAmount);
                        if (bestMatch != null) {
                            long amount = this.itemInventory.getLong(bestMatch);
                            long except = amount - needAmount;
                            if (except <= 0L) {
                                this.itemInventory.removeLong(bestMatch);
                            } else {
                                this.itemInventory.put(bestMatch, except);
                            }

                            it.remove();
                        }
                    }
                }

                return true;
            }
        }

        public boolean handleFluidInternal(Object2LongMap<FluidIngredient> left, boolean simulate) {
            if (left.isEmpty()) {
                return true;
            } else {
                ObjectIterator<Object2LongMap.Entry<FluidIngredient>> it = Object2LongMaps.fastIterator(left);

                while(it.hasNext()) {
                    Object2LongMap.Entry<FluidIngredient> entry = (Object2LongMap.Entry)it.next();
                    FluidIngredient ingredient = (FluidIngredient)entry.getKey();
                    long needAmount = entry.getLongValue();
                    if (needAmount <= 0L) {
                        it.remove();
                    } else {
                        AEFluidKey bestMatch = simulate ? this.cacheManager.getBestFluidMatchSimulate(ingredient, this.fluidInventory, this.fluidCatalystInventory, needAmount) : this.cacheManager.getBestFluidMatch(ingredient, this.fluidInventory, needAmount);
                        if (bestMatch == null) {
                            return false;
                        }
                    }
                }

                if (!simulate) {
                    it = Object2LongMaps.fastIterator(left);

                    while(it.hasNext()) {
                        Object2LongMap.Entry<FluidIngredient> entry = (Object2LongMap.Entry)it.next();
                        FluidIngredient ingredient = (FluidIngredient)entry.getKey();
                        long needAmount = entry.getLongValue();
                        AEFluidKey bestMatch = this.cacheManager.getBestFluidMatch(ingredient, this.fluidInventory, needAmount);
                        if (bestMatch != null) {
                            long amount = this.fluidInventory.getLong(bestMatch);
                            long except = amount - needAmount;
                            if (except <= 0L) {
                                this.fluidInventory.removeLong(bestMatch);
                            } else {
                                this.fluidInventory.put(bestMatch, except);
                            }

                            it.remove();
                        }
                    }
                }

                return true;
            }
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            ListTag itemsTag = AEUtils.createListTag(AEItemKey::toTag, this.itemInventory);
            if (!itemsTag.isEmpty()) {
                tag.put("inventory", itemsTag);
            }

            ListTag itemCatalystTag = AEUtils.createListTag(AEItemKey::toTag, this.itemCatalystInventory);
            if (!itemCatalystTag.isEmpty()) {
                tag.put("catalystInventory", itemCatalystTag);
            }

            ListTag fluidsTag = AEUtils.createListTag(AEFluidKey::toTag, this.fluidInventory);
            if (!fluidsTag.isEmpty()) {
                tag.put("fluidInventory", fluidsTag);
            }

            ListTag fluidCatalystTag = AEUtils.createListTag(AEFluidKey::toTag, this.fluidCatalystInventory);
            if (!fluidCatalystTag.isEmpty()) {
                tag.put("catalystFluidInventory", fluidCatalystTag);
            }

            return tag;
        }

        public void deserializeNBT(CompoundTag tag) {
            this.itemInventory.clear();
            this.itemCatalystInventory.clear();
            this.fluidInventory.clear();
            this.fluidCatalystInventory.clear();
            ListTag items = tag.getList("inventory", 10);
            AEUtils.loadInventory(items, AEItemKey::fromTag, this.itemInventory);
            ListTag catalystItems = tag.getList("catalystInventory", 10);
            AEUtils.loadInventory(catalystItems, AEItemKey::fromTag, this.itemCatalystInventory);
            ListTag fluids = tag.getList("fluidInventory", 10);
            AEUtils.loadInventory(fluids, AEFluidKey::fromTag, this.fluidInventory);
            ListTag catalystFluids = tag.getList("catalystFluidInventory", 10);
            AEUtils.loadInventory(catalystFluids, AEFluidKey::fromTag, this.fluidCatalystInventory);
        }

        public Runnable getOnContentsChanged() {
            return this.onContentsChanged;
        }

        public void setOnContentsChanged(Runnable onContentsChanged) {
            this.onContentsChanged = onContentsChanged;
        }

        public Object2LongOpenHashMap<AEItemKey> getItemInventory() {
            return this.itemInventory;
        }

        public Object2LongOpenHashMap<AEFluidKey> getFluidInventory() {
            return this.fluidInventory;
        }

        public SlotCacheManager getCacheManager() {
            return this.cacheManager;
        }

        public int getSlotIndex() {
            return this.slotIndex;
        }
    }

    protected class Ticker implements IGridTickable {
        public TickingRequest getTickingRequest(IGridNode node) {
            return new TickingRequest(MEExtendedOutputPartMachineBase.MIN_FREQUENCY, MEExtendedOutputPartMachineBase.MAX_FREQUENCY, false, true);
        }

        public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
            if (!MEPatternBufferPartMachine.this.getMainNode().isActive()) {
                return TickRateModulation.SLEEP;
            } else if (MEPatternBufferPartMachine.this.buffer.isEmpty()) {
                if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
                    MEPatternBufferPartMachine.this.isSleeping = true;
                    return TickRateModulation.SLEEP;
                } else {
                    return TickRateModulation.SLOWER;
                }
            } else {
                return AEUtils.reFunds(MEPatternBufferPartMachine.this.buffer, MEPatternBufferPartMachine.this.getMainNode().getGrid(), MEPatternBufferPartMachine.this.actionSource) ? TickRateModulation.URGENT : TickRateModulation.SLOWER;
            }
        }
    }

    protected class MEPatternTrait extends MEIOPartMachine.MEIOTrait implements IMEPatternTrait {
        public MEPatternTrait(MEPatternBufferPartMachine machine) {
            super(machine);
        }

        public MEPatternBufferPartMachine getMachine() {
            return (MEPatternBufferPartMachine)this.machine;
        }

        public @NotNull ObjectSet<@NotNull GTRecipe> getCachedGTRecipe() {
            ObjectSet<GTRecipe> recipes = new ObjectOpenHashSet();
            ObjectIterator<Int2ReferenceMap.Entry<ObjectSet<GTRecipe>>> it = Int2ReferenceMaps.fastIterator(MEPatternBufferPartMachine.this.recipeMultipleCacheMap);

            while(it.hasNext()) {
                Int2ReferenceMap.Entry<ObjectSet<GTRecipe>> entry = (Int2ReferenceMap.Entry)it.next();
                ObjectSet<GTRecipe> recipeSet = (ObjectSet)entry.getValue();
                int slot = entry.getIntKey();
                if (recipeSet.isEmpty()) {
                    it.remove();
                } else if (MEPatternBufferPartMachine.this.cacheRecipe[slot] && MEPatternBufferPartMachine.this.internalInventory[slot].isActive()) {
                    recipes.addAll(recipeSet);
                }
            }

            return recipes;
        }

        public void setSlotCacheRecipe(int index, GTRecipe recipe) {
            if (recipe != null && recipe.recipeType != GTRecipeTypes.DUMMY_RECIPES) {
                ObjectSet<GTRecipe> set = (ObjectSet)MEPatternBufferPartMachine.this.recipeMultipleCacheMap.computeIfAbsent(index, (integer) -> new ObjectArraySet());
                if (set.add(recipe)) {
                    MEPatternBufferPartMachine.this.cacheRecipe[index] = set.size() >= MEPatternBufferPartMachine.this.cacheRecipeCount[index];
                }
            }

        }

        public @NotNull Int2ReferenceMap<ObjectSet<@NotNull GTRecipe>> getSlot2RecipesCache() {
            return MEPatternBufferPartMachine.this.recipeMultipleCacheMap;
        }

        public void setOnPatternChange(IntConsumer removeMapOnSlot) {
            MEPatternBufferPartMachine.this.removeSlotFromMap = removeMapOnSlot;
        }

        public boolean hasCacheInSlot(int slot) {
            return MEPatternBufferPartMachine.this.cacheRecipe[slot];
        }
    }
}