package com.modernjavainaction.part7_java21_features.chapter23_sequenced_collections;

import java.util.*;

/**
 * Chapter 23: Sequenced Collections
 * 
 * Java 21 introduces new interfaces for collections with a defined encounter
 * order:
 * - SequencedCollection: ordered collection with first/last access
 * - SequencedSet: ordered set with first/last access
 * - SequencedMap: ordered map with first/last entry access
 * 
 * These provide uniform APIs for accessing elements at both ends of a
 * collection.
 */
public class SequencedCollections {

    public static void main(String[] args) {
        System.out.println("=== Sequenced Collections in Java 21 ===\n");

        // 1. SequencedCollection basics
        sequencedCollectionBasics();

        // 2. SequencedSet operations
        sequencedSetOperations();

        // 3. SequencedMap operations
        sequencedMapOperations();

        // 4. Reversed views
        reversedViews();
    }

    /**
     * Basic operations on SequencedCollection
     */
    static void sequencedCollectionBasics() {
        System.out.println("1. SequencedCollection Basics");
        System.out.println("-".repeat(40));

        // ArrayList implements SequencedCollection
        List<String> list = new ArrayList<>(List.of("apple", "banana", "cherry", "date"));

        // New methods for first/last access
        System.out.println("Original list: " + list);
        System.out.println("getFirst(): " + list.getFirst());
        System.out.println("getLast():  " + list.getLast());

        // Add at beginning or end
        list.addFirst("apricot");
        list.addLast("elderberry");
        System.out.println("After addFirst/addLast: " + list);

        // Remove from beginning or end
        String removedFirst = list.removeFirst();
        String removedLast = list.removeLast();
        System.out.println("Removed first: " + removedFirst + ", last: " + removedLast);
        System.out.println("Final list: " + list);
        System.out.println();
    }

    /**
     * SequencedSet provides ordered set operations
     */
    static void sequencedSetOperations() {
        System.out.println("2. SequencedSet Operations");
        System.out.println("-".repeat(40));

        // LinkedHashSet maintains insertion order and is a SequencedSet
        SequencedSet<String> set = new LinkedHashSet<>();
        set.add("first");
        set.add("second");
        set.add("third");
        set.add("fourth");

        System.out.println("Set: " + set);
        System.out.println("getFirst(): " + set.getFirst());
        System.out.println("getLast():  " + set.getLast());

        // Get a reversed view
        SequencedSet<String> reversed = set.reversed();
        System.out.println("Reversed:   " + reversed);
        System.out.println("Reversed getFirst(): " + reversed.getFirst());

        // TreeSet also implements SequencedSet
        SequencedSet<Integer> treeSet = new TreeSet<>(Set.of(5, 2, 8, 1, 9));
        System.out.println("\nTreeSet (sorted): " + treeSet);
        System.out.println("First (smallest): " + treeSet.getFirst());
        System.out.println("Last (largest):   " + treeSet.getLast());
        System.out.println();
    }

    /**
     * SequencedMap provides ordered map operations
     */
    static void sequencedMapOperations() {
        System.out.println("3. SequencedMap Operations");
        System.out.println("-".repeat(40));

        // LinkedHashMap is a SequencedMap
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);

        System.out.println("Map: " + map);

        // First and last entries
        System.out.println("firstEntry(): " + map.firstEntry());
        System.out.println("lastEntry():  " + map.lastEntry());

        // Put at beginning or end
        map.putFirst("zero", 0);
        map.putLast("five", 5);
        System.out.println("After putFirst/putLast: " + map);

        // Poll (remove and return) first/last entries
        Map.Entry<String, Integer> first = map.pollFirstEntry();
        Map.Entry<String, Integer> last = map.pollLastEntry();
        System.out.println("Polled first: " + first + ", last: " + last);
        System.out.println("Final map: " + map);

        // Sequenced views of keys, values, and entries
        System.out.println("\nSequenced key set: " + map.sequencedKeySet());
        System.out.println("Sequenced values:  " + map.sequencedValues());
        System.out.println();
    }

    /**
     * Reversed views - essential for reverse iteration
     */
    static void reversedViews() {
        System.out.println("4. Reversed Views");
        System.out.println("-".repeat(40));

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        System.out.println("Original: " + list);
        System.out.println("Reversed: " + list.reversed());

        // The old way (before Java 21)
        System.out.print("Old way (ListIterator): ");
        ListIterator<String> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            System.out.print(it.previous() + " ");
        }
        System.out.println();

        // The new way - much cleaner!
        System.out.print("New way (reversed()):   ");
        for (String s : list.reversed()) {
            System.out.print(s + " ");
        }
        System.out.println();

        // Reversed view is a VIEW, not a copy
        List<String> reversedView = list.reversed();
        list.add("F");
        System.out.println("\nAfter adding 'F' to original:");
        System.out.println("Original: " + list);
        System.out.println("Reversed view: " + reversedView);

        // Works with streams too!
        System.out.print("\nReversed stream: ");
        list.reversed().stream().forEach(s -> System.out.print(s + " "));
        System.out.println();
    }
}
