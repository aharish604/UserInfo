package com.mvpmobdealer.aes

import android.content.Context
import android.util.Base64
import android.util.Log
import com.mobikasa.userinfo.Utils.Constants
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encrypt {
    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?, key: SecretKey?, IV: ByteArray?): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key!!.encoded, "AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(plaintext)
    }


    fun startEnc(plaintext: String, context: Context) : String {
        try {
        val sKey=Constants.enc_key
        val iv = Constants.iv_key

        var secretKey: SecretKey? = null
        val IV: ByteArray?
        val cipherText: ByteArray
        val key = Base64.decode(sKey, Base64.DEFAULT)
        secretKey = SecretKeySpec(key, 0, key.size, "AES")
            IV = Base64.decode(iv, Base64.DEFAULT)
            cipherText = encrypt(plaintext = plaintext.trim().toByteArray(), secretKey, IV)
           return encoderfun(cipherText)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun encoderfun(decval: ByteArray?): String {
        return Base64.encodeToString(decval, Base64.DEFAULT)
    }

}