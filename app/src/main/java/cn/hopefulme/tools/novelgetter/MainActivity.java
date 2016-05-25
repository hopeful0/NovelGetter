package cn.hopefulme.tools.novelgetter;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PageParser pageParser;

    NovelParser novelParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------
        //test code
        novelParser = new NovelParser("http://www.biquge.la/book/176/");

        parser("http://www.biquge.la/book/176/143150.html");

        findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageParser.getLast() == null) return;
                parser(pageParser.getLast());
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageParser.getNext() == null) return;
                parser(pageParser.getNext());
            }
        });
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
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

    private void showListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("章节列表")
                .setItems(novelParser.getListLinkTexts(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parser(novelParser.getListLink(which));
                        dialog.dismiss();
                    }
                }).show();
    }
}
