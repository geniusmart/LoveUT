package com.geniusmart.loveut.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geniusmart.loveut.R;
import com.geniusmart.loveut.net.GithubService;
import com.geniusmart.loveut.net.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackActivity extends Activity {

    private ListView listView;
    private List<String> datas;
    private ProgressBar progressBar;
    /**
     * 定义一个全局的callback对象，并暴露出get方法供UT调用
     */
    private Callback<List<User>> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CallbackActivity.this,datas.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        //加载数据
        loadData();
    }

    public void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        datas = new ArrayList<>();
        //初始化回调函数对象
        callback = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                for(User user : response.body()){
                    datas.add(user.login);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CallbackActivity.this,
                        android.R.layout.simple_list_item_1, datas);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        };
        GithubService githubService = GithubService.Factory.create();
        githubService.followingUser("geniusmart").enqueue(callback);
    }

    public Callback<List<User>> getCallback(){
        return callback;
    }
}
