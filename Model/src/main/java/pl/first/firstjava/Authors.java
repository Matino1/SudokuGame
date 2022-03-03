package pl.first.firstjava;

import java.util.ListResourceBundle;

public class Authors extends ListResourceBundle {

    private final Object[][] contents = {
            {"author1", "Mateusz Strzelecki"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
