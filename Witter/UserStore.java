/**
 * PREAMBLE
 *
 * In this program, I implemented a Red-Black tree as a data structure for organising users.
 * I chose a Red-Black tree for all of my structures for two reasons. The first is speed. A 
 * balanced binary tree allows for the crucial insert methods and retrieval methods to
 * occur at logn time. My second reason was scalability. Unlike a hashmap, a binary tree
 * does not need to be resized.
 * 
 * In order to obtain a worst case O(log n) time complexity for my getUser method, as well
 * as a way of keeping users stored in order or date for my getUsers method, I implemented
 * two trees rather than one. One of them was ordered by user id and the other by date.
 *
 * I used generics so that I would only have to write one Tree Node class, which could 
 * be ordered by two Comparables: Integer and Date. I could use either as keys for my nodes.
 *
 * This also allowed users to be inserted very quickly, at an O(log n) time. The biggest
 * disadvantage is that it takes just under double the amount of memory required. I decided
 * that this was necessary, in order to avoid an O(n) insert time, lest n be very large.
 * 
 * Whenever getUsers() is called, the array it returns is stored as a cache. This makes it so
 * that getUsers() can run in constant time between two inserts and avoid a re-traversal of
 * the tree every time the list of users is needed. This is very practical and realistic
 * because it prevents a series of consecutive O(n) times for the same result.
 *
 * @author: u1504360
 */

package uk.ac.warwick.java.cs126.services;
import uk.ac.warwick.java.cs126.models.User;
import java.util.Date;

public class UserStore implements IUserStore {

    /*
     * Since we are going to be implementing a Red-Black tree, the following enum types
     * will certainly find themselves useful.
     * C means colour, and the colour of a tree node is either red or black.
     * S means side, and a tree node is either on the right of it's parent or on the left.
     * S is null when the node is root.
     */
    private enum C {RED, BLACK};
    private enum S {RIGHT, LEFT};

    /*
     * The Node we will be using can either have an Integer or Date as its key, which is
     * why I found it important to make K implement the Comparable interface.
     */
    private class Node<V extends Object, K extends Comparable<K>> {
        /* 
         * This is the node overhead – values always stored by the node.
         * In this case there are 8 variables. 
         * • two generics
         * • two enum types
         * • four pointers to related nodes
         */
        private K key;
        private V value;
        private C colour;
        private S side;
        private Node<V,K> parent, left, right, sibling;

        /*
         * The first constructor you see below is for when root is declared, where parent
         * isn't defined as a parameter and is set to null.
         * A root node is black by default.
         */
        public Node(V v, K k) {
            value = v;
            key = k;
            colour = C.BLACK;
            parent = null; left = null; right = null; sibling = null;
        }
        /*
         * The second constructor you see below is for all non-root nodes. since the node does
         * have a parent, we can therefore obtain its sibling using the getSibling method.
         * A non-root node is red by default.
         */
        public Node(V v, K k, Node<V,K> p) {
            value = v;
            key = k;
            colour = C.RED;
            parent = p; left = null; right = null; sibling = getSibling();
        }

        // Returns the Key, which is either a Date or an Integer
        public K getKey() { 
            return key;
        }
        // Returns the Value, which in this case is always a User
        public V getValue() { 
            return value;
        }
        // Returns the parent node, which is null only if the node is the root node of a tree
        public Node<V,K> getParent() { 
            return parent;
        }
        // Returns the node on the right (can be a null leaf)
        public Node<V,K> getLeft() { 
            return left;
        }
        // Returns the node on the left (can be a null leaf)
        public Node<V,K> getRight() {
            return right;
        }
        // Returns the side that the node is on after calling updateSide
        public S getSide() {
            updateSide(); return side;
        }
        // Updates the side which the node is on, relative to its parent
        public void updateSide() {
            if (parent == null) {side = null;}
            else if (this == parent.getLeft()) {side = S.LEFT;}
            else {side = S.RIGHT;}
        }
        // Returns the sibling of this node (can be null) after calling updateSibling
        public Node<V,K> getSibling() {
            updateSibling(); return sibling;
        }
        // Updates the sibling of the node, by getting the other child of the parent
        public void updateSibling() {
            if (parent == null) {sibling = null;}
            else if (this == parent.getLeft()) {sibling = parent.getRight();}
            else {sibling = parent.getLeft();}
        }
        // Allows for exterior updating of the parent
        public void setParent(Node<V,K> n) {
            parent = n;
        }
        // Allows for exterior updating of the child, by taking side as a variable
        public void setChild(Node<V,K> n, S s) {
            if (s == S.LEFT) {setLeft(n);}
            if (s == S.RIGHT) {setRight(n);}
        }
        // Allows for exterior updating of the left node
        public void setLeft(Node<V,K> n) {
            left = n;
        }
        // Allows for exterior updating of the right node
        public void setRight(Node<V,K> n) {
            right = n;
        }
        // Allows for checking if the colour of the node is red
        public boolean isRed() {
            return (colour == C.RED);
        }
        // Allows for checking if the colour of the node is black
        public boolean isBlack() {
            return (colour == C.BLACK);
        }
        // Allows for checking if the colour of the parent node is red
        public boolean redParent() {
            return (getParent().isRed());
        }
        // Allows for checking if both the node and its parent are of red colour
        public boolean doubleRed() {
            return (isRed() && redParent());
        }
        // allows for colouring the node red
        public void makeRed() {
            colour = C.RED;
        }
        // allows for colouring the node black
        public void makeBlack() {
            colour = C.BLACK;
        }
    }

    /*
     * These are the global variables required by the UserStore. 
     *
     * First, there are two nodes:
     * a root node for a binary tree that orders by date
     * a root node for a binary tree that orders by user id
     * 
     * Second, I define an array variable that will always be empty, so that I can
     * return it when I need to return an empty array of users.
     *
     * Third, I define a CACHE array of all users stored in the database, sorted by date
     * joined. Most recently joined Users come first. This will only be updated when
     * necessary.
     * 
     * Fourth is a boolean variable that holds whether or not the allUsersByDate array is up
     * to date. This is declared false after any new User is added, and true when the list is
     * updated in a specified method.
     * 
     * Fifth is an integer holding the total amount of users stored in the red-black tree.
     * And finally I define an iterator such that one must not be re-created every time
     * the program runs through 
     */
    private Node<User,Date> rootDA;
    private Node<User,Integer> rootID;
    private User[] emptyUserArray;
    private User[] allUsersByDate;
    private boolean dateListUpdated;
    private int total_number_of_users;
    private int iterator;

    /*
     * In the constructor, a number of the global variables are defined, such that all of the
     * publicly implemented methods of UserStore can run and work when the Red-Black tree is
     * empty.
     */
    public UserStore() {
        rootDA = null;
        rootID = null;
        total_number_of_users = 0;
        dateListUpdated = true;
        emptyUserArray = new User[0];
        allUsersByDate = new User[0];
    }

    /* HERE BEGIN THE PUBLIC METHODS – THOSE THAT IMPLEMENT WHATS INHERITED BY IUSERSTORE */

    /*
     * ADDUSER – O(log n) since it's just a binary tree traversal
     *
     * If the root nodes are null, create nodes for the user and assign them to the roots.
     * Otherwise, use the private insertID method to place them in the tree accordingly.
     */
    public boolean addUser(User usr) {
        if (rootDA == null) {
            rootDA = new Node<User,Date>(usr, usr.getDateJoined());
            rootID = new Node<User,Integer>(usr, new Integer(usr.getId()));
            dateListUpdated = false;
            total_number_of_users++;
            return true;
        }
        else {return insertID(usr);}
    }

    /*
     * GETUSER – O(log n) since it's just a binary tree traversal
     *
     * Traverse the rootID tree (sorted by userid) like this:
     * – if the uids are equal, that's the one you want, so return it
     * – if the parameter uid is larger, go right
     * – otherwise go left
     * – if the pointer is null, the id doesn't exist, so exit the loop and return null
     */
    public User getUser(int uid) {
        Node<User,Integer> pointer = rootID;
        Integer i = new Integer(uid), j;
        while (pointer!=null) {
            j = pointer.getKey();
            if (i.compareTo(j)==0) {return pointer.getValue();}
            else if (i.compareTo(j)>0) {pointer = pointer.getRight();}
            else if (i.compareTo(j)<0) {pointer = pointer.getLeft();}
        }
        return null;
    }

    /*
     * GETUSERS – O(n) since a full inOrder traversal of the tree is required to return O(n)
     * amount of data.
     * However, if the method has already been called before, and no extra users have been
     * added since, the CACHED array is returned and the time complexity becomes O(1).
     * 
     * Return the output of the getList method, which traverses the tree and makes
     * an updated array of Users (no traversal if the allUsersByDate array is already updated)
     */
    public User[] getUsers() {
        return getList();
    }

    /*
     * GETUSERSCONTAINING – O(n) since a full check of all users is required
     * 
     * Essentially, this method calls the getList method, which returns an array of all users
     * sorted by date joined.
     * getUsersContaining then uses a for loop to mark all of the positions in the array that
     * have usernames that contain the query. These are all copied out into a final array
     * which is then returned.
     */
    public User[] getUsersContaining(String query) {
        if (total_number_of_users==0) {return emptyUserArray;}
        User[] userList = getList();
        int totalContaining = 0;
        query = query.toLowerCase();
        int[] markerList = new int[total_number_of_users];
        for (int i = 0; i < total_number_of_users; i++) {
            if (userList[i].getName().toLowerCase().contains(query)) {
                markerList[totalContaining] = i;
                totalContaining++;
            }
        }
        if (totalContaining == 0) {return emptyUserArray;}
        User[] finalList = new User[totalContaining];
        for (int j = 0; j < totalContaining; j++) {
            finalList[j] = userList[markerList[j]];
        }
        return finalList;
    }

    /*
     * GETUSERSJOINEDBEFORE – O(n), as traversal of an array containing all users is required
     * 
     * This method calls the getList method, which returns an array of all users
     * sorted by date joined, descending.
     * getUsersJoinedBefore then uses a for loop to find the first position where the user
     * joined after the given date. It cuts the array there and copies the following subarray
     * into a new array, which it then returns.
     * The way this method cuts the array allows Users that joined on datebefore to be
     * included in the returned array. 
     */
    public User[] getUsersJoinedBefore(Date dateBefore) {
        if (total_number_of_users==0) {return emptyUserArray;}
        User[] userList = getList();
        int end = total_number_of_users-1;
        int copyUntil = 0;
        for (int i = end; i >= 0; i--) {
            if (userList[i].getDateJoined().after(dateBefore)) {copyUntil = ++i; break;}
        }
        if (copyUntil == 0) {return userList;}
        else if (copyUntil == total_number_of_users) {return emptyUserArray;}
        int size = total_number_of_users-copyUntil;
        User[] finalList = new User[size]; int k = 0;
        for (int j = copyUntil; j < total_number_of_users; j++) {
            finalList[k] = userList[j]; k++;
        }
        return finalList;
    }

    /* HERE BEGIN PRIVATE METHODS THAT ALLOW THE PUBLIC METHODS TO INTERACT WITH THE TREE */

    // GETLIST
    // if the allUsersByDate array is not updated, then update it by calling updateDAList
    private User[] getList() {
        if (dateListUpdated == false) {updateDAList();}
        return allUsersByDate;
    }

    // UPDATEDALIST
    // make an inOrder traversal of the tree and store everything in allUsersByDate
    private void updateDAList() {
        allUsersByDate = new User[total_number_of_users];
        iterator = 0;
        inOrder(rootDA);
        dateListUpdated = true;
    }
    
    // INORDER
    // basic inorder traverser of the Users by date tree, storing everything in allUsersByDate
    private void inOrder(Node<User,Date> n) {
        if (n!=null) {
            inOrder(n.getRight());
            allUsersByDate[iterator] = n.getValue(); iterator++;
            inOrder(n.getLeft());
        }
    }

    /* HERE BEGIN PRIVATE METHODS THAT DIRECTLY INTERACT WITH THE TREE */

    // this ensures that the root nodes of the tree are updated and coloured black
    private void checkRoot() {
        while (rootID.getParent()!=null) {rootID = rootID.getParent();} rootID.makeBlack();
        while (rootDA.getParent()!=null) {rootDA = rootDA.getParent();} rootDA.makeBlack();
    }

    // This method traverses the tree according to the given User id, using a pointer
    // It compares the given userid to to that of the pointer and moves accordingly left/right
    // If the user id already exists, it returns false
    // If the pointer is null, that it where the node containing this new user should be
    // and so it is declared there, before it is then declared in the order by date tree
    // Some extra node checks and global variable resets occur before it then returns true.
    private boolean insertID(User usr) {
        Integer i = new Integer(usr.getId());
        Node<User,Integer> parent = null;
        Node<User,Integer> pointer = rootID;
        S side = null;
        while (pointer!=null) {
            if (i.compareTo(pointer.getKey())>0) {
                parent = pointer;
                pointer = pointer.getRight();
                side = S.RIGHT;
            }
            else if (i.compareTo(pointer.getKey())<0) {
                parent = pointer;
                pointer = pointer.getLeft();
                side = S.LEFT;
            }
            else if (i.compareTo(pointer.getKey())==0) {
                return false;
            }
        }
        Node<User,Integer> u = new Node<User,Integer>(usr, i, parent);
        if (parent!=null) {parent.setChild(u, side);}
        checkNode(u);
        insertDA(usr);
        total_number_of_users++;
        dateListUpdated = false;
        checkRoot();
        return true;
    }

    // This method adds a user to the ordered by date tree.
    // This method works the same way as insertID, but since insertID has already checked
    // that the user doesn't already exists, it only find where to place the user and places.
    // Extra check are done, like in insertID.
    private void insertDA(User usr) {
        Date d = usr.getDateJoined();
        Node<User,Date> parent = null;
        Node<User,Date> pointer = rootDA;
        S side = null;
        while (pointer!=null) {
            if (d.before(pointer.getKey())) {
                parent = pointer;
                pointer = pointer.getLeft();
                side = S.LEFT;
            }
            else {
                parent = pointer;
                pointer = pointer.getRight();
                side = S.RIGHT;
            }
        }
        Node<User,Date> u = new Node<User,Date>(usr, d, parent);
        if (parent!=null) {parent.setChild(u, side);}
        checkNode(u);
    }

    /* This is where things get messy. */

    // this method checks whether or not a recolouring/restructuring of the tree is required
    // if the node is red and the parent's node is red:
    //      if the uncle node is black or null, restructure the tree
    //      if the uncle node is red, recolour the tree
    private <T extends Comparable<T>> void checkNode(Node<User,T> u) {
        if (u.doubleRed()) {
            Node<User,T> uncle = u.getParent().getSibling();
            if (uncle == null || uncle.isBlack()) {restructure(u);}
            else if (uncle.isRed()) {recolour(u);}
        }
    }

    // THIS IS A PROUDLY BRITISH PROGRAM – WE SPELL IT 'COLOUR'.
    // recolouring is a basic process:
    // • colour the parent node black
    // • colour the uncle node black
    // • colour the grandparent red
    // • check if the grandparent requires recolouring/restructuring
    private <T extends Comparable<T>> void recolour(Node<User,T> n) {
        Node<User,T> parent = n.getParent();
        Node<User,T> grandparent = parent.getParent();
        if (grandparent==null) {return;}
        if (grandparent.getParent()!=null) {
            grandparent.makeRed(); parent.makeBlack(); parent.getSibling().makeBlack();
            checkNode(grandparent);
        }
        else {
            grandparent.makeBlack(); parent.makeBlack(); parent.getSibling().makeBlack();
        }
    }

    // restructuring is a more complex process
    // if the parent and child are on the same side, a simple rotate is required
    // otherwise, a double rotate is required
    private <T extends Comparable<T>> void restructure(Node<User,T> n) {
        S parentSide = n.getParent().getSide();
        S childSide = n.getSide();
        if (parentSide == childSide) {simpleRotate(n, parentSide);}
        else {doubleRotate(n, parentSide);}
    }

    //         (C)
    //        /              (B)
    //     (B)      ->      /   \
    //    /               (A)   (C)
    // (A)
    // That's the structure – we then rotate it to put (B) on top as parent of (A) and (C)
    // and for (C) to inherit the right (or left) leaf of (B)
    private <T extends Comparable<T>> void simpleRotate(Node<User,T> n, S parentSide) {
        Node<User,T> a, b, c; a = n; b = a.getParent(); c = b.getParent();
        if (c.getParent()!=null) {c.getParent().setChild(b, c.getSide());}
        b.setParent(c.getParent()); c.setParent(b);
        if (parentSide == S.LEFT) {
            if (b.getRight()!=null) {b.getRight().setParent(c);}
            c.setLeft(b.getRight()); b.setRight(c);
        }
        else {
            if (b.getLeft()!=null) {b.getLeft().setParent(c);}
            c.setRight(b.getLeft()); b.setLeft(c);
        }
        b.makeBlack(); c.makeRed();
    }

    //     (C)     
    //    /                 (B)
    // (A)         ->      /   \
    //   \               (A)   (C)
    //   (B)        
    // We then rotate it to put (B) on top as parent of (A) and (C)
    // for (C) to inherit the right (or left) leaf of (B)
    // and for (A) to inherit the opposite node
    private <T extends Comparable<T>> void doubleRotate(Node<User,T> n, S parentSide) {
        Node<User,T> a, b, c; b = n; a = b.getParent(); c = a.getParent();
        if (c.getParent()!=null) {c.getParent().setChild(b, c.getSide());}
        b.setParent(c.getParent()); c.setParent(b); a.setParent(b);
        if (parentSide == S.LEFT) {
            if (b.getLeft()!=null) {b.getLeft().setParent(a);}
            a.setRight(b.getLeft());
            if (b.getRight()!=null) {b.getRight().setParent(c);}
            c.setLeft(b.getRight());
            b.setRight(c); b.setLeft(a);
        }
        else {
            if (b.getRight()!=null) {b.getRight().setParent(a);}
            a.setLeft(b.getRight());
            if (b.getLeft()!=null) {b.getLeft().setParent(c);}
            c.setRight(b.getLeft());
            b.setLeft(c); b.setRight(a);
        }
        b.makeBlack(); c.makeRed();
    }
}
