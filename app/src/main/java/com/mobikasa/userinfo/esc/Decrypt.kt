package com.mvpmobdealer.aes

import android.content.Context
import android.text.Html.fromHtml
import android.util.Base64
import androidx.core.text.HtmlCompat
import com.mobikasa.userinfo.Utils.Constants
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Decrypt{


    fun decrypt(cipherText: ByteArray?, key: SecretKey, IV: ByteArray?): String {
        try {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)
            var decryptString = ""
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                decryptString =  fromHtml(String(decryptedText), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            }else{
                decryptString = String(decryptedText).replace(("[^\\w\\d ]").toRegex(), "")
            }
            return  decryptString.trim()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decoderfun(enval: String?): ByteArray {
        return Base64.decode(enval, Base64.DEFAULT)
    }

    fun startDec(text:String) :String{

        try{
        val sKey=Constants.enc_key
        val iv = Constants.iv_key

        val encText: ByteArray = decoderfun(text)
        val ivkey: ByteArray = decoderfun(iv)
        val encodedSecretKey: ByteArray = decoderfun(sKey)
        val originalSecretKey: SecretKey = SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.size, "AES")

        return decrypt(encText, originalSecretKey, ivkey)
        }catch (e:Exception){

        }
        return ""
    }


    fun imgDecrypt(cipherText: ByteArray?, key: SecretKey, IV: ByteArray?): String {
        try {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)
            var decryptString = ""
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                decryptString =  fromHtml(String(decryptedText), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            }else{
                decryptString = String(decryptedText)
            }
            return  decryptString.trim()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }



   /* fun imgStartDec(context: Context,text:String) :String {
        if(MyDatabase.DbProvider.getInstance(context).AuthDAO()!=null) {
            val authTable = MyDatabase.DbProvider.getInstance(context).AuthDAO().getAll()
            if (authTable != null && authTable.enc_key.isNotEmpty() && authTable.iv_key.isNotEmpty()) {
                val sKey = authTable.enc_key
                val iv = authTable.iv_key

                val encText: ByteArray = decoderfun(text)
                val ivkey: ByteArray = decoderfun(iv)
                val encodedSecretKey: ByteArray = decoderfun(sKey)
                val originalSecretKey: SecretKey =
                    SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.size, "AES")

                return imgDecrypt(encText, originalSecretKey, ivkey)
            } else {
                return ""
            }
        }else {
            return ""
        }
    }*/


}