package mrfu.rxface.loader;

import java.io.UnsupportedEncodingException;

import retrofit.mime.TypedByteArray;

/**
 * 重写 TypedByteArray, 使其编码格式为 US-ASCII
 * Created by MrFu on 15/12/16.
 */
public class AsciiTypeString extends TypedByteArray {

    public AsciiTypeString(String string) {
        super("text/plain; charset=US-ASCII", convertToBytes(string));
    }

    private static byte[] convertToBytes(String string) {
        try {
            return string.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public String toString() {
        try {
            return "TypedString[" + new String(getBytes(), "US-ASCII") + "]";
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Must be able to decode US-ASCII");
        }
    }
}
