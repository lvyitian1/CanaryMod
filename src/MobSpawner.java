
/**
 * MobSpawner.java - Wrapper for mob spawners.
 * 
 * @author James
 */
public class MobSpawner implements ComplexBlock {

    OTileEntityMobSpawner spawner;

    /**
     * Creates an interface for the spawner.
     * 
     * @param spawner
     */
    public MobSpawner(OTileEntityMobSpawner spawner) {
        this.spawner = spawner;
    }

    @Override
    public int getX() {
        return spawner.j;
    }

    @Override
    public int getY() {
        return spawner.k;
    }

    @Override
    public int getZ() {
        return spawner.l;
    }

    @Override
    public Block getBlock() {
        return getWorld().getBlockAt(getX(), getY(), getZ());
    }

    @Override
    public World getWorld() {
        return spawner.i.world;
    }

    @Override
    public void update() {
        spawner.i();
    }

    /**
     * Allows what to spawn to change on-the-fly
     * 
     * @param spawn
     */
    public void setSpawn(String spawn) {
        spawner.d = spawn;
        //update(); If we call update here, mobspawner resets to default (Pig)
    }

    /**
     * Returns the spawn used.
     * 
     * @return
     */
    public String getSpawn() {
        return spawner.d;
    }

    /**
     * Allows delay of what to spawn to change on-the-fly Modification of this
     * is near-useless as delays get randomized after spawn. See:
     * Block.setSpawnData() if you want to adjust this value.
     * 
     * @param delay
     */
    public void setDelay(int delay) {
        spawner.a = delay;
    }
}
