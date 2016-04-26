package com.shzlabs.app.hnapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shzlabs.app.hnapp.model.NewsItem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass..
 *
 * Activities that contain this fragment must implement the
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    View rootView;
    Context ctx;
    ListView newsListView;
    Firebase mFirebaseRef, topStoriesFBRef;
    NewsListAdapter mNewsListAdapter;
    ArrayList<NewsItem> newsItemsList = new ArrayList<>();

    public MainListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainListFragment.
     */
    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        String param1 = "Nothing";
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        topStoriesFBRef = new Firebase(MainActivity.FIREBASE_URL).child("topstories");

        mFirebaseRef = new Firebase(MainActivity.FIREBASE_URL).child("item").child("8863");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
        ctx = getActivity();

        // Link all views
        newsListView = (ListView) rootView.findViewById(R.id.newsListView);

        /* on click listeners */
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((TextView)view.findViewById(R.id.news_complete_url)).getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)) ;
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup top-stories firebase listener
        topStoriesFBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot eventIDsnapshot: dataSnapshot.getChildren()) {
                    Firebase eventFBRef = new Firebase(MainActivity.FIREBASE_URL).child("item").child(String.valueOf(eventIDsnapshot.getValue()));
                    eventFBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.d("shzValue", "=====" + snapshot.child("id").getValue());
                            if(snapshot.child("url").getValue() != null) {
                                newsItemsList.add(new NewsItem(snapshot.child("title").getValue().toString(),
                                        snapshot.child("url").getValue().toString(),
                                        snapshot.child("type").getValue().toString(),
                                        snapshot.child("by").getValue().toString(),
                                        (long) snapshot.child("id").getValue(),
                                        (long) snapshot.child("score").getValue(),
                                        (long) snapshot.child("time").getValue()));
                                mNewsListAdapter.refresh(newsItemsList);
                                Log.d("shzValue", "Fetched data");
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                try {
                    ((MainActivity)getActivity()).setSubTitle("Top News");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        mNewsListAdapter = new NewsListAdapter(newsItemsList);
        newsListView.setAdapter(mNewsListAdapter);

        Log.d("shzValue", "Adapter Set");

    }

    class NewsListAdapter extends BaseAdapter{

        ArrayList<NewsItem> itemsList;
        LayoutInflater inflater;

        NewsListAdapter(ArrayList<NewsItem> newsItemsList){
            inflater = LayoutInflater.from(ctx);
            itemsList = newsItemsList;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return itemsList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.main_news_list_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.news_title);
                holder.completeUrl = (TextView) convertView.findViewById(R.id.news_complete_url);
                holder.displayUrl = (TextView) convertView.findViewById(R.id.news_display_url);
                holder.itemNo = (TextView) convertView.findViewById(R.id.item_no);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itemNo.setText(position+1 + ".");
            holder.title.setText(itemsList.get(position).getTitle());
            String url = itemsList.get(position).getUrl();
            holder.completeUrl.setText(url);
            String hostName = "";
            try {
                URI uri = new URI(url);
                hostName = uri.getHost();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            holder.displayUrl.setText(hostName.startsWith("www.") ? hostName.substring(4):hostName);

            return convertView;
        }

        public void refresh(ArrayList<NewsItem> list){
            Log.d("shzValue",  "In refresh " + itemsList.size() + itemsList.get(0).getTitle());
            notifyDataSetChanged();
        }

        class ViewHolder{
            TextView itemNo;
            TextView title;
            TextView completeUrl;
            TextView displayUrl;
        }
    }

}
