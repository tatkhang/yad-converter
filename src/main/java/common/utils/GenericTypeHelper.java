package common.utils;

public class GenericTypeHelper {
    /**
     * Creates a new instance of the specified class using its no-argument constructor.
     *
     * @param <T>   the type of the object to create
     * @param clazz the {@code Class} object representing the type to instantiate
     * @return a new instance of the specified class
     * @throws RuntimeException if the class cannot be instantiated or does not have a no-argument constructor
     */
    public static <T> T constructInstance(Class<T> clazz) throws RuntimeException {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating an instance of type: " + clazz.getName());
        }
    }
}
