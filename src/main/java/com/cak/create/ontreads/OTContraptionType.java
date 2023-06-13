package com.cak.create.ontreads;

import com.cak.create.ontreads.foundation.contraption.VehicleContraption;
import com.simibubi.create.content.contraptions.ContraptionType;

public class OTContraptionType {
  
  public static final ContraptionType
    TREADED = ContraptionType.register("treaded_vehicle", VehicleContraption::new);
  
  
  public static void register() {}
  
}
