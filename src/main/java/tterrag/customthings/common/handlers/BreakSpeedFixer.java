package tterrag.customthings.common.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.config.json.BlockType;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class BreakSpeedFixer
{
    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event)
    {
        if (event.block instanceof BlockCustom)
        {
            BlockType type = ((BlockCustom) event.block).getType(event.metadata);
            if (type.toolType.isEmpty())
            {
                return;
            }
            ItemStack held = event.entityPlayer.getHeldItem();
            if (held != null && !held.getItem().getToolClasses(held).contains(type.toolType))
            {
                event.newSpeed = ForgeHooks.canHarvestBlock(event.block, event.entityPlayer, event.metadata) ? 0.3f : 1;
            }
        }
    }
}
