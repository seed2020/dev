package com.innobiz.orange.web.wf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class CompressStringUtil {
    private static final String charsetName = "UTF-8";

    /**
     * String 객체를 압축하여 String 으로 리턴한다.
     * @param string
     * @return
     */
    public synchronized static String compressString(String string) throws Exception{
        return byteToString(compress(string));
    }

    /**
     * 압축된 스트링을 복귀시켜서 String 으로 리턴한다.
     * @param compressed
     * @return
     */
    public synchronized static String decompressString(String compressed) throws Exception{
        return decompress(hexToByteArray(compressed));
    }

    private static String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        try {
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    private static byte[] compress(String text) throws IOException{
        ByteArrayOutputStream baos = null;
        OutputStream out=null;
        try {
        	baos = new ByteArrayOutputStream();
            out = new DeflaterOutputStream(baos);
            out.write(text.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally{
        	if(out!=null) out.close();
        	if(baos!=null) baos.close();
        }
        return baos.toByteArray();
    }


    private static String decompress(byte[] bytes) throws IOException{
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0)
                baos.write(buffer, 0, len);
            return new String(baos.toByteArray(), charsetName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally{
        	if(baos!=null) baos.close();
        	if(in!=null) in.close();
        }
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     */
    private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

}
