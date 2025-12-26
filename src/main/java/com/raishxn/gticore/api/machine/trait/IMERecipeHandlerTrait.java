package com.raishxn.gticore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.raishxn.gticore.api.capability.IMERecipeHandler;

// import java.util.function.Predicate; // Não é mais estritamente necessário aqui

/**
 * @author Dragonators
 * @param <T>
 */
// ALTERAÇÃO: Removido "extends Predicate<S>"
public interface IMERecipeHandlerTrait<T, S> extends IMERecipeHandler<T, S> {

    IO getIo();

    ISubscription addChangedListener(Runnable var1);

    ISubscription addBufferChangedListener(Runnable var1);

    default int getPriority() {
        return switch (getIo()) {
            case OUT -> 10;
            case BOTH -> 5;
            case IN, NONE -> 0;
        };
    }
}