import java.util.Objects;

class Person {
    private String name;
    private int age;
    private String city;

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", city='" + city + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age ==
                person.age
                && name.equals(person.name)
                && city.equals(person.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, city);
    }
}
