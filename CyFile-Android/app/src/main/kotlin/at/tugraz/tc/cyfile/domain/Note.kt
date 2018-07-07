package at.tugraz.tc.cyfile.domain

import android.os.Parcel
import android.os.Parcelable
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.io.Serializable
import java.util.*


/**
 * Class that represents a Note
 * Created by cobri on 3/21/2018.
 */
class Note(var id: String, var title: String, var content: String) : Parcelable, Serializable {

    constructor(id: String, title: String, content: String, dateC: Long,
                dateM: Long?) : this(id, title, content) {
        this.dateTimeModified = dateM
        this.dateTimeCreated = dateC
    }

    var dateTimeCreated: Long? = Date().time

    var dateTimeModified: Long?

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
        dateTimeCreated = parcel.readValue(Long::class.java.classLoader) as? Long
        dateTimeModified = parcel.readValue(Long::class.java.classLoader) as? Long
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeValue(dateTimeCreated)
        parcel.writeValue(dateTimeModified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}
