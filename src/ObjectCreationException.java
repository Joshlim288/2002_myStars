@Deprecated
public class ObjectCreationException extends Exception {
    public ObjectCreationException(String s) {
        super(s);
    }

    public ObjectCreationException() {
        super("Object could not be created due to errors.");
    }
}
