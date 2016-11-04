/**
 * PREAMBLE
 *
 * In this program, I make use of three Red-Black trees and a TrendCounter class to organise
 * the data.
 * My first tree is contains weets ordered by weetid.
 * My second tree contains weets ordered by date.
 * My third contains users ordered by userid.
 * 
 *      Whenever a weet is added, it is added in three places.
 * First in the weetid and the weetdate tree. (allows for log n inserts and retrievals).
 * Then, it is added to an array in its UserNode in the tree of users.
 * 
 * This allows both getWeet and getWeetsByUser to run in logarithmic time, which is the
 * main advantage of structure. I decided to do this, because I imagined that these would
 * be methods that get called very commonly in a real Witter application.
 * 
 * If there are any trends in the weet message, they are added to the TrendCount class.
 * There, the count is incremented if the trend exists or added and set to 1 otherwise.
 * 
 * Both the weets in the UserNode and the trends in the TrendCount instance are sorted 
 * using a quickSort algorithm (which is of O(nlogn) time). In the case of weets, by date
 * and in the case of TrendCount by occurance (which is an integer).
 * I decided to use quickSort first because it was easy to implement and second because
 * in spite of being unstable, it rarely approached a worst-case runtime.
 *
 * While three trees may appear to be a heavy use of memory, it does not surpass O(n).
 * And I deemed it necessary to be able to execute crucial methods at quick run times.
 *
 * @author: u1504360
 */

package uk.ac.warwick.java.cs126.services;

import uk.ac.warwick.java.cs126.models.User;
import uk.ac.warwick.java.cs126.models.Weet;

import java.io.BufferedReader;
import java.util.Date;
import java.io.FileReader;
import java.text.ParseException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class WeetStore implements IWeetStore {

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
         * This is extra overhead for the second type of Node I will be using (essentially 
         * a user node). It will store the user id, the number of weets weeted, all of those
         * weets, the dates associated with them and a boolean variable to check whether or
         * not the array is ordered.
         */
        private int uid;
        private int numberofweets;
        private Weet[] weets;
        private Date[] weetsDates;
        private boolean weetsOrdered;

        /*
         * The first constructor you see below is for the root node, where the parent
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
        /*
         * These constructors are for my second type of node, which is for users.
         * Again, the same rules apply, but now other variables are initiated. 
         * This one is for a root node, so by default black and without a parent node.
         */
        public Node(int i, boolean t) {
            uid = i;
            colour = C.BLACK;
            parent = null; left = null; right = null; sibling = null;
            weets = new Weet[0];
            weetsDates = new Date[0];
            weetsOrdered = true;
            numberofweets = 0;
        }
        /*
         * This one is for a non-root node, so it's coloured red and has a parent and sibling.
         */
        public Node(int i, Node<V,K> p, boolean t) {
            uid = i;
            colour = C.RED;
            parent = p; left = null; right = null; sibling = getSibling();
            weets = new Weet[0];
            weetsDates = new Date[0];
            weetsOrdered = true;
            numberofweets = 0;
        }

        // Returns the Key, which is either a Date or an Integer
        public K getKey() { 
            return key;
        }
        // Returns the Value, which in this case is always a User
        public V getValue() { 
            return value;
        }
        public int getUID() {
            return uid;
        }
        public void addWeetU(Weet w, Date d) {
            if (numberofweets == 0) {
                weets = new Weet[1]; weetsDates = new Date[1];
                weets[0] = w; weetsDates[0] = d; numberofweets++; return;
            }
            Weet[] weets2 = weets; Date[] weetsDates2 = weetsDates;
            numberofweets++;
            weets = new Weet[numberofweets]; weetsDates = new Date[numberofweets];
            weets[numberofweets-1] = w; weetsDates[numberofweets-1] = d;
            for (int i = 0; i < (numberofweets-1); i++) {
                weets[i] = weets2[i]; weetsDates[i] = weetsDates2[i];
            }
            weetsOrdered = false;
        }
        public Weet[] getWeetsU() {
            if (!weetsOrdered) {
                sortWeetsByDate(weets,weetsDates,0,weets.length-1);
                weetsOrdered = true;
            }
            return weets;
        }
        // Returns the parent node, which is null only if the node is the root node of a tree
        public Node<V,K> getParent() { 
            return parent;
        }
        // Returns the node on the left (can be a null leaf)
        public Node<V,K> getLeft() { 
            return left;
        }
        // Returns the node on the right (can be a null leaf)
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
     * I required a seperate class to get the getTrending method done. This class takes
     * all of the trends, counts them, and returns the top ten methods.
     */
    private class TrendCount {
        /*
         * About the overhead:
         * • trendshold is just holds the trends temporarily when trends are updated and the
         * trends array is changed
         * • the counts array holds the amount of times a trend is held
         * • size is the size of the arrays trends and counts
         * • topTenTrends represents the trends with the most occurances
         * • orderUpdated checks whether or not the order of the trends has been updated
         */
        private String[] trends, trendshold;
        private int[] counts, countshold;
        private int size, max;
        private String[] topTenTrends;
        private boolean orderUpdated;

        /*
         * The constructor initiates some of the global variables
         */
        public TrendCount() {
            trends = null;
            counts = null;
            size = 0;
            topTenTrends = new String[10];
            orderUpdated = true;
        }

        /*
         * This method is to insert a trend.
         * If trendposition returns -1, then the trend doesn't exist and it must be added.
         * Otherwise its value must be incremented.
         */
        public void insert(String t) {
            int location = trendPosition(t);
            orderUpdated = false;
            if (location == -1) {add(t);}
            else {counts[location]++;}
        }

        /*
         * trendPosition runs throught the array of trends and returns the position of 
         * the given trend if it exists. Otherwise, it returns -1.
         */
        private int trendPosition(String t) {
            if (trends != null) {
                for (int i = 0; i < size; i++) {
                    if (t.equals(trends[i])) {return i;}
                }
            }
            return -1;
        }

        /*
         * this add method simply add the given trend at the end of the trends array
         * it then sets its count frequency to one
         */
        private void add(String t) {
            size++;
            counts = new int[size];
            trends = new String[size];
            counts[size-1] = 1;
            trends[size-1] = t;
            if (countshold != null) {
                for (int i = 0; i < (size-1); i++) {
                    counts[i] = countshold[i];
                    trends[i] = trendshold[i];
                }
            }
            countshold = counts;
            trendshold = trends;
        }

        /*
         * this methods prepares the final trends list to be sent by
         * (i) calling for a quicksort of the array
         * (ii) taking the top ten of the result
         */
        public void prepare() {
            if (trends!=null) {
                quickSort(0,size-1);
                calcTopTen();
                orderUpdated = true;
            }
        }

        /*
         * this is just a recursive quickSort method I made
         * it sorts trends by sorting counts in descending order and returning the result
         * there's really nothing much to it..
         */
        public void quickSort(int l, int h) {
            if (counts == null || counts.length == 0) {return;}
            if (l >= h) {return;}
            int centre = ((h-l)/2)+l, pivot = counts[centre], i = l, j = h;
            while (i <= j) {
                while (counts[i] > pivot) {i++;}
                while (counts[j] < pivot) {j--;}
                if (i <= j) {
                    int temp = counts[i]; String temp2 = trends[i];
                    counts[i] = counts[j]; trends[i] = trends[j];
                    counts[j] = temp; trends[j] = temp2;
                    i++; j--;
                }
            }
            if (l < j) {quickSort(l, j);}
            if (h > i) {quickSort(i, h);}
        }

        /*
         * as a result of the descending order quickSort, the top ten trends are going to be
         * at the front, so they're copied out into topTenTrends
         */
        private void calcTopTen() {
            if (size < 10) {max = size;} else {max = 10;}
            for (int i = 0; i < max; i++) {
                topTenTrends[i] = trends[i];
            }
        }

        /*
         * This method returns the orderUpdated boolean variable
         */
        public boolean orderUpdated() {
            return orderUpdated;
        }

        /*
         * this method just returns the stored topTenTrends global variable
         */
        public String[] give10Trends() {
            return topTenTrends;
        }
    }

    /*
     * Three trees, three root nodes: 
     * • rootDA is for weets ordered by date
     * • rootID is for weets ordered by weet id
     * • root is for users ordered by ID (I know that's weird)
     * 
     * • emptyWeetArray holds nothing – it's just so that I can return it when I need to
     * • allWeetsByDate is a CACHE array that holds all of the weets by date
     * • dateListUpdated is a boolean that is used to check whether allWeetsByDate is updated
     * • total_number_of_weets holds the amount of weets in the tree so far
     * • iterator is used for inOrder counting – you'll see it below
     * • I made a date format that allows me to convert a date so that I can compare it to 
     * getPrettyDateWeeted()
     * • trends is an instance of TrendCount, the class I made above
     */
    private Node<Weet,Date> rootDA;
    private Node<Weet,Integer> rootID;
    private Node<Weet,Integer> root;
    private Weet[] emptyWeetArray;
    private Weet[] allWeetsByDate;
    private boolean dateListUpdated;
    private int total_number_of_weets;
    private int iterator;
    private DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    private TrendCount trends;

    /* As ever, the constructor initiates the necessary global variables. */
    public WeetStore() {
        rootDA = null;
        rootID = null;
        root = null;
        total_number_of_weets = 0;
        iterator = 0;
        dateListUpdated = true;
        trends = new TrendCount();
        emptyWeetArray = new Weet[0];
        allWeetsByDate = new Weet[0];
    }

    /* HERE BEGIN THE PUBLIC METHODS – THOSE THAT IMPLEMENT WHATS INHERITED BY IWEETSTORE */

    /*
     * ADDWEET – O(log n) since it's just a binary tree traversal
     *
     * If the root nodes are null, create nodes for the user and assign them to the roots.
     * Otherwise, use the private insertID method to place them in the tree accordingly.
     * Any trends in the weet are added using private method addTrends.
     */
    public boolean addWeet(Weet weet) {
        if (rootDA == null) {
            rootDA = new Node<Weet,Date>(weet, weet.getDateWeeted());
            rootID = new Node<Weet,Integer>(weet, new Integer(weet.getId()));
            root = new Node<Weet,Integer>(weet.getUserId(), true);
            root.addWeetU(weet, weet.getDateWeeted());
            dateListUpdated = false;
            total_number_of_weets++;
            addTrends(weet);
            return true;
        }
        else {return insertID(weet);}
    }
    
    /*
     * GETWEET – O(log n) since it's just a binary tree traversal
     *
     * Traverse the rootID tree (sorted by weet id) like this:
     * – if the uids are equal, that's the one you want, so return it
     * – if the parameter uid is larger, go right
     * – otherwise go left
     * – if the pointer is null, the id doesn't exist, so exit the loop and return null
     */
    public Weet getWeet(int wid) {
        Node<Weet,Integer> pointer = rootID;
        Integer i = new Integer(wid);
        while (pointer!=null) {
            if (i.compareTo(pointer.getKey())==0) {
                return pointer.getValue();
            }
            else if (i.compareTo(pointer.getKey())>0) {
                pointer = pointer.getRight();
            }
            else if (i.compareTo(pointer.getKey())<0) {
                pointer = pointer.getLeft();
            }
        }
        return null;
    }
    
    /*
     * GETWEETS – O(n) since a full inOrder traversal of the tree is required to return O(n)
     * amount of data.
     * However, if the method has already been called before, and no extra users have been
     * added since, the CACHED array is returned and the time complexity becomes O(1).
     * 
     * Returns the output of the getList method, which traverses the tree and makes
     * an updated array of Users (no traversal if the allWeetsByDate array is already updated)
     */
    public Weet[] getWeets() {
        return getList();
    }
    
    /*
     * GETWEETSBYUSER – O(log n) since it's just a binary tree traversal
     *
     * Traverse the root tree (a tree of users sorted by user id) like this:
     * – if the uids are equal, that's the one you want, so return it
     * – if the parameter uid is larger, go right
     * – otherwise go left
     * – if the pointer is null, the id doesn't exist: exit the loop and return emptyWeetArray
     */
    public Weet[] getWeetsByUser(User usr) {
        Node<Weet,Integer> pointer = root;
        int id = usr.getId();
        while (pointer!=null) {
            if (id > pointer.getUID()) {pointer = pointer.getRight();}
            else if (id < pointer.getUID()) {pointer = pointer.getLeft();}
            else {return pointer.getWeetsU();}
        }
        return emptyWeetArray;
    }

    /*
     * GETWEETSCONTAINING – O(n) since a full check of all weets is required
     * 
     * Essentially, this method calls the getList method, which returns an array of all weets
     * sorted by date joined.
     * getWeetsContaining then uses a for loop to mark all of the positions in the array that
     * have weet messages that contain the query. These are all copied out into a final array
     * (finalList) which is then returned.
     */
    public Weet[] getWeetsContaining(String query) {
        if (query.replaceAll("\\s","").equals("")) {return emptyWeetArray;}
        Weet[] weetList = getList();
        int totalContaining = 0;
        int[] markerList = new int[total_number_of_weets];
        query = query.toLowerCase();
        for (int i = 0; i < total_number_of_weets; i++) {
            if (weetList[i].getMessage().toLowerCase().contains(query)) {
                markerList[totalContaining] = i;
                totalContaining++;
            }
        }
        if (totalContaining == 0) {return emptyWeetArray;}
        Weet[] finalList = new Weet[totalContaining];
        for (int j = 0; j < totalContaining; j++) {
            finalList[j] = weetList[markerList[j]];
        }
        return finalList;
    }
    
    /*
     * GETWEETSON – O(n) since a full check of all weets is required
     * 
     * Again, this method calls the getList method, which returns an array of all weets
     * sorted by date joined.
     * getWeetsOn then uses a for loop to mark all of the positions in the array that
     * have usernames that contain the query. These are all copied out into a final array
     * (finalList) which is then returned.
     */
    public Weet[] getWeetsOn(Date dateOn) {
        String stringdate = format.format(dateOn);
        Weet[] weetList = getList();
        int totalOn = 0;
        int[] markerList = new int[total_number_of_weets];
        for (int i = 0; i < total_number_of_weets; i++) {
            if (weetList[i].getPrettyDateWeeted().equals(stringdate)) {
                markerList[totalOn] = i; totalOn++;
            }
        }
        if (totalOn == 0) {return emptyWeetArray;}
        Weet[] finalList = new Weet[totalOn];
        for (int j = 0; j < totalOn; j++) {
            finalList[j] = weetList[markerList[j]];
        }
        return finalList;
    }
    
    /*
     * GETWEETSBEFORE – O(n) since up to the full array of weets weeted must be copied
     * 
     * Again, this method calls the getList method, which returns an array of all weets
     * sorted by date joined.
     * getWeetsBefore then uses a for loop to mark until what point the array must be copied.
     * These are all copied out into a final array (finalList) which is then returned.
     */
    public Weet[] getWeetsBefore(Date dateBefore) {
        if (total_number_of_weets==0) {return emptyWeetArray;}
        Weet[] weetList = getList();
        int end = total_number_of_weets-1;
        int copyUntil = 0;
        for (int i = end; i >= 0; i--) {
            if (weetList[i].getDateWeeted().after(dateBefore)) {copyUntil = ++i; break;}
        }
        if (copyUntil == 0) {return weetList;}
        else if (copyUntil == total_number_of_weets) {return emptyWeetArray;}
        int size = total_number_of_weets-copyUntil;
        Weet[] finalList = new Weet[size]; int k = 0;
        for (int j = copyUntil; j < total_number_of_weets; j++) {
            finalList[k] = weetList[j]; k++;
        }
        return finalList;
    }

    /*
     * GETTRENDING – O(1) if the trends are sorted, O(nlogn) otherwise, because quicksort
     */
    public String[] getTrending() {
        if (trends.orderUpdated()) {return trends.give10Trends();}
        trends.prepare(); return trends.give10Trends();
    }

    // SORTWEETSBYDATE
    // this is a basic quicksort, copied and edited from my previous implementation
    private void sortWeetsByDate(Weet[] a, Date[] basedUpon, int l, int h) {
        if (a == null || a.length == 0) {return;}
        if (l >= h) {return;}
        int centre = ((h-l)/2)+l, i = l, j = h;
        Date pivot = basedUpon[centre];
        while (i <= j) {
            while (basedUpon[i].after(pivot)) {i++;}
            while (basedUpon[j].before(pivot)) {j--;}
            if (i <= j) {
                Date d = basedUpon[i]; Weet u = a[i];
                basedUpon[i] = basedUpon[j]; a[i] = a[j];
                basedUpon[j] = d; a[j] = u;
                i++; j--;
            }
        }
        if (l < j) {sortWeetsByDate(a, basedUpon, l, j);}
        if (h > i) {sortWeetsByDate(a, basedUpon, i, h);}
    }

    // ADDTRENDS
    // this method adds whatever trends are in 
    private void addTrends(Weet weet) {
        String[] weetsplit = weet.getMessage().split(" ");
        for (String word : weetsplit) {
            if (word.startsWith("#")) {
                trends.insert(word.toLowerCase());
            }
        }
    }

    // GETLIST
    // if the allWeetsByDate array is not updated, then update it by calling updateDAList
    private Weet[] getList() {
        if (dateListUpdated == false) {updateDAList();}
        return allWeetsByDate;
    }

    // UPDATEDALIST
    // make an inOrder traversal of the tree and store everything in allWeetsByDate
    private void updateDAList() {
        allWeetsByDate = new Weet[total_number_of_weets];
        inOrder(rootDA);
        iterator = 0;
        dateListUpdated = true;
    }
    
    // INORDER
    // basic inorder traverser of the Weets by date tree, storing everything in allWeetsByDate
    private void inOrder(Node<Weet,Date> n) {
        if (n!=null) {
            inOrder(n.getRight());
            allWeetsByDate[iterator] = n.getValue(); iterator++;
            inOrder(n.getLeft());
        }
    }

    /* HERE BEGIN PRIVATE METHODS THAT DIRECTLY INTERACT WITH THE TREE */

    // this ensures that the root nodes of the tree are updated and coloured black
    private void checkRoot() {
        while (rootID.getParent()!=null) {rootID = rootID.getParent();} rootID.makeBlack();
        while (rootDA.getParent()!=null) {rootDA = rootDA.getParent();} rootDA.makeBlack();
        while (root.getParent()!=null) {root = root.getParent();} root.makeBlack();
    }

    // This method traverses the tree according to the given weet id, using a pointer
    // It compares the given weetid to to that of the pointer and moves accordingly left/right
    // If the user id already exists, it returns false
    // If the pointer is null, that it where the node containing this new user should be
    // and so it is declared there, before it is then declared in the order by date tree
    // Some extra node checks and global variable resets occur before it then returns true.
    private boolean insertID(Weet weet) {
        Integer i = new Integer(weet.getId());
        Node<Weet,Integer> parent = null;
        Node<Weet,Integer> pointer = rootID;
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
        Node<Weet,Integer> u = new Node<Weet,Integer>(weet, i, parent);
        if (parent!=null) {parent.setChild(u, side);}
        checkNode(u);
        insertDA(weet);
        checkUser(weet.getUserId(), weet);
        total_number_of_weets++;
        dateListUpdated = false;
        checkRoot();
        addTrends(weet);
        return true;
    }

    // This method adds a weet to the ordered by date tree.
    // This method works the same way as insertID, but since insertID has already checked
    // that the user doesn't already exists, it only find where to place the user and places.
    // Extra checks are done, like in insertID.
    private void insertDA(Weet weet) {
        Date d = weet.getDateWeeted();
        Node<Weet,Date> parent = null;
        Node<Weet,Date> pointer = rootDA;
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
        Node<Weet,Date> u = new Node<Weet,Date>(weet, d, parent);
        if (parent!=null) {parent.setChild(u, side);}
        checkNode(u);
    }

    // This method adds a user to the ordered by id tree.
    // This method works the same way as insertID but it also adds the weet to the 
    // user node, whether or not the node already exists.
    // Extra checks are done, like in insertID.
    private void checkUser(int uid, Weet weet) {
        Node<Weet,Integer> parent = null;
        Node<Weet,Integer> pointer = root;
        S side = null;
        while (pointer!=null) {
            if (uid < pointer.getUID()) {
                parent = pointer;
                pointer = pointer.getLeft();
                side = S.LEFT;
            }
            else if (uid > pointer.getUID()) {
                parent = pointer;
                pointer = pointer.getRight();
                side = S.RIGHT;
            }
            else if (uid == pointer.getUID()) {
                pointer.addWeetU(weet, weet.getDateWeeted()); return;
            }
        }
        Node<Weet,Integer> u = new Node<Weet,Integer>(uid, parent, true);
        if (parent!=null) {parent.setChild(u, side);}
        u.addWeetU(weet, weet.getDateWeeted());
        checkNode(u);
    }

    // this method checks whether or not a recolouring/restructuring of the tree is required
    // if the node is red and the parent's node is red:
    //      if the uncle node is black or null, restructure the tree
    //      if the uncle node is red, recolour the tree
    private <T extends Comparable<T>> void checkNode(Node<Weet,T> u) {
        if (u.doubleRed()) {
            Node<Weet,T> uncle = u.getParent().getSibling();
            if (uncle == null || uncle.isBlack()) {restructure(u);}
            else if (uncle.isRed()) {recolour(u);}
        }
    }

    // recolouring is a basic process:
    // • colour the parent node black
    // • colour the uncle node black
    // • colour the grandparent red
    // • check if the grandparent requires recolouring/restructuring
    private <T extends Comparable<T>> void recolour(Node<Weet,T> n) {
        Node<Weet,T> parent = n.getParent();
        Node<Weet,T> grandparent = parent.getParent();
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
    private <T extends Comparable<T>> void restructure(Node<Weet,T> n) {
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
    // As you obser
    private <T extends Comparable<T>> void simpleRotate(Node<Weet,T> n, S parentSide) {
        Node<Weet,T> a, b, c; a = n; b = a.getParent(); c = b.getParent();
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
    private <T extends Comparable<T>> void doubleRotate(Node<Weet,T> n, S parentSide) {
        Node<Weet,T> a, b, c; b = n; a = b.getParent(); c = a.getParent();
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
