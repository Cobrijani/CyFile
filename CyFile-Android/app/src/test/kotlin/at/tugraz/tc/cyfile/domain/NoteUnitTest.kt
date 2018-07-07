package at.tugraz.tc.cyfile.domain

import at.tugraz.tc.cyfile.BaseUnitTest
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.Test

class NoteUnitTest : BaseUnitTest() {


    @Test
    fun testEqualVerifier() {
        EqualsVerifier.forClass(Note::class.java)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
