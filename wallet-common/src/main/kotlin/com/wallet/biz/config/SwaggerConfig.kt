package com.wallet.biz.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.async.DeferredResult
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.schema.ModelRef
import java.util.ArrayList
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.service.Parameter


/**
 * Created by zhangxinglin on 2017/7/28.
 */
@Configuration
@EnableSwagger2
open class SwaggerConfig {

    @Bean
    open fun api(): Docket{
        val ticketPar = ParameterBuilder()
        val pars = ArrayList<Parameter>()
        ticketPar.name("access_token").description("access_token")
            .modelRef(ModelRef("string")).parameterType("header")
            .required(false).build() //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build())    //根据每个方法名也知道当前方法在设置什么参数

       return Docket(DocumentationType.SWAGGER_2)
            .enable(true)
            .globalOperationParameters(pars)
            .genericModelSubstitutes(DeferredResult::class.java)
            .useDefaultResponseMessages(false)
            .forCodeGeneration(false)
            .pathMapping("/")   // base，最终调用接口后会和paths拼接在一起
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.wallet"))
            .build()
            .apiInfo(loanApiInfo())
    }

    private fun loanApiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("token API")//大标题
            .description("token's REST API, all the applications could assess the Object model data via JSON.")//详细描述
            .version("2.1.3.11")//版本
            .termsOfServiceUrl("NO terms of service")
            //.contact(Contact("PIE", "https://github.com/pai01234", "dengrunali@gmail.com"))//作者
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .build()

    }
}
