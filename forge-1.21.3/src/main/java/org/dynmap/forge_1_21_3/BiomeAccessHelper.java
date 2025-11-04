package org.dynmap.forge_1_21_3;

import net.minecraft.world.level.biome.Biome;

/**
 * Helper para acceder de forma defensiva a propiedades de Biome
 * Evita IllegalAccessError en plataformas que restringen acceso directo
 */
public class BiomeAccessHelper {
    public static float getBaseTemperatureSafe(Biome b) {
        if (b == null) return 0.5f;
        try {
            return b.getBaseTemperature();
        }
        catch (IllegalAccessError | NoSuchMethodError e) {
            // Intentar vía getClimate().temperature() por reflexión
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
            catch (Throwable t) {
                // ignore and fallback
            }
            return 0.5f;
        }
        catch (Throwable t) {
            return 0.5f;
        }
    }

    public static float getDownfallSafe(Biome b) {
        if (b == null) return 0.5f;
        try {
            // Intentar el método preferido en mappings recientes
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
                // Fallback a getClimate() público
                java.lang.reflect.Method m2 = b.getClass().getMethod("getClimate");
                Object climate2 = m2.invoke(b);
                if (climate2 != null) {
                    java.lang.reflect.Method dm2 = climate2.getClass().getMethod("downfall");
                    Object val2 = dm2.invoke(climate2);
                    if (val2 instanceof Float) return (Float)val2;
                    if (val2 instanceof Double) return ((Double)val2).floatValue();
                }
            }
            // Si ninguno funciona, intentar vía API antigua si existe
            // (muchos mappings estarán cubiertos por lo anterior)
            return 0.5f;
        }
        catch (IllegalAccessError iae) {
            // Plataforma bloquea acceso; intentar getClimate() por reflexión
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
            catch (Throwable t) {
                // ignore
            }
            return 0.5f;
        }
        catch (Throwable t) {
            return 0.5f;
        }
    }
}
