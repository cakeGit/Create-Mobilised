package com.cak.create.ontreads.foundation.contraption;

import com.cak.create.ontreads.OTContraptionType;
import com.cak.create.ontreads.OTLang;
import com.cak.create.ontreads.content.block.treads.TreadsBlock;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class VehicleContraption extends Contraption {
  
  Direction.Axis controllerAssembleAxis;
  
  Map<BlockPos, BlockState> validLoadBearers = new HashMap<>();
  
  public void setControllerAssembleAxis(Direction.Axis controllerAssembleAxis) {
    this.controllerAssembleAxis = controllerAssembleAxis;
  }
  
  @Override
  public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
    if (!searchMovedStructure(world, pos, null))
      return false;
    if (validLoadBearers.size() < 2)
      throw new AssemblyException(OTLang.text("assemble.treaded.not_enough_load_bearers").component());
    return true;
  }
  
  @Override
  protected Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
    BlockState blockState = world.getBlockState(pos);
    
    if (blockState.getBlock() instanceof TreadsBlock &&
        (controllerAssembleAxis == null ||
            controllerAssembleAxis == blockState.getValue(HorizontalDirectionalBlock.FACING).getAxis())) {
      validLoadBearers.put(pos, blockState);
    }
    
    return super.capture(world, pos);
  }
  
  @Override
  public boolean canBeStabilized(Direction facing, BlockPos localPos) {
    return true;
  }
  
  @Override
  public ContraptionType getType() {
    return OTContraptionType.TREADED;
  }
  
  @Override
  protected boolean isAnchoringBlockAt(BlockPos pos) {
    return false;
  }
  
  @Override
  @OnlyIn(Dist.CLIENT)
  public ContraptionLighter<?> makeLighter() {
    return new NonStationaryLighter<>(this);
  }
  
}
