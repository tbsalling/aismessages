package dk.tbsalling.aismessages.ais.messages;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by tbsalling on 20/01/15.
 */
public interface CachedDecodedValues {

    /**
     * Decode a value and cache it for faster future calls. Use weak references for the caching to
     * allow the garbage collector to free up memory. The value can just be decoded again.
     *
     * @param refGetter A getter which gets the weak reference to be used as cache
     * @param refSetter A setter to set the weak reference caching the decoded value.
     * @param decoder A supplier which can extract the decoded value from a bit string.
     * @param <T> The return type.
     * @return The decoded (and now cached) value.
     */
    default <T> T getDecodedValueByWeakReference(Supplier<WeakReference<T>> refGetter, Consumer<WeakReference<T>> refSetter, Supplier<Boolean> condition, Supplier<T> decoder) {
        T decodedValue = null;
        if (condition.get()) {
            WeakReference<T> ref = refGetter.get();
            if (ref != null) {
                decodedValue = ref.get();
            }
            if (decodedValue == null) {
                decodedValue = decoder.get();
                refSetter.accept(new WeakReference<>(decodedValue));
            }
        }
        return decodedValue;
    }

    /**
     * Decode a value and cache it for faster future calls.
     *
     * @param getter A getter which gets previously decoded values of this property.
     * @param setter A setter which stores or caches the decoded value
     * @param decoder A supplier which can extract the decoded value from a bit string.
     * @param <T> The return type.
     * @return The decoded value.
     */
    default <T> T getDecodedValue(Supplier<T> getter, Consumer<T> setter, Supplier<Boolean> condition, Supplier<T> decoder) {
        T decodedValue = getter.get();
        if (condition.get() && decodedValue == null) {
            decodedValue = decoder.get();
            setter.accept(decodedValue);
        }
        return decodedValue;
    }

}
