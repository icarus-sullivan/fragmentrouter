package icarus.io.routers.fragments;

import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import icarus.io.router.R;

/**
 * Created by chris sullivan on 3/3/16.
 */
public class FragmentRouter {

    /**
     * Used to keep track of the Fragment stack, literally a vector with position
     * sliding
     */
    private int pos = 0;
    private Vector<Fragment> fragments = new Vector<>();

    @AnimRes private int nBackIn = R.anim.enter_left;
    @AnimRes private int nBackOut = R.anim.exit_right;
    @AnimRes private int nForIn = R.anim.enter_right;
    @AnimRes private int nForOut = R.anim.exit_left;
    @AnimRes private int nPopIn = R.anim.enter_bottom;
    @AnimRes private int nPopOut = R.anim.exit_top;

    private AppCompatActivity mAct;

    private FragmentRouter ourInstance;

    private final Lock lock = new ReentrantLock();

    private FragmentChangeListener fragmentListener;
    public interface FragmentChangeListener {
        void onFragmentChanged(Fragment current);
    }

    public FragmentRouter(AppCompatActivity act) {
        into( act );
    }

    public FragmentRouter withAnimations(@AnimRes int navBackIn, @AnimRes int navBackOut,
                               @AnimRes int navForwardIn, @AnimRes int navForwardOut,
                               @AnimRes int navPopIn, @AnimRes int navPopOut ) {
        this.nBackIn = navBackIn;
        this.nBackOut = navBackOut;
        this.nForIn = navForwardIn;
        this.nForOut = navForwardOut;
        this.nPopIn = navPopIn;
        this.nPopOut = navPopOut;
        return this;
    }

    public FragmentRouter into( AppCompatActivity act ) {
        clearAllNav();
        this.mAct = act;
        return this;
    }

    /**
     * Accessible methods for navigations, fragmentRouter must have been initialzied with
     * a valid AppCompatActivity or else the methods will fail
     */
    public void addFragmentChangeListener( FragmentChangeListener listen ) {
        fragmentListener = listen;
    }

    // API
    public Fragment getCurrent() {
        return pos < 0 || pos > fragments.size() - 1 ? null : fragments.get(pos);
    }

    // API
    public boolean canGoBack() {
        return fragments.size() > 0 && pos > 0;
    }

    // API
    public boolean canGoForward() {
        return fragments.size() > 0 && pos < fragments.size() - 1;
    }

    // API
    public void navigateTo( int container, Fragment fragment ) {
        // add to the end of forward navigation
        lock.lock();
        int exist = _exists( fragment.getClass() );
        if( exist == -1 ) {
            fragments.add( fragment );
            pos = fragments.size() - 1;
        } else {
            pos = exist;
        }
        lock.unlock();
        _handleNavigation( container, nPopIn, nPopOut );
    }

    // API
    public void navigateTo( int container, Class<? extends Fragment> clz ) {
        lock.lock();
        int exist = _exists( clz );
        if( exist == -1 ) {
            // only create if it doesn't exits
            Fragment f = Fragment.instantiate( mAct, clz.getCanonicalName() );
            fragments.add( f );
            pos = fragments.size() - 1;
        } else {
            pos = exist;
        }
        lock.unlock();
        _handleNavigation( container, nPopIn, nPopOut );
    }

    // API
    public void navigateBack( int container ) {
        if( pos > 0 ) {
            _ignoreFragmentManagerBackstack();

            pos--;
            _handleNavigation( container, nBackIn, nBackOut );
        }
    }

    // API
    public void navigateForward( int container ) {
        if( pos < fragments.size() - 1 ) {
            _ignoreFragmentManagerBackstack();

            pos++;
            _handleNavigation( container, nForIn, nForOut );
        }
    }

    private synchronized int _exists( Class<? extends Fragment> frag ) {
        for( int i = 0; i < fragments.size(); i++ ) {
            Fragment cur = fragments.get(i);
            if( cur.getClass().isAssignableFrom( frag ) ) {
                return i;
            }
        }
        return -1;
    }

    private void _ignoreFragmentManagerBackstack() throws NullPointerException {
        if( mAct == null ) {
            throw new NullPointerException("FragmentRouter must be initialized with a valid AppCompatActivity");
        }
        mAct.getSupportFragmentManager()
                .popBackStack();
    }

    private void _handleNavigation( int container, @AnimRes int animIn, @AnimRes int animOut ) {
        Fragment current = getCurrent();
        if(  current == null ) {
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
    public void clearAllNav() {
        pos = -1;

        Fragment current = getCurrent();
        if( current != null ) {
            mAct.getSupportFragmentManager()
                    .beginTransaction()
                    .hide( current )
                    .commit();

            if( fragmentListener != null ) fragmentListener.onFragmentChanged( null );
        }

        fragments.clear();
    }

    // API
    public synchronized void clearForwardNav() {
        lock.lock();
        int cut = fragments.size() - 1 - pos;
        while( cut > 0 ) {
            fragments.remove( pos + 1 );
            fragments.remove( pos + 1 );
            cut--;
        }
        lock.unlock();
    }

    // API
    public synchronized void clearBackNav() {
        lock.lock();
        for( int i = 0; i < pos; i++ ) {
            fragments.remove(0);
        }
        pos = 0;
        lock.unlock();
    }

}
