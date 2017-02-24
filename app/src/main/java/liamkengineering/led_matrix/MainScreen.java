package liamkengineering.led_matrix;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;


import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class MainScreen extends AppCompatActivity {

    BluetoothSocket btSocket = null;
    OutputStream[] outStream = btSetup();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");;
    private VisionServiceClient client;
    Bitmap mBitmap;
    View v;
    DrawView myC;

    public OutputStream[] btSetup() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            OutputStream myoutStream;
            OutputStream[] returnStream = new OutputStream[pairedDevices.size()];
            if (pairedDevices.size() > 0) {
                int counter = 0;

                for (BluetoothDevice device : pairedDevices) {
                    try {
                        btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                        btSocket.connect();
                        myoutStream = btSocket.getOutputStream();
                        returnStream[counter] = myoutStream;

                        counter++;

                    } catch (IOException e) {

                    }

                }
            }
            return returnStream;
        }
        else return null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

        if (client==null){
            client = new VisionServiceRestClient("2b5a021bfd0c4535b4a41b13678c6e1e");
        }
        setContentView(R.layout.content_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        v = findViewById(R.id.canvas);
        myC = (DrawView)v;
        View analyze = (Button)findViewById(R.id.analyze);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBitmap = getBitmapFromView(v);
                try {
                    if(mBitmap == null)
                    {
                        Toast toast = Toast.makeText(MainScreen.this, "NPE bitmap", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        saveBMP(mBitmap);
                        //ImageView imageView = (ImageView)findViewById(R.id.img);
                        //imageView.setImageBitmap(mBitmap);
                        Toast toast = Toast.makeText(MainScreen.this,googleAnalyze(mBitmap), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                catch(Exception e) {

                    Toast toast = Toast.makeText(MainScreen.this, e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        View reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myC.l.clear();
            }
        });
        if(textRecognizer.isOperational()) {
            Toast t = Toast.makeText(MainScreen.this, "operational", Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            Toast t = Toast.makeText(MainScreen.this, "not operational", Toast.LENGTH_SHORT);
            t.show();
        }
    }
    private String googleAnalyze(Bitmap imageBitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

        if(!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w("app", "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this,"Low Storage", Toast.LENGTH_LONG).show();
                Log.w("app", "Low Storage");
            }
        }


        Frame imageFrame = new Frame.Builder()
                .setBitmap(imageBitmap)
                .build();

        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

            Log.i("app", textBlock.getValue());
            Toast t = Toast.makeText(MainScreen.this, textBlock.getValue(), Toast.LENGTH_SHORT);
            t.show();
            StringRecognizer sLet = new StringRecognizer(textBlock.getValue().toLowerCase(), "letter", true);
            String let = sLet.wordRecognizer();
            if(let != null) // if it's not null, it'll be a letter
            {
                sendData(let.charAt(0));
            }
            StringRecognizer sHeart = new StringRecognizer(textBlock.getValue().toLowerCase(), "heart", false);
            String hrt = sHeart.wordRecognizer();
            if(hrt !=null) {
                sendData('*');
            }
            // Do something with value
        }
        return "Analyzed!";
        //return textBlocks.toString();
    }
    private String analyze(Bitmap mBitmap) throws VisionServiceException, IOException {
        Gson gson = new Gson();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        OCR ocr;
        ocr = this.client.recognizeText(inputStream, LanguageCodes.AutoDetect, true);

        String result = gson.toJson(ocr);
        Log.d("lul", result);
        Toast toast = Toast.makeText(this, result, Toast.LENGTH_LONG);
        toast.show();

        return result;
    }

    public Bitmap getBitmapFromView(View view) {
        v.buildDrawingCache();
        mBitmap = v.getDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);

        view.draw(canvas);
        return returnedBitmap;
    }

    public Bitmap takeScreenshotForView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
        view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(), (int) view.getY() + view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }
    // test method for seeing if the bmp is created correctly
    public void saveBMP(Bitmap b) {
        FileOutputStream f = null;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "/test.png");
            f = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, f);
        }
        catch (FileNotFoundException e) {
            Toast fnf = Toast.makeText(MainScreen.this, "fnf", Toast.LENGTH_SHORT);
            fnf.show();
        }
        finally {
            try {
                if(f != null) {
                    f.close();
                }

            } catch (IOException e) {
                Toast IOexcept = Toast.makeText(MainScreen.this, "IO except", Toast.LENGTH_SHORT);
                IOexcept.show();
        }
        }
    }
    public void sendData(char sendChar) {
		/*BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices) {
				try {
					btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
					btSocket.connect();
					outStream = btSocket.getOutputStream();

					}
				catch (IOException e) {

				}
		*/
        byte[] b1 = {(byte)sendChar};
        for(OutputStream stream: outStream) {


            try {

                stream.write(b1);
            }
            catch (IOException e) {

            }
        }

        //}
        //}
    }
}
