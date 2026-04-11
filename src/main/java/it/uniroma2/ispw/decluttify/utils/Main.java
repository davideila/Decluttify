package it.uniroma2.ispw.decluttify.utils;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;


//This is a utility to generate the pwd hash and write manually on persistence for pwd of users
public class Main{

    public static void main(String[] args) {
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 10);

        Hash hash = Password.hash("richard")
                .addPepper("shared-secret")
                .with(bcrypt);

        System.out.println("Pwd to copy in DB: " + hash.getResult());
    }
}