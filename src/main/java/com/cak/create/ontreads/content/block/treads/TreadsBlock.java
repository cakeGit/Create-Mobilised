package com.cak.create.ontreads.content.block.treads;

import com.cak.create.ontreads.OTBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class TreadsBlock extends HorizontalDirectionalBlock implements IBE<TreadsBlockEntity> {
  
  public TreadsBlock(Properties properties) {
    super(properties);
  }
  
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(HorizontalDirectionalBlock.FACING);
    super.createBlockStateDefinition(builder);
  }
  
  @Override
  public Class<TreadsBlockEntity> getBlockEntityClass() {
    return TreadsBlockEntity.class;
  }
  
  @Override
  public BlockEntityType<? extends TreadsBlockEntity> getBlockEntityType() {
    return OTBlockEntities.TREADS.get();
  }

}
