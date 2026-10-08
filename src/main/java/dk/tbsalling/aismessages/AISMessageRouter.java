/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link Consumer} of AIS messages which routes each message to a handler registered for its type.
 * <p>
 * Routers are created with a fluent {@link Builder}:
 * <pre>{@code
 * AISMessageRouter router = AISMessageRouter.builder()
 *         .on(PositionReport.class, positionReport -> ...)       // message types 1, 2 and 3
 *         .on(ShipAndVoyageData.class, shipAndVoyageData -> ...)
 *         .on(DynamicDataReport.class, dynamicDataReport -> ...) // any other dynamic data report
 *         .otherwise(message -> ...)
 *         .onError(e -> log.log(Level.WARNING, "AIS message handler failed", e))
 *         .build();
 *
 * new AISInputStreamReader(inputStream, router).run();
 * }</pre>
 * <p>
 * Handlers may be registered for concrete message classes, for abstract message classes such as
 * {@code PositionReport}, and for interfaces implemented by message classes such as
 * {@code DynamicDataReport} or {@code StaticDataReport}. When several registered types match a message,
 * the <em>most specific</em> one wins: a type beats any of its supertypes, regardless of registration
 * order. Messages matched by no registered type go to the {@link Builder#otherwise(Consumer) otherwise}
 * handler, which by default ignores them.
 * <p>
 * All routing decisions are resolved once, in {@link Builder#build()}, so routing a message is a single
 * map lookup. {@code build()} rejects registrations that could not be routed unambiguously.
 * <p>
 * A {@code RuntimeException} thrown by a handler is passed to the {@link Builder#onError(Consumer) onError}
 * handler, if one is set, and otherwise propagates to the caller. Note that the router only ever sees
 * messages which were successfully decoded: NMEA sentences that fail to decode are dropped and logged
 * earlier in the pipeline, and never reach the router or its {@code onError} handler.
 * <p>
 * An {@code AISMessageRouter} is immutable and thread-safe, provided its handlers are.
 */
public final class AISMessageRouter implements Consumer<AISMessage> {

    private static final List<Class<?>> CONCRETE_MESSAGE_CLASSES = concreteSubclassesOf(AISMessage.class);

    private final Map<Class<?>, Consumer<AISMessage>> handlers;
    private final Consumer<AISMessage> fallback;
    private final Consumer<? super RuntimeException> errorHandler;

    private AISMessageRouter(Map<Class<?>, Consumer<AISMessage>> handlers, Consumer<AISMessage> fallback, Consumer<? super RuntimeException> errorHandler) {
        this.handlers = Map.copyOf(handlers);
        this.fallback = fallback;
        this.errorHandler = errorHandler;
    }

    /**
     * Create a new builder for an {@code AISMessageRouter}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Route the message to the handler registered for its type, or to the fallback handler if there is none.
     *
     * @param aisMessage the message to route
     * @throws NullPointerException if {@code aisMessage} is {@code null}
     * @throws RuntimeException     any exception thrown by the handler, if no {@code onError} handler is set
     */
    @Override
    public void accept(AISMessage aisMessage) {
        Objects.requireNonNull(aisMessage, "aisMessage cannot be null.");
        try {
            handlers.getOrDefault(aisMessage.getClass(), fallback).accept(aisMessage);
        } catch (RuntimeException e) {
            errorHandler.accept(e);
        }
    }

    private static List<Class<?>> concreteSubclassesOf(Class<?> sealedClass) {
        List<Class<?>> concreteClasses = new ArrayList<>();
        for (Class<?> subclass : sealedClass.getPermittedSubclasses()) {
            if (subclass.isSealed()) {
                concreteClasses.addAll(concreteSubclassesOf(subclass));
            }
            if (!Modifier.isAbstract(subclass.getModifiers())) {
                concreteClasses.add(subclass);
            }
        }
        return List.copyOf(concreteClasses);
    }

    /**
     * Fluent builder for {@link AISMessageRouter}. A builder is not thread-safe, but may be reused to build
     * several routers.
     */
    public static final class Builder {

        private final Map<Class<?>, Consumer<AISMessage>> handlers = new LinkedHashMap<>();
        private Consumer<AISMessage> fallback = _ -> { };
        private Consumer<? super RuntimeException> errorHandler = e -> { throw e; };

        private Builder() {
        }

        /**
         * Route messages of the given type to the given handler.
         * <p>
         * The type may be a concrete or abstract AIS message class, or an interface implemented by AIS
         * message classes. If several registered types match a message, the most specific one is used.
         *
         * @param type    the message type to handle
         * @param handler the handler for messages of that type
         * @return this builder
         * @throws NullPointerException  if {@code type} or {@code handler} is {@code null}
         * @throws IllegalStateException if a handler is already registered for {@code type}
         */
        @SuppressWarnings("unchecked")
        public <T> Builder on(Class<T> type, Consumer<? super T> handler) {
            Objects.requireNonNull(type, "type cannot be null.");
            Objects.requireNonNull(handler, "handler cannot be null.");
            if (handlers.containsKey(type)) {
                throw new IllegalStateException("A handler is already registered for %s.".formatted(type.getName()));
            }
            // Safe: build() only routes messages of a class assignable to type to this handler.
            handlers.put(type, (Consumer<AISMessage>) handler);
            return this;
        }

        /**
         * Route messages matched by no registered type to the given handler. By default, such messages are
         * ignored.
         *
         * @param fallback the handler for unmatched messages
         * @return this builder
         * @throws NullPointerException if {@code fallback} is {@code null}
         */
        @SuppressWarnings("unchecked")
        public Builder otherwise(Consumer<? super AISMessage> fallback) {
            this.fallback = (Consumer<AISMessage>) Objects.requireNonNull(fallback, "fallback cannot be null.");
            return this;
        }

        /**
         * Pass any {@code RuntimeException} thrown by a handler (including the {@code otherwise} handler) to
         * the given error handler, instead of propagating it to the caller. This prevents a failing handler
         * from terminating the thread that feeds the router, such as an {@link AISInputStreamReader}.
         * <p>
         * Only handler failures are reported here; NMEA sentences that cannot be decoded never reach the
         * router. An exception thrown by the error handler itself propagates to the caller.
         *
         * @param errorHandler the handler for exceptions thrown by message handlers
         * @return this builder
         * @throws NullPointerException if {@code errorHandler} is {@code null}
         */
        public Builder onError(Consumer<? super RuntimeException> errorHandler) {
            this.errorHandler = Objects.requireNonNull(errorHandler, "errorHandler cannot be null.");
            return this;
        }

        /**
         * Build an immutable router from the current state of this builder, resolving the handler for every
         * AIS message class up front.
         *
         * @return the router
         * @throws IllegalArgumentException if a registered type matches no AIS message class
         * @throws IllegalStateException    if, for some AIS message class, no single registered type is more
         *                                  specific than all other matching registered types
         */
        public AISMessageRouter build() {
            for (Class<?> type : handlers.keySet()) {
                if (CONCRETE_MESSAGE_CLASSES.stream().noneMatch(type::isAssignableFrom)) {
                    throw new IllegalArgumentException("%s matches no AIS message type.".formatted(type.getName()));
                }
            }

            Map<Class<?>, Consumer<AISMessage>> resolved = new HashMap<>();
            for (Class<?> messageClass : CONCRETE_MESSAGE_CLASSES) {
                List<Class<?>> candidates = handlers.keySet().stream().filter(type -> type.isAssignableFrom(messageClass)).toList();
                List<Class<?>> mostSpecific = candidates.stream().filter(type -> candidates.stream().noneMatch(other -> other != type && type.isAssignableFrom(other))).toList();
                if (mostSpecific.size() > 1) {
                    throw new IllegalStateException("Ambiguous handlers for %s: none of %s is more specific than the others.".formatted(messageClass.getName(), mostSpecific.stream().map(Class::getName).toList()));
                }
                if (mostSpecific.size() == 1) {
                    resolved.put(messageClass, handlers.get(mostSpecific.getFirst()));
                }
            }

            return new AISMessageRouter(resolved, fallback, errorHandler);
        }
    }
}
