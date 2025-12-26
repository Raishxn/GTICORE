package com.raishxn.gticore.mixin.ae2.service;

import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.InterestManager;
import appeng.me.helpers.StackWatcher;
import appeng.me.service.StorageService;
import com.raishxn.gticore.config.ConfigHolder;
import com.raishxn.gticore.utils.NumberUtils;
import org.spongepowered.asm.mixin.*;

@Mixin(StorageService.class)
public abstract class StorageServiceMixin {

    @Shadow(remap = false)
    @Final
    @Mutable
    private final InterestManager<StackWatcher<IStorageWatcherNode>> interestManager;
    @Shadow(remap = false)
    private boolean cachedStacksNeedUpdate;

    @Shadow(remap = false)
    protected abstract void updateCachedStacks();

    @Unique
    private static final int STORAGE_MASK = NumberUtils.nearestPow2Lookup(ConfigHolder.INSTANCE.ae2StorageServiceUpdateInterval) - 1;

    public StorageServiceMixin(InterestManager<StackWatcher<IStorageWatcherNode>> interestManager) {
        this.interestManager = interestManager;
    }

    /**
     * @author .
     * @reason 减少更新频率
     */
    @Overwrite(remap = false)
    public void onServerEndTick() {
        if (this.interestManager.isEmpty()) {
            this.cachedStacksNeedUpdate = true;
        } else {
            if ((TickHandler.instance().getCurrentTick() & STORAGE_MASK) == 0) this.updateCachedStacks();
        }
    }
}
