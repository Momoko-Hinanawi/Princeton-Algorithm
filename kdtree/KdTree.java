
/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
public class KdTree {
    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb = null; // the left/bottom subtree
        private Node rt = null; // the right/top subtree

        Node(Point2D pt) {
            p = pt;
        }
    }

    private Node root; // root node
    private int card; // cardinality

    public boolean isEmpty() // is the set empty?
    {
        return card == 0;
    }

    public int size() // number of points in the set
    {
        return card;
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (root == null) {
            ++card;
            root = new Node(p);
            root.rect = new RectHV(0, 0, 1, 1);
            return;
        }
        PutNode(p, root, root, true, true); // Initially, while inserting a node to root node, use X coordinate as key
    }

    private Node PutNode(Point2D point2D, Node prev, Node position, boolean isX, boolean isLeftChild)
    // point2D: the point which should be inserted in
    // prev: the previous node,that is, the parent node of position
    // position: position where the new node should be added
    // isX: if the x coordinate is regarded as the key,false if the y coordinate is
    // regarded as the key
    // isLeftChild: if 'position' is the left child of 'prev'
    {
        if (position == null) {
            ++card;
            Node newNode = new Node(point2D);
            double xmin, xmax, ymin, ymax;
            xmin = prev.rect.xmin();
            xmax = prev.rect.xmax();
            ymin = prev.rect.ymin();
            ymax = prev.rect.ymax();
            if (!isX) // split the region by vertical line
            {
                if (isLeftChild) // left subregion
                {
                    xmax = prev.p.x();
                } else // right subregion
                {
                    xmin = prev.p.x();
                }
            } else // split the region by horizontal line
            {
                if (isLeftChild) // bottom subregion
                {
                    ymax = prev.p.y();
                } else // top subregion
                {
                    ymin = prev.p.y();
                }
            }
            newNode.rect = new RectHV(xmin, ymin, xmax, ymax);
            return newNode;
        }
        int comp;
        if (isX) {
            comp = Double.compare(point2D.x(), position.p.x());
        } else {
            comp = Double.compare(point2D.y(), position.p.y());
        }
        if (comp == -1) // go left or bottom
        {
            position.lb = PutNode(point2D, position, position.lb, !isX, true);
            // node.RChild = PutNode(key, value, node.RChild);
        } else if (comp == 0) {
            if (!position.p.equals(point2D)) {
                position.rt = PutNode(point2D, position, position.rt, !isX, false);// equal keys : go right or up
            }
        } else // go right or up
        {
            position.rt = PutNode(point2D, position, position.rt, !isX, false);// equal keys : go right or up
        }
        // Maintain an R-B tree
        /*
         * if (isRed(node.RChild) && !isRed(node.LChild)) //Right red, left black:
         * rotate left node = rotateLeft(node); if (isRed(node.LChild) &&
         * isRed(node.LChild.LChild)) //two left edges are red node = rotateRight(node);
         * if (isRed(node.LChild) && isRed(node.RChild)) flipColors(node);
         */
        return position;
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        Node node = root;
        boolean isX = true;
        while (true) {

            if (node == null) {
                return false;
            }
            int comp;
            // compare keys
            if (isX) {
                comp = Double.compare(p.x(), node.p.x());
            } else {
                comp = Double.compare(p.y(), node.p.y());
            }

            if (comp < 0)// go left
            {
                node = node.lb;
            } else if (comp == 0)// equal keys
            {
                if (p.x() == node.p.x() && p.y() == node.p.y())
                    return true; // search hits
                else // default: go right
                {
                    node = node.rt;
                    isX = !isX;
                }
            } else // go right
            {
                node = node.rt;
                isX = !isX;
            }
        }

    }

    public void draw() // draw all points to standard draw
    {
        DrawRecursively(root);
    }

    private void DrawRecursively(Node node) {
        if (node == null)
            return;
        StdDraw.point(node.p.x(), node.p.y());
        DrawRecursively(node.lb);
        DrawRecursively(node.rt);
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
    {
        Stack<Point2D> returnStack = new Stack<>();
        if (isEmpty())
            return returnStack;
        findRange(returnStack, rect, root);
        return returnStack;
    }

    private void findRange(Stack<Point2D> stack, RectHV rect, Node searchFrom) {
        if (rect.contains(searchFrom.p)) {
            stack.push(searchFrom.p);
        }
        if (searchFrom.lb != null && searchFrom.lb.rect.intersects(rect)) {
            findRange(stack, rect, searchFrom.lb);
        }
        if (searchFrom.rt != null && searchFrom.rt.rect.intersects(rect)) {
            findRange(stack, rect, searchFrom.rt);
        }
    }

    private Point2D currentNearest;

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (isEmpty())
            return null;
        currentNearest = root.p;
        findNearest(p, root);
        return currentNearest;
    }

    private void findNearest(Point2D p, Node searchFrom) {
        double currentMinDist = currentNearest.distanceSquaredTo(p);
        if (searchFrom.p.distanceSquaredTo(p) < currentMinDist) {
            currentNearest = searchFrom.p; // update minimum
        }
        if (searchFrom.lb != null && searchFrom.lb.rect.distanceSquaredTo(p) <= currentMinDist)
            findNearest(p, searchFrom.lb);
        if (searchFrom.rt != null && searchFrom.rt.rect.distanceSquaredTo(p) <= currentMinDist)
            findNearest(p, searchFrom.rt);
    }

    private void PrintBFS(boolean showRect) {
        if (isEmpty()) {
            StdOut.println("empty tree");
            return;
        }
        Queue<Node> nodeQueue = new Queue<>();
        nodeQueue.enqueue(root);
        while (!nodeQueue.isEmpty()) {
            Node node = nodeQueue.dequeue();
            if (node.lb != null)
                nodeQueue.enqueue(node.lb);
            if (node.rt != null)
                nodeQueue.enqueue(node.rt);
            StdOut.print(node.p + " ");
            if (showRect)
                StdOut.println(node.rect);
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.12, 0.34));
        kdTree.insert(new Point2D(0.20, 0.34));
        kdTree.insert(new Point2D(0.20, 0.34));
        kdTree.insert(new Point2D(0.0, 0.34));
        kdTree.insert(new Point2D(0.14, 0.78));
        kdTree.insert(new Point2D(0.15, -0.12));
        StdOut.println(kdTree.contains(new Point2D(0.15, -0.12)));
        StdOut.println(kdTree.contains(new Point2D(0.2, -0.3)));
    }
}
