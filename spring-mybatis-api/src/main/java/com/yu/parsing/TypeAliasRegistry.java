package com.yu.parsing;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class TypeAliasRegistry {

    private static final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    static  {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);

        registerAlias("byte[]", Byte[].class);
        registerAlias("long[]", Long[].class);
        registerAlias("short[]", Short[].class);
        registerAlias("int[]", Integer[].class);
        registerAlias("integer[]", Integer[].class);
        registerAlias("double[]", Double[].class);
        registerAlias("float[]", Float[].class);
        registerAlias("boolean[]", Boolean[].class);
    }

    public static void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            throw new IllegalArgumentException("The parameter alias cannot be null");
        }
        // issue #748
        String key = alias.toLowerCase(Locale.ENGLISH);
        if (TYPE_ALIASES.containsKey(key) && TYPE_ALIASES.get(key) != null && !TYPE_ALIASES.get(key).equals(value)) {
            throw new IllegalArgumentException("The alias '" + alias + "' is already mapped to the value '" + TYPE_ALIASES.get(key).getName() + "'.");
        }
        TYPE_ALIASES.put(key, value);
    }

    public static <T> Class<T> resolveAlias(String string) {
        if (string == null) {
            return null;
        }
        // issue #748
        String key = string.toLowerCase(Locale.ENGLISH);
        Class<T> value;
        if (TYPE_ALIASES.containsKey(key)) {
            value = (Class<T>) TYPE_ALIASES.get(key);
        } else {
            throw new IllegalArgumentException("Could not resolve type alias '" + string + "'.  Cause: ");
        }
        return value;

    }

}
