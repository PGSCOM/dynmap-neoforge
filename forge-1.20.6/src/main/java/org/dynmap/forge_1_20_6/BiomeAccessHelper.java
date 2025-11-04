package org.dynmap.forge_1_20_6;

import net.minecraft.world.level.biome.Biome;

/** Helper defensivo para acceso a Biome */
public class BiomeAccessHelper {
    public static float getBaseTemperatureSafe(Biome b) {
        if (b == null) return 0.5f;
        try { return b.getBaseTemperature(); }
        catch (IllegalAccessError | NoSuchMethodError e) {
            try {
                java.lang.reflect.Method m = b.getClass().getMethod("getClimate");
                Object climate = m.invoke(b);
                if (climate != null) {
                    java.lang.reflect.Method tm = climate.getClass().getMethod("temperature");
                    Object val = tm.invoke(climate);
                    if (val instanceof Float) return (Float)val;
                    if (val instanceof Double) return ((Double)val).floatValue();
                }
            }
            catch (Throwable t) {}
            return 0.5f;
        }
        catch (Throwable t) { return 0.5f; }
    }

    public static float getDownfallSafe(Biome b) {
        if (b == null) return 0.5f;
        try {
            try {
                java.lang.reflect.Method m = b.getClass().getMethod("getModifiedClimateSettings");
                Object climate = m.invoke(b);
                if (climate != null) {
                    java.lang.reflect.Method dm = climate.getClass().getMethod("downfall");
                    Object val = dm.invoke(climate);
                    if (val instanceof Float) return (Float)val;
                    if (val instanceof Double) return ((Double)val).floatValue();
                }
            }
            catch (NoSuchMethodException nsme) {
                java.lang.reflect.Method m2 = b.getClass().getMethod("getClimate");
                Object climate2 = m2.invoke(b);
                if (climate2 != null) {
                    java.lang.reflect.Method dm2 = climate2.getClass().getMethod("downfall");
                    Object val2 = dm2.invoke(climate2);
                    if (val2 instanceof Float) return (Float)val2;
                    if (val2 instanceof Double) return ((Double)val2).floatValue();
                }
            }
            return 0.5f;
        }
        catch (IllegalAccessError iae) {
            try {
                java.lang.reflect.Method m = b.getClass().getMethod("getClimate");
                Object climate = m.invoke(b);
                if (climate != null) {
                    java.lang.reflect.Method dm = climate.getClass().getMethod("downfall");
                    Object val = dm.invoke(climate);
                    if (val instanceof Float) return (Float)val;
                    if (val instanceof Double) return ((Double)val).floatValue();
                }
            }
            catch (Throwable t) {}
            return 0.5f;
        }
        catch (Throwable t) { return 0.5f; }
    }
}
