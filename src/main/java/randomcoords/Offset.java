package randomcoords;

public final class Offset<T> {

    private final T x;
    private final T z;

    public Offset(T x, T z) {
        this.x = x;
        this.z = z;
    }

    public T getX() {
        return x;
    }

    public T getZ() {
        return z;
    }
}
