package com.wallet.biz.dict

/** 
 * Created by pie on 2020/7/24 18: 50. 
 */
enum class SysConfigKey(var defaultValue:String,var description:String,var group:String) {

    HSM_URL("http://127.0.0.1:10888","hsm 请求地址","全局"),
    ETH_RPC_URL("http://127.0.0.1:8545","eth RPC地址","以太坊"),
    OMNI_RPC_URL("http://127.0.0.1:8332","omni RPC地址","比特币"),
    DASH_RPC_URL("http://127.0.0.1:8332","dash RPC地址","达世"),
    LTC_RPC_URL("http://127.0.0.1:8332","ltc RPC地址","莱特币"),
    BCH_RPC_URL("http://127.0.0.1:8332","bch RPC地址","比特现金"),
    BSV_RPC_URL("http://127.0.0.1:8332","bsv RPC地址","比特币SV"),
    DOGE_RPC_URL("http://127.0.0.1:8332","doge RPC地址","狗狗币"),
    EOS_RPC_URL("https://api.eosflare.io","eos RPC地址","柚子"),
    TRX_API_URL("https://api.trongrid.io","trx HTTP API地址","波场"),
    ETH_SCAN_BACK("12","以太坊回扫高度数","以太坊"),
    ETH_GAS_LEVEL("fast","以太坊手续费等级 fast  average safeLow","以太坊"),
    BTC_SCAN_BACK("6","比特币回扫高度数","比特币"),
    BTC_GAS_LEVEL("fastestFee","比特币手续费等级 fastestFee halfHourFee hourFee","比特币"),
    GAS_PROP("1","gas使用比例，范围0-1","全局"),
    LOG_LEVEL("1","0-9 log等级","全局"),
    ERROR_LEVEL("0","0 展示错误信息 |1 展示堆栈","全局"),
    TRX_SCAN_BACK("12","波场回扫高度数","波场"),
    DEPOSIT_POST_NOTIFY_SALT("{\"3\":\"2\",\"9\":\"1\"}","充值同步post数据盐值","充值通知"),
    DEPOSIT_POST_NOTIFY_URL("http://192.168.31.222","充值同步post数据地址 逗号分割可以推送多个服务端 例如 http://192.168.31.222,http://192.168.31.223","充值通知"),
    DEPOSIT_NOTIFY_MODE("1","充值同步模式 post rabbitmq 逗号分割可以同时推送多个 例如 post,rabbitmq","充值通知");

}

