import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OChunkProviderServer implements OIChunkProvider {

    private Set b = new HashSet();
    private OChunk c;
    private OIChunkProvider d;
    private OIChunkLoader e;
    public boolean a = false;
    private OPlayerHash f = new OPlayerHash();
    private List g = new ArrayList();
    private OWorldServer h;
    // CanaryMod: load status
    boolean loaded = false;
    boolean loadedpreload = false;

    public OChunkProviderServer(OWorldServer var1, OIChunkLoader var2, OIChunkProvider var3) {
        super();
        var1.getClass();
        this.c = new OEmptyChunk(var1, new byte[256 * 128], 0, 0);
        this.h = var1;
        this.e = var2;
        this.d = var3;
    }

    public boolean a(int var1, int var2) {
        return this.f.b(OChunkCoordIntPair.a(var1, var2));
    }

    public void d(int var1, int var2) {
        OChunkCoordinates var3 = this.h.m();
        int var4 = var1 * 16 + 8 - var3.a;
        int var5 = var2 * 16 + 8 - var3.c;
        short var6 = 128;
        if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
            this.b.add(Long.valueOf(OChunkCoordIntPair.a(var1, var2)));
        }

    }

    public OChunk c(int var1, int var2) {
        long var3 = OChunkCoordIntPair.a(var1, var2);
        this.b.remove(Long.valueOf(var3));
        OChunk var5 = (OChunk) this.f.a(var3);
        if (var5 == null) {
            // CanaryMod: load preload plugins once!
            if (!loadedpreload) {
                etc.getLoader().loadPreloadPlugins();
                loadedpreload = true;
            }
            var5 = this.e(var1, var2);
            if (var5 == null) {
                // Canary onChunkCreate hook
                byte[] blocks = (byte[]) etc.getLoader().callHook(PluginLoader.Hook.CHUNK_CREATE, var1, var2, h.world);
                if (blocks != null) {
                    var5 = Chunk.getNewChunk(h, blocks, var1, var2).chunk;
                    var5.p = true; // is populated = true
                    var5.b(); // lightning update
                } else if (this.d == null) {
                    var5 = this.c;
                } else {
                    var5 = this.d.b(var1, var2);
                }
                // Canary onChunkCreated hook
                etc.getLoader().callHook(PluginLoader.Hook.CHUNK_CREATED, var5.chunk);
            }

            this.f.a(var3, var5);
            this.g.add(var5);
            if (var5 != null) {
                var5.c();
                var5.d();
                // Canary onChunkLoaded hook
                etc.getLoader().callHook(PluginLoader.Hook.CHUNK_LOADED, var5.chunk);
                // }

                if (!var5.p && this.a(var1 + 1, var2 + 1) && this.a(var1, var2 + 1) && this.a(var1 + 1, var2)) {
                    this.a(this, var1, var2);
                }
            }// To prevent NullPointerExceptions
            if (this.a(var1 - 1, var2) && !this.b(var1 - 1, var2).p && this.a(var1 - 1, var2 + 1) && this.a(var1, var2 + 1) && this.a(var1 - 1, var2)) {
                this.a(this, var1 - 1, var2);
            }

            if (this.a(var1, var2 - 1) && !this.b(var1, var2 - 1).p && this.a(var1 + 1, var2 - 1) && this.a(var1, var2 - 1) && this.a(var1 + 1, var2)) {
                this.a(this, var1, var2 - 1);
            }

            if (this.a(var1 - 1, var2 - 1) && !this.b(var1 - 1, var2 - 1).p && this.a(var1 - 1, var2 - 1) && this.a(var1, var2 - 1) && this.a(var1 - 1, var2)) {
                this.a(this, var1 - 1, var2 - 1);
            }

            var5.a(this, this, var1, var2);
        }

        return var5;
    }

    public OChunk b(int var1, int var2) {
        OChunk var3 = (OChunk) this.f.a(OChunkCoordIntPair.a(var1, var2));
        return var3 == null ? (!this.h.D && !this.a ? this.c : this.c(var1, var2)) : var3;
    }

    private OChunk e(int var1, int var2) {
        if (this.e == null) {
            return null;
        } else {
            try {
                OChunk var3 = this.e.a(this.h, var1, var2);
                if (var3 != null) {
                    var3.t = this.h.l();
                }

                return var3;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    private void a(OChunk var1) {
        if (this.e != null) {
            try {
                this.e.b(this.h, var1);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    private void b(OChunk var1) {
        if (this.e != null) {
            var1.t = this.h.l();
            this.e.a(this.h, var1);

        }
    }

    public void a(OIChunkProvider var1, int var2, int var3) {
        OChunk var4 = this.b(var2, var3);
        if (!var4.p) {
            var4.p = true;
            if (this.d != null) {
                this.d.a(var1, var2, var3);
                var4.f();
            }
        }

    }

    public boolean a(boolean var1, OIProgressUpdate var2) {
        // CanaryMod: load once!
        if (!loaded) {
            etc.getLoader().loadPlugins();
            loaded = true;
        }

        int var3 = 0;

        for (int var4 = 0; var4 < this.g.size(); ++var4) {
            OChunk var5 = (OChunk) this.g.get(var4);
            if (var1 && !var5.r) {
                this.a(var5);
            }

            if (var5.a(var1)) {
                this.b(var5);
                var5.q = false;
                ++var3;
                if (var3 == 24 && !var1) {
                    return false;
                }
            }
        }

        if (var1) {
            if (this.e == null) {
                return true;
            }

            this.e.b();
        }

        return true;
    }

    public boolean a() {
        if (!this.h.O) {
            for (int var1 = 0; var1 < 100; ++var1) {
                if (!this.b.isEmpty()) {
                    Long var2 = (Long) this.b.iterator().next();
                    OChunk var3 = (OChunk) this.f.a(var2.longValue());
                    // Canary onChunkUnload hook
                    etc.getLoader().callHook(PluginLoader.Hook.CHUNK_UNLOAD, var3.chunk);
                    var3.e();
                    this.b(var3);
                    this.a(var3);
                    this.b.remove(var2);
                    this.f.d(var2.longValue());
                    this.g.remove(var3);
                }
            }

            if (this.e != null) {
                this.e.a();
            }
        }

        return this.d.a();
    }

    public boolean b() {
        return !this.h.O;
    }
}
