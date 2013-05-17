package com.lynx.widget.pulltorefreshlistview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * 
 * @author chris.liu
 * 
 */
public class RefreshableListView extends ListView {

	private static final int STATE_NORMAL = 0;
	private static final int STATE_READY = 1;
	private static final int STATE_PULL = 2;
	private static final int STATE_UPDATING = 3;
	private static final int INVALID_POINTER_ID = -1;

	private static final int MIN_UPDATE_TIME = 500;

	protected ListHeaderView mListHeaderView;

	private int mActivePointerId;
	private float mLastY;

	private int mState;

	private OnUpdateTask mOnUpdateTask;

	private int mTouchSlop;

	public RefreshableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public RefreshableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	/***
	 * Set the Header Content View.
	 * 
	 * @param id
	 *            The view resource.
	 */
	public void setContentView(int id) {
		final View view = LayoutInflater.from(getContext()).inflate(id,
				mListHeaderView, false);
		mListHeaderView.addView(view);
	}

	public void setContentView(View v) {
		mListHeaderView.addView(v);
	}

	public ListHeaderView getListHeaderView() {
		return mListHeaderView;
	}

	/**
	 * Setup the update task.
	 * 
	 * @param task
	 */
	public void setOnUpdateTask(OnUpdateTask task) {
		mOnUpdateTask = task;
	}

	/**
	 * Update immediately.
	 */
	public void startUpdateImmediate() {
		if (mState == STATE_UPDATING) {
			return;
		}
		setSelectionFromTop(0, 0);
		mListHeaderView.moveToUpdateHeight();
		update();
	}

	/**
	 * Set the Header View change listener.
	 * 
	 * @param listener
	 */
	public void setOnHeaderViewChangedListener(
			OnHeaderViewChangedListener listener) {
		mListHeaderView.mOnHeaderViewChangedListener = listener;
	}

	private void initialize() {
		final Context context = getContext();
		mListHeaderView = new ListHeaderView(context, this);
		addHeaderView(mListHeaderView, null, false);
		mState = STATE_NORMAL;
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	private void update() {
		if (mListHeaderView.isUpdateNeeded()) {
			if (mOnUpdateTask != null) {
				mOnUpdateTask.onUpdateStart();
			}
			mListHeaderView.startUpdate(new Runnable() {
				public void run() {
					final long b = System.currentTimeMillis();
					if (mOnUpdateTask != null) {
						mOnUpdateTask.updateBackground();
					}
					final long delta = MIN_UPDATE_TIME
							- (System.currentTimeMillis() - b);
					if (delta > 0) {
						try {
							Thread.sleep(delta);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					post(new Runnable() {
						public void run() {
							mListHeaderView.close(STATE_NORMAL);
							if (mOnUpdateTask != null) {
								mOnUpdateTask.updateUI();
							}
						}
					});
				}
			});
			mState = STATE_UPDATING;
		} else {
			mListHeaderView.close(STATE_NORMAL);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mState == STATE_UPDATING) {
			return super.dispatchTouchEvent(ev);
		}
		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mLastY = ev.getY();
			isFirstViewTop();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mActivePointerId == INVALID_POINTER_ID) {
				break;
			}

			if (mState == STATE_NORMAL) {
				isFirstViewTop();
			}

			if (mState == STATE_READY) {
				final int activePointerId = mActivePointerId;
				final int activePointerIndex = MotionEventCompat
						.findPointerIndex(ev, activePointerId);
				final float y = MotionEventCompat.getY(ev, activePointerIndex);
				final int deltaY = (int) (y - mLastY);
				mLastY = y;
				if (deltaY <= 0 || Math.abs(y) < mTouchSlop) {
					mState = STATE_NORMAL;
				} else {
					mState = STATE_PULL;
					ev.setAction(MotionEvent.ACTION_CANCEL);
					super.dispatchTouchEvent(ev);
				}
			}

			if (mState == STATE_PULL) {
				final int activePointerId = mActivePointerId;
				final int activePointerIndex = MotionEventCompat
						.findPointerIndex(ev, activePointerId);
				final float y = MotionEventCompat.getY(ev, activePointerIndex);
				final int deltaY = (int) (y - mLastY);
				mLastY = y;

				final int headerHeight = mListHeaderView.getHeight();
				setHeaderHeight(headerHeight + deltaY * 5 / 9);
				return true;
			}

			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mActivePointerId = INVALID_POINTER_ID;
			if (mState == STATE_PULL) {
				update();
			}
			break;
		case MotionEventCompat.ACTION_POINTER_DOWN:
			final int index = MotionEventCompat.getActionIndex(ev);
			final float y = MotionEventCompat.getY(ev, index);
			mLastY = y;
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = MotionEventCompat.getActionIndex(ev);
		final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
		if (pointerId == mActivePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastY = MotionEventCompat.getY(ev, newPointerIndex);
			mActivePointerId = MotionEventCompat.getPointerId(ev,
					newPointerIndex);
		}
	}

	void setState(int state) {
		mState = state;
	}

	private void setHeaderHeight(int height) {
		mListHeaderView.setHeaderHeight(height);
	}

	private boolean isFirstViewTop() {
		final int count = getChildCount();
		if (count == 0) {
			return true;
		}
		final int firstVisiblePosition = this.getFirstVisiblePosition();
		final View firstChildView = getChildAt(0);
		boolean needs = firstChildView.getTop() == 0
				&& (firstVisiblePosition == 0);
		if (needs) {
			mState = STATE_READY;
		}

		return needs;
	}

	/** When use custom List header view */
	public static interface OnHeaderViewChangedListener {
		/**
		 * When user pull the list view, we can change the header status here.
		 * for example: the arrow rotate down or up.
		 * 
		 * @param v
		 *            : the list view header
		 * @param canUpdate
		 *            : if the list view can update.
		 */
		void onViewChanged(View v, boolean canUpdate);

		/**
		 * Change the header status when we really do the update task. for
		 * example: display the progressbar.
		 * 
		 * @param v
		 *            the list view header
		 */
		void onViewUpdating(View v);

		/**
		 * Will called when the update task finished. for example: hide the
		 * progressbar and show the arrow.
		 * 
		 * @param v
		 *            the list view header.
		 */
		void onViewUpdateFinish(View v);
	}

	/** The callback when the updata task begin, doing. or finish. */
	public static interface OnUpdateTask {

		/**
		 * will called before the update task begin. Will Run in the UI thread.
		 */
		public void onUpdateStart();

		/**
		 * Will called doing the background task. Will Run in the background
		 * thread.
		 */
		public void updateBackground();

		/**
		 * Will called when doing the background task. Will Run in the UI
		 * thread.
		 */
		public void updateUI();

	}

}
