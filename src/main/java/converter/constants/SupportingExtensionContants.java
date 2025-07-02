package converter.constants;

public class SupportingExtensionContants {
    public static final String CSV = "csv";
    public static final String PARQUET = "parquet";

    private static final String[] VALUES = new String[] { CSV, PARQUET };

    public static String[] getValues() {
        return VALUES;
    }

    public static final boolean isValidFileExtension(String extension) {
        for (String value : VALUES) {
            if (value.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }
}
