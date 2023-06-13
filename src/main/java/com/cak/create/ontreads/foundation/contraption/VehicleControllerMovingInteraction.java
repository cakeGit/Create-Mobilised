package com.cak.create.ontreads.foundation.contraption;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class VehicleControllerMovingInteraction extends MovingInteractionBehaviour {
  
  @Override
  public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
  
    if (contraptionEntity instanceof VehicleContraptionEntity &&
        player.getItemInHand(activeHand).getItem() == AllItems.WRENCH.get() &&
        !player.level.isClientSide()) {
      contraptionEntity.disassemble();
      return true;
    } else {
    
    }
    
    return super.handlePlayerInteraction(player, activeHand, localPos, contraptionEntity);
  }
  
}
