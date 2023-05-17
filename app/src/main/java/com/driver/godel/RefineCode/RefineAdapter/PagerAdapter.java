package com.driver.godel.RefineCode.RefineAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.driver.godel.RefineCode.RefineFragments.OngoingFragment;
import com.driver.godel.RefineCode.RefineFragments.PendingDeliveryFragment;
import com.driver.godel.RefineCode.RefineFragments.PickupFragment;
import com.driver.godel.RefineCode.RefineFragments.ScanPackage;

/**
 * Created by QSYS\simarjot.singh on 7/6/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter
{
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PagerAdapter(FragmentManager fm, int tabCount)
    {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position)
    {
        //Returning the current tabs
        switch (position)
        {
            case 0:
                OngoingFragment tab1 = new OngoingFragment();
                return tab1;
            case 1:
                PickupFragment tab2 = new PickupFragment();
                return tab2;
            case 2:
                PendingDeliveryFragment tab3 = new PendingDeliveryFragment();
                return tab3;
            case 3:
                ScanPackage tab4 = new ScanPackage();
                return tab4;

//            case 4:
//                NewRequestsFragment tab5 = new NewRequestsFragment();
//                return tab5;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount()
    {
        return tabCount;
    }

}
