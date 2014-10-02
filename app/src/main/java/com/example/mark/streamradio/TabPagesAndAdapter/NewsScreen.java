package com.example.mark.streamradio.TabPagesAndAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mark.streamradio.NewsDetailActivity;
import com.example.mark.streamradio.NewsItemsData;
import com.example.mark.streamradio.R;

/**
 * Created by kalyan on 1/10/14.
 */
public class NewsScreen extends Fragment {

    NewsItemsData newsItemsData;
    ListView newsListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_news, container, false);

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        //News Paper Section

        newsListView = (ListView) rootView.findViewById(R.id.news_list);


        newsItemsData = new NewsItemsData();

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.news_list_item, R.id.newsListName, newsItemsData.titles);

        newsListView.setAdapter(newsAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String itemValue = (String) newsListView.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getActivity(),
                //        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                //        .show();

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
