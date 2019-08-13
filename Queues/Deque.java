/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>
{
    private class Node<T1>
    {
        private T1 value;
        private Node<T1> previous; // the reference of the last Node
        private Node<T1> next; // the reference of the next Node

        public Node(T1 newItem, Node<T1> prevnode, Node<T1> nextnode)
        {
            value = newItem;
            previous = prevnode;
            next = nextnode;
        }
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node<Item> current = firstSentinel;

        @Override
        public boolean hasNext()
        {
            return !current.next.equals(lastSentinel);
        }

        @Override
        public Item next()
        {
            if (!hasNext())
            {
                throw new java.util.NoSuchElementException();
            }
            current = current.next;
            return current.value;
        }

        @Override
        public void remove()
        {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    private Node<Item> firstSentinel; // sentinel
    private Node<Item> lastSentinel; // sentinel

    private int size = 0;

    public Deque()                           // construct an empty deque FINISHED
    {
        firstSentinel = new Node<>(null, null, null);
        lastSentinel = new Node<>(null, null, null);
        firstSentinel.next = lastSentinel;
        lastSentinel.previous = firstSentinel;
    }

    public boolean isEmpty()                 // is the deque empty? FINISHED
    {
        return firstSentinel.next.equals(lastSentinel);
    }

    public int size()                        // return the number of items on the deque FINISHED
    {
        return size;
    }

    public void addFirst(Item item)          // add the item to the front FINISHED
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException();
        }
        Node<Item> newNode = new Node<Item>(item, firstSentinel, firstSentinel.next);
        firstSentinel.next.previous = newNode;
        firstSentinel.next = newNode;
        ++size;
    }

    public void addLast(Item item)           // add the item to the end FINISHED
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException();
        }
        Node<Item> newNode = new Node<>(item, lastSentinel.previous, lastSentinel);
        lastSentinel.previous.next = newNode;
        lastSentinel.previous = newNode;
        ++size;
    }

    public Item removeFirst()                // remove and return the item from the front FINISHED
    {
        if (isEmpty())
        {
            throw new java.util.NoSuchElementException();
        }
        Item ret = firstSentinel.next.value;
        firstSentinel.next.next.previous = firstSentinel;
        firstSentinel.next = firstSentinel.next.next;
        --size;
        return ret;
    }

    public Item removeLast()                 // remove and return the item from the end FINISHED
    {
        if (isEmpty())
        {
            throw new java.util.NoSuchElementException();
        }
        Item ret = lastSentinel.previous.value;
        lastSentinel.previous.previous.next = lastSentinel;
        lastSentinel.previous = lastSentinel.previous.previous;
        --size;
        return ret;
    }

    @Override
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private void print()
    {
        for (Item t : this)
        {
            StdOut.print(t + " ");
        }
        StdOut.println();
    }

    public static void main(String[] args)
    {
    }
    /* Corner cases.  Throw the specified exception for the following corner cases:

    √ Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
    √ Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
    Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
    √ Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
    Performance requirements.  Your deque implementation must support each deque operation
    (including construction) in constant worst-case time.
    A deque containing n items must use at most 48n + 192 bytes of memory
    and use space proportional to the number of items currently in the deque.
    Additionally, your iterator implementation must support each operation (including construction) in constant worst-case time.
    */
}
