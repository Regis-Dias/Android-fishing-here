package com.fh.miltec;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {

    public String criptografarSenha(String senha) {
        try {

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(senha.getBytes(), 0, senha.length());

            return new BigInteger(1, m.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            return "MD5: " + e.getMessage();
        }
    }

}
