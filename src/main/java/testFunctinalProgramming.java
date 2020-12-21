import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class testFunctinalProgramming {
    public static void main(String[] args){
        // introduction
        // Java introduced functinal programming in Java 8
        // Java 8 introduced 1. lambda expression 2. method interfaces 3. plethora of functional interfaces
        // While discussiing functional interfaces, functional interfaces like consumer, suppier, Predicate
        // Functions are most crucial

        // 1. Consumer
        // consumer is a functional interface that accepts a single input and returns no output
        // the implementation of this interface consumes the input supplied to it, comsumer interface has
        // two method.
        // (1) void accept(T t);
        // (2) default Consumer<T> andThen(Consumer<? super T> after);
        List<String> cities = Arrays.asList("Sydney", "Dhaka", "New York", "London");
        Consumer<List<String>> upperCaseConsumer = list -> {
            for(int i=0; i< list.size(); i++){
                list.set(i, list.get(i).toUpperCase());
            }
        };
        Consumer<List<String>> printConsumer = list -> list.stream().forEach(System.out::println);
        upperCaseConsumer.andThen(printConsumer).accept(cities);

        // 2. Supplier
        // A supplier is a simple interface which indicates that this implementation is a supplier of result
        // The supplier has only one method get()
        Supplier<Double> doubleSupplier1 = ()->Math.random();    System.out.println(doubleSupplier1.get());
        DoubleSupplier doubleSupplier2 = Math::random;   System.out.println(doubleSupplier2.getAsDouble());
        //One of the primary usage of supplier to enable deferred execution. This means delaying the
        // execution until it is needed. For example, Optional class has a method named orElseGet.
        // This method is triggered if optional does not have data. This is demonstrated below:
        Supplier<Double> doubleSupplier = () -> Math.random();
        Optional<Double> optionalDouble = Optional.empty();
        System.out.println(optionalDouble.orElseGet(doubleSupplier));

        // 3. predicate
        // A Predicate interface represents a boolean-valued-function of an argument.
        // This is mainly used to filter data from a Java Stream. The filter method of a stream
        // accepts a predicate to filter the data and return a new stream satisfying the predicate.
        // A predicate has a test() method which accepts an argument and returns a boolean value.
        // Predicate also provides a few default and static method for composition and other purposes:
        //        default Predicate<T> and(Predicate<? super T> other);
        //        default Predicate<T> or(Predicate<? super T> other);
        //        static <T> Predicate<T> isEquals(Object targetRef);
        //        default Predicate<T> negate();
        List<String> names = Arrays.asList("John", "Smith", "Samueal", "Catley", "Sie");
        Predicate<String> startPredicate = str -> str.startsWith("S");
        Predicate<String> lengthPredicate = str -> str.length() >= 5;
        names.stream().filter(startPredicate.and(lengthPredicate)).forEach(System.out::println);

        // 4. function
        // A Function interface is more of a generic one that takes one argument and produces a
        // result. This has a Single Abstract Method (SAM) 'apply' which accepts an argument of
        // a type T and produces a result of type R. One of the common use cases of this interface
        // is Stream.map method. This is shown as below:
        names = Arrays.asList("Smith", "Gourav", "Heather", "John", "Catania");
        Function<String, Integer> nameMappingFunction = String::length;
        List<Integer> nameLength = names.stream().map(nameMappingFunction).collect(Collectors.toList());
        System.out.println(nameLength);

        // Stream API sounds similar to InputStream and OutputStream from Java I/O. But Java 8 streams
        // are a completely different thing. Streams are Monads, thus playing a big part in bringing
        // functional programming to Java:
        //In functional programming, a monad is a structure that represents computations defined as
        // sequences of steps. A type with a monad structure defines what it means to chain
        // operations, or nest functions of that type together.
        List<String> myList =
                Arrays.asList("a1", "a2", "b1", "c2", "c1");

        myList
                .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
        // Stream operations are either intermediate or terminal. Intermediate operations return a
        // stream so we can chain multiple intermediate operations without using semicolons. Terminal
        // operations are either void or return a non-stream result. In the above example filter,
        // map and sorted are intermediate operations whereas forEach is a terminal operation.
        // Such a chain of stream operations as seen in the example above is also known as operation
        // pipeline.

        // Most stream operations accept some kind of lambda expression parameter, a functional interface
        // specifying the exact behavior of the operation. Most of those operations must be both
        // non-interfering and stateless. What does that mean?

        // A function is non-interfering when it does not modify the underlying data source of the stream,
        // e.g. in the above example no lambda expression does modify myList by adding or removing
        // elements from the collection.

        // A function is stateless when the execution of the operation is deterministic, e.g. in the above
        // example no lambda expression depends on any mutable variables or states from the outer scope
        // which might change during execution.

        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);  // a1
        // Calling the method stream() on a list of objects returns a regular object stream. But we don't
        // have to create collections in order to work with streams as we see in the next code sample:

        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);  // a1
        // Just use Stream.of() to create a stream from a bunch of object references.

        // Besides regular object streams Java 8 ships with special kinds of streams for working with
        // the primitive data types int, long and double. As you might have guessed it's IntStream, LongStream
        // and DoubleStream.
        // IntStreams can replace the regular for-loop utilizing IntStream.range():
        IntStream
                .range(1, 4)
                .forEach(System.out::println);

        // All those primitive streams work just like regular object streams with the following differences:
        // Primitive streams use specialized lambda expressions, e.g. IntFunction instead of Function or
        // IntPredicate instead of Predicate. And primitive streams support the additional terminal aggregate
        // operations sum() and average():
        Arrays.stream(new int[] {1, 2, 3})
                .map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);  // 5.0

        //Sometimes it's useful to transform a regular object stream to a primitive stream or vice versa.
        // For that purpose object streams support the special mapping operations mapToInt(), mapToLong()
        // and mapToDouble:
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);  // 3

        // Primitive streams can be transformed to object streams via mapToObj():
        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);

        // Here's a combined example: the stream of doubles is first mapped to an int stream and
        // than mapped to an object stream of strings:
        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);


//        An important characteristic of intermediate operations is laziness. Look at this sample where a terminal operation is missing:
//        Stream.of("d2", "a2", "b1", "b3", "c")
//                .filter(s -> {System.out.println("filter: " + s); return true;});
//        When executing this code snippet, nothing is printed to the console. That is because intermediate operations will only be
//        executed when a terminal operation is present.
        Stream.of("d2", "a2", "b1", "b3", "c")
            .filter(s -> {
                System.out.println("filter: " + s);
                return true;
            })
            .forEach(s -> System.out.println("forEach: " + s));
//        filter:  d2
//        forEach: d2
//        filter:  a2
//        forEach: a2
//        filter:  b1
//        forEach: b1
//        filter:  b3
//        forEach: b3
//        filter:  c
//        forEach: c

        //This behavior can reduce the actual number of operations performed on each element,
        // as we see in the next example:
        boolean anymatch = Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });
        System.out.println(anymatch);
// map:      d2
// anyMatch: D2
// map:      a2
// anyMatch: A2
        // The operation anyMatch returns true as soon as the predicate applies to the given input element.
        // This is true for the second element passed "A2". Due to the vertical execution of the stream
        // chain, map has only to be executed twice in this case. So instead of mapping all elements
        // of the stream, map will be called as few as possible.


        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        // Sorting is a special kind of intermediate operation. It's a so called stateful operation since in
        // order to sort a collection of elements you have to maintain state during ordering.
//        sort:    a2; d2
//        sort:    b1; a2
//        sort:    b1; d2
//        sort:    b1; a2
//        sort:    b3; b1
//        sort:    b3; d2
//        sort:    c; b3
//        sort:    c; d2
//        filter:  a2
//        map:     a2
//        forEach: A2
//        filter:  b1
//        filter:  b3
//        filter:  c
//        filter:  d2
        // First, the sort operation is executed on the entire input collection. In other words sorted is
        // executed horizontally. So in this case sorted is called eight times for multiple combinations
        // on every element in the input collection.


//        Java 8 streams cannot be reused. As soon as you call any terminal operation the stream is closed:
//        Stream<String> stream = Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));
//        stream.anyMatch(s -> true);    // ok
//        stream.noneMatch(s -> true);   // exception
//        Calling noneMatch after anyMatch on the same stream results in the following exception:
//        java.lang.IllegalStateException: stream has already been operated upon or closed
//            at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:229)
//            at java.util.stream.ReferencePipeline.noneMatch(ReferencePipeline.java:459)
//            at com.winterbe.java8.Streams5.test7(Streams5.java:38)
//            at com.winterbe.java8.Streams5.main(Streams5.java:28)
//        To overcome this limitation we have to to create a new stream chain for every terminal operation
//        we want to execute, e.g. we could create a stream supplier to construct a new stream with
//        all intermediate operations already set up:
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));
        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok


        // prepare the data
        List<Person> persons = Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));
        List<Person> filtered = persons
            .stream()
            .filter(p -> p.name.startsWith("P"))
            .collect(Collectors.toList());
        System.out.println(filtered);    // [Peter, Pamela]

        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));
        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
    // age 18: [Max]
    // age 23: [Peter, Pamela]
    // age 12: [David]

        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));
        System.out.println(averageAge);     // 19.0
        IntSummaryStatistics ageSummary = persons
                        .stream()
                        .collect(Collectors.summarizingInt(p -> p.age));
        System.out.println(ageSummary);// IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}

        String phrase = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(phrase);
        // In Germany Max and Peter and Pamela are of legal age.

        // In order to transform the stream elements into a map, we have to specify how both the keys and the
        // values should be mapped. Keep in mind that the mapped keys must be unique, otherwise an
        // IllegalStateException is thrown. You can optionally pass a merge function as an additional
        // parameter to bypass the exception:
        Map<Integer, String> map = persons.stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));

        System.out.println(map);
// {18=Max, 23=Peter;Pamela, 12=David}


       //Now that we know some of the most powerful built-in collectors, let's try to build our own
        // special collector. We want to transform all persons of the stream into a single string
        // consisting of all names in upper letters separated by the | pipe character. In order
        // to achieve this we create a new collector via Collector.of(). We have to pass the four
        // ingredients of a collector: a supplier, an accumulator, a combiner and a finisher.Since strings
        // in Java are immutable, we need a helper class like StringJoiner to let the collector construct
        // our string. The supplier initially constructs such a StringJoiner with the appropriate
        // delimiter. The accumulator is used to add each persons upper-cased name to the StringJoiner.
        // The combiner knows how to merge two StringJoiners into one. In the last step the finisher
        // constructs the desired String from the StringJoiner.
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher
        String tnames = persons
                .stream()
                .collect(personNameCollector);
        System.out.println(tnames);  // MAX | PETER | PAMELA | DAVID

        // flatmap
        class Bar {
            String name;

            Bar(String name) {
                this.name = name;
            }
        }
        class Foo {
            String name;
            List<Bar> bars = new ArrayList<>();

            Foo(String name) {
                this.name = name;
            }
        }
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " +f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));

       // FlatMap is also available for the Optional class introduced in Java 8. Optionals
        // flatMap operation returns an optional object of another type. So it can be utilized to
        // prevent nasty null checks.
        // Think of a highly hierarchical structure like this:
        class Inner {
            String foo;
        }
        class Nested {
            Inner inner;
        }
        class Outer {
            Nested nested;
        }
        //In order to resolve the inner string foo of an outer instance you have to add multiple null
        // checks to prevent possible NullPointerExceptions:
        Outer outer = new Outer();
        if (outer != null && outer.nested != null && outer.nested.inner != null) {
            System.out.println(outer.nested.inner.foo);
        }
        //The same behavior can be obtained by utilizing optionals flatMap operation:
        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);
        //Each call to flatMap returns an Optional wrapping the desired object if present
        // or null if absent.


        //The reduction operation combines all elements of the stream into a single result. Java 8 supports
        // three different kind of reduce methods. The first one reduces a stream of elements to exactly one
        // element of the stream. Let's see how we can use this method to determine the oldest person:
        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela
        //The reduce method accepts a BinaryOperator accumulator function. That's actually a BiFunction where
        // both operands share the same type, in that case Person. BiFunctions are like Function but accept
        // two arguments. The example function compares both persons ages in order to return the person with
        // the maximum age.

        //The second reduce method accepts both an identity value and a BinaryOperator accumulator. This
        // method can be utilized to construct a new Person with the aggregated names and ages from all other
        // persons in the stream:
        Person result = persons
                        .stream()
                        .reduce(new Person("", 0), (p1, p2) -> {
                            p1.age += p2.age;
                            p1.name += p2.name;
                            return p1;
                        });
        System.out.format("name=%s; age=%s", result.name, result.age);
        // name=MaxPeterPamelaDavid; age=76
        // The third reduce method accepts three parameters: an identity value, a BiFunction accumulator and a
        // combiner function of type BinaryOperator. Since the identity values type is not restricted to the
        // Person type, we can utilize this reduction to determine the sum of ages from all persons:
        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);  // 76
        // As you can see the result is 76, but what's happening exactly under the hood? Let's extend the
        // above code by some debug output:
        ageSum = persons
                .stream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

// accumulator: sum=0; person=Max
// accumulator: sum=18; person=Peter
// accumulator: sum=41; person=Pamela
// accumulator: sum=64; person=David
        // As you can see the accumulator function does all the work. It first get called with the
        // initial identity value 0 and the first person Max. In the next three steps sum continually
        // increases by the age of the last steps person up to a total age of 76.

        // Wait wat? The combiner never gets called? Executing the same stream in parallel will lift the
        // secret:

        ageSum = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

// accumulator: sum=0; person=Pamela
// accumulator: sum=0; person=David
// accumulator: sum=0; person=Max
// accumulator: sum=0; person=Peter
// combiner: sum1=18; sum2=23
// combiner: sum1=23; sum2=12
// combiner: sum1=41; sum2=35
        // Executing this stream in parallel results in an entirely different execution behavior. Now
        // the combiner is actually called. Since the accumulator is called in parallel, the combiner
        // is needed to sum up the separate accumulated values.

        // visit https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/ for details

    }
}

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }
}

