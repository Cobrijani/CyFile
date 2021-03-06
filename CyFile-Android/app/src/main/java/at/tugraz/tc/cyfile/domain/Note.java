package at.tugraz.tc.cyfile.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a Note
 * Created by cobri on 3/21/2018.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Note implements Serializable {

    private String id;

    private String title;

    private String content;

    private Long dateTimeCreated;

    private Long dateTimeModified;

    public Note(String id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTimeCreated = new Date().getTime();
        this.dateTimeModified = dateTimeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return new EqualsBuilder()
                .append(id, note.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();

    }
}
