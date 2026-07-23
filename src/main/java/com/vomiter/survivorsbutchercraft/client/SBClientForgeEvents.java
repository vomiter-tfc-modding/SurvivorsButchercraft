package com.vomiter.survivorsbutchercraft.client;

import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import net.dries007.tfc.client.ClientHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Optional;

public class SBClientForgeEvents {
    public static void init(IEventBus bus){
        MinecraftForge.EVENT_BUS.addListener(SBClientForgeEvents::onRenderGameOverlayPost);
    }

    private static void drawCenteredText(Minecraft minecraft, GuiGraphics graphics, Component text, int x, int y)
    {
        final int textWidth = minecraft.font.width(text) / 2;
        graphics.drawString(minecraft.font, text, x - textWidth, y, 0xCCCCCC, false);
    }

    private static int getStageNumber(Item item){
        for (int i = 0; i < MeatHookStage.values().length; i++) {
                  if(MeatHookStage.values()[i].iconicTool().equals(item)){
                      return i;
                  }
        }
        return -1;
    }

    private static Ingredient getStageTool(Item item){
        for (int i = 0; i < MeatHookStage.values().length; i++) {
            if(MeatHookStage.values()[i].iconicTool().equals(item)){
                return MeatHookStage.values()[i].acceptableTools();
            }
        }
        return Ingredient.EMPTY;
    }

    private static void renderButcherToolToolTip(Item idealItem, ItemStack mainHandItem, RenderGuiOverlayEvent.Post event){
        var window = event.getWindow();
        final GuiGraphics stack = event.getGuiGraphics();
        final Minecraft minecraft = Minecraft.getInstance();
        int x = window.getGuiScaledWidth() / 2 + 3;
        int y = window.getGuiScaledHeight() / 2 + 8;
        int paddingX = 4;
        int paddingY = 3;

        if(getStageTool(idealItem).test(mainHandItem)){
            Component text = Component.translatable(
                    "tooltip.survivorsbutchercraft.ideal_tool",
                    idealItem.getDefaultInstance().getHoverName()
            );
            Component text2 = Component.translatable("tooltip.survivorsbutchercraft.alt_note1")
                    .withStyle(style -> style.withColor(ChatFormatting.GOLD));
            Component text3 = Component.translatable("tooltip.survivorsbutchercraft.alt_note2")
                    .withStyle(style -> style.withColor(ChatFormatting.GOLD));

            int textWidth = Math.max(
                    Math.max(
                            minecraft.font.width(text),
                            minecraft.font.width(text2)
                    ), minecraft.font.width(text3)
            );

            int left = x - textWidth / 2 - paddingX;
            int right = x + textWidth / 2 + paddingX;
            int top = y - paddingY;
            int bottom = y + 32 + minecraft.font.lineHeight + paddingY;

            stack.fill(
                    left,
                    top,
                    right,
                    bottom,
                    0xA0000000
            );

            drawCenteredText(minecraft, stack, text, x, y);
            drawCenteredText(minecraft, stack, text2, x, y + 16);
            drawCenteredText(minecraft, stack, text3, x, y + 32);

        } else {
            Component text = Component.translatable(
                    "tooltip.survivorsbutchercraft.ideal_tool",
                    idealItem.getDefaultInstance().getHoverName()
            );
            Component text2 = Component.translatable(
                    "tooltip.survivorsbutchercraft.alt",
                    Component.translatable("tooltip.survivorsbutchercraft.alt" + getStageNumber(idealItem))
            );
            int textWidth = Math.max(
                    minecraft.font.width(text),
                    minecraft.font.width(text2)
            );

            int left = x - textWidth / 2 - paddingX;
            int right = x + textWidth / 2 + paddingX;
            int top = y - paddingY;
            int bottom = y + 16 + minecraft.font.lineHeight + paddingY;

            stack.fill(
                    left,
                    top,
                    right,
                    bottom,
                    0xA0000000
            );

            drawCenteredText(minecraft, stack, text, x, y);
            drawCenteredText(minecraft, stack, text2, x, y + 16);
        }
    }


    private static void renderMeatHookToolTip(Player player, IButcherBlock meatHookBlockEntity, RenderGuiOverlayEvent.Post event){
        var idealItem = Optional.ofNullable(ToolAlternative.getIdealTool(meatHookBlockEntity.sbtfcInterface$getCurTool())).orElse(Items.AIR);
        if (idealItem.getDefaultInstance().isEmpty()) return;
        if(idealItem instanceof BucketItem) return;
        var mainHandItem = player.getMainHandItem();
        var idealItems = ToolAlternative.getIdealTool(idealItem);
        if(idealItems.test(mainHandItem)){
            return;
        }

        renderButcherToolToolTip(idealItem, mainHandItem, event);
    }

    public static void onRenderGameOverlayPost(RenderGuiOverlayEvent.Post event){
        final Minecraft minecraft = Minecraft.getInstance();
        final Player player = minecraft.player;
        if (player != null)
        {
            if (
                    event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()
                            && minecraft.screen == null
                            && (! player.isShiftKeyDown())
            ) {
                final BlockPos targetedPos = ClientHelpers.getTargetedPos();
                if(minecraft.level == null) return;
                if(targetedPos == null) return;
                final BlockEntity targetedBlockEntity = minecraft.level.getBlockEntity(targetedPos);
                if(targetedBlockEntity instanceof IButcherBlock meatHookBlockEntity){
                    renderMeatHookToolTip(player, meatHookBlockEntity, event);
                }
            }
        }
    }

}

