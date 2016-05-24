package cn.hopefulme.tools.novelgetter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PageParser pageParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------
        //test code
        parser("http://www.biquge.la/book/176/143150.html");

        findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parser(pageParser.getLast());
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parser(pageParser.getNext());
            }
        });
        //----------------
    }

    private void parser(String url) {
        pageParser = new PageParser(url,new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.tv_bookname)).setText(pageParser.getBookName());
                        ((TextView)findViewById(R.id.tv_content)).setText(pageParser.getContent());
                    }
                });
            }
        });
    }
}
