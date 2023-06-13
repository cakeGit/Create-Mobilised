package com.cak.create.ontreads.content.block.vehiclecontroller;

import com.cak.create.ontreads.foundation.contraption.VehicleContraption;
import com.cak.create.ontreads.foundation.contraption.VehicleContraptionEntity;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class VehicleControllerBlockEntity extends BlockEntity {
  
  public VehicleControllerBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
    super(p_155228_, p_155229_, p_155230_);
  }
  
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
  
    if (level.isClientSide())
      return InteractionResult.SUCCESS;
    
    VehicleContraption contraption = new VehicleContraption();
    contraption.setControllerAssembleAxis(state.getValue(HorizontalDirectionalBlock.FACING).getAxis());
    
    boolean created = false;
    AssemblyException assemblyException = null;
    try {
      created = contraption.assemble(level, pos);
    } catch (AssemblyException e) {
      assemblyException = e;
    }
    
    if (!created) {
      assert assemblyException != null;
      player.displayClientMessage(assemblyException.component.copy().withStyle(ChatFormatting.RED), true);
      return InteractionResult.SUCCESS;
    }
    
    contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
    contraption.startMoving(level);
    contraption.expandBoundsAroundAxis(Direction.Axis.Y);
    
    VehicleContraptionEntity contraptionEntity = VehicleContraptionEntity.create(level, contraption, Vec3.atLowerCornerOf(pos).add(0.5, 0, 0.5));
    
    level.addFreshEntity(contraptionEntity);
    
    AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(level, worldPosition);
    
    return InteractionResult.SUCCESS;
  
  }
  
}
