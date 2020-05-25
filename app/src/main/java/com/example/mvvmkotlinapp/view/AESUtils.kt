package com.example.mvvmkotlinapp.view

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class AESUtils {

    val secretKey = "662ede816988e58f"
    val salt = "QWlGNHNhMTJT" // base64 decode => AiF4sa12SAfvlhiWu
    val iv = "bVQzNFNhRkQ1Njc4UUFaWA==" // base64 decode => mT34SaFD5678QAZX


    var keyStr = "abcdef"

    // Original
    fun encrypt(strToEncrypt: String) :  String?
    {
        try
        {
            val bytes = encrypt(keyStr, keyStr, strToEncrypt.toByteArray(charset("UTF-8")))
            return String(Base64.encode(bytes, Base64.DEFAULT), Charsets.UTF_8)

            /*val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 500, 128)
            val tmp = factory.generateSecret(spec)
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)*/
        }
        catch (e: Exception)
        {
            println("Error while encrypting: $e")
        }
        return null
    }


    //Second
    /*fun encrypt(strToEncrypt: String): String? {
        var keyBytes: ByteArray

        try {
            keyBytes = secretKey.toByteArray(charset("UTF8"))
            val secretKey = SecretKeySpec(keyBytes, "AES")
            val input = strToEncrypt.toByteArray(charset("UTF8"))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)

                val cipherText = ByteArray(cipher.getOutputSize(input.size))
                var ctLength = cipher.update(input, 0, input.size, cipherText, 0)
                return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
            }
        } catch (uee: UnsupportedEncodingException) {
            uee.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        } catch (nspe: NoSuchPaddingException) {
            nspe.printStackTrace()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        } catch (e: ShortBufferException) {
            e.printStackTrace()
        }

        return null
    }*/


    //Original
    fun decrypt(strToDecrypt : String) : String? {
        try
        {
            val bytes = decrypt(keyStr, keyStr, Base64.decode(strToDecrypt.toByteArray(charset("UTF-8")), Base64.DEFAULT))
            return String(bytes!!, Charsets.UTF_8)

           /* val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 500, 128)
            val tmp = factory.generateSecret(spec);
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))*/
        }
        catch (e : Exception) {
            println("Error while decrypting: $e");
        }
        return null
    }



    fun decryptWithAES(strToDecrypt: String?): String? {
        var keyBytes: ByteArray

        try {
            keyBytes = secretKey.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            //val input = Base64.decode(strToDecrypt?.trim { it <= ' ' }?.toByteArray(charset("UTF8")))
            val input = Base64.decode(strToDecrypt, Base64.DEFAULT)

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.DECRYPT_MODE, skey)

                val plainText = ByteArray(cipher.getOutputSize(input.size))
                val decryptedString = String(plainText)
                return decryptedString.trim { it <= ' ' }
            }
        } catch (uee: UnsupportedEncodingException) {
            uee.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        } catch (nspe: NoSuchPaddingException) {
            nspe.printStackTrace()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        } catch (e: ShortBufferException) {
            e.printStackTrace()
        }

        return null
    }


// Second
    /*fun decrypt(key: String): String? {
        var keyBytes: ByteArray

        try {
            keyBytes = key.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = Base64.decode(secretKey, Base64.DEFAULT)

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, skey);
                return String(cipher.doFinal(Base64.decode(key, Base64.DEFAULT)))
            }
        } catch (uee: UnsupportedEncodingException) {
            uee.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        } catch (nspe: NoSuchPaddingException) {
            nspe.printStackTrace()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        } catch (e: ShortBufferException) {
            e.printStackTrace()
        }

        return null
    }*/


    @Throws(java.lang.Exception::class)
    fun encrypt(
        ivStr: String,
        keyStr: String,
        bytes: ByteArray?
    ): ByteArray? {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        md.update(ivStr.toByteArray())
        val ivBytes: ByteArray = md.digest()
        val sha: MessageDigest = MessageDigest.getInstance("SHA-256")
        sha.update(keyStr.toByteArray())
        val keyBytes: ByteArray = sha.digest()
        return encrypt(ivBytes, keyBytes, bytes)
    }

    @Throws(java.lang.Exception::class)
    fun encrypt(
        ivBytes: ByteArray?,
        keyBytes: ByteArray?,
        bytes: ByteArray?
    ): ByteArray? {
        val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
        val newKey =
            SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
        return cipher.doFinal(bytes)
    }

    @Throws(java.lang.Exception::class)
    fun decrypt(
        ivStr: String,
        keyStr: String,
        bytes: ByteArray?
    ): ByteArray? {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        md.update(ivStr.toByteArray())
        val ivBytes: ByteArray = md.digest()
        val sha: MessageDigest = MessageDigest.getInstance("SHA-256")
        sha.update(keyStr.toByteArray())
        val keyBytes: ByteArray = sha.digest()
        return decrypt(ivBytes, keyBytes, bytes)
    }

    @Throws(java.lang.Exception::class)
    fun decrypt(
        ivBytes: ByteArray?,
        keyBytes: ByteArray?,
        bytes: ByteArray?
    ): ByteArray? {
        val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
        val newKey =
            SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
        return cipher.doFinal(bytes)
    }

    @Throws(java.lang.Exception::class)
    fun encryptStrAndToBase64(enStr: String): String? {
        val bytes = encrypt(keyStr, keyStr, enStr.toByteArray(charset("UTF-8")))
        return String(Base64.encode(bytes, Base64.DEFAULT), Charsets.UTF_8)
    }

    @Throws(java.lang.Exception::class)
    fun decryptStrAndFromBase64(deStr: String): String? {
        val bytes = decrypt(keyStr, keyStr, Base64.decode(deStr.toByteArray(charset("UTF-8")), Base64.DEFAULT))
        return String(bytes!!, Charsets.UTF_8)
    }
}