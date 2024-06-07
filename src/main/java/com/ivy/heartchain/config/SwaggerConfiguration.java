package com.ivy.heartchain.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author ivy
 * @date 2024/5/25 19:51
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String USER_API_DOC = ".*(\\/user\\/).*";

    private static final String BACK_API_DOC = ".*(\\/admin\\/).*";

    /**
     * 接口地址中包含/api/user
     */
    @Bean
    public Docket createUserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ivy.heartchain.controller"))
                .paths(PathSelectors.regex(USER_API_DOC))
                .build()
                .groupName("用户端接口文档")
                .pathMapping("/")
                .apiInfo(new ApiInfoBuilder()
                        //页面标题
                        .title("用户端接口文档")
                        //创建人
                        .contact(new Contact("", "", ""))
                        //版本号
                        .version("2.0")
                        //描述
                        .description("用户端接口文档")
                        .build()
                );
    }

    /**
     * 接口地址中包含/api/admin
     */
    @Bean
    public Docket createBackEndApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ivy.heartchain.controller"))
                .paths(PathSelectors.regex(BACK_API_DOC))
                .build()
                .groupName("管理端接口文档")
                .pathMapping("/")
                .apiInfo(new ApiInfoBuilder()
                        //页面标题
                        .title("管理端接口文档")
                        //创建人
                        .contact(new Contact("", "", ""))
                        //版本号
                        .version("2.0")
                        //描述
                        .description("管理端接口文档")
                        .build()
                );
    }

}
