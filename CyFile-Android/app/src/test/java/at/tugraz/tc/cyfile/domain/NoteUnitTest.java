package at.tugraz.tc.cyfile.domain;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class NoteUnitTest {


    @Test
    public void testEqualVerifier() {
        EqualsVerifier.forClass(Note.class)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
