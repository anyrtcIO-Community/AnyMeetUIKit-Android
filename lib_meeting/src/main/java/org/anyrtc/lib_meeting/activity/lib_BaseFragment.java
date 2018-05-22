package org.anyrtc.lib_meeting.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class lib_BaseFragment extends Fragment {
    protected static String TAG = "BaseFragment";
    protected Context mContext;
    protected Activity mActivity;
    protected View mContentView;
    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) context;
        TAG = this.getClass().getSimpleName();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {

            mContentView = inflater.inflate(getContentViewID(), null);
            initViewsAndEvents(mContentView, savedInstanceState);


        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    protected void startAnimActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
    }
    protected void startAnimActivity(Class<?> cls, String key, String value) {

        Intent intent = new Intent(mContext, cls);
        intent.putExtra(key, value);
        mContext.startActivity(intent);
    }

    public void startAnimActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startAnimActivity(Class<?> cls, boolean isFinish) {
        startAnimActivity(cls);
        if (isFinish) {
            getActivity().finish();
        }
    }

    /**
     * override this method to return content view id of the fragment
     */
    protected abstract int getContentViewID();

    /**
     * override this method to do operation in the fragment
     */
    protected abstract void initViewsAndEvents(View rootView, Bundle savedInstanceState);

}
