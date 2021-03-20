package slimeknights.tconstruct.tools.modifiers.traits;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.ToolStatsModifierBuilder;
import slimeknights.tconstruct.tools.modifiers.free.OverslimeModifier;

public class OverlordModifier extends Modifier {
  public OverlordModifier() {
    super(0x236c45);
  }

  /** Gets the durability boost per level */
  private int getBoost(IModDataReadOnly volatileData, int level) {
    return (int)(volatileData.getInt(ToolStack.ORIGINAL_DURABILITY_KEY) * .15f * level);
  }

  @Override
  public void addVolatileData(IModDataReadOnly persistentData, int level, ModDataNBT volatileData) {
    volatileData.putBoolean(OverslimeModifier.KEY_OVERSLIME_FRIEND, true);
    // gains +15% of the durability per level
    OverslimeModifier.addCap(volatileData, getBoost(volatileData, level));
  }

  @Override
  public void addToolStats(IModDataReadOnly persistentData, IModDataReadOnly volatileData, int level, ToolStatsModifierBuilder builder) {
    // at most subtract 90% durability
    builder.addDurability(-getBoost(volatileData, Math.min(level, 6)));
  }
}
