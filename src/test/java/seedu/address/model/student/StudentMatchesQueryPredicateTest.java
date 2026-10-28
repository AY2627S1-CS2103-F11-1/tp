package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.CARL;

import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentMatchesQueryPredicateTest {

    @Test
    public void test_nameQuery_matchesCaseInsensitiveFragmentsOnly() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("aLiCe");

        assertTrue(predicate.test(ALICE));
        assertFalse(predicate.test(CARL));
    }

    @Test
    public void test_idQuery_matchesExactIdOnly() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate(new Id("A1234567Y"));

        assertTrue(predicate.test(ALICE));
        assertFalse(predicate.test(CARL));
    }

    @Test
    public void test_multipleQueries_matchesAnyQuery() {
        StudentMatchesQueryPredicate predicate =
                new StudentMatchesQueryPredicate(List.of("Alice", "Kurz"));

        assertTrue(predicate.test(ALICE));
        assertTrue(predicate.test(CARL));
    }

    @Test
    public void constructor_emptyQueries_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new StudentMatchesQueryPredicate(List.of()));
        assertThrows(IllegalArgumentException.class, () -> StudentMatchesQueryPredicate.forIds(List.of()));
    }

    @Test
    public void equalsAndHashCode() {
        StudentMatchesQueryPredicate namePredicate = new StudentMatchesQueryPredicate("Alice");
        StudentMatchesQueryPredicate sameNamePredicate = new StudentMatchesQueryPredicate("Alice");
        StudentMatchesQueryPredicate idPredicate = new StudentMatchesQueryPredicate(new Id("A1234567Y"));

        assertTrue(namePredicate.equals(namePredicate));
        assertTrue(namePredicate.equals(sameNamePredicate));
        assertEquals(namePredicate.hashCode(), sameNamePredicate.hashCode());
        assertFalse(namePredicate.equals(idPredicate));
        assertFalse(namePredicate.equals(1));
        assertFalse(namePredicate.equals(null));
    }

    @Test
    public void toStringMethod() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("Alice");
        String expected = StudentMatchesQueryPredicate.class.getCanonicalName()
                + "{nameQueries=Optional[[Alice]], idQueries=Optional.empty}";
        assertEquals(expected,
                predicate.toString());
    }
}
