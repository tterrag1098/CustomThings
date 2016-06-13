package tterrag.customthings.common.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tterrag.customthings.common.block.IBlockCustom;
import tterrag.customthings.common.config.json.BlockType;

import com.enderio.core.common.Handlers.Handler;

@Handler
public class BreakSpeedFixer
{
    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event)
    {
        if (event.getState().getBlock() instanceof IBlockCustom)
        {
            BlockType type = event.getState().getValue(((IBlockCustom)event.getState().getBlock()).getProperty());
            if (type.toolType.isEmpty())
            {
                return;
            }
            ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
            if (held != null && !held.getItem().getToolClasses(held).contains(type.toolType))
            {
                event.setNewSpeed(ForgeHooks.canHarvestBlock(event.getState().getBlock(), event.getEntityPlayer(), event.getEntity().worldObj, event.getPos()) ? 0.3f : 1);
            }
        }
    }
}
