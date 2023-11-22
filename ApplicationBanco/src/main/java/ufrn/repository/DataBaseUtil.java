package ufrn.repository;

public class DataBaseUtil {

    private static long nextId = 0;

    public static void incrementId() {
        nextId++;
    }

    public static long getNextId() {
        incrementId();
        return nextId;
    }

}
