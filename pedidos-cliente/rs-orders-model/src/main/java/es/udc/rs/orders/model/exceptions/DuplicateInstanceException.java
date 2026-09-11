package es.udc.rs.orders.model.exceptions;

@SuppressWarnings("serial")
public class DuplicateInstanceException extends Exception {

    private final Object key;
    private final String className;
    private final String uniqueField;

    public DuplicateInstanceException(String message) {
        super(message);
        this.key = null;
        this.className = null;
        this.uniqueField = null;
    }

    public DuplicateInstanceException(Object key, Class<?> clazz, String uniqueField) {
        super(String.format("Duplicado: %s con %s=%s ya existe", clazz.getSimpleName(), uniqueField, key));
        this.key = key;
        this.className = clazz.getSimpleName();
        this.uniqueField = uniqueField;
    }

    public Object getKey() {
        return key;
    }

    public String getClassName() {
        return className;
    }
    public String getUniqueField() {
        return uniqueField;
    }
}