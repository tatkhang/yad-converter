package common.exceptions;

public class YadConverterException extends Exception {
    /**
     * Auto generated ID
     */
    private static final long serialVersionUID = -5699133180133363514L;

    public YadConverterException(String message) {
        super(message);
    }

    public YadConverterException(String message, Object... args) {
        this(String.format(message, args));
    }
}
