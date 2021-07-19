//package con.wallet
//
//import com.wallet.WebApiApplication
//import com.wallet.biz.rpc.TrxApi
//import com.wallet.biz.utils.TRONUtils
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//import org.springframework.web.client.RestTemplate
//
//@RunWith(SpringRunner::class)
//@SpringBootTest(classes=[WebApiApplication::class])
//class T {
//
//    @Test
//    fun t(){
//        val balance=tronUtils.getContractBalance("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t","TCMbJvEtbwVfNYpzjj4kGAufGTgsek9w45")
//
//        println(balance)
//
//    }
//
//    @Test
//    fun f() {
//        val api = TrxApi("http://13.127.47.162:8090", restTemplate)
//        val node = api.easyTransferByPrivate("c071a24e1a81556a6ed5ed41897a3566c14ea3bc8d81d06bbd854d4741db45d1","TQxd3QfuvRuZbZC6vRyw2ummQ6Ftv16xeo", 1)
//        println(node)
//    }
//
//    @Autowired
//    lateinit var tronUtils: TRONUtils
//
//    @Autowired
//    lateinit var restTemplate: RestTemplate
//}