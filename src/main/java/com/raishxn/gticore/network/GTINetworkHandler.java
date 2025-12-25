package com.raishxn.gticore.network;

import com.glodblock.github.glodium.network.NetworkHandler;
import com.raishxn.gticore.GTICORE;
import com.raishxn.gticore.network.packet.SStructureDetectHighlight;

public class GTINetworkHandler extends NetworkHandler {

    public static final GTINetworkHandler INSTANCE = new GTINetworkHandler();

    public GTINetworkHandler() {
        super(GTICORE.MOD_ID);
    }

    public void init() {
        registerPacket(SStructureDetectHighlight.class, SStructureDetectHighlight::new);
    }
}
