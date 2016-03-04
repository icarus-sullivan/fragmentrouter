package icarus.io.router;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by chris sullivan on 3/3/16.
 */
public class FragmentRouter {

    private static Stack<Fragment> back = new Stack<Fragment>();
    private static Stack<Fragment> forward = new Stack<Fragment>();

    private static AppCompatActivity mAct;
    private static Fragment current = null;

    private static FragmentRouter ourInstance;

    private static Lock lock = new ReentrantLock();

    private static FragmentChangeListener fragmentListener;
    public interface FragmentChangeListener {
        void onFragmentChanged(Fragment current);
    }

    public static FragmentRouter init( AppCompatActivity act ) {
        ourInstance = new FragmentRouter( act );
        return ourInstance;
    }

    private FragmentRouter(AppCompatActivity act) {
        mAct = act;
    }

    /**
     * Accessible methods for navigations, fragmentRouter must have been initialzied with
     * a valid AppCompatActivity or else the methods will fail
     */

    public static void addFragmentChangeListener( FragmentChangeListener listen ) {
        fragmentListener = listen;
    }

    public static Fragment getCurrent() {
        return current;
    }

    // API
    public static void navigateTo( int container, Fragment fragment ) {
        // add to the end of forward navigation
        lock.lock();
        forward.push( fragment );
        lock.unlock();
        navigateForward(container);
    }

    // API
    public static void navigateBack( int container ) {
        if( back.size() > 0 ) {
            _ignoreFragmentManagerBackstack();

            Fragment check = _pipeStacks(back, forward);
            // if the last popped fragment was already current, pop again
            current = (_wasLast(check) ? _pipeStacks( back, forward ) : check);

            _handleNavigation( container, R.anim.enter_left, R.anim.exit_right );
        }
    }

    // API
    public static void navigateForward( int container ) {
        if( forward.size() > 0 ) {
            _ignoreFragmentManagerBackstack();

            Fragment check = _pipeStacks(forward, back);
            // if the last popped fragment was already current, pop again
            current = (_wasLast( check ) ? _pipeStacks(forward, back) : check);

            _handleNavigation( container, R.anim.enter_right, R.anim.exit_left );
        }
    }

    /**
     * Internal calls that should not be modified, the _ prefix denotes that changing
     * these methods may break the overall architecture of {@link FragmentRouter}
     */
    private static boolean _wasLast( Fragment frag ) {
        return current == frag;
    }

    private static void _ignoreFragmentManagerBackstack() throws NullPointerException {
        if( mAct == null ) {
            throw new NullPointerException("FragmentRouter must be initialized with a valid AppCompatActivity");
        }
        mAct.getSupportFragmentManager()
                .popBackStack();
    }

    private static Fragment _pipeStacks( Stack<Fragment> s1, Stack<Fragment> s2 ) {
        if( s1.size() > 0 )
        {
            lock.lock();
            Fragment fragment = s1.pop();
            s2.push(fragment);
            lock.unlock();
            return fragment;
        }
        return null;
    }

    private static void _handleNavigation( int container, int animIn, int animOut ) {
        if( current == null ) {
            return;
        }
        mAct.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(animIn, animOut)
                .replace(container, current)
                .commit();

        if( fragmentListener != null ) fragmentListener.onFragmentChanged( current );
    }

    /**
     * Accessible Stack management -- clearing of nav
     */

    // API -- note clearing both navs will not remove the current fragment but will only
    //        erase its navigation from the current fragments position
    public static void clearAllNav() {
        forward.clear();
        back.clear();
    }

    // API
    public static void clearForwardNav() {
        forward.clear();
    }

    // API
    public static void clearBackNav() {
        back.clear();
    }

}
