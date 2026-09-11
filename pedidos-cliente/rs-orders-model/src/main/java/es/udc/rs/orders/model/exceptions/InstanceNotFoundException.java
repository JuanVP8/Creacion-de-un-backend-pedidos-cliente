package es.udc.rs.orders.model.exceptions;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends Exception {

    private final Object key;
    private final String className;

    public InstanceNotFoundException(Object key, Class<?> clazz) {
        super(String.format("No se encontró la instancia de %s con clave %s", clazz.getSimpleName(), key));
        this.key = key;
        this.className = clazz.getSimpleName();
    }

    public InstanceNotFoundException(Object key, String className) {
        super(String.format("No se encontró la instancia de %s con clave %s", className, key));
        this.key = key;
        this.className = className;
    }

    public Object getKey() {
        return key;
    }

    public String getClassName() {
        return className;
    }
}