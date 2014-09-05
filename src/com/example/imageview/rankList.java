package com.example.imageview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

public class rankList extends Activity {
    ListView listview;
    EditText text;
    String jsonString = "";
    String urlRankList;
    List<Map<String, Object>> listems;
    private ProgressBar  bar3;
    Handler rankHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                bar3.setVisibility(View.GONE);
                break;
            case 2:
                text.setText(jsonString);
                break;
            
            }
            super.handleMessage(msg);
        }
    };
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranklist);
        text = (EditText) findViewById(R.id.ranklistedit);
        text.setText("网络排名");
        text.clearFocus();     
        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        urlRankList = b.getString("urlRankList");
        listview = (ListView) findViewById(R.id.ranklistview);
        listems = new ArrayList<Map<String, Object>>();
         bar3 = (ProgressBar)findViewById(R.id.bar3);
         bar3.setVisibility(View.VISIBLE);

        /*
         * for (int i = 0; i < name.length; i++) { Map<String, Object> listem =
         * new HashMap<String, Object>(); listem.put("name", name[i]);
         * listem.put("desc", desc[i]); listems.add(listem); }
         */

        /*
         * SimpleAdapter的参数说明 第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
         * 第二个参数表示生成一个Map(String ,Object)列表选项 第三个参数表示界面布局的id 表示该文件作为列表项的组件
         * 第四个参数表示该Map对象的哪些key对应value来生成列表项 第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件
         * 顺序有对应关系 注意的是map对象可以key可以找不到 但组件的必须要有资源填充 因为 找不到key也会返回null
         * 其实就相当于给了一个null资源 下面的程序中如果 new String[] { "name", "head",
         * "desc","name" } new int[] {R.id.name,R.id.head,R.id.desc,R.id.head}
         * 这个head的组件会被name资源覆盖
         */
        // listem.put("name", name[i]);
        // listem.put("desc", desc[i]);
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.listitem, new String[] { "user", "gameTime","createTime" },
                new int[] { R.id.gameuser, R.id.gametime,R.id.createtime });

        listview.setAdapter(simplead);
        new Thread() {
            public void run() {
           int res = 0;
           HttpClient client = new DefaultHttpClient();
           StringBuilder str = new StringBuilder();
           HttpGet httpGet = new HttpGet(urlRankList);
           try {
               HttpResponse httpRes = client.execute(httpGet);
               // 请求超时
               client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
               // 读取超时
               client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000    );
               httpRes = client.execute(httpGet);
               res = httpRes.getStatusLine().getStatusCode();
               if (res == 200) {
                   BufferedReader buffer = new BufferedReader(
                           new InputStreamReader(httpRes.getEntity()
                                   .getContent()));
                   for (String s = buffer.readLine(); s != null; s = buffer
                           .readLine()) {
                       str.append(s);
                   }
                   String newstr = new String(str.toString().getBytes(),
                           "UTF-8");

                   JSONArray jsonArray = new JSONArray(newstr);

                   for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject jsonElement = (JSONObject) jsonArray
                               .opt(i);
                       String user = jsonElement.getString("user");
                       String gameTime = jsonElement
                               .getString("game_time");
                      String createTime = jsonElement.getString("create_time");

                       Map<String, Object> listem = new HashMap<String, Object>();
                       listem.put("user", user);
                       listem.put("gameTime", gameTime);
                       listem.put("createTime", createTime);


                       listems.add(listem);
                       jsonString = "网络排名";

                   }
                   /*
                    * //jsonString = ""; for (int i = 0; i <
                    * jsonArray.length(); i++) { JSONObject jsonElement =
                    * (JSONObject) jsonArray.opt(i); String title =
                    * jsonElement.getString("title"); String value =
                    * jsonElement.getString("value"); //jsonString += title
                    * + value; }
                    */
                  
               } else {
                   jsonString = "对不起 连接网络超时";

               }
               Message message2 = new Message();
               message2.what = 2;
               rankHandler.sendMessage(message2);
               
               Message message1 = new Message();
               message1.what = 1;
               rankHandler.sendMessage(message1);
           } catch (Exception e) {
               jsonString = "对不起 连接网络超时";
               Message message2 = new Message();
               message2.what = 2;
               rankHandler.sendMessage(message2);
               
               Message message1 = new Message();
               message1.what = 1;
               rankHandler.sendMessage(message1);
               e.printStackTrace();
           }

            }

            }.start();
    }
}