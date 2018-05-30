package at.tugraz.tc.cyfile.secret.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.tugraz.tc.cyfile.secret.Secret;

public abstract class AbstractSecret implements Secret {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Secret that = (Secret) o;

        return new EqualsBuilder()
                .append(this.getSecretValue(), that.getSecretValue())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.getSecretValue())
                .toHashCode();
    }

}
