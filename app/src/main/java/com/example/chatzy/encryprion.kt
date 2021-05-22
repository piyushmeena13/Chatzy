package com.example.chatzy

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

// Call requires API level 26 (current min is 22): java.util.Base64#getEncoder
@RequiresApi(Build.VERSION_CODES.O)
class encryprion {

    //key genration
    val secretKey: String = "662ede816988e58fb6d057d9d85605e0"
    var keyBytes = secretKey.toByteArray(charset("UTF8"))
    val key = SecretKeySpec(keyBytes, "AES")
//    val keygen = KeyGenerator.getInstance("AES")
//    keygen.init(256)
//    val key = keygen.generateKey()
//    keyTV.setText(key.toString())

    val encoder = Base64.getEncoder()
    val decoder = Base64.getDecoder()

    fun encrypt(plainText:String):String{

        // create object of Cipher and initilization
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE,key)

        // encrypted message in ByteArray
        val ciphertext = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))


        return (String(encoder.encode(ciphertext)))
    }

    fun decrypt(encryptedText: String): String{
        // encrypted text ko bytearray me convert kiya
        val ciphertext = decoder.decode(encryptedText.toByteArray(Charsets.UTF_8))

        //create object of Cipher and initilize with decrypt mode and key
        val decipher = Cipher.getInstance("AES")
        decipher.init(Cipher.DECRYPT_MODE, key)

        val plaintext = decipher.doFinal(ciphertext)
        return (String(plaintext))
    }
}