package com.wallet.biz.xservice

import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.TransactionLogVo
import com.wallet.biz.domain.vo.TransactionVo
import com.wallet.entity.domain.Address
import com.wallet.entity.domain.AddressAdmin
import com.wallet.entity.domain.Deposit
import com.wallet.entity.domain.Token
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.springframework.data.domain.Page
import org.web3j.protocol.core.methods.response.Transaction
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-13 14: 22. 
 */
interface WalletXService {

    fun getAddress(chainType: String, num: Int): List<String?>

    fun getUtxos(chainType: String, address: String): ArrayList<BitcoinTransaction.UTXO>

    fun getTransaction(getTransactionByHashPo: GetTransactionPo): List<TransactionVo>

    fun saveWithReocord(
        amount: BigDecimal, hash: String, toAddress: String, fromAddress: String, withType: Int, chainType: String,
        tokenSymbol: String
    )

    fun ethContractTransferData(amount: BigDecimal, toAddress: String, decimals: Int): String

    fun ethConractBalanceData(address: String): String

    fun getHotAddress(chainType: String, type: Int): List<String>?

    fun checkAddress(checkAddressPo: CheckAddressPo): String

    fun getDepositTransaction(): List<Deposit>

    fun getLocalTransaction(type: Int?, page: Int, size: Int): Page<TransactionLogVo>

    fun createHotAddress(createSendAddressPo: CreateHotAddressPo): AddressAdmin

    fun rescanTransaction(addTransactionPo: RescanTransactionPo)

    fun synEthByTransaction(
            it: Transaction,
            map: Map<String, Address>,
            contractMap: Map<String, Token>,
            diffHeight: Long
    )


    fun rescanBlock(rescanBlockPo: RescanBlockPo)

    fun send(sendPo: SendPo): String

    fun exportWallet(exportWalletPo: ExportWalletPo): String

    fun importWallet(importWalletPo: ImportWalletPo): String

    fun checkAddressInDb(map: Map<String, Address>, address: String?): Boolean

    fun checkAddressInContract(map: Map<String, Token>, address: String?): Boolean

    fun putAllAddressToWaitImport()

    fun removeUselessWallet()

    fun checkTxidIsSendFee(txid: String): Boolean

    fun checkTxStatus(hash: String, chain: String): Boolean
    fun getDepositTransactionAndSave(): List<Deposit>
    fun saveUploadedDepositTransaction(saveList: List<Deposit>)
}
