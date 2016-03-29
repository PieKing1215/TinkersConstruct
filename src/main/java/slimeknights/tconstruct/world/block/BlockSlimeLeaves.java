package slimeknights.tconstruct.world.block;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Random;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockSlimeGrass.FoliageType;
import slimeknights.tconstruct.world.client.SlimeColorizer;

public class BlockSlimeLeaves extends BlockLeaves {

  public BlockSlimeLeaves() {
    this.setCreativeTab(TinkerRegistry.tabWorld);
    this.setHardness(0.3f);

    this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for(FoliageType type : FoliageType.values()) {
      list.add(new ItemStack(this, 1, getMetaFromState(this.getDefaultState().withProperty(BlockSlimeGrass.FOLIAGE, type))));
    }
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return Blocks.leaves.isOpaqueCube(state);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return Blocks.leaves.getBlockLayer();
  }

  @Override
  protected int getSaplingDropChance(IBlockState state) {
    return 25;
  }

  // sapling item
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(TinkerWorld.slimeSapling);
  }

  @Override
  protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
    if(worldIn.rand.nextInt(chance) == 0) {
      ItemStack stack = null;
      if(state.getValue(BlockSlimeGrass.FOLIAGE) == FoliageType.PURPLE) {
        stack = TinkerCommons.matSlimeBallPurple.copy();
      }
      else if(state.getValue(BlockSlimeGrass.FOLIAGE) == FoliageType.BLUE) {
        if(worldIn.rand.nextInt(3) == 0) {
          stack = TinkerCommons.matSlimeBallBlue.copy();
        }
        else {
          stack = new ItemStack(Items.slime_ball);
        }
      }

      if(stack != null) {
        spawnAsEntity(worldIn, pos, stack);
      }
    }
  }

  // sapling meta
  @Override
  public int damageDropped(IBlockState state) {
    return (state.getValue(BlockSlimeGrass.FOLIAGE)).ordinal() & 3; // only first 2 bits
  }

  // item dropped on silktouching
  @Override
  protected ItemStack createStackedBlock(IBlockState state)
  {
    return new ItemStack(Item.getItemFromBlock(this), 1, (state.getValue(BlockSlimeGrass.FOLIAGE)).ordinal() & 3);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BlockSlimeGrass.FOLIAGE, CHECK_DECAY, DECAYABLE);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    int type = meta % 4;
    if(type < 0 || type >= FoliageType.values().length) {
      type = 0;
    }
    FoliageType grass = FoliageType.values()[type];
    return this.getDefaultState()
               .withProperty(BlockSlimeGrass.FOLIAGE, grass)
               .withProperty(DECAYABLE, (meta & 4) == 0)
               .withProperty(CHECK_DECAY, (meta & 8) > 0);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {

    int meta = (state.getValue(BlockSlimeGrass.FOLIAGE)).ordinal() & 3; // only first 2 bits

    if (!state.getValue(DECAYABLE))
    {
      meta |= 4;
    }

    if (state.getValue(CHECK_DECAY))
    {
      meta |= 8;
    }

    return meta;
  }

  @Override
  public BlockPlanks.EnumType getWoodType(int meta) {
    throw new NotImplementedException(); // unused by our code.
  }

  @Override
  public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
    IBlockState state = world.getBlockState(pos);
    return Lists.newArrayList(createStackedBlock(state));
  }

  @Override
  public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
    return true;
  }
}
