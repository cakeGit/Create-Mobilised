package com.cak.create.ontreads;

import com.cak.create.ontreads.content.block.treads.TreadsBlock;
import com.cak.create.ontreads.content.block.vehiclecontroller.VehicleControllerBlock;
import com.cak.create.ontreads.foundation.contraption.VehicleControllerMovingInteraction;
import com.tterrag.registrate.util.entry.BlockEntry;

import static com.cak.create.ontreads.CreateOnTreads.REGISTRATE;
import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;

public class OTBlocks {
  
  public static BlockEntry<VehicleControllerBlock> VEHICLE_CONTROLLER = REGISTRATE.block("vehicle_controller", VehicleControllerBlock::new)
      .onRegister(interactionBehaviour(new VehicleControllerMovingInteraction()))
      .simpleItem()
      .register();
  
  public static BlockEntry<TreadsBlock> TREADS = REGISTRATE.block("treads", TreadsBlock::new)
      .simpleItem()
      .register();
  
  public static void register() {}
  
}
