package info.haoxiqiang.webpv8;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.webp.libwebp;

public class MainActivity extends ActionBarActivity {

    private ImageView above14, below14;


    static {
        System.loadLibrary("webp");
    }

    private Bitmap webpToBitmap(byte[] encoded) {

        int[] width = new int[] {0};
        int[] height = new int[] {0};
        byte[] decoded = libwebp.WebPDecodeARGB(encoded, encoded.length, width, height);

        int[] pixels = new int[decoded.length / 4];
        ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);

        return Bitmap.createBitmap(pixels, width[0], height[0], Bitmap.Config.ARGB_8888);

    }

    public static byte[] streamToBytes(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        above14 = (ImageView) findViewById(R.id.above14);
        below14 = (ImageView) findViewById(R.id.below14);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            above14.setImageResource(R.drawable.webp_800c);
        }

        InputStream rawImageStream = getResources().openRawResource(R.drawable.webp_800c);
        byte[] data = streamToBytes(rawImageStream);
        final Bitmap webpBitmap = webpToBitmap(data);
        below14.setImageBitmap(webpBitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
