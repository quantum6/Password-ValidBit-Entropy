package net.quantum6.password;

/**
 * password有很多位没有利用到。现在想充分利用。
 * 
 * 总体思路：
 * 只使用小写（可自行扩展）。这也符合绝大多数人的习惯。
 * 
 * 压缩思路有：
 * 1. 合并策略。2个合并为1个，注意合并后必须使然是7位。
 * 2. 大数策略。即CDKEY策略，
 * 
 *
 */
public class PasswordConfig
{

    public static final int ENCRYPT_SIZE = 8;
    public static final String PASSWORD = "quantumoffice";

}
