package com.vomiter.survivorsbutchercraft.core;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;

import java.util.Arrays;

public enum MeatHookStage {
    HOOK("hooked"),
    SKIN("skinned"),
    BISECT("bisected"),
    DISEMBOWEL("gutted"),
    BUTCHER("butchered");

    public final String pp;
    public String pre;

    public String previousStep(){
        if(pre != null) return pre;
        String previousStep;
        int i = Arrays.stream(values()).toList().indexOf(this);
        if(i == 0) previousStep = "";
        else previousStep = Arrays.stream(values()).toList().get(i-1).pp;
        SurvivorsButchercraft.LOGGER.info(name() + ":" + previousStep);
        pre = previousStep;
        return previousStep;
    };
    MeatHookStage(String pp){
        this.pp = pp;
    }
}
