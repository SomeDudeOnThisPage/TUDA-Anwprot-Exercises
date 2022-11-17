import java.util.Arrays;
import java.util.HashMap;

public final class Base64Decoder {
    private static final HashMap<Character, Byte> B64_LOOKUP = new HashMap<>() {{
        for (int i = 0; i < 26; i++) {
            put((char) ('A' + i), (byte) i);
            put((char) ('a' + i), (byte) (i + 26));
        }

        for (int j = 0; j < 10; j++) {
            put((char) ('0' + j), (byte) (j + 52));
        }

        put('+', (byte) 62);
        put('/', (byte) 63);
        put('=', (byte) 0);
    }};

    public static void main(String[] args) {
        System.out.println(decode("SGVsbG8="));
    }

    public static String decode(final String base64) {
        char[] data = base64.toCharArray();
        if (data.length % 4 != 0) {
            throw new RuntimeException("You absolute donkey!");
        }
        char[] output = new char[data.length * 3 / 4];
        System.out.println(Arrays.toString(data) + " " + data.length);

        // four chars = three bytes
        // we assume the base64 is correctly padded
        int o = 0;
        for (int i = 0; i < data.length; i+=4) {
            byte b0 = B64_LOOKUP.get(data[i]);
            byte b1 = B64_LOOKUP.get(data[i + 1]);
            byte b2 = B64_LOOKUP.get(data[i + 2]);
            byte b3 = B64_LOOKUP.get(data[i + 3]);

            int b = ((b0 << 18) + (b1 << 12) + (b2 << 6) + (b3));

            for (int j = 2; j >= 0; j--) {
                char c = (char) ((b >> (j * 8)) & 0xFF);
                output[o++] = c == 0
                        ? ' ' // TODO: Here it would be nicer to adjust size of char array but im lazy...
                        : c;
            }
        }

        return new String(output);
    }
}
