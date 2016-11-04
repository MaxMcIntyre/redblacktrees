/**
 * PREAMBLE
 *
 * I again implemented a Red-Black tree in FollowerStore, for speed and scalability.
 *
 * However, this time I implemented only one tree: a tree of Users ordered by UserID.
 * The node overhead this time includes an integer array of followers and followees.
 * It also contains two arrays of dates that represent when follows occured.
 *
 * When one user follows another, the relationship is stored twice. First, the uid of the
 * follower is stored in the follower array of the node of the followee. Second, the uid
 * of the followee is stored in the followee array of the follower. This still keeps O(n)
 * memory, despite the fact that everything is stored twice.
 * I justify doing this because it speeds up the getFollowers and getFollows methods to
 * lograithmic time.
 *
 * Again, I order the followers and followees by quickSort, which is of O(nlogn) runtime.
 * This is slow, but I deemed it to be acceptable since it is unrealistic that the number of 
 * followees often exceed 10,000.
 *
 * However, between two follows, the order is stored and retrieval of both the followers and
 * followees arrays of a User can occur in constant time once the User is found.
 *
 * I made it that users could not follow themselves, because I regarded such a relationship
 * as useless since a User has already access to his/her own weets.
 *
 * My methods with worst complexity are getMutualFollowers and getMutualFollows, with O(n^2)
 * time, as I had to find the union of the two sets of followers/followees and then order them
 * by latest Date. This really was just because I did not know of a method to do it any 
 * quicker, and I decided that since mutual followers or followees was a relatively
 * unorthodox thing to ask for, it wasn't worth the extra complexity.
 *
 * @author: u1504360
 */

package uk.ac.warwick.java.cs126.services;

import uk.ac.warwick.java.cs126.models.Weet;
import uk.ac.warwick.java.cs126.models.User;

import java.util.Date;


public class FollowerStore implements IFollowerStore {

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
     * This time, I make a node for users instead. I make one single tree.
     */
    private class UserNode {

        /*
         * These variables are self-explanatory.
         * • the user's id is the key of the node.
         * • followers, when they followed, and the amount
         * • follows, when they got followed, and the amount
         * • a boolean to store whether or not the order is updated
         * • the colour and side of the node
         * • the nodes connected to this one
         */
        private int userid;
        private int[] followers;
        private Date[] followerDates;
        private int number_of_followers;
        private int[] follows;
        private Date[] followDates;
        private int number_of_follows;
        private boolean ordersUpdated;
        private C colour;
        private S side;
        private UserNode parent, left, right, sibling;

        /*
         * This is the constructor for the root node. A root node is coloured black by default
         * It has no parent or sibling.
         * some variables are initialised to make the methods error-proof.
         */
        public UserNode(int id) {
            userid = id;
            colour = C.BLACK;
            parent = null; left = null; right = null; sibling = null;
            ordersUpdated = true;
            number_of_followers = 0;
            number_of_follows = 0;
            followers = new int[0];
            follows = new int[0];
        }
        /*
         * ..constructor for a non-root node. Coloured red by default.
         * Has a parent and sibling.
         */
        public UserNode(int id, UserNode p) {
            userid = id;
            colour = C.RED;
            parent = p; left = null; right = null; sibling = getSibling();
            ordersUpdated = true;
            number_of_followers = 0;
            number_of_follows = 0;
            followers = new int[0];
            follows = new int[0];
        }

        // returns the id, which is the de facto key of the node
        public int getId() {
            return userid;
        }
        // returns a boolean value which says whether or not the array of followers 
        // and followees are updated in terms of their order
        public boolean ordersUpdated() {
            return ordersUpdated;
        }
        // this returns the dates on which the followers first followed the user
        public Date[] getFollowerDates() {
            return followerDates;
        }
        // this returns the dates on which the followees were first followed by the user
        public Date[] getFollowDates() {
            return followDates;
        }
        // this returns the array of followers' ids
        public int[] getFollowers() { 
            return followers;
        }
        // this returns the array of followees' ids
        public int[] getFollows() { 
            return follows;
        }
        // this returns the amount of followers
        public int getNumberOfFollowers() {
            return number_of_followers;
        }
        // this allows a follower to be added externally to the node
        // it essentially turns the array into a de facto arraylist
        // every time a new follower is added, the array is expanded, but not sorted
        // temporary arrays followers2 and followerDates2 are set up to keep the contents
        public void addFollower(int uid, Date d) {
            int[] followers2 = followers;
            Date[] followerDates2 = followerDates;
            number_of_followers++;
            followers = new int[number_of_followers];
            followerDates = new Date[number_of_followers];
            followers[number_of_followers-1] = uid;
            followerDates[number_of_followers-1] = d;
            for (int i = 0; i < (number_of_followers-1); i++) {
                followers[i] = followers2[i];
                followerDates[i] = followerDates2[i];
            }
            ordersUpdated = false;
        }
        // this method does the same as the above, just for followees
        public boolean addFollow(int uid, Date d) {
            for (int i = 0; i < number_of_follows; i++) {
                if (follows[i]==uid) {return false;}
            }
            int[] follows2 = follows;
            Date[] followDates2 = followDates;
            number_of_follows++;
            follows = new int[number_of_follows];
            followDates = new Date[number_of_follows];
            follows[number_of_follows-1] = uid;
            followDates[number_of_follows-1] = d;
            for (int i = 0; i < (number_of_follows-1); i++) {
                follows[i] = follows2[i];
                followDates[i] = followDates2[i];
            }
            follows2 = null;
            return true;
        }
        // this method call for a both the followers array and the follows array to be
        // sorted by a global quicksort method
        public void prepare() {
            if (number_of_followers!=0) {
                this.quickSortByDate(followers, followerDates,0,number_of_followers-1);
            }
            if (number_of_follows!=0) {
                this.quickSortByDate(follows, followDates,0,number_of_follows-1);
            }
            ordersUpdated = true;
        }
        // Returns the parent node, which is null only if the node is the root node of a tree
        public UserNode getParent() { 
            return parent;
        }
        // Returns the node on the left (can be a null leaf)
        public UserNode getLeft() { 
            return left;
        }
        // Returns the node on the right (can be a null leaf)
        public UserNode getRight() {
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
        public UserNode getSibling() {
            updateSibling(); return sibling;
        }
        // Updates the sibling of the node, by getting the other child of the parent
        public void updateSibling() {
            if (parent == null) {sibling = null;}
            else if (this == parent.getLeft()) {sibling = parent.getRight();}
            else {sibling = parent.getLeft();}
        }
        // Allows for exterior updating of the parent
        public void setParent(UserNode n) {
            parent = n;
        }
        // Allows for exterior updating of the child, by taking side as a variable
        public void setChild(UserNode n, S s) {
            if (s == S.LEFT) {setLeft(n);}
            if (s == S.RIGHT) {setRight(n);}
        }
        // Allows for exterior updating of the left node
        public void setLeft(UserNode n) {
            left = n;
        }
        // Allows for exterior updating of the right node
        public void setRight(UserNode n) {
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
        // due to SOLID programming guidelines, I had to copy out the 
        // quickSortByDate method here, to make the class independent
        private void quickSortByDate(int[] a, Date[] basedUpon, int l, int h) {
            if (a == null || a.length == 0) {return;}
            if (l >= h) {return;}
            int centre = ((h-l)/2)+l, i = l, j = h;
            Date pivot = basedUpon[centre];
            while (i <= j) {
                while (basedUpon[i].after(pivot)) {i++;}
                while (basedUpon[j].before(pivot)) {j--;}
                if (i <= j) {
                    Date d = basedUpon[i]; int u = a[i];
                    basedUpon[i] = basedUpon[j]; a[i] = a[j];
                    basedUpon[j] = d; a[j] = u;
                    i++; j--;
                }
            }
            if (l < j) {quickSortByDate(a, basedUpon, l, j);}
            if (h > i) {quickSortByDate(a, basedUpon, i, h);}
        }
    }

    /*
     * GLOBAL VARIABLES
     * root – root node of the red black tree
     * total_number_of_users – amount of nodes in the tree so far
     * topUsers – array of Users by order of the followers
     * topUserCount – array of number of followers by user
     * topUsersUpdated – boolean variable to verify whether or not array needs updating
     * iterator – global iterator variable do I don't need to declare one when I do
     * an inOrder traversal
     * emptyArray – an empty array I can return so I don't have to make a new one
     * when I need to return it in my methods
     */
    private UserNode root;
    private int total_number_of_users;
    private boolean ordersUpdated;
    private int[] topUsers;
    private int[] topUserCount;
    private boolean topUsersUpdated;
    private int iterator;
    private int[] emptyArray;

    /*
     * CONSTRUCTOR
     * some of the global variables are initialised to make some methods error-proof
     */
    public FollowerStore() {
        root = null;
        total_number_of_users = 0;
        topUsersUpdated = false;
        topUsers = new int[0];
        iterator = 0;
    }

    /* HERE BEGIN THE PUBLIC METHODS–THOSE THAT IMPLEMENT WHATS INHERITED BY IFOLLOWERSTORE */

    /*
     * ADDFOLLOWER – O(log n) [finding of a position in a balanced binary tree]
     * 
     * If the root node is null, create nodes for the users and assign them to the roots.
     * Otherwise, use the private insert method to place them in the tree accordingly.
     * If one of the users doesn't already exist, add him in.
     * then add uid2 as a followee on the uid1 node
     * if he already exists, return false
     * otherwise, go ahead and add uid2 if he doesn't already exist
     * and add uid1 as his follower
     */
    public boolean addFollower(int uid1, int uid2, Date followDate) {
        if (uid1 == uid2) {return false;}
        if (root == null) {
            root = new UserNode(uid1); insert(uid2);
            root.addFollow(uid2, followDate);
            getUser(uid2).addFollower(uid1, followDate);
            total_number_of_users++;
            return true;
        }
        else {
            UserNode u1 = getUser(uid1);
            UserNode u2 = getUser(uid2);
            if (u1 == null) {insert(uid1); getUser(uid1).addFollow(uid2, followDate);}
            else if (!u1.addFollow(uid2, followDate)) {return false;}
            if (u2 == null) {insert(uid2); getUser(uid2).addFollower(uid1, followDate);}
            else {u2.addFollower(uid1, followDate);}
            return true;
        }
    }

    /*
     * GETFOLLOWERS – O(log n) [finding of a position in a balanced binary tree]
     * 
     * If the user doesn't exist, return an empty array
     * if the order of followers of a user are not sorted by date, sort them
     * return the array of followers
     */
    public int[] getFollowers(int uid) {
        UserNode n = getUser(uid);
        if (n == null) {return emptyArray;}
        if (!n.ordersUpdated()) {n.prepare();}
        return n.getFollowers();
    }

    /*
     * GETFOLLOWERS – O(log n) [finding of a position in a balanced binary tree]
     * 
     * If the user doesn't exist, return an empty array
     * if the order of followers of a user are not sorted by date, sort them
     * return the array of followers
     */
    public int[] getFollows(int uid) {
        UserNode n = getUser(uid);
        if (n == null) {return emptyArray;}
        if (!n.ordersUpdated()) {n.prepare();}
        return n.getFollows();
    }

    /*
     * ISAFOLLOWER – O(log n) [finding of a position in a balanced binary tree]
     * 
     * If the followee doesn't exist, return false
     * otherwise, go through his list of followers
     * if the follower is there return true
     * otherwise, return false
     */
    public boolean isAFollower(int uidFollower, int uidFollows) {
        UserNode n = getUser(uidFollows);
        if (n == null) {return false;}
        int[] hisFollowers = n.getFollowers();
        for (int num : hisFollowers) {
            if (num == uidFollower) {return true;}
        }
        return false;
    }

    /*
     * GETNUMFOLLOWERS – O(log n) [finding of a position in a balanced binary tree]
     * 
     * If the followee doesn't exist, return 0, because there have been no recorded
     * followers for that user, since he/she hasn't been added to the tree, but may exist
     * (that's possible but only if the user doesn't follow any one else either and so
     * doesn't properly exist in witter yet)
     * I decided to return 0 instead of -1, because the user might still exist
     * 
     * otherwise, just obtain the number of followers conventionally, using 
     * the getNumberOfFollowers method
     */
    public int getNumFollowers(int uid) {
        UserNode n = getUser(uid);
        if (n == null) {return 0;}
        return n.getNumberOfFollowers();
    }

    /*
     * GETMUTUALFOLLOWERS – O(n^2) [finding the union set of two arrays]
     * 
     * first check if the users exist
     * this method takes two arrays: followers of uid1 and those of uid2
     * it also takes the dates of when the users where first followed, in two arrays
     * after that, it uses two for-loops to go check if any of the followers are the same
     * when they are, they are added to an array.
     * The arrays of dates are also taken and the later date of follow for a mutual follow
     * is stored. Then the array of users is quicksorted by date.
     */
    public int[] getMutualFollowers(int uid1, int uid2) {
        UserNode n1 = getUser(uid1), n2 = getUser(uid2);
        if (n1 == null || n2 == null) {return emptyArray;}
        int[] followers1 = n1.getFollowers();
        int[] followers2 = n2.getFollowers();
        if (followers1.length == 0 || followers2.length == 0) {return emptyArray;}
        Date[] followerDates1 = n1.getFollowerDates();
        Date[] followerDates2 = n2.getFollowerDates(); 
        int l = followers1.length, follower1, follower2;
        Date[] saveDates = new Date[l];
        int[] save = new int[l];
        Date date1, date2, laterDate;
        int total = 0;
        for (int i = 0; i < l; i++) {
            follower1 = followers1[i];
            for (int j = 0; j < followers2.length; j++) {
                follower2 = followers2[j];
                if (follower1==follower2) {
                    save[total] = follower1;
                    date1 = followerDates1[i];
                    date2 = followerDates2[j];
                    if (date1.after(date2)) {laterDate = date1;}
                    else {laterDate = date2;}
                    saveDates[total] = laterDate;
                    total++; break;
                }
            }
        }
        int[] finalList = new int[total];
        Date[] finalDates = new Date[total];
        for (int k = 0; k < total; k++) {
            finalList[k] = save[k];
            finalDates[k] = saveDates[k];
        }
        quickSortByDate(finalList,finalDates,0,total-1);
        return finalList;
    }

    /*
     * GETMUTUALFOLLOWS – O(n^2) [finding the union set of two arrays]
     * 
     * This is exactly the same method as above, just for follows instead of followers.
     */
    public int[] getMutualFollows(int uid1, int uid2) {
        UserNode n1 = getUser(uid1), n2 = getUser(uid2);
        if (n1 == null || n2 == null) {return emptyArray;}
        int[] follows1 = n1.getFollows();
        int[] follows2 = n2.getFollows();
        if (follows1.length == 0 | follows2.length == 0) {return emptyArray;}
        Date[] followDates1 = n1.getFollowDates();
        Date[] followDates2 = n2.getFollowDates(); 
        int l = follows1.length;
        Date[] saveDates = new Date[l];
        int[] save = new int[l];
        Date date1, date2, laterDate;
        int total = 0;
        for (int i = 0; i < l; i++) {
            int follow1 = follows1[i];
            for (int j = 0; j < follows2.length; j++) {
                int follow2 = follows2[j];
                if (follow1==follow2) {
                    save[total] = follow1;
                    date1 = followDates1[i];
                    date2 = followDates2[j];
                    if (date1.after(date2)) {laterDate = date1;}
                    else {laterDate = date2;}
                    saveDates[total] = laterDate;
                    total++; break;
                }
            }
        }
        int[] finalList = new int[total];
        Date[] finalDates = new Date[total];
        for (int k = 0; k < total; k++) {
            finalList[k] = save[k];
            finalDates[k] = saveDates[k];
        }
        quickSortByDate(finalList,finalDates,0,total-1);
        return finalList;
    }

    /*
     * GETTOPUSERS – O(n), since an entire array needs to be (re)created 
     * Also O(1) 
     */
    public int[] getTopUsers() {
        if (!ordersUpdated) {updateTopUsers();}
        return topUsers;
    }

    /* HERE BEGIN PRIVATE METHODS THAT HELP THE PUBLIC METHODS */

    // This method traverses the tree according to the given user id, using a pointer
    // It compares the given weetid to to that of the pointer and moves accordingly left/right
    // If the user id already exists, it returns false
    // If the pointer is null, that it where the node containing this new user should be
    // and so it is declared there, before it is then declared in the order by date tree
    // Some extra node checks and global variable resets occur before it then returns true.
    private void insert(int uid) {
        UserNode parent = null;
        UserNode pointer = root;
        S side = null;
        while (pointer!=null) {
            int j = pointer.getId();
            if (uid > j) {
                parent = pointer; pointer = pointer.getRight();
                side = S.RIGHT;
            }
            else if (uid < j) {
                parent = pointer; pointer = pointer.getLeft();
                side = S.LEFT;
            }
            else if (uid == j) {
                System.err.println("We have a serious problem here.");
            }
        }
        UserNode u = new UserNode(uid, parent);
        if (parent!=null) {parent.setChild(u, side);}
        checkNode(u);
        total_number_of_users++;
        checkRoot();
        ordersUpdated = false;
    }

    // this is just a recursive quicksort method I made (I adapted from my Weetstore one)
    // it sorts array 'a' by date using array basedUpon
    private void quickSortByDate(int[] a, Date[] basedUpon, int l, int h) {
        if (a == null || a.length == 0) {return;}
        if (l >= h) {return;}
        int centre = ((h-l)/2)+l, i = l, j = h;
        Date pivot = basedUpon[centre];
        while (i <= j) {
            while (basedUpon[i].after(pivot)) {i++;}
            while (basedUpon[j].before(pivot)) {j--;}
            if (i <= j) {
                Date d = basedUpon[i]; int u = a[i];
                basedUpon[i] = basedUpon[j]; a[i] = a[j];
                basedUpon[j] = d; a[j] = u;
                i++; j--;
            }
        }
        if (l < j) {quickSortByDate(a, basedUpon, l, j);}
        if (h > i) {quickSortByDate(a, basedUpon, i, h);}
    }

    // this one sorts array 'id' by using array basedUpon
    private void quickSortByInt(int[] id, int[] basedUpon, int l, int h) {
        if (basedUpon == null || basedUpon.length == 0) {return;}
        if (l >= h) {return;}
        int centre = ((h-l)/2)+l, pivot = basedUpon[centre], i = l, j = h;
        while (i <= j) {
            while (basedUpon[i] > pivot) {i++;}
            while (basedUpon[j] < pivot) {j--;}
            if (i <= j) {
                int temp = basedUpon[i]; int temp2 = id[i];
                basedUpon[i] = basedUpon[j]; id[i] = id[j];
                basedUpon[j] = temp; id[j] = temp2;
                i++; j--;
            }
        }
        if (l < j) {quickSortByInt(id, basedUpon, l, j);}
        if (h > i) {quickSortByInt(id, basedUpon, i, h);}
    }

    private UserNode getUser(int uid) {
        UserNode pointer = root;
        int pointerid;
        while (pointer != null) {
            pointerid = pointer.getId();
            if (uid == pointerid) {return pointer;}
            else if (uid > pointerid) {pointer = pointer.getRight();}
            else if (uid < pointerid) {pointer = pointer.getLeft();}
        }
        return null;
    }

    // UPDATETOPUSERS
    // make an inOrder traversal of the tree and store everything in topUser
    // order the tree with a quicksort
    private void updateTopUsers() {
        topUsers = new int[total_number_of_users];
        topUserCount = new int[total_number_of_users];
        inOrder(root);
        quickSortByInt(topUsers,topUserCount,0,total_number_of_users-1);
        ordersUpdated = true;
        iterator = 0;
    }

    // INORDER
    // basic inorder traverser of the Users tree, storing everything in topUsers[]
    // also store the amount of followers of each user in topUserCount
    private void inOrder(UserNode n) {
        if (n!=null) {
            inOrder(n.getRight());
            topUsers[iterator] = n.getId();
            topUserCount[iterator] = n.getNumberOfFollowers();
            iterator++;
            inOrder(n.getLeft());
        }
    }

    /* HERE BEGIN PRIVATE METHODS THAT DIRECTLY INTERACT WITH THE TREE */

    // this ensures that the root node of the tree is updated and coloured black
    private void checkRoot() {
        while (root.getParent()!=null) {root = root.getParent();} root.makeBlack();
    }

    // this method checks whether or not a recolouring/restructuring of the tree is required
    // if the node is red and the parent's node is red:
    //      if the uncle node is black or null, restructure the tree
    //      if the uncle node is red, recolour the tree
    private void checkNode(UserNode u) {
        if (u.doubleRed()) {
            UserNode uncle = u.getParent().getSibling();
            if (uncle == null || uncle.isBlack()) {restructure(u);}
            else if (uncle.isRed()) {recolour(u);}
        }
    }

    // recolouring is a basic process:
    // • colour the parent node black
    // • colour the uncle node black
    // • colour the grandparent red
    // • check if the grandparent requires recolouring/restructuring
    private void recolour(UserNode n) {
        UserNode parent = n.getParent();
        UserNode grandparent = parent.getParent();
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
    private void restructure(UserNode n) {
        S parentSide = n.getParent().getSide();
        S childSide = n.getSide();
        if (parentSide == childSide) {simpleRotate(n, parentSide);}
        else {doubleRotate(n, parentSide);}
    }

    // http://cs.lmu.edu/~ray/notes/redblacktrees/
    //         (C)
    //        /              (B)
    //     (B)      ->      /   \
    //    /               (A)   (C)
    // (A)
    // That's the structure – we then rotate it to put (B) on top as parent of (A) and (C)
    // and for (C) to inherit the right (or left) leaf of (B)
    private void simpleRotate(UserNode n, S parentSide) {
        UserNode a, b, c; a = n; b = a.getParent(); c = b.getParent();
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
    private void doubleRotate(UserNode n, S parentSide) {
        UserNode a, b, c; b = n; a = b.getParent(); c = a.getParent();
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