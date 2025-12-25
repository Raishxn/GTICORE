package com.raishxn.gticore.api.data.info;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;
import lombok.Getter;
import com.raishxn.gticore.client.renderer.item.StereoscopicItemRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class GTIMaterialIconSet extends MaterialIconSet {

    private final ICustomRenderer customRenderer;

    public GTIMaterialIconSet(@NotNull String name,
                              @Nullable MaterialIconSet parentIconset,
                              boolean isRootIconset,
                              ICustomRenderer customRenderer) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer;
    }

    public static final GTIMaterialIconSet CUSTOM_TRANSCENDENT_MENTAL = new GTIMaterialIconSet(
            "transcendent_mental",
            MaterialIconSet.METALLIC,
            false,
            () -> StereoscopicItemRenderer.INSTANCE);

    public static final MaterialIconSet LIMPID = new MaterialIconSet("limpid", DULL);
}
