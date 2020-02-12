package com.example.meetpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Template extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);

        final boolean hasConfirmation = getIntent().getBooleanExtra("hasConfirmation", false);

        findViewById(R.id.match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Template.this, Matches.class);
                    startActivity(n);
                    Template.this.finish();
                }
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Template.this, NearYou.class);
                    startActivity(n);
                    Template.this.finish();
                }
            }
        });

        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConfirmation)
                    showConfirmDialog(ProfileSelf.class);
                else {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Intent n = new Intent(Template.this, ProfileSelf.class);
                    startActivity(n);
                    Template.this.finish();
                }
            }
        });


    }

    /**
     * @param idLayout
     */
    protected void addContent(int idLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(idLayout, null);
        ((LinearLayout) findViewById(R.id.content)).addView(v);
    }

    /**
     *
     */
    private void showConfirmDialog(final Class target) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Descartar cambios")
                .setMessage("Los cambios realizados no se guardarán")
                .setCancelable(false)
                .setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent n = new Intent(Template.this, target);
                        startActivity(n);
                        Template.this.finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
