package edu.ucsb.cs.cs184.grousseva.groussevageopictures;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MarkerDialogue extends DialogFragment {
    public MarkerDialogue() {
    }

    public static String titleFrag;
    public static String timestampFrag;


    public static MarkerDialogue newInstance(String title, String timestamp)
    {
        MarkerDialogue markerFrag = new MarkerDialogue();
        Bundle args = new Bundle();
        titleFrag = title;
        timestampFrag = timestamp;
        args.putString("title", title);
        args.putString("timestamp", timestamp);
        markerFrag.setArguments(args);
        return markerFrag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout2 = layoutInflater.inflate(R.layout.dialog_fragment, null);
        dialog.setContentView(layout2);

        ImageView myImage = (ImageView) layout2.findViewById(R.id.imageView);
        TextView myText = (TextView) layout2.findViewById(R.id.textView);


        String formattedTimeStamp = String.format("%.0f", Double.parseDouble(timestampFrag.toString()));

        DateFormat convertedTime = new SimpleDateFormat("MMM dd yyyy HH:mm:ss" );

        Date finalTime = new Date(Long.parseLong(formattedTimeStamp));

        myText.setText(convertedTime.format(finalTime));



        String formattedTitle = String.format("%.0f", Double.parseDouble(titleFrag.toString()));

        File imgFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/" + formattedTitle + ".jpg");
        Log.i("marker dialogue", String.valueOf(imgFile.exists()));
        Log.i("PATH!!!!", imgFile.getAbsolutePath());
        Log.i("TITLE!!!!", String.valueOf(formattedTitle));

        if (imgFile.exists()) {

            Log.i("File exists", "hi");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myImage.setImageBitmap(myBitmap);
        }

        Log.i("marker dialogue", String.valueOf(imgFile.exists()));


        dialog.setTitle(timestampFrag);
        dialog.show();

        return dialog;
    }

}