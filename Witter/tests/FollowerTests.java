import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import uk.ac.warwick.java.cs126.services.IFollowerStore;
import uk.ac.warwick.java.cs126.services.FollowerStore;

class FollowerTests {
    protected boolean testAddFollower() 
    {
        IFollowerStore followerStore = new FollowerStore();
        
        boolean result = followerStore.addFollower(1,2,createDate("12/12/12 12:12"));   




        if (result == true) 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }
    protected boolean testAddFollowerFail() 
    {
        IFollowerStore followerStore = new FollowerStore();

        boolean result = followerStore.addFollower(1,2,createDate("12/12/12 12:12"));
        boolean result2 = followerStore.addFollower(1,2,createDate("12/12/12 12:12"));

        if (result2 == true) 
        {
            return false;
        }
        else 
        { 
            return true;
        }
    }
    protected boolean testGetFollowers()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("10/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("13/12/12 12:12"));
      followerStore.addFollower(1,3,createDate("14/12/12 12:12"));
      followerStore.addFollower(4,1,createDate("15/12/12 12:12"));
      followerStore.addFollower(7,1,createDate("16/12/12 12:12"));
		followerStore.addFollower(1,7,createDate("17/12/12 12:12"));
      int[] result = followerStore.getFollowers(1);
      
      if (result.length!=4)
      {
        return false;
      }
      if (result[0]!=7)
      {
        return false;
      }
      if (result[1]!=4)
      {
        return false;
      }
      if (result[2]!=2)
      	return false;
      if(result[3]!=3)
      	return false;
      return true;
    }
    protected boolean testGetFollowersFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));

      int[] result = followerStore.getFollowers(3);
      
      if(result.length!=0)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetFollows()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      int[] result = followerStore.getFollows(2);
      
      if (result.length!=2)
      {
        return false;
      }
      if (result[0]!=1)
      {
        return false;
      }
      if (result[1]!=5)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetFollowsFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      int[] result = followerStore.getFollows(5);
      
      if(result.length!=0)
      {
        return false;
      }
      return true;
    }
    protected boolean testIsAFollower()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      boolean result = followerStore.isAFollower(2,5);
      
      if(result==false)
      {
        return false;
      }
      return true;
    }
    protected boolean testIsAFollowerFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      boolean result = followerStore.isAFollower(1,3);
      
      if(result==true)
      {
        return false;
      }
      return true;
    }
    protected boolean testIsAFollowerNotExist()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      boolean result = followerStore.isAFollower(7,3);
      
      if(result==true)
      {
        return false;
      }
      return true;
    }
    protected boolean testNumFollowers()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      int result = followerStore.getNumFollowers(2);
      
      if(result!=2)
      {
        return false;
      }
      return true;
    }
    protected boolean testNumFollowersFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));

      int result = followerStore.getNumFollowers(3);
      
      if(result!=0)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetMutualFollowers()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));
      followerStore.addFollower(5,1,createDate("11/12/12 12:12"));
      followerStore.addFollower(5,2,createDate("10/12/12 12:12"));
      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("13/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));

      int[] result = followerStore.getMutualFollowers(1,2);
      
      if(result[0]!=3)
      {
        return false;
      }
      if (result[1]!=5)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetMutualFollowersFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));
      followerStore.addFollower(5,1,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,2,createDate("10/12/12 12:12"));

      int[] result = followerStore.getMutualFollowers(1,2);
      
      if(result.length!=0)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetMutualFollows()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(2,5,createDate("11/12/12 12:12"));
      followerStore.addFollower(5,1,createDate("11/12/12 12:12"));
      followerStore.addFollower(5,2,createDate("10/12/12 12:12"));
      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,1,createDate("13/12/12 12:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));

      int[] result = followerStore.getMutualFollows(5,3);
      
      if(result[0]!=1)
      {
        return false;
      }
      if (result[1]!=2)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetMutualFollowsFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(5,1,createDate("11/12/12 12:12"));
      followerStore.addFollower(3,2,createDate("12/12/12 12:12"));
      followerStore.addFollower(1,2,createDate("11/12/12 12:12"));

      int[] result = followerStore.getMutualFollows(1,2);
      for (int i=0; i<result.length; i++){
        System.err.println(result[i]);
      }
      
      if(result.length!=0)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetTopUsers()
    {
      IFollowerStore followerStore = new FollowerStore();

      followerStore.addFollower(2,5,createDate("11/11/12 17:12"));
      followerStore.addFollower(5,1,createDate("11/1/12 16:12"));
      followerStore.addFollower(5,2,createDate("10/6/12 15:12"));
      followerStore.addFollower(3,2,createDate("12/7/12 09:12"));
      followerStore.addFollower(1,2,createDate("11/8/12 08:12"));
      followerStore.addFollower(7,2,createDate("11/9/12 11:12"));
      followerStore.addFollower(3,1,createDate("13/10/12 13:12"));
      followerStore.addFollower(2,1,createDate("12/12/12 12:12"));
      followerStore.addFollower(2,3,createDate("12/7/12 09:12"));
      int[] result = followerStore.getTopUsers();
      
      if(result[0]!=2)
      {
        return false;
      }
      if (result[1]!=1)
      {
        return false;
      }
      if (result[2]!=5)
      {
        return false;
      }
      return true;
    }
    protected boolean testGetTopUsersFail()
    {
      IFollowerStore followerStore = new FollowerStore();

      int[] result = followerStore.getTopUsers();
      
      if(result.length!=0)
      {
        return false;
      }
      return true;
    }
    

    /*
     * Tests the ability to retrieve a follower
     * @return Returns true is the test passed, false is it failed
     */

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
