package SuperLee.Transport.BusinessLayer;
import java.util.Objects;

// For creating key with two fields
public class CustomKey {
    private final Object key1;
    private final Object key2;

    public CustomKey(Object key1, Object key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomKey customKey = (CustomKey) obj;
        return Objects.equals(key1, customKey.key1) && Objects.equals(key2, customKey.key2);
    }

    @Override
    public int hashCode() { // returns an integer hash code that is computed based on the key1 and key2 fields of the CustomKey object
        return Objects.hash(key1, key2);
    }
}
