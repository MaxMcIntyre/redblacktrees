import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import uk.ac.warwick.java.cs126.services.IUserStore;
import uk.ac.warwick.java.cs126.services.UserStore;
import uk.ac.warwick.java.cs126.models.User;

class UserTests {
    /*
     * Tests the ability to add a user
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testAddUser() 
    {
        IUserStore userStore = new UserStore();

        // Generate Weet to add
        User user = new User("Test User", 1, new Date(System.currentTimeMillis()));
        for (int i=0; i<10000; i++){
          user = new User("Test User", i, new Date(System.currentTimeMillis()));
          return userStore.addUser(user);
        }
        // Issue the command, suitably storing the return value
        return !userStore.addUser(user);
    }
    protected boolean testAddUserFail() 
    {
        IUserStore userStore = new UserStore();

        // Generate Weet to add
        User user = new User("Rhiannon", 1, createDate("02/11/2012 23:11"));

        // Issue the command, suitably storing the return value
        boolean result = userStore.addUser( user );
        boolean result2 = userStore.addUser( user );

        // Check the return value for the expected result
        if (result2 == true) // Here we expect addUser to return false if it worked
        {
            return false;
        }
        else 
        { 
            return true;
        }
    }

    /*
     * Tests the ability to retrieve a user
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetUserSuccess()
    {
        // Create new User Store
        IUserStore userStore = new UserStore();

        // Generate User to add
        int userId = 1;
        User user = new User("name", userId, createDate("02/11/2012 23:11"));

        // Add that user
        userStore.addUser( user );

        // Issue the command, suitably storing the return value
        User returnedUser = userStore.getUser(userId);
        
        // Check the return value for the expected result
        if (returnedUser == user) // We expect the User we get back to be the same as the one put in 
        {
            return true;
        }
        else 
        { 
            return false;
        }

    }

    /*
     * Tests what happens when an invalid User is searched for 
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetUserFail()
    {
        // Create new User Store
        IUserStore userStore = new UserStore();

        int userId = 2;
        // Try retrieve a User that won't be in the store 
        User returnedUser = userStore.getUser(userId);
        
        // Check the return value for the expected result
        if (returnedUser == null) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }

    protected boolean testUserListSorted()
    {
      IUserStore userStore = new UserStore();
      for (int i=9;i<=20;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/0"+(i+1)+" 12:20")));
      }
      User[] toCheck = userStore.getUsers();
      Date refDate = toCheck[0].getDateJoined();
      if (toCheck.length==0)
      {
        return false;
      }
      if (toCheck.length!=20)
      {
        return false;
      }
      for (int j=0;j<toCheck.length;j++)
      {
        if (toCheck[j].getDateJoined().compareTo(refDate)>0)
        {
          return false;
        }
        refDate = toCheck[j].getDateJoined();
      }
      return true;
    }
    protected boolean testUserListSortedSpeed()
    {
      IUserStore userStore = new UserStore();
      for (int i=999;i<=4000;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/"+(i%11+1)+"/"+(i+1)+" 12:20")));
      }
      for (int i=99;i<=998;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/"+((i%11)+1)+"/0"+(i+1)+" 12:20")));
      }
      for (int i=9;i<=98;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/"+((i%11)+1)+"/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/"+((i%11)+1)+"/0"+(i+1)+" 12:20")));
      }
      User[] toCheck = userStore.getUsers();
      Date refDate = toCheck[0].getDateJoined();
      if (toCheck.length==0)
      {
        return false;
      }
      if (toCheck.length!=4000)
      {
        return false;
      }
      for (int j=0;j<toCheck.length;j++)
      {
        if (toCheck[j].getDateJoined().compareTo(refDate)>0)
        {
	  System.out.println(toCheck[j].getDateJoined());
          return false;
        }
        refDate = toCheck[j].getDateJoined();
      }
      return true;
    }
    protected boolean testUserListSortedFail()
    {
      IUserStore userStore = new UserStore();
      User[] toCheck = userStore.getUsers();
      if (toCheck.length==0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }

    protected boolean testUserGetBefore()
    {
      IUserStore userStore = new UserStore();
      for (int i=9;i<=20;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/0"+(i+1)+" 12:20")));
      }
      User[] userArray = userStore.getUsers();
      User[] toCheck = userStore.getUsersJoinedBefore(userArray[4].getDateJoined());
      if (toCheck.length==0)
      {
        return false;
      }
      if (toCheck.length!=16)
      {
        return false;
      }
      for (int j=0;j<toCheck.length;j++)
      {
        if (toCheck[j].getDateJoined().compareTo(userArray[4].getDateJoined())>0)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testUserGetBeforeFail()
    {
      IUserStore userStore = new UserStore();
      for (int i=9;i<=20;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        userStore.addUser(new User("name"+i, i, createDate("11/11/0"+(i+1)+" 12:20")));
      }
      User[] toCheck = userStore.getUsersJoinedBefore(createDate("01/01/00 23:54"));
      if (toCheck.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }

    protected boolean testUsersContaining()
    {
      IUserStore userStore = new UserStore();
      userStore.addUser(new User("Rhiannon",1,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Rhia",2,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Nothing",3,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Rhiann",4,createDate("04/05/12 12:11")));
      User[] userArray = userStore.getUsersContaining("Rhi");
      if (userArray==null)
      {
        return false;
      }
      if (userArray.length!=3)
      {
        return false;
      }
      Date refDate = userArray[0].getDateJoined();
      for (int i=0;i<userArray.length;i++)
      {
        if (userArray[i].getName().contains("Rhi")==false||userArray[i].getDateJoined().compareTo(refDate)>0)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testUsersContainingFail()
    {
      IUserStore userStore = new UserStore();
      userStore.addUser(new User("Rhiannon",1,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Rhia",2,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Nothing",3,createDate("04/05/12 12:11")));
      userStore.addUser(new User("Rhiann",4,createDate("04/05/12 12:11")));
      User[] userArray = userStore.getUsersContaining("Butt");
      if (userArray.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    protected boolean testAddUserSpeed() 
    {
        IUserStore userStore = new UserStore();

        for (int i=1;i<2000;i++)
        {
          User toAdd = new User("Rhi"+i,i,createDate("02/03/04 02:03"));
          boolean res = userStore.addUser(toAdd);
          if (res==false)
          {
            return false;
          }
        }
        return true;
    }
    protected boolean testAddUserRetrieve() 
    {
        IUserStore userStore = new UserStore();

        User[] check = new User[2000];
        for (int i=1;i<=2000;i++)
        {
          User toAdd = new User("Rhi"+i,i,createDate("02/03/04 02:03"));
          boolean res = userStore.addUser(toAdd);
          check[i-1]=toAdd;
          if (res==false)
          {
            return false;
          }
        }
        User[] users = userStore.getUsers();
        if (users==null)
        {
          return false;
        }
        if (users.length!=2000)
        {
          System.out.println(users.length);
          return false;
        }
        for (int j=1;j<=2000;j++)
        {
          User retrieve = userStore.getUser(j);
          User compare = check[j-1];
          if (retrieve!=compare)
          {
            System.out.println(userStore.getUser(j).getName()+ " " + userStore.getUser(j).getPrettyDateJoined()+ " " + userStore.getUser(j).getId());
            System.out.println(compare.getName()+ " " + userStore.getUser(j).getPrettyDateJoined()+ " " + compare.getId());
            return false;
          }
        }
        return true;
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
