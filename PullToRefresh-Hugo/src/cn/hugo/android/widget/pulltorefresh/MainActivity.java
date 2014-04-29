package cn.hugo.android.widget.pulltorefresh;

import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.hugo.android.widget.pulltorefresh.lib.PullToRefreshBase.OnRefreshListener;
import cn.hugo.android.widget.pulltorefresh.lib.PullToRefreshListView;

/**
 * 
 * @ClassName MainActivity.java
 * @Author hugo
 * @Date 2013-10-30 pm 02:56:09
 * @Desc 此例主要是展示上拉、下拉和上下拉刷新
 */
public class MainActivity extends Activity {

	private LinkedList<String> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessMethod();
	}

	private void initPage() {
		setContentView(R.layout.activity_main_example_both2refresh);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pullrefresh);
	}

	private void setClickListener() {
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
	}

	private void businessMethod() {
		mListView = mPullRefreshListView.getRefreshableView();

		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mListItems);
		// You can also just use setListAdapter(mAdapter)
		mListView.setAdapter(mAdapter);
	}

	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			new GetDataTask(mPullRefreshListView.getRefreshType()).execute();
		}
	};

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		int pullState;

		public GetDataTask(int pullType) {
			this.pullState = pullType;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {

			if (pullState == PullToRefreshListView.MODE_PULL_DOWN_TO_REFRESH) {
				mListItems.addFirst("Added after refresh by first...");
			}
			if (pullState == PullToRefreshListView.MODE_PULL_UP_TO_REFRESH) {
				mListItems.addLast("Added after refresh by last...");
			}

			mAdapter.notifyDataSetChanged();

			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private String[] mStrings = { "Abbaye de Belloc" };
}
