package com.cak.create.ontreads.content.block.vehiclecontroller;

import com.cak.create.ontreads.OTBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class VehicleControllerBlock extends HorizontalDirectionalBlock implements IBE<VehicleControllerBlockEntity> {
  
  public VehicleControllerBlock(Properties p_49795_) {
    super(p_49795_);
  }
  
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(HorizontalDirectionalBlock.FACING);
    super.createBlockStateDefinition(builder);
  }
  
  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
    return ((VehicleControllerBlockEntity) level.getBlockEntity(pos)).use(state, level, pos, player, interactionHand, blockHitResult);
  }
  
  @Override
  public Class<VehicleControllerBlockEntity> getBlockEntityClass() {
    return VehicleControllerBlockEntity.class;
  }
  
  @Override
  public BlockEntityType<? extends VehicleControllerBlockEntity> getBlockEntityType() {
    return OTBlockEntities.VEHICLE_CONTROLLER.get();
  }

}
