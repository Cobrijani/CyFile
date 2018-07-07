package at.tugraz.tc.cyfile.secret.impl

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

import at.tugraz.tc.cyfile.secret.Secret

abstract class AbstractSecret : Secret {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || javaClass != other.javaClass) return false

        val that = other as Secret?

        return EqualsBuilder()
                .append(this.secretValue, that!!.secretValue)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(this.secretValue)
                .toHashCode()
    }

}
