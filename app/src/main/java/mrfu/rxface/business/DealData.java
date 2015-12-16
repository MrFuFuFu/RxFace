package mrfu.rxface.business;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;

import mrfu.rxface.loader.AsciiTypeString;
import mrfu.rxface.loader.CustomMultipartTypedOutput;
import mrfu.rxface.loader.CustomTypedByteArray;
import mrfu.rxface.models.FacePlusPlusEntity;

/**
 * Created by MrFu on 15/12/15.
 */
public class DealData {

    /**
     * 设置参数
     * 使用 MultipartTypedOutput, 而不使用 Retrofit @Multipart 的 @Part 和 @PartMap 的形式
     * http://stackoverflow.com/questions/25249042/retrofit-multiple-images-attached-in-one-multipart-request/25260556#25260556
     *
     * @see CustomMultipartTypedOutput 重写 MultipartTypedOutput 使之接受 boundary 参数
     * @see AsciiTypeString , 重写 TypedByteArray, 使其编码格式为 US-ASCII
     * @see CustomTypedByteArray , 重写 TypedByteArray 设置其 fileName 为 "NoName",
     * 以上参数格式和参数类型都必须指定,否则会返回对应的错误
     * http://www.faceplusplus.com.cn/detection_detect/
     * @param bitmap need upload image
     * @param boundary must same with http header boundary
     * @return http post body param
     */
    public static CustomMultipartTypedOutput mulipartData(Bitmap bitmap, String boundary){
        byte[] data = getBitmapByte(bitmap);
        CustomMultipartTypedOutput multipartTypedOutput = new CustomMultipartTypedOutput(boundary);
        multipartTypedOutput.addPart("api_key", "8bit", new AsciiTypeString(Constants.API_KEY));
        multipartTypedOutput.addPart("api_secret", "8bit", new AsciiTypeString(Constants.API_SECRET));
        multipartTypedOutput.addPart("img", new CustomTypedByteArray("application/octet-stream", data));
        return multipartTypedOutput;
    }

    /**
     * convert bitmap to byte[]
     * @param bitmap need convert bitmap
     * @return byte[]
     */
    private static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = Math.min(1, Math.min(600f / bitmap.getWidth(), 600f / bitmap.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap imgSmall = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * draw to include face bitmap
     * @param entity data
     * @param viewBitmap display bitmap
     * @return face bitmap
     */
    public static Bitmap drawLineGetBitmap(FacePlusPlusEntity entity, Bitmap viewBitmap){
        //use the red paint
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(Math.max(viewBitmap.getWidth(), viewBitmap.getHeight()) / 100f);

        //create a new canvas
        Bitmap bitmap = Bitmap.createBitmap(viewBitmap.getWidth(), viewBitmap.getHeight(), viewBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(viewBitmap, new Matrix(), null);

        try {
            //find out all faces
            final int count = entity.face.size();
            for (int i = 0; i < count; ++i) {
                float x, y, w, h;
                //get the center point
                x = (float) entity.face.get(i).position.center.x;
                y = (float) entity.face.get(i).position.center.y;
                //get face size
                w = (float) entity.face.get(i).position.width;
                h = (float) entity.face.get(i).position.height;
                //change percent value to the real size
                x = x / 100 * viewBitmap.getWidth();
                w = w / 100 * viewBitmap.getWidth() * 0.7f;
                y = y / 100 * viewBitmap.getHeight();
                h = h / 100 * viewBitmap.getHeight() * 0.7f;
                //draw the box to mark it out
                canvas.drawLine(x - w, y - h, x - w, y + h, paint);
                canvas.drawLine(x - w, y - h, x + w, y - h, paint);
                canvas.drawLine(x + w, y + h, x - w, y + h, paint);
                canvas.drawLine(x + w, y + h, x + w, y - h, paint);
            }
            //save new image
            viewBitmap = bitmap;
            return viewBitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getDisplayInfo(FacePlusPlusEntity entity){
        if (entity == null || entity.face == null || entity.face.size() == 0
                || entity.face.get(0).attribute == null){
            return "";
        }
        int age = 0;
        if (entity.face.get(0).attribute.age != null){
            age = entity.face.get(0).attribute.age.value;
        }
        String gender = "";
        if (entity.face.get(0).attribute.gender != null){
            gender = "male".equalsIgnoreCase(entity.face.get(0).attribute.gender.value) ? "男" : "女";
        }
        return "年龄约 " + age + " 性别为 " + gender;
    }
}
