package slimeknights.tconstruct.library.traits;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;

import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class StoneboundTrait extends AbstractTrait {

  public StoneboundTrait() {
    super("stonebound", EnumChatFormatting.DARK_GRAY);

    addItem("cobblestone");
    aspects.clear(); // remove traitModifiers aspects
    addAspects(new ModifierAspect.LevelAspect(this, 3), ModifierAspect.freeModifier);
  }

  @Override
  public boolean canApplyCustom(ItemStack stack) {
    return true;
  }

  @Override
  public int getMaxCount() {
    return 2;
  }

  @Override
  public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
    float damaged = (float)tool.getItemDamage() / (float) ToolHelper.getDurability(tool);

    event.newSpeed = Math.max(0f, event.newSpeed + damaged*5.0f);
  }
}
