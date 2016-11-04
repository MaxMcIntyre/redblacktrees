import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import uk.ac.warwick.java.cs126.services.IWeetStore;
import uk.ac.warwick.java.cs126.services.WeetStore;
import uk.ac.warwick.java.cs126.models.Weet;
import uk.ac.warwick.java.cs126.models.User;

class WeetTests {
    

    /*
     * Tests the ability to add a weet
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testAddWeet() 
    {
        // Create new Weet Store
        IWeetStore weetStore = new WeetStore();

        // Generate Weet to add
        Weet weet = new Weet(1, 1, "Hello World!", createDate("02/11/2012 23:11"));

        // Issue the command, suitably storing the return value
        boolean result = weetStore.addWeet( weet );

        // Check the return value for the expected result
        if (result == true) // Here we expect addWeet to return true if it worked
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }
    protected boolean testAddWeetFail() 
    {
        // Create new Weet Store
        IWeetStore weetStore = new WeetStore();

        // Generate Weet to add
        Weet weet = new Weet(1, 1, "Hello World!", createDate("02/11/2012 23:11"));

        // Issue the command, suitably storing the return value
        boolean result = weetStore.addWeet( weet );
        boolean result2 = weetStore.addWeet(weet);

        // Check the return value for the expected result
        if (result2 == true) // Here we expect addWeet to return true if it worked
        {
            return false;
        }
        else 
        { 
            return true;
        }
    }

    /*
     * Tests the ability to retrieve a weet
     * @return Returns true is the test passed, false is it failed
     */
    protected boolean testGetWeetSuccess()
    {
        // Create new Weet Store
        IWeetStore weetStore = new WeetStore();

        // Generate Weet to add
        int weetId = 1;
        Weet weet = new Weet(weetId, 1, "Hello World!", createDate("02/11/2012 23:11"));

        // Add that weet
        weetStore.addWeet( weet );

        // Issue the command, suitably storing the return value
        Weet returnedWeet = weetStore.getWeet(weetId);
        
        // Check the return value for the expected result
        if (returnedWeet == weet) // We expect the Weet we get back to be the same as the one put in 
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
    protected boolean testGetWeetFail()
    {
        // Create new Weet Store
        IWeetStore weetStore = new WeetStore();

        int weetId = 2;
        // Try retrieve a Weet that won't be in the store 
        Weet returnedWeet = weetStore.getWeet(weetId);
        
        // Check the return value for the expected result
        if (returnedWeet == null) // We expect it to return null 
        {
            return true;
        }
        else 
        { 
            return false;
        }
    }
    protected boolean testGetWeets()
    {
      IWeetStore weetStore = new WeetStore();

      Weet weet1 = new Weet(1,1,"Hello, World!",createDate("01/01/12 23:54"));
      Weet weet2 = new Weet(2,1,"Hello, World!",createDate("01/01/12 23:57"));
      Weet weet3 = new Weet(3,1,"Hello, World!",createDate("01/01/12 23:58"));
      Weet weet4 = new Weet(4,2,"Hello, World!",createDate("01/01/12 23:59"));
      Weet weet5 = new Weet(5,2,"Hello, World!",createDate("01/01/13 23:59"));

      weetStore.addWeet(weet5);
      weetStore.addWeet(weet2);
      weetStore.addWeet(weet1);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);

      Weet[] results = weetStore.getWeets();

      if (results==null)
      {
        return false;
      }
      if (results.length!=5)
      {
        return false;
      }

      Date compare = results[0].getDateWeeted();
      for (int i=0;i<results.length;i++)
      {
        if (results[i].getDateWeeted().compareTo(compare)>0)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testGetWeetsFail()
    {
      IWeetStore weetStore = new WeetStore();
      Weet[] results = weetStore.getWeets();

      if (results.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }

    protected boolean testGetWeetByUser()
    {
      IWeetStore weetStore = new WeetStore();

      Weet weet1 = new Weet(1,1,"Hello, World!",createDate("01/01/12 23:54"));
      Weet weet2 = new Weet(2,1,"Hello, World!",createDate("01/01/12 23:57"));
      Weet weet3 = new Weet(3,1,"Hello, World!",createDate("01/01/12 23:58"));
      Weet weet4 = new Weet(4,2,"Hello, World!",createDate("01/01/12 23:59"));

      weetStore.addWeet(weet2);
      weetStore.addWeet(weet1);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);

      Weet[] results = weetStore.getWeetsByUser(new User("Rhi",1,createDate("01/01/11 11:23")));
      if (results==null)
      {
        return false;
      }

      if (results.length!=3)
      {
        return false;
      }

      Date compare = results[0].getDateWeeted();
      for (int i=0;i<results.length;i++)
      {
        if (results[i].getDateWeeted().compareTo(compare)>0)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testGetWeetByUserFail()
    {
      IWeetStore weetStore = new WeetStore();

      Weet weet1 = new Weet(1,1,"Hello, World!",createDate("01/01/12 23:54"));
      Weet weet2 = new Weet(2,1,"Hello, World!",createDate("01/01/12 23:57"));
      Weet weet3 = new Weet(3,1,"Hello, World!",createDate("01/01/12 23:58"));
      Weet weet4 = new Weet(4,2,"Hello, World!",createDate("01/01/12 23:59"));

      weetStore.addWeet(weet2);
      weetStore.addWeet(weet1);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);

      Weet[] results = weetStore.getWeetsByUser(new User("Rhi",3,createDate("01/01/11 11:23")));

      if (results.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    protected boolean testWeetGetOn()
    {
      IWeetStore weetStore = new WeetStore();
      for (int i=9;i<=20;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("12/11/11 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/11 11:11")));
      }
      Weet[] toCheck = weetStore.getWeetsOn(createDate("11/11/11 11:11"));
      if (toCheck==null)
      {
        return false;
      }
      if (toCheck.length!=8)
      {
        return false;
      }

      SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
      for (int j=0;j<toCheck.length;j++)
      {
        if (toCheck[j].getDateWeeted().compareTo(toCheck[0].getDateWeeted())>0)
        {
          return false;
        }
        if (fmt.format(toCheck[j].getDateWeeted()).equals(fmt.format(createDate("11/11/11 09:31")))==false)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testWeetGetOnFail()
    {
      IWeetStore weetStore = new WeetStore();
      for (int i=9;i<=20;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("12/11/11 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/11 11:11")));
      }
      Weet[] toCheck = weetStore.getWeetsOn(createDate("13/11/11 10:32"));
      if (toCheck.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    
    protected boolean testWeetGetBefore()
    {
      IWeetStore weetStore = new WeetStore();
      for (int i=9;i<=20;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/0"+(i+1)+" 12:20")));
      }
      weetStore.addWeet(new Weet(0,1,"hi", createDate("11/11/22 12:20")));


      Weet[] weetArray = weetStore.getWeets();
      Weet[] toCheck = weetStore.getWeetsBefore(createDate("11/11/20 12:10"));
      if (toCheck==null)
      {
        System.out.println("Failed here1");
        return false;
      }
     
      if (toCheck.length!=18)
      {
         System.out.println("Failed here2");
        return false;
      }
      for (int j=0;j<toCheck.length;j++)
      {
        if (toCheck[j].getDateWeeted().compareTo(createDate("11/11/20 12:10"))>0)
        {
           System.out.println("Failed here3");
          return false;
        }
      }
      return true;
    }

    /*protected boolean testWeetGetBefore() {
      IWeetStore weetStore = new WeetStore();
     
      //setting up weetStore
      weetStore.addWeet(new Weet(1, 1, "sup", createDate("19/07/0" +1+" 12:01")));
      weetStore.addWeet(new Weet(2, 2, "m8", createDate("19/07/0" +2+" 12:01")));
      weetStore.addWeet(new Weet(3, 3, "weet", createDate("19/07/0" +3+" 12:01")));
      weetStore.addWeet(new Weet(4, 4, "is", createDate("19/07/0" +4+" 12:01")));
      weetStore.addWeet(new Weet(5, 5, "smoking", createDate("19/07/0" +5+" 12:01")));
      weetStore.addWeet(new Weet(14, 14, "smoking", createDate("19/07/0" +5+" 12:01")));

      weetStore.addWeet(new Weet(6, 6, "innit", createDate("19/07/0" +6+" 12:01")));
      weetStore.addWeet(new Weet(7, 7, "jus", createDate("19/07/0" +7+" 12:01")));
      weetStore.addWeet(new Weet(8, 9, "cant", createDate("19/07/0" +8+" 12:01")));
      weetStore.addWeet(new Weet(9, 9, "get", createDate("19/07/0" +9+" 12:01")));
      weetStore.addWeet(new Weet(10, 10, "enough!", createDate("19/07/" +10+" 12:01")));
     
      Weet[] toCheck = weetStore.getWeetsBefore(createDate("18/07/06 12:01"));
      if (toCheck == null) {
        System.out.println("getWeetsBefore returned null...");
        return false;
       }
       if (toCheck.length != 5) {
        System.out.println("getWeetsBefore was not length 5 but was length "+toCheck.length);
        return false;
       }
       for (int count = 1; count<5; count++) {
        if (toCheck[count].getDateWeeted().compareTo(createDate("19/07/05 12:00")) <=0) {
          System.out.println("index " +count+" was not before the given date:");
          System.out.println("What I am expecting: "+createDate("19/07/0"+(5-count)+" 12:00"));
          System.out.println("What I get: "+toCheck[count].getDateWeeted());
          return false;
        }
       }
       System.out.println("testWeetsGetBefore passed!!!! Yayyyy!!!!");
       return true;
       
     
    }*/
   
 


    protected boolean testWeetGetBeforeFail()
    {
      IWeetStore weetStore = new WeetStore();
      for (int i=9;i<=20;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/"+(i+1)+" 12:20")));
      }
      for (int i=1;i<=8;i++)
      {
        weetStore.addWeet(new Weet(i,1,"hi",createDate("11/11/0"+(i+1)+" 12:20")));
      }
      Weet[] toCheck = weetStore.getWeetsBefore(createDate("01/01/1999 12:11"));
      if (toCheck.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }

    }
    protected boolean testGetWeetsContaining()
    {
      IWeetStore weetStore = new WeetStore();
      Weet weet1 = new Weet(1,1,"A new weet.",createDate("01/01/14 23:23"));
      Weet weet2 = new Weet(2,2,"A magical new weet.",createDate("01/01/14 23:25"));
      Weet weet3 = new Weet(3,3,"A lovely weet.",createDate("01/01/14 23:21"));
      Weet weet4 = new Weet(4,4,"A weet.",createDate("01/01/14 23:21"));
      Weet weet5 = new Weet(5,5,"A new, wonderful weet.",createDate("01/01/14 23:21"));

      weetStore.addWeet(weet1);
      weetStore.addWeet(weet2);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);
      weetStore.addWeet(weet5);

      Weet[] result = weetStore.getWeetsContaining("new");
      if (result==null)
      {
        return false;
      }
      if (result.length!=3)
      {
        return false;
      }
      for (int j=0;j<result.length;j++)
      {
        if (result[j].getDateWeeted().compareTo(result[0].getDateWeeted())>0||result[j].getMessage().contains("new")==false)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testGetWeetsContainingFail()
    {
      IWeetStore weetStore = new WeetStore();
      Weet weet1 = new Weet(1,1,"A new weet.",createDate("01/01/14 23:23"));
      Weet weet2 = new Weet(2,2,"A magical new weet.",createDate("01/01/14 23:25"));
      Weet weet3 = new Weet(3,3,"A lovely weet.",createDate("01/01/14 23:21"));
      Weet weet4 = new Weet(4,4,"A weet.",createDate("01/01/14 23:21"));
      Weet weet5 = new Weet(5,5,"A new, wonderful weet.",createDate("01/01/14 23:21"));

      weetStore.addWeet(weet1);
      weetStore.addWeet(weet2);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);
      weetStore.addWeet(weet5);

      Weet[] result = weetStore.getWeetsContaining("neew");
      if (result.length!=0)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    protected boolean testGetTrending()
    {
      IWeetStore weetStore = new WeetStore();
      Weet weet1 = new Weet(1,1,"#1",createDate("01/01/14 23:23"));
      Weet weet2 = new Weet(2,2,"#2",createDate("01/01/14 23:25"));
      Weet weet3 = new Weet(3,3,"#3",createDate("01/01/14 23:21"));
      Weet weet4 = new Weet(4,4,"#4",createDate("01/01/14 23:21"));
      Weet weet5 = new Weet(5,5,"#5",createDate("01/01/14 23:21"));
      Weet weet6 = new Weet(6,1,"#6",createDate("01/01/14 23:23"));
      Weet weet7 = new Weet(7,2,"#7",createDate("01/01/14 23:25"));
      Weet weet8 = new Weet(8,3,"#8",createDate("01/01/14 23:21"));
      Weet weet9 = new Weet(9,4,"#9",createDate("01/01/14 23:21"));
      Weet weet10 = new Weet(10,5,"#10",createDate("01/01/14 23:21"));
      Weet weet11 = new Weet(11,4,"#11",createDate("01/01/14 23:21"));
      Weet weet12 = new Weet(12,5,"#12",createDate("01/01/14 23:21"));

      weetStore.addWeet(weet10);
      weetStore.addWeet(weet4);
      weetStore.addWeet(weet5);
      weetStore.addWeet(weet11);
      weetStore.addWeet(weet8);
      weetStore.addWeet(weet9);
      weetStore.addWeet(weet6);
      weetStore.addWeet(weet7);
      weetStore.addWeet(weet12);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet1);
      weetStore.addWeet(weet2);

      String[] result = weetStore.getTrending();

      if (result.length!=10)
      {
	System.out.println("Here!");
        return false;
      }

      /*for (int i=0;i<result.length;i++)
      {
        if (result[i]==null)
        {
          return false;
        }
        if (i==0&&result[i].equals("#12")==false)
        {
          return false;
        }
        if (i==1&&result[i].equals("#11")==false)
        {
          return false;
        }
        if (i==2&&result[i].equals("#10")==false)
        {
          return false;
        }
        if (i==3&&result[i].equals("#9")==false)
        {
          return false;
        }
        if (i==4&&result[i].equals("#8")==false)
        {
          return false;
        }
        if (i==5&&result[i].equals("#7")==false)
        {
          return false;
        }
        if (i==6&&result[i].equals("#6")==false)
        {
          return false;
        }
        if (i==7&&result[i].equals("#5")==false)
        {
          return false;
        }
        if (i==8&&result[i].equals("#4")==false)
        {
          return false;
        }
        if (i==9&&result[i].equals("#3")==false)
        {
          return false;
        }
      }*/
      return true;
    }
    protected boolean testGetTrendingUnderTenTags()
    {
      IWeetStore weetStore = new WeetStore();
      Weet weet1 = new Weet(1,1,"#1",createDate("01/01/14 23:23"));
      Weet weet2 = new Weet(2,2,"#2",createDate("01/01/14 23:25"));
      Weet weet3 = new Weet(3,3,"#3",createDate("01/01/14 23:21"));
      Weet weet4 = new Weet(4,4,"#4",createDate("01/01/14 23:21"));
      Weet weet5 = new Weet(5,5,"#5",createDate("01/01/14 23:21"));
      Weet weet6 = new Weet(6,1,"#6",createDate("01/01/14 23:23"));
      Weet weet7 = new Weet(7,2,"#7",createDate("01/01/14 23:25"));

      weetStore.addWeet(weet1);
      weetStore.addWeet(weet2);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);
      weetStore.addWeet(weet5);
      weetStore.addWeet(weet6);
      weetStore.addWeet(weet7);

      String[] result = weetStore.getTrending();

      if (result.length!=10)
      {
	System.out.println("I assume, it's this one.");
        return false;
      }

      /*for (int i=0;i<result.length;i++)
      {
        if ((i==0||i==1||i==2||i==3||i==4||i==5||i==6)&&result[i]==null)
        {
          return false;
        }
        if (i==0&&result[i].equals("#7")==false)
        {
          return false;
        }
        if (i==1&&result[i].equals("#6")==false)
        {
          return false;
        }
        if (i==2&&result[i].equals("#5")==false)
        {
          return false;
        }
        if (i==3&&result[i].equals("#4")==false)
        {
          return false;
        }
        if (i==4&&result[i].equals("#3")==false)
        {
          return false;
        }
        if (i==5&&result[i].equals("#2")==false)
        {
          return false;
        }
        if (i==6&&result[i].equals("#1")==false)
        {
          return false;
        }
        if (i==7&&result[i]!=null)
        {
          return false;
        }
        if (i==8&&result[i]!=null)
        {
          return false;
        }
        if (i==9&&result[i]!=null)
        {
          return false;
        }
      }*/
      return true;
    }
    protected boolean testGetTrendingEmpty()
    {
      IWeetStore weetStore = new WeetStore();

      String[] trending = weetStore.getTrending();
      if (trending.length!=10)
      {
        return false;
      }
      for (int i=0;i<trending.length;i++)
      {
        if (trending[i]!=null)
        {
          return false;
        }
      }
      return true;
    }
    protected boolean testGetTrendingSpeed()
    {
      IWeetStore weetStore = new WeetStore();
      Weet weet1 = new Weet(1,5,"#1 ",createDate("01/01/14 23:21"));
      Weet weet2 = new Weet(2,5,"#2",createDate("01/01/14 23:21"));
      Weet weet3 = new Weet(3,5,"#3",createDate("01/01/14 23:21"));
      Weet weet4 = new Weet(4,5,"#4",createDate("01/01/14 23:21"));
      Weet weet5 = new Weet(5,5,"#5",createDate("01/01/14 23:21"));
      Weet weet6 = new Weet(6,1,"#6",createDate("01/01/14 23:23"));
      Weet weet7 = new Weet(7,2,"#7",createDate("01/01/14 23:25"));
      Weet weet8 = new Weet(8,3,"#8",createDate("01/01/14 23:21"));
      Weet weet9 = new Weet(9,4,"#9",createDate("01/01/14 23:21"));
      Weet weet10 = new Weet(10,5,"#10",createDate("01/01/14 23:21"));
      Weet weet11 = new Weet(11,4,"#11",createDate("01/01/14 23:21"));
      Weet weet12 = new Weet(12,5,"#12",createDate("01/01/14 23:21"));

      weetStore.addWeet(weet1);
      weetStore.addWeet(weet2);
      weetStore.addWeet(weet3);
      weetStore.addWeet(weet4);
      weetStore.addWeet(weet5);
      weetStore.addWeet(weet6);
      weetStore.addWeet(weet7);
      weetStore.addWeet(weet8);
      weetStore.addWeet(weet9);
      weetStore.addWeet(weet10);
      weetStore.addWeet(weet11);
      weetStore.addWeet(weet12);
      for (int e=13;e<4000;e++)
      {
        Weet weetToAdd = new Weet(e,1,"#lol",createDate("01/04/12 21:21"));
        weetStore.addWeet(weetToAdd);
      }
        Weet weetToAdd = new Weet(4001,1,"#suchweet",createDate("01/04/12 21:21"));
        weetStore.addWeet(weetToAdd);

      String[] result = weetStore.getTrending();

      if (result.length!=10)
      {
        return false;
      }
      for (int i=0;i<10;i++)
      {
        if (result[i]==null)
        {
          return false;
        }
      }
      /*if (result[0].equals("#suchweet")==false)
      {
        return false;
      }
      else if (result[1].equals("#lol")==false)
      {
        return false;
      }
      else if (result[2].equals("#wow")==false)
      {
        return false;
      }
      else if (result[3].equals("#12")==false)
      {
        return false;
      }
      else if (result[4].equals("#11")==false)
      {
        return false;
      }
      else if (result[5].equals("#10")==false)
      {
        return false;
      }
      else if (result[6].equals("#9")==false)
      {
        return false;
      }
      else if (result[7].equals("#8")==false)
      {
        return false;
      }
      else if (result[8].equals("#7")==false)
      {
        return false;
      }
      else if (result[9].equals("#6")==false)
      {
        return false;
      }*/
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
