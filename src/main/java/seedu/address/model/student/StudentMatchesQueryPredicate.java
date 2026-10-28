package seedu.address.model.student;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Student} matches a name fragment or an exact student ID.
 */
public class StudentMatchesQueryPredicate implements Predicate<Student> {
    private final Optional<List<String>> nameQueries;
    private final Optional<List<Id>> idQueries;

    private StudentMatchesQueryPredicate(Optional<List<String>> nameQueries, Optional<List<Id>> idQueries) {
        this.nameQueries = requireNonNull(nameQueries);
        this.idQueries = requireNonNull(idQueries);
    }

    /** Creates a predicate that matches names containing {@code nameQuery}, ignoring case. */
    public StudentMatchesQueryPredicate(String nameQuery) {
        this(Optional.of(List.of(requireNonNull(nameQuery))), Optional.empty());
    }

    /** Creates a predicate that matches names containing any query, ignoring case. */
    public StudentMatchesQueryPredicate(List<String> nameQueries) {
        this(Optional.of(copyNonEmpty(nameQueries)), Optional.empty());
    }

    /** Creates a predicate that matches an exact student ID. */
    public StudentMatchesQueryPredicate(Id idQuery) {
        this(Optional.empty(), Optional.of(List.of(requireNonNull(idQuery))));
    }

    /** Creates a predicate that matches any of the given student IDs exactly. */
    public static StudentMatchesQueryPredicate forIds(List<Id> idQueries) {
        return new StudentMatchesQueryPredicate(Optional.empty(), Optional.of(copyNonEmpty(idQueries)));
    }

    private static <T> List<T> copyNonEmpty(List<T> values) {
        requireNonNull(values);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("At least one search query is required.");
        }
        return List.copyOf(values);
    }

    @Override
    public boolean test(Student student) {
        requireNonNull(student);
        if (nameQueries.isPresent()) {
            String studentName = student.getName().fullName.toLowerCase(Locale.ROOT);
            return nameQueries.get().stream()
                    .map(query -> query.toLowerCase(Locale.ROOT))
                    .anyMatch(studentName::contains);
        }
        return idQueries.get().contains(student.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentMatchesQueryPredicate otherPredicate)) {
            return false;
        }
        return nameQueries.equals(otherPredicate.nameQueries) && idQueries.equals(otherPredicate.idQueries);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nameQueries, idQueries);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameQueries", nameQueries)
                .add("idQueries", idQueries)
                .toString();
    }
}
