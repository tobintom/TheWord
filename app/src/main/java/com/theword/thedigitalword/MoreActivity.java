package com.theword.thedigitalword;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.Calendar;
import java.util.List;

public class MoreActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    BottomNavigationView navigationView = null;

    TextView faceb = null;
    TextView twit = null;
    TextView email = null;
    TextView about = null;
    TextView help = null;
    TextView rate = null;
    TextView share = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        TextView v = (TextView) findViewById(R.id.more_footer1);
        v.setText("\u00a9 "+ Calendar.getInstance().get(Calendar.YEAR)+" The Digital Word");

        //views
        about = (TextView)findViewById(R.id.aboutm);
        help = (TextView)findViewById(R.id.helpm);
        rate = (TextView)findViewById(R.id.ratem);
        share = (TextView)findViewById(R.id.sharem);

        //Rate
        rate.setOnClickListener(v1 -> {
            try{
                Intent rateIntent = rateIntentForUrl("market://details");
                startActivity(rateIntent);
            }catch (ActivityNotFoundException e){
                Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
                startActivity(rateIntent);
            }
        });

        //Share
        share.setOnClickListener(v1 -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word");
                String shareMessage= "\nTry out The Digital Word Bible app by thedigitalword.org.\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });

        help.setOnClickListener(v1 -> {
            Intent i = new Intent(this,HelpActivity.class);
            startActivity(i);
        });

        about.setOnClickListener(v1 -> {
            Intent i = new Intent(this,AboutActivity.class);
            startActivity(i);
        });
        //Facebook
        faceb = (TextView)findViewById(R.id.facebookm);
        faceb.setOnClickListener(v1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/theworddigital"));
            startActivity(browserIntent);
        });
        //Twitter
        twit = (TextView)findViewById(R.id.twitterm);
        twit.setOnClickListener(v1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/digitalTheWord"));
            startActivity(browserIntent);
        });
        //Feedback
        email = (TextView)findViewById(R.id.emailm);
        email.setOnClickListener(v1 -> {
            startActivity(createEmailIntent("thewordhelp@gmail.com","Feedback: The Digital Word",""));
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_read:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        break;
                    case R.id.action_me:
                        startActivity(new Intent(getApplicationContext(), MeActivity.class));
                        break;
                    case R.id.action_search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        break;
                    case R.id.action_today:
                        startActivity(new Intent(getApplicationContext(), TodayActivity.class));
                        break;
                    default:

                        break;
                }
                return true;
            }
        });
        navigationView.setSelectedItemId(R.id.action_more);

    }

    private  Intent createEmailIntent(final String toEmail,
                                           final String subject,
                                           final String message)
    {
        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);
        sendTo.setData(uri);

        List<ResolveInfo> resolveInfos =
                getPackageManager().queryIntentActivities(sendTo, 0);

        // Emulators may not like this check...
        if (!resolveInfos.isEmpty())
        {
            return sendTo;
        }

        // Nothing resolves send to, so fallback to send...
        Intent send = new Intent(Intent.ACTION_SEND);

        send.setType("message/rfc822");
        send.putExtra(Intent.EXTRA_EMAIL,
                new String[] { toEmail });
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, message);

        return Intent.createChooser(send, "Send Feedback email..");
    }

    private Intent rateIntentForUrl(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21){
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }else{
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        finishAffinity();
    }
}
