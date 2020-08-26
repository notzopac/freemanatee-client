package me.zopac.freemanatee.module.modules.render;

import me.zopac.freemanatee.module.Module;
import me.zopac.freemanatee.setting.Setting;
import me.zopac.freemanatee.setting.Settings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Module.Info(name = "InventoryViewer", category = Module.Category.RENDER)
public class InventoryViewer extends Module {

    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");

    private Setting<Integer> optionX = register(Settings.integerBuilder("X").withMinimum(0).withValue(400).withMaximum(2000).build());
    private Setting<Integer> optionY = register(Settings.integerBuilder("Y").withMinimum(0).withValue(400).withMaximum(2000).build());

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    @Override
    public void onRender() {
        final NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
        this.boxrender(optionX.getValue(), optionY.getValue());
        this.itemrender(items, optionX.getValue(), optionY.getValue());
    }

    private void boxrender(final int x, final int y) {
        preboxrender();
        mc.renderEngine.bindTexture(InventoryViewer.box);
        mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 162, 54);
        postboxrender();
    }

    private void itemrender(final NonNullList<ItemStack> items, final int x, final int y) {
        for (int size = items.size(), item = 9; item < size; ++item) {
            final int slotx = x + 1 + item % 9 * 18;
            final int sloty = y + 1 + (item / 9 - 1) * 18;
            preitemrender();
            mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotx, sloty);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotx, sloty);
            postitemrender();
        }
    }

}
