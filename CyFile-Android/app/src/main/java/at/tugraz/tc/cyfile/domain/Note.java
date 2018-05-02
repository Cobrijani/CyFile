package at.tugraz.tc.cyfile.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a Note
 * Created by cobri on 3/21/2018.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Note implements Serializable {

    private String id;

    private String title;

    private String content;
}
