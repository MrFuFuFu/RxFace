package mrfu.rxface.loader;

import retrofit.mime.TypedByteArray;

/**
 * 重写 TypedByteArray 设置其 fileName 为 "NoName",
 * Created by MrFu on 15/12/16.
 */
public class CustomTypedByteArray extends TypedByteArray {
    /**
     * Constructs a new typed byte array.  Sets mimeType to {@code application/unknown} if absent.
     *
     * @param mimeType
     * @param bytes
     * @throws NullPointerException if bytes are null
     */
    public CustomTypedByteArray(String mimeType, byte[] bytes) {
        super(mimeType, bytes);
    }

    @Override
    public String fileName() {
        return "NoName";
    }
}
