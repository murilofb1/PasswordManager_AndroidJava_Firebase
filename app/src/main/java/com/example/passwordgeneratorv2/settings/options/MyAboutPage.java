package com.example.passwordgeneratorv2.settings.options;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordgeneratorv2.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class MyAboutPage extends AppCompatActivity {
    private Element elementWhats = new Element();
    private Element elementGmail = new Element();
    private Element elementLinkedin = new Element();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNewElements();
        View aboutPage = new AboutPage(this)
                .setImage(R.drawable.myself_round)
                .setDescription(getString(R.string.about_page_description))
                .addGitHub("murilofb1", getString(R.string.element_github_title))
                .addItem(elementLinkedin)
                .addItem(elementWhats)
                .addItem(elementGmail)
                .create();

        setContentView(aboutPage);
    }

    private void setNewElements() {
        Intent intentWhats = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+5532999074177"));
        elementWhats.setTitle(getString(R.string.element_whats_title));
        elementWhats.setIconDrawable(R.drawable.ic_whatsapp_bigger);
        elementWhats.setIntent(intentWhats);

        Intent intentLinkedin = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/murilofranciscob/"));
        elementLinkedin.setTitle(getString(R.string.element_linkedin_title));
        elementLinkedin.setIconDrawable(R.drawable.ic_linkedin);
        elementLinkedin.setIntent(intentLinkedin);

        Intent intentGmail = new Intent(Intent.ACTION_SENDTO);
        intentGmail.setData(Uri.parse("mailto:" + "murilofranciscobento@gmail.com"));
        elementGmail.setTitle(getString(R.string.element_gmail_title));
        elementGmail.setIconDrawable(R.drawable.ic_gmail);
        elementGmail.setIntent(intentGmail);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
