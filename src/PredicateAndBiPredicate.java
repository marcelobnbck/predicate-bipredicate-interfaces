import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PredicateAndBiPredicate {

    public static void main(String[] args) {

        List<Person> people = new ArrayList<>(Arrays.asList(
                new Person("Maria", 30, "Sao Leopoldo"),
                new Person("Joao", 17, "Sapucaia do Sul"),
                new Person("Jose", 25, "Sao Leopoldo"),
                new Person("Juvenal", 40, "Novo Hamburgo"),
                new Person("Jhennyfer", 19, "Sapucaia do Sul"),
                new Person("Praxedes", 22, "Sao Leopoldo")
        ));

        System.out.println("\nOriginal List of People:");
        people.forEach(System.out::println);

        // Part 1: Predicate<T> Demonstration
        // 1. Basic Predicate: Check if a person is an adult (age >= 18)
        Predicate<Person> isAdult = person -> person.getAge() >= 18;
        System.out.println("Is Maria an adult? "
                + isAdult.test(people.get(0))); // true
        System.out.println("Is Joao an adult? "
                + isAdult.test(people.get(1))); // false

        // 2. Predicate for city: Check if a person lives in "Sao Leopoldo"
        Predicate<Person> livesInSL = person -> person
                .getCity()
                .equals("New York");
        System.out.println("Does Jose live in Sao Leopoldo? "
                + livesInSL.test(people.get(2))); // true
        System.out.println("Does Juvenal live in Sao Leopoldo? "
                + livesInSL.test(people.get(3))); // false

        // 3. Combining Predicates: and(), or(), negate()
        // Predicate: isAdult AND livesInSL
        Predicate<Person> adultFromSL = isAdult.and(livesInSL);
        System.out.println("Is Maria an adult from Sao Leopoldo? "
                + adultFromSL.test(people.get(0))); // true
        System.out.println("Is Joao an adult from Sao Leopoldo? "
                + adultFromSL.test(people.get(1))); // false
        System.out.println("Is Jose an adult from Sao Leopoldo? "
                + adultFromSL.test(people.get(2)));
                // true (25 >= 18 && Sao Leopoldo)

        // Predicate: isAdult OR livesInSL
        Predicate<Person> adultOrSLResident = isAdult.or(livesInSL);
        System.out.println("Is Joao an adult OR Sao Leopoldo resident? "
                + adultOrSLResident.test(people.get(1)));
                // true (17 < 18 but Sapucaia do Sul, so false, but Joao is 17.
                // Joao is not adult, not Sao Leopoldo. So this should be false)
        // Let's re-evaluate Joao: age 17 (not adult), city Sapucaia do Sul (not
        // Sao Leopoldo).
        // So (false OR false) = false. Correct.
        System.out.println("Is Prexedes an adult OR Sao Leopoldo resident? "
                + adultOrSLResident.test(people.get(5)));
                // true (22 >= 18 OR Sao Leopoldo)

        // Predicate: NOT isAdult
        Predicate<Person> isMinor = isAdult.negate();
        System.out.println("Is Joao a minor? "
                + isMinor.test(people.get(1))); // true
        System.out.println("Is Maria a minor? "
                + isMinor.test(people.get(0))); // false

        // 4. Using Predicates with Collections API (e.g., List.removeIf())
        List<Person> mutablePeopleList =
                new ArrayList<>(people); // Create a copy to modify
        System.out.println("People before removal: " + mutablePeopleList);
        mutablePeopleList.removeIf(isMinor); // Remove all minors
        System.out.println("People after removing minors: " + mutablePeopleList);

        // 5. Predicate.isEqual(Object targetRef) -
        // for checking equality using equals()
        Person referencePerson = new Person("Jose", 25, "Sao Leopoldo");
        Predicate<Person> isJose = Predicate.isEqual(referencePerson);
        System.out.println("Is the first person 'Jose'? "
                + isJose.test(people.get(0))); // false (Maria)
        System.out.println("Is the third person 'Jose'? "
                + isJose.test(people.get(2))); // true (Jose)

        // Part 2: BiPredicate<T, U> Demonstration
        // 1. Basic BiPredicate: Check if a person's age is greater than
        // a given value
        BiPredicate<Person, Integer> isOlderThan = (person, ageLimit) -> person.getAge() > ageLimit;
        System.out.println("Is Maria older than 25? "
                + isOlderThan.test(people.get(0), 25)); // true (30 > 25)
        System.out.println("Is Joao older than 18? "
                + isOlderThan.test(people.get(1), 18)); // false (17 > 18 is false)

        // 2. BiPredicate for String comparison: Check if two strings are equal
        // ignoring case
        BiPredicate<String, String> stringsAreEqualIgnoreCase = (
                s1, s2) -> s1.equalsIgnoreCase(s2);
        System.out.println("'Hello' and 'hello' are equal (ignore case)? "
                + stringsAreEqualIgnoreCase.test("Hello", "hello")); // true
        System.out.println("'Java' and 'Python' are equal (ignore case)? "
                + stringsAreEqualIgnoreCase.test("Java", "Python")); // false

        // 3. Combining BiPredicates (and(), or(), negate())
        BiPredicate<Person, String> isFromCity = (
                person, city) -> person.getCity().equals(city);
        BiPredicate<Person, Integer> isAgeBetween = (
                person, minAge) -> person.getAge() >= minAge;

        // BiPredicate: isFromCity("Sao Leopoldo") AND isAgeBetween(20)
        BiPredicate<Person, String> isAdultFromSpecificCity = (
                person, city) ->
                isOlderThan.test(person, 17)
                        && isFromCity.test(person, city);
                // Combining logic directly

        System.out.println("Is Jose an adult from Sao Leopoldo? "
                + isAdultFromSpecificCity.test(people.get(2), "Sao Leopoldo"));
                // true
        System.out.println("Is Juvenal an adult from Sao Leopoldo? "
                + isAdultFromSpecificCity.test(people.get(3), "Sao Leopoldo"));
                // false (Juvenal is from Novo Hamburgo)

        // Another combination: person is older than 20 OR lives in Sapucaia do Sul
        BiPredicate<Person, Integer> olderThan20OrLivesInSap = (
                person, ageThreshold) ->
                person.getAge() >
                        ageThreshold
                        || person.getCity().equals("Sapucaia do Sul");

        System.out.println("Is Jhennyfer older than 20 OR lives in Sapucaia do Sul? "
                + olderThan20OrLivesInSap.test(people.get(4), 20));
                // true (Jhennyfer is 19, but lives in Sapucaia do Sul)
        System.out.println("Is Praxedes older than 20 OR lives in Sapucaia do Sul? "
                + olderThan20OrLivesInSap.test(people.get(5), 20));
                // true (Frank is 22, and lives in Sao Leopoldo)

    }
}