package com.wallet.biz.utils

import org.apache.commons.lang3.StringUtils
import org.bitcoinj.core.Base58
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.springframework.beans.BeanUtils
import org.web3j.crypto.Hash
import java.util.HashSet

import org.springframework.beans.BeanWrapperImpl

import org.springframework.beans.BeanWrapper




/** 
 * Created by pie on 2020/9/7 19: 24. 
 */
class BasicUtils {
    companion object{

        fun copyPropertiesIgnoreNull(source:Any,target:Any){
            BeanUtils.copyProperties(source,target,*getNullPropertyNames(source))
        }

        fun getNullPropertyNames(source: Any): Array<String> {
            val src: BeanWrapper = BeanWrapperImpl(source)
            val pds = src.propertyDescriptors
            val emptyNames= HashSet<Any?>()
            for (pd in pds) {
                //check if value of this property is null then add it to the collection
                val srcValue = src.getPropertyValue(pd.name)
                if (srcValue == null) {
                    emptyNames.add(pd.name)
                }
            }
            val result = arrayOfNulls<String>(emptyNames.size)
            return emptyNames.toArray(result)
        }


        fun base58CheckToHexString(input: String):String{
            val byteArray=decode58Check(input)
            return NumericUtil.bytesToHex(byteArray)
        }

        fun hexToBase58(input: String):String{
            val byteArray=NumericUtil.hexToBytes(input)
            return encode58Check(byteArray)
        }

        fun encode58Check(input: ByteArray): String {
            val hash0: ByteArray = Hash.sha256(input)
            val hash1: ByteArray = Hash.sha256(hash0)
            val inputCheck = ByteArray(input.size + 4)
            System.arraycopy(input, 0, inputCheck, 0, input.size)
            System.arraycopy(hash1, 0, inputCheck, input.size, 4)
            return Base58.encode(inputCheck)
        }

        fun decode58Check(input: String): ByteArray? {
            val decodeCheck: ByteArray = Base58.decode(input)
            if (decodeCheck.size <= 4) {
                return null
            }
            val decodeData = ByteArray(decodeCheck.size - 4)
            System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.size)
            val hash0: ByteArray = Hash.sha256(decodeData)
            val hash1: ByteArray = Hash.sha256(hash0)
            return if (hash1[0] == decodeCheck[decodeData.size] && hash1[1] == decodeCheck[decodeData.size + 1] && hash1[2] == decodeCheck[decodeData.size + 2] && hash1[3] == decodeCheck[decodeData.size + 3]
            ) {
                decodeData
            } else null
        }

        fun decodeFromBase58Check(addressBase58: String): ByteArray? {
            if (StringUtils.isEmpty(addressBase58)) {
                println("Warning: Address is empty !!")
                return null
            }
            return  decode58Check(addressBase58)
        }
    }

}
