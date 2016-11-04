import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Arrays;

import uk.ac.warwick.java.cs126.services.IFollowerStore;
import uk.ac.warwick.java.cs126.services.FollowerStore;

class FollowTests {

    /*
     * Tests the ability to retrieve a weet
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetFollowersSuccess()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int uid1 = 1; int uid2 = 2;
        // Generate Weet to add
        followerStore.addFollower(uid1,uid2,createDate("02/11/2012 23:11"));

        int[] u1followers = followerStore.getFollowers(uid1);
        int[] u2followers = followerStore.getFollowers(uid2);
        
        // Check the return value for the expected result
        if (Arrays.equals(u1followers,new int[0]) && u2followers[0]==uid1) // We expect the Weet we get back to be the same as the one put in 
        {
            return true;
        }
        else 
        { 
            return false;
        }

    }

    /*
     * Tests what happens when an invalid Weet is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetFollowersFail()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int[] u1followers = followerStore.getFollowers(1);
        
        // Check the return value for the expected result
        if (Arrays.equals(u1followers,new int[0])) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }

    /*
     * Tests what happens when an invalid Weet is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean isAFollower()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int uid1 = 1; int uid2 = 2;
        // Generate Weet to add
        followerStore.addFollower(uid1,uid2,createDate("02/11/2012 23:11"));

        boolean is = followerStore.isAFollower(1,2);
        boolean is2 = followerStore.isAFollower(2,1);
        
        // Check the return value for the expected result
        if (is == true && is2 == false) // We expect it to return null 
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    /*
     * Tests what happens when an invalid Weet is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetNumFollowers()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int uid1 = 1, uid2 = 2, uid3 = 3, uid4 = 4, uid5 = 5, uid6 = 6;
        // Generate Weet to add
        followerStore.addFollower(uid1,uid3,createDate("02/11/2013 23:11"));
        followerStore.addFollower(uid2,uid3,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid4,uid3,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid5,uid3,createDate("02/11/2012 23:11"));
        
        int total = followerStore.getNumFollowers(uid3);

        // Check the return value for the expected result
        if (total == 4) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }

    /*
     * Tests what happens when an invalid Weet is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetMutualFollows()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int uid1 = 1, uid2 = 2, uid3 = 3, uid4 = 4, uid5 = 5, uid6 = 6;
        // Generate Weet to add
        followerStore.addFollower(uid1,uid3,createDate("02/11/2013 23:11"));
        followerStore.addFollower(uid1,uid4,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid1,uid2,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid1,uid5,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid2,uid1,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid2,uid5,createDate("02/11/2015 23:11"));
        followerStore.addFollower(uid2,uid3,createDate("02/11/2011 23:11"));
        followerStore.addFollower(uid2,uid4,createDate("02/11/2014 23:11"));
        
        int[] f1 = followerStore.getMutualFollows(uid1,uid2);
        int[] a = {uid5,uid4,uid3};

        // Check the return value for the expected result
        if (Arrays.equals(f1,a)) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }

    /*
     * Tests what happens when an invalid Weet is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetTopUsers()
    {
        // Create new Weet Store
        IFollowerStore followerStore = new FollowerStore();

        int uid1 = 1, uid2 = 2, uid3 = 3, uid4 = 4, uid5 = 5, uid6 = 6;
        // Generate Weet to add
        followerStore.addFollower(uid2,uid1,createDate("02/11/2013 23:11"));
        followerStore.addFollower(uid3,uid1,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid4,uid1,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid5,uid1,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid6,uid1,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid1,uid2,createDate("02/11/2015 23:11"));
        followerStore.addFollower(uid3,uid2,createDate("02/11/2011 23:11"));
        followerStore.addFollower(uid1,uid3,createDate("02/11/2014 23:11"));
        followerStore.addFollower(uid2,uid3,createDate("02/11/2012 23:11"));
        followerStore.addFollower(uid4,uid3,createDate("02/11/2011 23:11"));
        followerStore.addFollower(uid1,uid4,createDate("02/11/2015 23:11"));
        followerStore.addFollower(uid2,uid4,createDate("02/11/2011 23:11"));
        followerStore.addFollower(uid3,uid4,createDate("02/11/2014 23:11"));
        followerStore.addFollower(uid3,uid5,createDate("02/11/2014 23:11"));
        
        int[] f1 = followerStore.getTopUsers();
        int[] a = {uid1,uid3,uid4,uid2,uid5,uid6};  
        
        // Check the return value for the expected result
        if (Arrays.equals(f1,a)) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }

    /*
     * Returns a Date based on the input string
     * @param inputString Takes a string of form dd/MM/yy hour:minute. Example: 01/03/12 18:00
     * @return Returns the create Date
     */
    private Date createDate(String inputString) // This method is useful for creating dates quickly, where can be good for testing
    {
        try {
            // Take input string and create date
            DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy H:m");
            return dateFormatter.parse(inputString);
        }
        catch (ParseException pe) 
        {
            // Bad input string
            System.out.println("Couldn't parse " + inputString);
            return new Date();
        }
    }
}
