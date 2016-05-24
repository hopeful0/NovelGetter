package cn.hopefulme.tools.novelgetter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------
        //test code

        final PageParser pageParser = new PageParser("http://www.biquge.tw/9_9080/5134179.html");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.tv_bookname)).setText(pageParser.getBookName());
                ((TextView)findViewById(R.id.tv_content)).setText(pageParser.getContent());
            }
        },1000);


        //----------------
    }
}
