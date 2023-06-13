package com.cak.create.ontreads;

import com.cak.create.ontreads.content.block.treads.TreadsBlockEntity;
import com.cak.create.ontreads.content.block.vehiclecontroller.VehicleControllerBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.cak.create.ontreads.CreateOnTreads.REGISTRATE;

public class OTBlockEntities {
  
  public static BlockEntityEntry<VehicleControllerBlockEntity> VEHICLE_CONTROLLER = REGISTRATE.blockEntity("vehicle_controller", VehicleControllerBlockEntity::new)
      .validBlock(OTBlocks.VEHICLE_CONTROLLER)
      .register();
  
  public static BlockEntityEntry<TreadsBlockEntity> TREADS = REGISTRATE.blockEntity("treads", TreadsBlockEntity::new)
      .validBlock(OTBlocks.TREADS)
      .register();
  
  public static void register() {}
  
}
