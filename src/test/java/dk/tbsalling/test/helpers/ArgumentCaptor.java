package dk.tbsalling.test.helpers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ArgumentCaptor<T> {
    private T capturedObject;

    @SuppressWarnings("unchecked")
    public Matcher<T> getMatcher() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(Object item) {
                capturedObject = (T) item;
                return true; // accept and capture any value
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("captures argument");
            }
        };
    }

    public T getCapturedObject() {
        return capturedObject;
    }
}
