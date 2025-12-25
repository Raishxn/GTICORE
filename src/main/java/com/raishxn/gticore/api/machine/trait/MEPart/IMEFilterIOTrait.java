package com.raishxn.gticore.api.machine.trait.MEPart;

public interface IMEFilterIOTrait extends IMEIOTrait {

    default boolean hasFilter() {
        return false;
    }
}
