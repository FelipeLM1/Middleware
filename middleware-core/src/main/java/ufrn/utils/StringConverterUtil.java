package ufrn.utils;

public class StringConverterUtil {
    public static <T> T convert(String s, Class<T> clazz) {
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(s);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(s);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(s);
        } else if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(s);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) Boolean.valueOf(s);
        } else if (clazz == String.class) {
            return (T) s;
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + clazz.getName());
        }
    }
}
