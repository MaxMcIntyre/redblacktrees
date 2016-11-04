public class TestRunner 
{

    public static void main(String[] args)
    {


        // Weet Tests
        WeetTests e = new WeetTests();
        System.out.println("----------------------------------");
        System.out.println("|                                |");
        System.out.println("| Written by Rhiannon Michelmore |");
        System.out.println("|                                |");
        System.out.println("|  If crash occurs, something    |");
        System.out.println("|  may be returning null from    |");
        System.out.println("|  your code that isn't meant    |");
        System.out.println("|            to be...            |");
        System.out.println("|                                |");
        System.out.println("----------------------------------");
        System.out.println();
        System.out.println("[Testing Weets]");

        


        // TODO: Test remaining IWeetStore methods (pass and fail).
        //
        

        // User Tests
        

        // Call our testAddWeet method in order to ensure that a weet is added succesfully
        System.out.print("--> testAddWeet : \t\t"); 
        boolean testAddWeet = e.testAddWeet();
        if (testAddWeet == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testAddWeetFail : \t\t"); 
        boolean testAddWeetFail = e.testAddWeetFail();
        if (testAddWeetFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }


        // Call our get weet fail method, remebering to test all possible outcomes of a method 
        System.out.print("--> testGetWeetFail : \t\t");
        boolean testGetWeetFail = e.testGetWeetFail();
        if (testGetWeetFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }

        // Call our get weet success method, remebering to test all possible outcomes of a method 
        System.out.print("--> testGetWeetSuccess : \t");
        boolean testGetWeetSuccess = e.testGetWeetSuccess();
        if (testGetWeetSuccess == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsArray : \t");
        boolean testGetWeets = e.testGetWeets();
        if (testGetWeets == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsArrayFail : \t");
        boolean testGetWeetsFail = e.testGetWeetsFail();
        if (testGetWeetsFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsByUser : \t");
        boolean testGetWeetsByUser = e.testGetWeetByUser();
        if (testGetWeetsByUser == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsByUserFail : \t");
        boolean testGetWeetsByUserFail = e.testGetWeetByUserFail();
        if (testGetWeetsByUserFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsOn : \t\t");
        boolean testGetWeetsOn = e.testWeetGetOn();
        if (testGetWeetsOn == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsOnFail : \t");
        boolean testGetWeetsOnFail = e.testWeetGetOnFail();
        if (testGetWeetsOnFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsBefore : \t");
        boolean testGetWeetsBefore = e.testWeetGetBefore();
        if (testGetWeetsBefore == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsBeforeFail : \t");
        boolean testGetWeetsBeforeFail = e.testWeetGetBeforeFail();
        if (testGetWeetsBeforeFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsContaining : \t");
        boolean testGetWeetsContaining = e.testGetWeetsContaining();
        if (testGetWeetsContaining == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetWeetsContainingFail :");
        boolean testGetWeetsContainingFail = e.testGetWeetsContainingFail();
        if (testGetWeetsContainingFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetTrending : \t\t");
        boolean testGetTrending = e.testGetTrending();
        if (testGetTrending == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetTrending<10Tags : \t");
        boolean testGetTrendingUTen = e.testGetTrendingUnderTenTags();
        if (testGetTrendingUTen == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetTrendingEmpty : \t");
        boolean testGetTrendingEmpty = e.testGetTrendingEmpty();
        if (testGetTrendingEmpty == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetTrendingSpeed : \t");
        long startTimeT = System.nanoTime();
        boolean testTrending = e.testGetTrendingSpeed();
        long endTimeT = System.nanoTime();
        long durationT = endTimeT - startTimeT;
        double secondsT = (double)durationT / 1000000000.0;
        if (testTrending == true) {
            System.out.println("...success [["+secondsT+" s]]");
        }
        else {
            System.out.println("...fail.");
        }


        UserTests f = new UserTests();
        System.out.println();
        System.out.println("[Testing Users]");
        System.out.println("~~--WARNING: JVM CACHING MEANS ONLY FIRST SPEED TEST IS ACCURATE, CHANGE ORDER TO TEST OTHERS--~~");

        System.out.print("--> testAddUser : \t\t"); 
        boolean testAddUser = f.testAddUser();
        if (testAddUser == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testAddUserFail : \t\t"); 
        boolean testAddUserFail = f.testAddUserFail();
        if (testAddUserFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }


        System.out.print("--> testGetUserFail : \t\t");
        boolean testGetUserFail = f.testGetUserFail();
        if (testGetUserFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }

        System.out.print("--> testGetUserSuccess : \t");
        boolean testGetUserSuccess = f.testGetUserSuccess();
        if (testGetUserSuccess == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        
        System.out.print("--> testGetUserArraySortSuccess :");
        boolean testUserListSorted = f.testUserListSorted();
        if (testUserListSorted == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetUserArraySortSuccessSpeed : \t");
        long startTimeA = System.nanoTime();
        boolean testUserListSortedSpeed = f.testUserListSortedSpeed();
        long endTimeA = System.nanoTime();
        long durationA = endTimeA - startTimeA;
        double secondsA = (double)durationA / 1000000000.0;
        if (testUserListSortedSpeed == true) {
            System.out.println("...success [["+secondsA+" s]]");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetUserArraySortFail : \t");
        boolean testUserListSortedFail = f.testUserListSortedFail();
        if (testUserListSortedFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        
        System.out.print("--> testGetUserBeforeSuccess : \t");
        boolean testUserGetBefore = f.testUserGetBefore();
        if (testUserGetBefore == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetUserBeforeFail : \t");
        boolean testUserGetBeforeFail = f.testUserGetBeforeFail();
        if (testUserGetBeforeFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetUsersContaining : \t");
        boolean testUsersContaining = f.testUsersContaining();
        if (testUsersContaining == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetUsersContainingFail :");
        boolean testUsersContainingFail = f.testUsersContainingFail();
        if (testUsersContainingFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testAddUserSpeedRetrieve : \t"); 
        long startTimeR = System.nanoTime();
        boolean testAddUserSpeedR = f.testAddUserRetrieve();
        long endTimeR = System.nanoTime();
        long durationR = endTimeR - startTimeR;
        double secondsR = (double)durationR / 1000000000.0;
        if (testAddUserSpeedR == true) {
            System.out.print("...success");
            System.out.println("  [["+secondsR+" s]]");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testAddUserSpeed : \t\t"); 
        long startTime = System.nanoTime();
        boolean testAddUserSpeed = f.testAddUserSpeed();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double seconds = (double)duration / 1000000000.0;
        if (testAddUserSpeed == true) {
            System.out.print("...success");
            System.out.println("  [["+seconds+" s]]");
        }
        else {
            System.out.println("...fail.");
        }

        
        System.out.println();
        System.out.println("[Testing Followers]");
        FollowerTests g = new FollowerTests();
        System.out.print("--> testAddFollowerSuccess : \t");
        boolean testAddFSuccess = g.testAddFollower();
        if (testAddFSuccess == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testAddFollowerFail : \t");
        boolean testAddFFail = g.testAddFollowerFail();
        if (testAddFFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetFollowers : \t\t");
        boolean testGetF = g.testGetFollowers();
        if (testGetF == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetFollowersFail : \t");
        boolean testGetFFail = g.testGetFollowersFail();
        if (testGetFFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetFollows : \t\t");
        boolean testGetFl = g.testGetFollows();
        if (testGetFl == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetFollowsFail : \t");
        boolean testGetFlFail = g.testGetFollowsFail();
        if (testGetFlFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testIsAFollower : \t\t");
        boolean testIsAFollower = g.testIsAFollower();
        if (testIsAFollower == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testIsAFollowerFail : \t");
        boolean testIsAFollowerFail = g.testIsAFollowerFail();
        if (testIsAFollowerFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testIsAFollowerNotExist : \t");
        boolean testIsAFollowerNE = g.testIsAFollowerNotExist();
        if (testIsAFollowerNE == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testNumFollowers : \t\t");
        boolean testNumF = g.testNumFollowers();
        if (testNumF == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testNumFollowersFail : \t");
        boolean testNumFFail = g.testNumFollowersFail();
        if (testNumFFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetMutualFollowers : \t");
        boolean testGetMutFol = g.testGetMutualFollowers();
        if (testGetMutFol == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetMutualFollowersFail :");
        boolean testGetMutFolFail = g.testGetMutualFollowersFail();
        if (testGetMutFolFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        System.out.print("--> testGetMutualFollows : \t");
        boolean testGetMutFols = g.testGetMutualFollows();
        if (testGetMutFols == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }
        // System.out.print("--> testGetMutualFollowsFail : \t");
        // boolean testGetMutFolsFail = g.testGetMutualFollowsFail();
        // if (testGetMutFolsFail == true) {
        //     System.out.println("...success");
        // }
        // else {
        //     System.out.println("...fail.");
        // }
        // System.out.print("--> testGetTopUsers : \t\t");
        // boolean testGetTopUsers = g.testGetTopUsers();
        // if (testGetTopUsers == true) {
        //     System.out.println("...success");
        // }
        // else {
        //     System.out.println("...fail.");
        // }
        System.out.print("--> testGetTopUsersFail : \t");
        boolean testGetTopUsersFail = g.testGetTopUsersFail();
        if (testGetTopUsersFail == true) {
            System.out.println("...success");
        }
        else {
            System.out.println("...fail.");
        }

    
    }

}
