import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int size = 0;
    private Node<Item> firstSentinel = new Node<>(null, null);
    private Node<Item> last = firstSentinel;

    private class Node<T1>
    {
        private T1 value;
        private Node<T1> next; // the reference of the next Node

        public Node(T1 newItem, Node<T1> nextnode)
        {
            value = newItem;
            next = nextnode;
        }
    }

    private class RQIterator implements Iterator<Item>
    {
        private Node<Item> current = firstSentinel;

        @Override
        public boolean hasNext()
        {
            return !(current.next == null);
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

    public RandomizedQueue()                 // construct an empty randomized queue
    {

    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return last.equals(firstSentinel);
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return size;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException();
        }
        Node<Item> newNode = new Node<>(item, null);
        last.next = newNode;
        last = newNode;
        ++size;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty())
        {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        Node<Item> node = firstSentinel;
        for (int i = 0; i != index; ++i)
        {
            node = node.next;
        }
        if (index == size - 1)
            last = node;
        Node<Item> ret = node.next;
        node.next = ret.next;
        --size;
        return ret.value;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty())
        {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        Node<Item> node = firstSentinel;
        for (int i = 0; i != index; ++i)
        {
            node = node.next;
        }
        Node<Item> ret = node.next;
        return ret.value;
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RQIterator();
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(36);
        rq.dequeue();
        rq.enqueue(20);
        rq.size();
        rq.dequeue();
    }
}
/*
√ Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a null argument.
√ Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
√ Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
√ Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
 */
