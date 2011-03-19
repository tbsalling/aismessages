package dk.tbsalling.test.helpers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ArgumentCaptor<T> {
    T capturedObject;

    public Matcher<T> getMatcher() {
        return new BaseMatcher<T>() {
            @SuppressWarnings("unchecked")
            public boolean matches(Object item) {
                capturedObject = (T) item;
                return true;
            }
			
			public void describeTo(Description paramDescription) {
			}
        };
    }

    public T getCapturedObject() {
        return capturedObject;
    }
}
