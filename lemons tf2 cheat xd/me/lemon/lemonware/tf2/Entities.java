// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.tf2;

import java.util.Iterator;
import me.lemon.lemonware.Main;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Entities
{
    private List<Entity> entities;
    private long lastClear;
    
    public Entities() {
        this.entities = new CopyOnWriteArrayList<Entity>();
        this.lastClear = System.currentTimeMillis();
    }
    
    public void findEntities() {
        if (System.currentTimeMillis() - this.lastClear >= 5000L) {
            this.getEntities().clear();
            this.lastClear = System.currentTimeMillis();
        }
        final long localPlayer = Main.INSTANCE.getClient().readUnsignedInt(Main.INSTANCE.getMemory().getOffset("m_dwLocalPlayer").getAddress());
        if (localPlayer > 0L) {
            for (int i = 0; i < 32; ++i) {
                final long entityBase = Main.INSTANCE.getClient().readUnsignedInt(Main.INSTANCE.getMemory().getOffset("m_dwEntityList").getAddress() + i * 16);
                if (entityBase >= 512L) {
                    if (entityBase != localPlayer && entityBase > 0L) {
                        final long team = Main.INSTANCE.getTF2Process().readUnsignedInt(entityBase + Main.INSTANCE.getMemory().getOffset("m_iTeamNum").getAddress());
                        final long hp = Main.INSTANCE.getTF2Process().readUnsignedInt(entityBase + Main.INSTANCE.getMemory().getOffset("m_iHealth").getAddress());
                        final boolean dormant = Main.INSTANCE.getTF2Process().readBoolean(entityBase + Main.INSTANCE.getMemory().getOffset("m_bDormant").getAddress());
                        if (!this.hasEntity(entityBase) && (team == 2L || team == 3L) && hp > 0L && !dormant) {
                            this.add(new Entity(entityBase, i));
                        }
                    }
                    else if (entityBase == localPlayer && !this.hasEntity(entityBase)) {
                        this.add(new LocalPlayer(entityBase, i));
                    }
                }
            }
            for (final Entity entity : this.getEntities()) {
                entity.update();
                if (entity.isDormant() || (entity.getHp() <= 1L && entity != this.getLocalPlayer()) || (entity.getHp() <= 0L && entity == this.getLocalPlayer())) {
                    this.remove(entity);
                }
            }
        }
    }
    
    public void add(final Entity e) {
        this.entities.add(e);
    }
    
    public void remove(final Entity e) {
        this.entities.remove(e);
    }
    
    public boolean hasEntity(final long address) {
        for (final Entity entity : this.getEntities()) {
            if (entity.getAddress() == address) {
                return true;
            }
        }
        return false;
    }
    
    public Entity getEntity(final long address) {
        for (final Entity entity : this.getEntities()) {
            if (entity.getAddress() == address) {
                return entity;
            }
        }
        return null;
    }
    
    public Entity getEntityIndex(final long index) {
        for (final Entity entity : this.getEntities()) {
            if (entity.getIndex() == index) {
                return entity;
            }
        }
        return null;
    }
    
    public LocalPlayer getLocalPlayer() {
        for (final Entity entity : this.getEntities()) {
            if (entity instanceof LocalPlayer) {
                return (LocalPlayer)entity;
            }
        }
        return null;
    }
    
    public List<Entity> getEntities() {
        return this.entities;
    }
}
