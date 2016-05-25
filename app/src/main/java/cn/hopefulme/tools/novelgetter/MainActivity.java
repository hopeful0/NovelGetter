package cn.hopefulme.tools.novelgetter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PageParser pageParser;

    NovelParser novelParser;

    AlertDialog inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------
        //test code

        showEditTextDialog();

        //----------------
    }

    private void showEditTextDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("输入源")
                .setView(R.layout.dialog_input)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et_input = (EditText) inputDialog.getWindow().findViewById(R.id.et_input);
                        String input = et_input.getText().toString().trim();
                        if(!input.startsWith("http://")) input = "http://" + input;
                        parserNovel(input);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false);
        inputDialog = builder.create();
        inputDialog.show();
    }

    private void parserNovel(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this))
                .setTitle("Loading...")
                .setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        novelParser = new NovelParser(url, new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.tv_source)).setText("小说内容来自:"+url);
                        dialog.dismiss();
                        parserPage(novelParser.getListLink(0));
                    }
                });
            }
        });

        findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageParser.getLast() == null) return;
                parserPage(pageParser.getLast());
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageParser.getNext() == null) return;
                parserPage(pageParser.getNext());
            }
        });
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });
    }

    private void parserPage(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this))
                .setTitle("Loading...")
                .setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        pageParser = new PageParser(url,new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.tv_bookname)).setText(pageParser.getBookName());
                        ((TextView)findViewById(R.id.tv_content)).setText(pageParser.getContent());
                        dialog.dismiss();
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
                        parserPage(novelParser.getListLink(which));
                        dialog.dismiss();
                    }
                }).show();
    }
}
