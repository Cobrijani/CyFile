package at.tugraz.tc.cyfile.domain

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.io.Serializable
import java.util.*


/**
 * Class that represents a Note
 * Created by cobri on 3/21/2018.
 */
class Note(var id: String, var title: String?, var content: String?) : Serializable {

    var dateTimeCreated: Long? = Date().time

    var dateTimeModified: Long?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || javaClass != other.javaClass) return false

        val note = other as Note?

        return EqualsBuilder()
                .append(id, note!!.id)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode()

    }

    init {
        this.dateTimeModified = dateTimeCreated
    }
}
