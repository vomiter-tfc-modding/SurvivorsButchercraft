package com.vomiter.survivorsbutchercraft.client;

import net.minecraftforge.eventbus.api.IEventBus;

public class SBClientModEvents {
    public static void init(IEventBus modBus){
        modBus.addListener(SkullLikeRenderer::onRegisterRenderers);
        modBus.addListener(HookTransformReloadListener::onAddReloadListener);
    }
}
