// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.tf2;

import me.lemon.lemonware.Main;
import me.lemon.lemonware.utils.vector.Vector3f;

public class Entity
{
    private long address;
    private long index;
    private Vector3f pos;
    private Vector3f velocity;
    private Vector3f viewAngles;
    private Vector3f viewOffsets;
    private long hp;
    private long flags;
    private boolean spotted;
    private boolean dormant;
    private long team;
    private long boneMatrix;
    private long cloak;
    
    public Entity(final long adress, final long index) {
        this.address = adress;
        this.index = index;
    }
    
    public void update() {
        final float posX = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecOrigin").getAddress());
        final float posY = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecOrigin").getAddress() + 4L);
        final float posZ = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecOrigin").getAddress() + 8L);
        this.pos = new Vector3f(posX, posY, posZ);
        final float velX = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecVelocity").getAddress());
        final float velY = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecVelocity").getAddress() + 4L);
        final float velZ = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecVelocity").getAddress() + 8L);
        this.velocity = new Vector3f(velX, velY, velZ);
        final float pitch = Main.INSTANCE.getEngine().readFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress());
        final float yaw = Main.INSTANCE.getEngine().readFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress() + 4);
        final float roll = Main.INSTANCE.getEngine().readFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress() + 8);
        this.viewAngles = new Vector3f(yaw, pitch, roll);
        final float viewOffsetX = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecViewOffset").getAddress());
        final float viewOffsetY = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecViewOffset").getAddress() + 4L);
        final float viewOffsetZ = Main.INSTANCE.getTF2Process().readFloat(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_vecViewOffset").getAddress() + 8L);
        this.viewOffsets = new Vector3f(viewOffsetX, viewOffsetY, viewOffsetZ);
        this.hp = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_iHealth").getAddress());
        this.flags = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_fFlags").getAddress());
        this.dormant = Main.INSTANCE.getTF2Process().readBoolean(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_bDormant").getAddress());
        this.team = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_iTeamNum").getAddress());
        this.cloak = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_iCloaked").getAddress());
        this.boneMatrix = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_dwBoneMatrix").getAddress());
    }
    
    public boolean isOnGround() {
        return this.getFlags() == 257L;
    }
    
    public Vector3f getBonePos(final Entity entity, final int bone) {
        final Vector3f bonePos = new Vector3f();
        bonePos.x = Main.INSTANCE.getTF2Process().readFloat(entity.getBoneMatrix() + 48 * bone + 12L);
        bonePos.y = Main.INSTANCE.getTF2Process().readFloat(entity.getBoneMatrix() + 48 * bone + 28L);
        bonePos.z = Main.INSTANCE.getTF2Process().readFloat(entity.getBoneMatrix() + 48 * bone + 44L);
        return bonePos;
    }
    
    public long getAddress() {
        return this.address;
    }
    
    public long getIndex() {
        return this.index;
    }
    
    public Vector3f getPos() {
        return this.pos;
    }
    
    public Vector3f getVelocity() {
        return this.velocity;
    }
    
    public Vector3f getViewAngles() {
        return this.viewAngles;
    }
    
    public Vector3f getViewOffsets() {
        return this.viewOffsets;
    }
    
    public long getHp() {
        return this.hp;
    }
    
    public long getFlags() {
        return this.flags;
    }
    
    public boolean isSpotted() {
        return this.spotted;
    }
    
    public boolean isDormant() {
        return this.dormant;
    }
    
    public long getTeam() {
        return this.team;
    }
    
    public long getBoneMatrix() {
        return this.boneMatrix;
    }
    
    public long getCloak() {
        return this.cloak;
    }
}
