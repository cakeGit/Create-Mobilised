package com.cak.create.ontreads.foundation.contraption;

import com.cak.create.ontreads.CreateOnTreads;
import com.cak.create.ontreads.OTEntityTypes;
import com.cak.create.ontreads.foundation.wheel.AbstractLoadBearing;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.foundation.collision.Matrix3d;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.simibubi.create.foundation.utility.AngleHelper.angleLerp;

public class VehicleContraptionEntity extends OrientedContraptionEntity {
  
  Vec3 roll = new Vec3(0, 20, 20);
  Vec3 prevRoll = new Vec3(0, 20, 20);
  
  ArrayList<AbstractLoadBearing> loadBearings = new ArrayList<>();
  
  public VehicleContraptionEntity(EntityType<?> type, Level world) {
    super(type, world);
  }
  
  public static VehicleContraptionEntity create(Level world, VehicleContraption contraption, Vec3 pos) {
    VehicleContraptionEntity entity =
        new VehicleContraptionEntity(OTEntityTypes.TREADED_CONTRAPTION.get(), world);
    entity.setPos(pos);
    entity.setContraption(contraption);
  
    for (BlockPos wheelPos : contraption.validLoadBearers.keySet()) {
      BlockPos localPos = wheelPos.offset(-pos.x, -pos.y, -pos.z);
      
      Vec3 localPosVec = new Vec3(localPos.getX(), localPos.getY(), localPos.getZ());
  
      entity.loadBearings.add(new AbstractLoadBearing(localPosVec, contraption.validLoadBearers.get(wheelPos)));
      CreateOnTreads.LOGGER.info("added trad");
    }
    
    return entity;
  }
  
  @Override
  public void tick() {
    super.tick();
    this.prevYaw = yaw;
    this.prevPitch = pitch;
    this.prevRoll = roll;
    if (loadBearings.size() == 0) return;
    
    for (AbstractLoadBearing loadBearing : loadBearings) {
      loadBearing.updateGroundDistance(this);
    }
    
    float highestDist = loadBearings.get(0).getGroundDistance();
    
    for (AbstractLoadBearing loadBearing : loadBearings) {
      if (highestDist < loadBearing.getGroundDistance())
        highestDist = loadBearing.getGroundDistance();
    }
    
    setPos(this.position().add(0, -highestDist, 0));
    
    this.yaw = 0;
    this.pitch = 0;
    this.roll = new Vec3(0, 0, 0);
  }
  
  @Override
  public ContraptionRotationState getRotationState() {
    VehicleRotationState crs = new VehicleRotationState();
    
    float yawOffset = getYawOffset();
    crs.zRotation = pitch;
    crs.yRotation = -yaw + yawOffset;
    
    if (pitch != 0 && yaw != 0) {
      crs.secondYRotation = -yaw;
      crs.yRotation = yawOffset;
    }
    
    crs.roll = roll;
    
    return crs;
  }
  
  public static class VehicleRotationState extends AbstractContraptionEntity.ContraptionRotationState {
    float xRotation = 0;
    float yRotation = 0;
    float zRotation = 0;
    float secondYRotation = 0;
  
    Vec3 roll;
  
    Matrix3d matrix;
    
    public Matrix3d asMatrix() {
      if (matrix != null)
        return matrix;
      
      matrix = new Matrix3d().asIdentity();
      if (xRotation != 0)
        matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad(-xRotation)));
      if (yRotation != 0)
        matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(yRotation)));
      if (zRotation != 0)
        matrix.multiply(new Matrix3d().asZRotation(AngleHelper.rad(-zRotation)));
  
      matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad(-roll.x)));
      matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(-roll.y)));
      
      matrix.multiply(new Matrix3d().asZRotation(AngleHelper.rad(roll.z)));
      
      matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(roll.y)));
      matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad(roll.x)));
  
  
  
      return matrix;
    }
    
    public boolean hasVerticalRotation() {
      return true;
    }
    
    public float getYawOffset() {
      return secondYRotation;
    }
    
  }
  
  @Override
  public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
    
    localPos = VecHelper.rotate(localPos, getInitialYaw(), Direction.Axis.Y);
    
    localPos = VecHelper.rotate(localPos, getViewXRot(partialTicks), Direction.Axis.X);
    localPos = VecHelper.rotate(localPos, getViewYRot(partialTicks), Direction.Axis.Y);

    Vec3 roll = getViewRollRot(partialTicks);
    
    localPos = VecHelper.rotate(localPos, roll.x, Direction.Axis.X);
    localPos = VecHelper.rotate(localPos, roll.y, Direction.Axis.Y);
    
    localPos = VecHelper.rotate(localPos, roll.z, Direction.Axis.Z);
    
    localPos = VecHelper.rotate(localPos, -roll.y, Direction.Axis.Y);
    localPos = VecHelper.rotate(localPos, -roll.x, Direction.Axis.X);
    
    return localPos;
  }
  
  @Override
  public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
  
    Vec3 roll = getViewRollRot(partialTicks);
  
    localPos = VecHelper.rotate(localPos, -roll.x, Direction.Axis.X);
    localPos = VecHelper.rotate(localPos, -roll.y, Direction.Axis.Y);
  
    localPos = VecHelper.rotate(localPos, -roll.z, Direction.Axis.Z);
  
    localPos = VecHelper.rotate(localPos, roll.y, Direction.Axis.Y);
    localPos = VecHelper.rotate(localPos, roll.x, Direction.Axis.X);
    
    localPos = VecHelper.rotate(localPos, -getViewYRot(partialTicks), Direction.Axis.Y);
    localPos = VecHelper.rotate(localPos, -getViewXRot(partialTicks), Direction.Axis.Z);
    
    localPos = VecHelper.rotate(localPos, -getInitialYaw(), Direction.Axis.Y);
  
    return localPos;
  }
  
  @Override
  @OnlyIn(Dist.CLIENT)
  public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {
    float angleInitialYaw = getInitialYaw();
    float angleYaw = getViewYRot(partialTicks);
    float anglePitch = getViewXRot(partialTicks);
    
    Vec3 vecRoll = getViewRollRot(partialTicks);
    
    matrixStack.translate(-.5f, 0, -.5f);
    
    Entity ridingEntity = getVehicle();
    if (ridingEntity instanceof AbstractMinecart)
      repositionOnCart(matrixStack, partialTicks, ridingEntity);
    else if (ridingEntity instanceof AbstractContraptionEntity) {
      if (ridingEntity.getVehicle() instanceof AbstractMinecart)
        repositionOnCart(matrixStack, partialTicks, ridingEntity.getVehicle());
      else
        repositionOnContraption(matrixStack, partialTicks, ridingEntity);
    }
    
    TransformStack.cast(matrixStack)
        .nudge(getId())
        .centre()
        .rotateX(-vecRoll.x)
        .rotateY(-vecRoll.y)
        .rotateZ(vecRoll.z)
        .rotateY(vecRoll.y)
        .rotateX(vecRoll.x)
        .rotateY(angleYaw)
        .rotateZ(anglePitch)
        .rotateY(angleInitialYaw)
        .unCentre();
  }
  
  public Vec3 getViewRollRot(float partialTicks) {
    return (partialTicks == 1.0F ? roll : vec3AngleLerp(partialTicks, prevRoll, roll))
        .multiply(-1, -1, -1);
  }
  
  private Vec3 vec3AngleLerp(double pct, Vec3 current, Vec3 target) {
    return new Vec3(
        angleLerp(pct, current.x, target.x),
        angleLerp(pct, current.y, target.y),
        angleLerp(pct, current.z, target.z)
    );
  }
  
  //>Clone of private create classes :/
  
  @OnlyIn(Dist.CLIENT)
  private void repositionOnContraption(PoseStack matrixStack, float partialTicks, Entity ridingEntity) {
    Vec3 pos = getContraptionOffset(partialTicks, ridingEntity);
    matrixStack.translate(pos.x, pos.y, pos.z);
  }
  
  @OnlyIn(Dist.CLIENT)
  private void repositionOnCart(PoseStack matrixStack, float partialTicks, Entity ridingEntity) {
    Vec3 cartPos = getCartOffset(partialTicks, ridingEntity);
    
    if (cartPos == Vec3.ZERO)
      return;
    
    matrixStack.translate(cartPos.x, cartPos.y, cartPos.z);
  }
  
  @OnlyIn(Dist.CLIENT)
  private Vec3 getContraptionOffset(float partialTicks, Entity ridingEntity) {
    AbstractContraptionEntity parent = (AbstractContraptionEntity) ridingEntity;
    Vec3 passengerPosition = parent.getPassengerPosition(this, partialTicks);
    double x = passengerPosition.x - Mth.lerp(partialTicks, this.xOld, this.getX());
    double y = passengerPosition.y - Mth.lerp(partialTicks, this.yOld, this.getY());
    double z = passengerPosition.z - Mth.lerp(partialTicks, this.zOld, this.getZ());
    
    return new Vec3(x, y, z);
  }
  
  @OnlyIn(Dist.CLIENT)
  private Vec3 getCartOffset(float partialTicks, Entity ridingEntity) {
    AbstractMinecart cart = (AbstractMinecart) ridingEntity;
    double cartX = Mth.lerp(partialTicks, cart.xOld, cart.getX());
    double cartY = Mth.lerp(partialTicks, cart.yOld, cart.getY());
    double cartZ = Mth.lerp(partialTicks, cart.zOld, cart.getZ());
    Vec3 cartPos = cart.getPos(cartX, cartY, cartZ);
    
    if (cartPos != null) {
      Vec3 cartPosFront = cart.getPosOffs(cartX, cartY, cartZ, (double) 0.3F);
      Vec3 cartPosBack = cart.getPosOffs(cartX, cartY, cartZ, (double) -0.3F);
      if (cartPosFront == null)
        cartPosFront = cartPos;
      if (cartPosBack == null)
        cartPosBack = cartPos;
      
      cartX = cartPos.x - cartX;
      cartY = (cartPosFront.y + cartPosBack.y) / 2.0D - cartY;
      cartZ = cartPos.z - cartZ;
      
      return new Vec3(cartX, cartY, cartZ);
    }
    
    return Vec3.ZERO;
  }
  
  @Override
  public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand interactionHand) {
    return InteractionResult.SUCCESS;
  }
  
  @Override
  public Vec3 getContactPointMotion(Vec3 globalContactPoint) {
    return Vec3.ZERO;
  }
}
