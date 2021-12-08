package net.lht.common.crypto;

import net.lht.common.codec.ByteArrayUtils;

public class CRC {

    /**
     * 计算CRC16（Modbus格式）
     * 
     * @param bytes 待校验字节数组
     * @return CRC字节数组
     */
    public static byte[] getModbusCRC16(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        byte[] crc = new byte[2];
        ByteArrayUtils.copy(ByteArrayUtils.intToByteArray4(Integer.reverseBytes(CRC)), crc, 0, 0, 2);
        return crc;
    }

}
