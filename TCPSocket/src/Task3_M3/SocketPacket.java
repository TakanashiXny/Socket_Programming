package Task3_M3;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

public class SocketPacket {
    // 消息头长度
    static final int HEAD_SIZE = 8;

    public byte[] toBytes(String message) {
        byte[] bodyByte = message.getBytes();
        int bodySize = bodyByte.length;

        byte[] result = new byte[HEAD_SIZE + bodySize];

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(HEAD_SIZE);
        numberFormat.setGroupingUsed(false);

        byte[] headByte = numberFormat.format(bodySize).getBytes();

        System.arraycopy(headByte, 0, result, 0, HEAD_SIZE);

        System.arraycopy(bodyByte, 0, result, HEAD_SIZE, bodySize);
        return result;
    }

    public int getHeader(InputStream inputStream) throws IOException, NumberFormatException {
        int result = 0;
        byte[] bytes = new byte[HEAD_SIZE];
        inputStream.read(bytes, 0, HEAD_SIZE);
        String msg = new String(bytes);
        result = Integer.valueOf(msg);
        return result;
    }

}
