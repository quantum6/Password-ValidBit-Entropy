package net.quantum6.password;

import java.math.BigInteger;

public class PasswordEntropy4BigNumber
{
    
    private final static char[]  DECIMAL_DIGIT_PLAIN = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};
    private static final BigInteger JZ_PLAIN   = new BigInteger(String.valueOf(DECIMAL_DIGIT_PLAIN.length));
    private static final BigInteger JZ_ENCRYPT = new BigInteger(String.valueOf("256"));

    
    private static void jzPlainToJzEncrypt(byte[] plain, byte[] encrypt)
    {
        int i;
        for (i=0; i<plain.length; i++)
        {
            for (int j=0; j<DECIMAL_DIGIT_PLAIN.length; j++)
            {
                if (plain[i] == DECIMAL_DIGIT_PLAIN[j])
                {
                    plain[i] = (byte)j;
                }
            }
        }
        
        BigInteger intPlain = new BigInteger("0");
        for (i=plain.length-1; i>=0; i--)
        {
            intPlain = intPlain.multiply(JZ_PLAIN);
            intPlain = intPlain.add(new BigInteger(String.valueOf(plain[i] & 0xFF)));
        }
        
        for (i=encrypt.length-1; i>=0; i--)
        {
            BigInteger data128 = intPlain.divide(JZ_ENCRYPT);
            //表示已经剩下最后一点。
            if (data128.compareTo(BigInteger.ZERO) == 0)
            {
                encrypt[i] = intPlain.byteValue();
                break;
            }
            
            //得到除128之后的余数。
            encrypt[i] = intPlain.subtract(data128.multiply(JZ_ENCRYPT)).byteValue();
            
            intPlain = data128;
        }

        //如果位数太少，目前不处理。也可以考虑移位操作。
    }


    private static void appendByte(StringBuffer sb, byte v)
    {
        int value = (v & 0xFF);
        if (value < 0x10)
        {
            sb.append('0');
        }
        sb.append(Integer.toHexString(value));
    }
    
    public static String dump(byte[] data)
    {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        appendByte(sb, data[0]);
        for (int i=1; i<data.length; i++)
        {
            sb.append(", ");
            appendByte(sb, data[i]);
        }
        sb.append(']');
        
        String result = sb.toString();
        System.out.println(result);
        return result;
    }

    public static byte[] checkEntropy(String password, int len)
    {
        byte[] encrypt = new byte[len];
        jzPlainToJzEncrypt(password.getBytes(), encrypt);
        return encrypt;
    }

    public static void main(String[] args)
    {
        byte[] encrypt = PasswordEntropy4BigNumber.checkEntropy(PasswordConfig.PASSWORD, PasswordConfig.ENCRYPT_SIZE);
        dump(PasswordConfig.PASSWORD.getBytes());
        dump(encrypt);
        
    }

}
