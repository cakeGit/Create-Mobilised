package com.cak.create.ontreads.foundation.wheel;

import com.cak.create.ontreads.foundation.contraption.VehicleContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AbstractLoadBearing {
  
  Vec3 localPos;
  BlockState state;
  
  float groundDistance;
  
  public AbstractLoadBearing(Vec3 localPos, BlockState state) {
    this.localPos = localPos;
    this.state = state;
  }
  
  public void updateGroundDistance(VehicleContraptionEntity contraptionEntity) {
  
    for (float verticalOffset = -1; verticalOffset < 32; verticalOffset+=1f/16) {
      for (float horizontalOffset = -1; horizontalOffset < 2; horizontalOffset++) {
        
        Vec3 position = contraptionEntity.applyRotation(localPos, 0)
            .add(0,  -verticalOffset, horizontalOffset).add(contraptionEntity.getPosition(0));
  
        BlockState state = contraptionEntity.level.getBlockState(new BlockPos(position));
        
        if (state.entityCanStandOn(contraptionEntity.level, new BlockPos(position), contraptionEntity)) {
          this.groundDistance = verticalOffset - 2.1f;
          
          return;
        }
        
      }
    }
    
  }
  
  public float getGroundDistance() {
    return groundDistance;
  }
}
