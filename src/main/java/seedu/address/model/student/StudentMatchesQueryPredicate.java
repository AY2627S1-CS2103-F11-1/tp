package seedu.address.model.student;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Student} matches a name fragment or an exact student ID.
 */
public class StudentMatchesQueryPredicate implements Predicate<Student> {
    private final Optional<String> nameQuery;
    private final Optional<Id> idQuery;

    /** Creates a predicate that matches names containing {@code nameQuery}, ignoring case. */
    public StudentMatchesQueryPredicate(String nameQuery) {
        requireNonNull(nameQuery);
        this.nameQuery = Optional.of(nameQuery);
        this.idQuery = Optional.empty();
    }

    /** Creates a predicate that matches an exact student ID. */
    public StudentMatchesQueryPredicate(Id idQuery) {
        this.nameQuery = Optional.empty();
        this.idQuery = Optional.of(requireNonNull(idQuery));
    }

    @Override
    public boolean test(Student student) {
        requireNonNull(student);
        if (nameQuery.isPresent()) {
            return student.getName().fullName.toLowerCase(Locale.ROOT)
                    .contains(nameQuery.get().toLowerCase(Locale.ROOT));
        }
        return student.getId().equals(idQuery.orElseThrow());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentMatchesQueryPredicate otherPredicate)) {
            return false;
        }
        return nameQuery.equals(otherPredicate.nameQuery) && idQuery.equals(otherPredicate.idQuery);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nameQuery, idQuery);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameQuery", nameQuery)
                .add("idQuery", idQuery)
                .toString();
    }
}
