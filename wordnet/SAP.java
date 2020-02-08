/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP
{
    private Digraph digraph;
    private int CapturedBy[]; // 0: not searched; 1: from vertex v; 2: from vertex w
    private int Distance[];
    private ResizingArrayStack<Integer> recoveryList = new ResizingArrayStack<>(); // record vertices whose state should be recovered

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        digraph = new Digraph(G);// DEFENSIVE COPYING
        CapturedBy = new int[digraph.V()];
        Distance = new int[digraph.V()];
    }

    // recover the CapturedBy[] array, thus restore search status
    private void Recover()
    {
        while (!recoveryList.isEmpty())
        {
            int i = recoveryList.pop();
            CapturedBy[i] = 0;
            Distance[i] = 0;
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        if (v == w) return 0;
        if (v < 0 || v >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
        if (w < 0 || w >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();
        CapturedBy[v] = 1;
        CapturedBy[w] = 2;
        vQueue.enqueue(v);
        wQueue.enqueue(w);
        recoveryList.push(v);
        recoveryList.push(w);
        while (true)
        {
            if (vQueue.isEmpty())
            {
                if (wQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int v1 = vQueue.dequeue(); //v1: vertex from which we search currently
                for (int i : digraph.adj(v1))
                {
                    if (CapturedBy[i] == 0) //if captured by neither v nor w
                    {
                        CapturedBy[i] = 1; //claim that the vertex is marked by v
                        Distance[i] = Distance[v1] + 1; //record distance
                        vQueue.enqueue(i);
                        recoveryList.push(i);
                    } else
                    {
                        if (CapturedBy[i] == 2) //captured by opposite w
                        {
                            int ret = Distance[v1] + Distance[i] + 1;
                            Recover();
                            return ret;
                        }
                    }
                    //if captured by v, do nothing since BFS always finds shortest path
                }
            }

            if (wQueue.isEmpty())
            {
                if (vQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int w1 = wQueue.dequeue(); //vertex from which we currently search
                for (int i : digraph.adj(w1))
                {
                    if (CapturedBy[i] == 0) //unmarked
                    {
                        CapturedBy[i] = 2; //claim that w captured it
                        Distance[i] = Distance[w1] + 1;
                        wQueue.enqueue(i);
                        recoveryList.push(i);
                    } else if (CapturedBy[i] == 1)
                    {
                        int ret = Distance[w1] + Distance[i] + 1; //if captured by v, return the length
                        Recover();
                        return ret;
                    }
                    //if already captured by w, do nothing
                }
            }
        }
    }

    //a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        if (v == w) return v;
        if (v < 0 || v >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
        if (w < 0 || w >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();
        CapturedBy[v] = 1;
        CapturedBy[w] = 2;
        vQueue.enqueue(v);
        wQueue.enqueue(w);
        recoveryList.push(v);
        recoveryList.push(w);
        while (true)
        {
            if (vQueue.isEmpty())
            {
                if (wQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int v1 = vQueue.dequeue(); //v1: vertex from which we search currently
                for (int i : digraph.adj(v1))
                {
                    if (CapturedBy[i] == 0) //if captured by neither v nor w
                    {
                        CapturedBy[i] = 1; //claim that the vertex is marked by v
                        vQueue.enqueue(i);
                        recoveryList.push(i);
                    } else
                    {
                        if (CapturedBy[i] == 2) //captured by opposite w
                        {
                            Recover();
                            return i;
                        }
                    }
                    //if captured by v, do nothing since BFS always finds shortest path
                }
            }

            if (wQueue.isEmpty())
            {
                if (vQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int w1 = wQueue.dequeue(); //vertex from which we currently search
                for (int i : digraph.adj(w1))
                {
                    if (CapturedBy[i] == 0) //unmarked
                    {
                        CapturedBy[i] = 2; //claim that w captured it
                        wQueue.enqueue(i);
                        recoveryList.push(i);
                    } else if (CapturedBy[i] == 1)
                    {
                        Recover();
                        return i;
                    }
                    //if already captured by w, do nothing
                }
            }
        }
    }

    //length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        //Corner cases : null argument
        if (v == null || w == null)
        {
            throw new IllegalArgumentException();
        }
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();


        for (Integer i : v)
        {
            if (i == null) throw new IllegalArgumentException(); //Corner case: null element
            if (i < 0 || i >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
            CapturedBy[i] = 1; //Mark all vertices in v as captured by v
            vQueue.enqueue(i); //Enqueue all the vertices in v into vQueue, to initialize a BFS
            recoveryList.push(i); //Push all the vertices in v into recovery list
        }
        //The lines below are symmetric
        for (Integer i : w)
        {
            if (i == null) throw new IllegalArgumentException(); //Corner case: null element
            if (i < 0 || i >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
            CapturedBy[i] = 2;
            wQueue.enqueue(i);
            recoveryList.push(i);
        }
        //BFS from two group of vertices alternatively
        while (true)
        {
            if (vQueue.isEmpty())
            {
                if (wQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int v1 = vQueue.dequeue(); //v1: vertex from which we search currently
                for (int i : digraph.adj(v1))
                {
                    if (CapturedBy[i] == 0) //if captured by neither v nor w
                    {
                        CapturedBy[i] = 1; //claim that the vertex is marked by v
                        Distance[i] = Distance[v1] + 1; //record distance
                        vQueue.enqueue(i);
                        recoveryList.push(i);
                    } else
                    {
                        if (CapturedBy[i] == 2) //captured by opposite w
                        {
                            int ret = Distance[v1] + Distance[i] + 1;
                            Recover();
                            return ret;
                        }
                    }
                    //if captured by v, do nothing since BFS always finds shortest path
                }
            }

            if (wQueue.isEmpty())
            {
                if (vQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int w1 = wQueue.dequeue(); //vertex from which we currently search
                for (int i : digraph.adj(w1))
                {
                    if (CapturedBy[i] == 0) //unmarked
                    {
                        CapturedBy[i] = 2; //claim that w captured it
                        Distance[i] = Distance[w1] + 1; //record distance
                        wQueue.enqueue(i);
                        recoveryList.push(i);
                    } else if (CapturedBy[i] == 1)
                    {
                        int ret = Distance[w1] + Distance[i] + 1;
                        Recover();
                        return ret;
                    }
                    //if already captured by w, do nothing
                }
            }
        }
    }

    //a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        //Corner cases
        if (v == null || w == null)
        {
            throw new IllegalArgumentException();
        }
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();


        for (Integer i : v)
        {
            if (i == null) throw new IllegalArgumentException(); //Corner case: null element
            if (i < 0 || i >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
            CapturedBy[i] = 1; //Mark all vertices in v as captured by v
            vQueue.enqueue(i); //Enqueue all the vertices in v into vQueue, to initialize a BFS
            recoveryList.push(i); //Push all the vertices in v into recovery list
        }
        //The lines below are symmetric
        for (Integer i : w)
        {
            if (i == null) throw new IllegalArgumentException(); //Corner case: null element
            if (i < 0 || i >= digraph.V()) throw new IllegalArgumentException(); //Corner case: element outrange
            CapturedBy[i] = 2;
            wQueue.enqueue(i);
            recoveryList.push(i);
        }
        //BFS from two group of vertices alternatively
        while (true)
        {
            if (vQueue.isEmpty())
            {
                if (wQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int v1 = vQueue.dequeue(); //v1: vertex from which we search currently
                for (int i : digraph.adj(v1))
                {
                    if (CapturedBy[i] == 0) //if captured by neither v nor w
                    {
                        CapturedBy[i] = 1; //claim that the vertex is marked by v
                        vQueue.enqueue(i);
                        recoveryList.push(i);
                    } else
                    {
                        if (CapturedBy[i] == 2) //captured by opposite w
                        {
                            Recover();
                            return i;
                        }
                    }
                    //if captured by v, do nothing since BFS always finds shortest path
                }
            }

            if (wQueue.isEmpty())
            {
                if (vQueue.isEmpty())
                {
                    Recover();
                    return -1;
                }
            } else
            {
                int w1 = wQueue.dequeue(); //vertex from which we currently search
                for (int i : digraph.adj(w1))
                {
                    if (CapturedBy[i] == 0) //unmarked
                    {
                        CapturedBy[i] = 2; //claim that w captured it
                        wQueue.enqueue(i);
                        recoveryList.push(i);
                    } else if (CapturedBy[i] == 1)
                    {
                        Recover();
                        return i;
                    }
                    //if already captured by w, do nothing
                }
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph g = new Digraph(in);
        SAP sap = new SAP(g);
        Stack<Integer> vset = new Stack<Integer>();
        Stack<Integer> wset = new Stack<Integer>();
        while (true)
        {
            while (true)
            {
                int i = StdIn.readInt();
                if (i == -1) break;
                vset.push(i);
            }
            while (true)
            {
                int i = StdIn.readInt();
                if (i == -1) break;
                wset.push(i);
            }
            StdOut.println("-----------------------------------");
            if (wset.size() == 1 && vset.size() == 1)
            {
                int v = vset.pop();
                int w = wset.pop();
                StdOut.println(sap.ancestor(v, w) + " " + sap.length(v, w));
            } else
            {
                StdOut.println(sap.ancestor(vset, wset) + " " + sap.length(vset, wset));
                vset = new Stack<>();
                wset = new Stack<>();
            }

        }

    }
}
