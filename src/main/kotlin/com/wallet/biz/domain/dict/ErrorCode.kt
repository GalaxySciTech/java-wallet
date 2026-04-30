package com.wallet.biz.domain.dict

/** 
 * Created by pie on 2020/7/20 02: 32. 
 */
enum class ErrorCode(val code: Int, val msg: String) {

    ERROR_PARAM(501, "错误参数"),
    NO_THIS_CHAIN_TYPE(502, "没有这个chaintype"),
    NOT_IN_WHITE_LIST(503, "不在白名单"),
    NO_THIS_TYPE(504, "没有这个type"),
    SEND_ADDRESS_NOT_FOUND(505, "发送地址没找到"),
    ADDRESS_NOT_FOUND(506, "地址没找到"),
    NO_THIS_SYMBOL(507, "没有这个symbol"),
    GAS_ADDRESS_NOT_FOUND(508, "gas地址没找到"),
    NOT_SUPPORT(509, "暂不支持此方式"),
    COLLECT_ADDRESS_NOT_FOUND(510, "归集地址没找到"),
    DB_WALLET_HEIGHT_MUST_HAVE(511, "db区块高度必须有值"),
    INSUFFICIENT_BALANCE(512, "余额不够"),
    UTXO_NOT_FOUND(513, "utxo没找到"),
    DOUBLE_ENTRY_HASH(514, "重复入账hash"),
    API_NETWORK_BUSY(515, "api网关繁忙"),
    GET_ADDRESS_FAILURE_FROM_HSM(516,"从hsm获取地址失败"),
    HSM_CONNECT_FAILURE(517,"hsm连接失败，请确认hsm打开状态"),
    CAN_NOT_FIND_TOKEN(518,"不能找到token"),
    NOT_SUPPORT_MODE(519, "不支持的mode"),
    UPLOAD_FAILURE(520,"同步充值失败，上传交易返回值不成功");

}
